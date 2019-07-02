package com.pd.config.pdonlineconfig.impls;

import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;
import com.pd.config.pdonlineconfig.pojo.ResultObj;
import com.pd.config.pdonlineconfig.utils.ConversionTool;

import java.util.ArrayList;

public class ByteAndObjConverter {
    private byte[] intArr;
    private byte[] shortArr;
    private byte[] floatArr;
    private byte[] stringArr;
    private byte[] normalStringArr;


    public ByteAndObjConverter() {
        intArr = new byte[4];
        shortArr = new byte[2];
        floatArr = new byte[4];
        stringArr = new byte[4];
        normalStringArr = new byte[8];
    }

    public short getShortArr(byte[] raw, int offset) {
        resetShortArr();
        System.arraycopy(raw, offset, shortArr, 0, shortArr.length);
        return toshort(shortArr);
    }
    public short toshort(byte[] data) {
        short temp = 0;
        if (data.length >= 2) {
            temp = (short) (temp | (data[0] & 0xff) << 0);
            temp = (short) (temp | (data[1] & 0xff) << 8);

        }
        return temp;
    }
    public String getString(byte[] raw, int offset) {
        resetStringArr();
        System.arraycopy(raw, offset, stringArr, 0, stringArr.length);
        return ConversionTool.binaryArray2Ipv4Address(stringArr);
    }

    public String getNormalString(byte[] raw, int offset) {
        resetNormalStringArr();
        System.arraycopy(raw, offset, normalStringArr, 0, normalStringArr.length);
        return new String(normalStringArr);
    }

    public int getInt(byte[] raw, int offset) {
        //intArr初始化
        resetIntArr();
        System.arraycopy(raw, offset, intArr, 0, intArr.length);
        return ConversionTool.toint(intArr);
    }

    public float getFloat(byte[] raw, int offset) {
        resetFloatArr();
        System.arraycopy(raw, offset, floatArr, 0, floatArr.length);
        return ConversionTool.tofloat(floatArr);
    }

    private void resetStringArr() {
        for (int i = 0; i < stringArr.length; i++) {
            stringArr[i] = 0;
        }
    }

    private void resetFloatArr() {
        for (int i = 0; i < floatArr.length; i++) {
            floatArr[i] = 0;
        }
    }

    private void resetIntArr() {
        for (int i = 0; i < intArr.length; i++) {
            intArr[0] = 0;
        }
    }

    private void resetShortArr() {
        for (int i = 0; i < shortArr.length; i++) {
            shortArr[i] = 0;
        }
    }

    private void resetNormalStringArr() {
        for (int i = 0; i < normalStringArr.length; i++) {
            normalStringArr[i] = 0;
        }
    }

    public int appendFloat(float value, byte[] raw, int offset) {
        byte[] bytes = ConversionTool.floattobits(value);
        System.arraycopy(bytes, 0, raw, offset, bytes.length);
        return bytes.length;
    }

    public int appendInt(int value, byte[] raw, int offset) {
        byte[] bytes = ConversionTool.int2BytesArray(offset);
        System.arraycopy(bytes, 0, raw, offset, bytes.length);
        return bytes.length;
    }

    public int appendShort(short value, byte[] raw, int offset) {
        byte[] bytes = ConversionTool.short2bits(value);
        System.arraycopy(bytes, 0, raw, offset, bytes.length);
        return bytes.length;
    }

    public int appendStringIpFormat(String value, byte[] raw, int offset) {
        byte[] bytes = ipStr2ByteArr(value);
        System.arraycopy(bytes, 0, raw, offset, bytes.length);
        return bytes.length;
    }

    public int appendAByte(byte value, byte[] raw, int offset) {
        raw[offset] = value;
        return 1;
    }

    private byte[] ipStr2ByteArr(String ipStr) {
        String[] ipStrs = ipStr.split("[.]");
        byte[] ipArr = new byte[4];
        for (int i = 0; i < ipStrs.length; i++) {
            ipArr[i] = (byte) Integer.parseInt(ipStrs[i]);
        }
        return ipArr;
    }

    public int appendNormalStringArr(byte[] commandBody, String codeOfMonitor, int offset) {
        byte[] strBytes = codeOfMonitor.getBytes();
        System.arraycopy(strBytes, 0, commandBody, offset, strBytes.length);
        return strBytes.length;
    }

    public int appendString32(byte[] commandBody, String codeOfWatchedDevice, int offset) {
        byte[] strBytes = codeOfWatchedDevice.getBytes();
        System.arraycopy(strBytes, 0, commandBody, offset, strBytes.length);
        return 32;
    }

