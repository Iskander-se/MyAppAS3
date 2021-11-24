package com.example.myappas3;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;

public class HexUtil {
    private final static char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static int toByte(char c) {
        if (c >= '0' && c <= '9')
            return (c - '0');
        if (c >= 'A' && c <= 'F')
            return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f')
            return (c - 'a' + 10);
        throw new InvalidParameterException("Invalid hex char '" + c + "'");
    }


    static int DubleHexToInt(@NonNull char[] cVal)
    {   int val=0;
        val=toByte(cVal[0])<<4;
        val+=toByte(cVal[1]);
        return val;
    }

    static int[] DataToValue(@NonNull String str)
    {
        int[] cData = new int[4];
        char[] cVal= str.toCharArray();
        int lh=cVal.length+1;
        for(int i=0; i<4; i++)
        {
            if(lh<i*2)  cData[i]=0;
            else cData[i]=DubleHexToInt(new char[]{cVal[i * 2], cVal[i * 2 + 1]});
        }
        return cData;
    }
}
