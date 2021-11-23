package com.example.myappas3;

import android.os.Handler;

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
    }

    private static class QueueItem {
        byte[] data;

        QueueItem(byte[] data, Exception e) { this.data=data;}
    }



}