    public int appendARelationship(byte[] command, ResultObj resultObj, int offset) {
        byte[] typeOfMonitor = getTypeOfMonitorByName(resultObj.getTypeOfMonitor(), resultObj.getTypeOfWatchedDevice());
        System.arraycopy(typeOfMonitor, 0, command, offset, typeOfMonitor.length);
        command[offset + 2] = findSortNumberForListOfMonitorDevice(resultObj);
        command[offset + 3] = getChannelSort(resultObj.getChannel(), resultObj.getTypeOfMonitor());
        return 4;
    }

    private byte getChannelSort(String channel, String typeOfMonitor) {

        switch (typeOfMonitor) {
            case "电缆仓温湿度":
                if (channel.equals("温湿度1")) {
                    return 0x00;
                } else {
                    return 0x01;
                }
            case "仪表仓温湿度":
                if (channel.equals("温湿度1")) {
                    return 0x00;
                } else {
                    return 0x01;
                }
            case "开关上触头A相温度监测":
                return Byte.parseByte(channel.trim());
            case "开关上触头B相温度监测":
                return Byte.parseByte(channel.trim());
            case "开关上触头C相温度监测":
                return Byte.parseByte(channel.trim());
            case "开关下触头A相温度监测":
                return Byte.parseByte(channel.trim());
            case "开关下触头B相温度监测":
                return Byte.parseByte(channel.trim());
            case "开关下触头C相温度监测":
                return Byte.parseByte(channel.trim());
            case "电缆连接头A相温度监测":
                return Byte.parseByte(channel.trim());
            case "电缆连接头B相温度监测":
                return Byte.parseByte(channel.trim());
            case "电缆连接头C相温度监测":
                return Byte.parseByte(channel.trim());
            default:
                return 0;
        }
    }

    private byte[] getTypeOfMonitorByName(String typeOfMonitor, String typeOfWachedDevice) {
        byte[] resultArr = new byte[2];
        if (typeOfWachedDevice.equals("环境监测装置")) {
            switch (typeOfMonitor) {
                case "特高频局放监测":
                    resultArr[0] = 0x01;
                    break;
                case "空气式超声局放监测":
                    resultArr[0] = 0x04;
                    break;
                case "红外热像监测":
                    resultArr[0] = 0x05;
                    break;
                case "视频图像监测":
                    resultArr[0] = 0x07;
                    break;
                case "气体监测":
                    resultArr[0] = 0x08;
                    break;
            }
        } else {
            switch (typeOfMonitor) {
                case "特高频局放监测":
                    resultArr[0] = (byte) 0x81;
                    break;
                case "空气式超声局放监测":
                    resultArr[0] = (byte) 0x80;
                    break;
                case "高频电流局放监测":
                    resultArr[0] = (byte) 0x82;
                    break;
                case "暂态地电压局放监测":
                    resultArr[0] = (byte) 0x83;
                    break;
                case "电缆仓温湿度":
                    resultArr[0] = (byte) 0x86;

                    break;
                case "仪表仓温湿度":
                    resultArr[0] = (byte) 0x86;
                    resultArr[1] = 0x01;
                    break;
                case "开关上触头A相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x00;
                    break;
                case "开关上触头B相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x01;
                    break;
                case "开关上触头C相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x02;
                    break;
                case "开关下触头A相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x03;
                    break;
                case "开关下触头B相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x04;
                    break;
                case "开关下触头C相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x05;
                    break;
                case "电缆连接头A相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x06;
                    break;
                case "电缆连接头B相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x07;
                    break;
                case "电缆连接头C相温度监测":
                    resultArr[0] = (byte) 0x84;
                    resultArr[1] = 0x08;
                    break;

            }
        }
        return resultArr;
    }

    private byte findSortNumberForListOfMonitorDevice(ResultObj resultObj) {
        ArrayList<MonitorDevice> listOfMonitor = (ArrayList) CacheData.listOfMonitorDevice;
        for (int i = 0; i < listOfMonitor.size(); i++) {
            if (listOfMonitor.get(i).getCodeOfMonitor().equals(resultObj.getNameOfMoniorDevice())) {
                return listOfMonitor.get(i).getSortNum();
            }
        }
        return 0;
    }

    public int appendString2(String comAdress, byte[] commandBody, int offset) {
        byte[] strBytes = comAdress.getBytes();
        System.arraycopy(strBytes, 0, commandBody, offset, strBytes.length);
        return 2;
    }

    public int appendAJsonStr(String jsonStr, byte[] command, int offset) {
        byte[] jsonArr = jsonStr.getBytes();
        System.arraycopy(jsonArr, 0, command, offset, jsonArr.length);
        return jsonArr.length;

    }
}
