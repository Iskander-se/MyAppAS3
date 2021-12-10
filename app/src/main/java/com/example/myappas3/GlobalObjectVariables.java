package com.example.myappas3;

import android.os.Handler;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.LinkedList;
import java.util.Queue;

public class GlobalObjectVariables {

    public static Handler mainHandler;
    public static Handler serviceHandler;

    public static class SerialPack //команда

    {
        boolean status;
        char type;
        char part;
        String data ;
        String raw="";
    }

}

class CustomProber {

    static UsbSerialProber getCustomProber() {
        ProbeTable customTable = new ProbeTable();
        customTable.addProduct(0x1EAF, 0x0004, CdcAcmSerialDriver.class); // e.g. STM32 Maple CDC
        return new UsbSerialProber(customTable);
    }

}