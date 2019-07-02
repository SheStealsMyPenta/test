package com.pd.config.pdonlineconfig.utils;

import android.graphics.Color;

import java.io.IOException;
import java.text.DecimalFormat;

public final class  ConversionTool {
    public static float tofloat(byte[] data) {
        return Float.intBitsToFloat(toint(data));
    }

    public static double todouble(byte[] data) {

        return Double.longBitsToDouble(tolong(data));

    }
//
    public static short toshort(byte[] data) {
        short temp = 0;
        if (data.length >= 2) {
            temp = (short) (temp | (data[0] & 0xff) << 0);
            temp = (short) (temp | (data[1] & 0xff) << 8);

        }
        return temp;
    }
    public static int byteToPositive(byte b) {

        if (b < 0) {
            return b + 256;
        } else {
            return b;
        }
    }
    public static int ConvertRgb565(short nColor565){
        int r, g, b;
        r = (int)(nColor565 & 0xF800);
        r >>= 8;	// r >>= 11; r <<= 3

        g = (int)(nColor565 & 0x07E0);
        g >>= 3;	//g >>= 5; g <<= 2;

        b = (int)(nColor565 & 0x001F);
        b <<= 3;

       return Color.rgb(r,g,b);

    }
    public static String binaryArray2Ipv4Address(byte[]addr){
        String ip="";
        for(int i=0;i<addr.length;i++){
            ip+=(addr[i]&0xFF)+".";
        }
        return ip.substring(0, ip.length()-1);
    }
    public static byte[] ipv4Address2BinaryArray(String ipAdd){
        byte[] binIP = new byte[4];
        String[] strs = ipAdd.split("\\.");
        for(int i=0;i<strs.length;i++){
            binIP[i] = (byte) Integer.parseInt(strs[i]);
        }
        return binIP;
    }
    public static int toint(byte[] data) {
        int temp = 0;
        if (data.length >= 4) {
            temp = temp | (data[0] & 0xff) << 0;
            temp = temp | (data[1] & 0xff) << 8;
            temp = temp | (data[2] & 0xff) << 16;
            temp = temp | (data[3] & 0xff) << 24;
        }
        return temp;
    }

    public static long tolong(byte[] data) {
        long temp = 0;
        if (data.length >= 8) {
            temp = temp | (data[0] & 0xff) << 0;
            temp = temp | (data[1] & 0xff) << 8;
            temp = temp | (data[2] & 0xff) << 16;
            temp = temp | (data[3] & 0xff) << 24;
            temp = temp | (data[0] & 0xff) << 32;
            temp = temp | (data[1] & 0xff) << 40;
            temp = temp | (data[2] & 0xff) << 48;
            temp = temp | (data[3] & 0xff) << 56;
        }

        return temp;
    }


    public static byte[] floattobits(float data) {
        int temp = Float.floatToIntBits(data);
        byte[] temp_buffer = new byte[4];
        for (int i = 0; i < 4; i++) {
            temp_buffer[i] = (byte) (temp >> 8 * i & 0xFF);
        }

        return temp_buffer;

    }


    public static byte[] int2BytesArray(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (i * 8));
        }
        return b;
    }

    public static byte[] short2bits(short n) {
        byte[] temp = new byte[2];

        temp[0] = (byte) n;
        temp[1] = (byte) (n >> 8);
        return temp;
    }

    public static byte[] Long2Bits(long Data) {
        byte[] temp = new byte[8];
        for (int i = 0; i < 8; i++) {
            temp[i] = (byte) (Data >> 8 * i & 0xFF);
        }

        return temp;
    }

    /**
     * ????
     *
     * @param data
     * @return
     */
    public static byte[] Arr_Reverse(byte[] data) {
        byte[] temp = new byte[data.length];
        for (int i = 0; i <= data.length - 1; i++) {
            temp[i] = data[data.length - 1 - i];
        }
        return temp;
    }

    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }


    public static int String2Unicodebyte(String msg, byte[] data, boolean isbigend) {
        if ((data == null) || (msg == null) || msg.isEmpty()) {
            return 0;
        }
        char[] temp = msg.toCharArray();
        int i = 0;
        for (char c : temp) {
            if (i >= data.length) {
                return i;
            }
            if (isbigend) {
                data[i++] = (byte) ((c >> 8) & 0xff);
                data[i++] = (byte) (c & 0xff);
            } else {
                data[i++] = (byte) (c & 0xff);
                data[i++] = (byte) ((c >> 8) & 0xff);
            }


        }
        return i;
    }

    public static String Unicodebytes2String(byte[] data, boolean isbigend) {
        if ((data == null) || ((data.length % 2) != 0)) {
            return "";
        }
        char temp;
        StringBuilder builder = new StringBuilder("");
        int i = 0;
        while (i < data.length) {

            if (isbigend) {
                temp = (char) (data[i++] & 0xff);
                temp <<= 8;
                temp += (char) (data[i++] & 0xff);
            } else {
                temp = (char) (data[i++] & 0xff);
                temp += ((char) (data[i++] & 0xff) << 8);
            }
            builder.append(temp);

        }

        return builder.toString();

    }


    public static int String2bytes(String msg, byte[] data) {
        if ((data == null) || (msg == null) || msg.isEmpty()) {
            return 0;
        }
        int i = 0;
        for (byte temp : msg.getBytes()) {
            if (i >= data.length) {
                data[--i] = '\0';
                return ++i;
            }
            data[i++] = temp;
        }
        return i;
    }


    public static int String2GB2312bytes(String msg, byte[] data) {

        if ((data == null) || (msg == null) || msg.isEmpty()) {
            return 0;
        }
        int i = 0;
        try {
            for (byte temp : msg.getBytes("gb2312")) {
                if (i >= data.length) {
                    data[--i] = '\0';
                    return ++i;
                }
                data[i++] = temp;
            }
        } catch (IOException E) {
            E.printStackTrace();
            return i;
        }

        return i;


    }

    public static String FormatVaule(float Vaule) {
        DecimalFormat fmt = new DecimalFormat("#.##");
        String out = fmt.format(Vaule);
        return out;
    }

}
