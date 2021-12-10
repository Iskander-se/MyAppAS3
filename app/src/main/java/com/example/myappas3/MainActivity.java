package com.example.myappas3;

import static com.example.myappas3.GlobalObjectVariables.SerialPack;
import static com.example.myappas3.GlobalObjectVariables.mainHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myappas3.databinding.ActivityMainBinding;
import com.example.myappas3.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public static final String LogTAG = "UsbService";
    private ActivityMainBinding binding;
    private int dataArray[];

    @Override
    protected void onStart() {
        super.onStart();
        this.startService(new Intent(this, SerialService.class));

        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleAirECU(msg);
            }
        };
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;





        viewPager.setCurrentItem(0);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    protected void onNewIntent(Intent intent) {
        //this.moveTaskToBack(true);
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            Toast.makeText(getApplicationContext(), "USB device detected...", Toast.LENGTH_SHORT).show();
            this.startService(new Intent(this, SerialService.class));

        }else Toast.makeText(getApplicationContext(), "NO USB device", Toast.LENGTH_SHORT).show();
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        //processExtraData();
    }


//@w:b:011001^



    @SuppressLint("Range")
    public void handleAirECU(Message msg) {
        if (msg.obj != null) {
            if (msg.obj.getClass() == SerialPack.class) {
                SerialPack UImsg = (SerialPack)msg.obj;
                Log.d(LogTAG, UImsg.type+":"+UImsg.part+":"+UImsg.data);
               if(UImsg.type=='l'&&UImsg.part=='c'){
                   this.dataArray = HexUtil.DataToValue(UImsg.data);
                    TextView tvFLm = findViewById(R.id.tvFLm);
                    tvFLm.setText(dataArray[0]+"%");
                    TextView tvFRm = findViewById(R.id.tvFRm);
                    tvFRm.setText(dataArray[1]+"%");
                    TextView tvRLm = findViewById(R.id.tvRLm);
                    tvRLm.setText(dataArray[2]+"%");
                    TextView tvRRm = findViewById(R.id.tvRRm);
                    tvRRm.setText(dataArray[3]+"%");
               }
                else if(UImsg.type=='w'){
                    char[] cVal= UImsg.data.toCharArray();

                    ImageView VAGblockIN = findViewById(R.id.easIN);
                    ImageView VAGblockOUT = findViewById(R.id.easOUT);
                    ImageView VAGblockB = findViewById(R.id.easB);
                    VAGblockOUT.setAlpha(0F);
                    VAGblockIN.setAlpha(0F);
                    VAGblockB.setAlpha(0F);
                    if(cVal[5]=='7'){
                        VAGblockB.setAlpha(1F);
                    }else if(cVal[0]=='2'){
                        VAGblockOUT.setAlpha(1F);
                    }else if(cVal[0]=='1') {
                        if(cVal[5]=='0'){
                            VAGblockIN.setAlpha(1F);
                        }else{
                            VAGblockIN.setAlpha(1F);
                            VAGblockB.setAlpha(1F);
                        }
                    }else  if(cVal[5]=='1'){
                        VAGblockIN.setAlpha(1F);
                        VAGblockB.setAlpha(1F);
                    }

                    ImageView VAGblockFL = findViewById(R.id.easFL);
                    VAGblockFL.setAlpha((float)(cVal[1]-48));
                    ImageView VAGblockFR = findViewById(R.id.easFR);
                    VAGblockFR.setAlpha((float)(cVal[2]-48));
                    ImageView VAGblockRL = findViewById(R.id.easRL);
                    VAGblockRL.setAlpha((float)(cVal[3]-48));
                    ImageView VAGblockRR = findViewById(R.id.easRR);
                    VAGblockRR.setAlpha((float)(cVal[4]-48));

                }

            }
        }
    }
}