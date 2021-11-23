package com.example.myappas3;

import static com.example.myappas3.GlobalObjectVariables.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappas3.ui.main.SectionsPagerAdapter;
import com.example.myappas3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        this.startService(new Intent(this, SerialService.class));

        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleSIPmsg(msg);
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





        viewPager.setCurrentItem(1);


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

    public void handleSIPmsg(Message msg) {
        if (msg.obj != null) {
            if (msg.obj.getClass() == SerialPack.class) {
                SerialPack UImsg = (SerialPack)msg.obj;
                if(UImsg.type=='l'){
                    TextView tvFLm = findViewById(R.id.tvFLm);
                    tvFLm.setText("50%");
                    TextView tvFRm = findViewById(R.id.tvFRm);
                    tvFRm.setText("50%");
                    TextView tvRLm = findViewById(R.id.tvRLm);
                    tvRLm.setText("50%");
                    TextView tvRRm = findViewById(R.id.tvRRm);
                    tvRRm.setText("50%");
                }

            }
        }
    }
}