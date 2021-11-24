package com.example.myappas3;

import static com.example.myappas3.GlobalObjectVariables.SerialPack;
import static com.example.myappas3.GlobalObjectVariables.mainHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private ActivityMainBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        this.startService(new Intent(this, SerialService.class));

        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleAirECUmsg(msg);
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



    public void handleAirECUmsg(Message msg) {
        if (msg.obj != null) {
            if (msg.obj.getClass() == SerialPack.class) {
                SerialPack UImsg = (SerialPack)msg.obj;
                if(UImsg.type=='l'&&UImsg.part=='c'){
                    int dataArray[] = HexUtil.DataToValue(UImsg.data);
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
                    ImageView VAGblockFL = findViewById(R.id.VAGblockFL);
                    if(cVal[1]=='1') VAGblockFL.setImageDrawable(Drawable.createFromPath("@android:drawable/presence_online"));
                    else             VAGblockFL.setImageDrawable(Drawable.createFromPath("@android:drawable/presence_invisible"));
                    ImageView VAGblockFR = findViewById(R.id.VAGblockFR);
                    if(cVal[2]=='1') VAGblockFR.setImageDrawable(Drawable.createFromPath("@android:drawable/presence_online"));
                    else             VAGblockFR.setImageDrawable(Drawable.createFromPath("@android:drawable/presence_invisible"));
                    ImageView VAGblockRL = findViewById(R.id.VAGblockFL);
                    VAGblockRL.setImageDrawable(Drawable.createFromPath("@android:drawable/presence_online"));
//@android:drawable/presence_online
                    //@android:drawable/presence_invisible

                }

            }
        }
    }
}