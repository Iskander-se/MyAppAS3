package com.example.myappas3;

import static com.example.myappas3.GlobalObjectVariables.*;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SerialService<QueueItem> extends Service {
    private Context context;
    private boolean serialPortConnected;
    public static boolean SERVICE_CONNECTED = false;
    public static final String LogTAG = "UsbService";
    private int PID, VID, portNum, baudRate;
    private UsbSerialPort usbSerialPort;
    private SerialInputOutputManager mSerialIoManager;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private UsbManager usbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        serialPortConnected = false;
        Toast.makeText(this, "Служба создана",Toast.LENGTH_SHORT).show();
        Log.d(LogTAG, "Служба создана");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        baudRate=9600;
        VID=4292;
        PID=60000;
        usbSerialPort = null;
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No serial USB devices!",
                    Toast.LENGTH_SHORT).show();
            return Service.START_REDELIVER_INTENT;
        }

        UsbSerialDriver driver=null;
        try {
            //driver = availableDrivers.get(0);
            for(UsbSerialDriver v : availableDrivers) {
                if (v.getDevice().getProductId() == PID && v.getDevice().getVendorId() == VID) driver = v;
                if (v.getDevice().getProductId() == 29987 && v.getDevice().getVendorId() == 6790) driver = v;
                Log.v("USB",v.toString()+"{{{{"+v.toString()+"@@@@@@@");
            }

        } catch (Exception ignored) {
            Toast.makeText(getApplicationContext(), "No correct serial USB devices!", Toast.LENGTH_SHORT).show();
            return Service.START_REDELIVER_INTENT;
        }

        // Check and grant permissions
        if (!checkAndRequestPermission(usbManager, driver.getDevice())) {
            Toast.makeText(getApplicationContext(),
                    "Please grant permission and try again", Toast.LENGTH_LONG).show();
            return Service.START_REDELIVER_INTENT;
        }

        // Open USB device
        UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
        if (connection == null) {
            Toast.makeText(getApplicationContext(), "Error opening USB device!",
                    Toast.LENGTH_SHORT).show();
            return Service.START_REDELIVER_INTENT;
        }

        try {
            // Open first available serial port
            usbSerialPort = driver.getPorts().get(0);
            usbSerialPort.open(connection);
            usbSerialPort.setParameters(baudRate,8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            mSerialIoManager = new SerialInputOutputManager(usbSerialPort, mListener);
            mExecutor.submit(mSerialIoManager);
            usbSerialPort.write("@m1:1100^".getBytes(), 0);
            /* TODO если служба остановлена, зарустить */
        } catch (IOException e) {
            // Print exception message
            Toast.makeText(getApplicationContext(),"Error opening serial port!", Toast.LENGTH_LONG).show();
            stopSelf();
        }
        Toast.makeText(this, "Служба запущена",Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    public SerialService() {
       // mainLooper = new Handler(Looper.getMainLooper());
       // qArray = new LinkedList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        try {
//            //usbSerialPort.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
        SerialService.SERVICE_CONNECTED = false;
    }

    // @w:b:011001^
    // @l:c:4B4A514D^

    Queue<byte[]> qArray = new LinkedList<byte[]>();

    SerialPack pcmd = new SerialPack();

    private final SerialInputOutputManager.Listener mListener=
            new SerialInputOutputManager.Listener() {
                @Override
                public void onRunError(Exception e) {
                   Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNewData(final byte[] data) {
                    FqArray(data);
                }
            };

    private void FqArray(byte[] data)
    {
        qArray.add(data);
        String dataStr="";
        byte[] tmparr = qArray.poll();
        int iterator=0;
        for (int i = 0; i < tmparr.length; i++) {
            char c = (char)tmparr[i];
            if(c=='@') iterator=1;
            if(iterator==2) pcmd.type=c;
            if(iterator==4) pcmd.part=c;
            if(c=='^') {
                qArray.clear();
                if(dataStr!="")
                {   pcmd.data=dataStr;
                    Message msg =  mainHandler.obtainMessage(0, pcmd );
                    mainHandler.sendMessage(msg);
                    return;
                }

            }
            if(iterator>5){
                dataStr = dataStr+Character.toString(c);
            }
            if(iterator>0) iterator++;

        }

    }

    //
    // For other applications
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(LogTAG, "Служба создана (Binder)");
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private boolean checkAndRequestPermission(UsbManager manager, UsbDevice usbDevice) {
        // Check if permissions already exists
        if (manager.hasPermission(usbDevice))
            return true;
        else {
            // Request USB permission
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(usbDevice, pendingIntent);
            return false;
        }
    }

}