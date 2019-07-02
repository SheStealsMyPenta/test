package com.pd.config.pdonlineconfig.impls;

import android.graphics.Color;
import com.pd.config.pdonlineconfig.constants.Constants;
import com.pd.config.pdonlineconfig.interfaces.InfoCreator;
import com.pd.config.pdonlineconfig.pojo.*;
import com.pd.config.pdonlineconfig.utils.ConversionTool;


public class InfoCreateImpl implements InfoCreator {
    public ByteAndObjConverter converter;

    public InfoCreateImpl() {
        converter = new ByteAndObjConverter();
    }

    //获取设备详细信息
    @Override
    public ControlUnit getBasicInfo(byte[] rawArr) {
        if (rawArr != null && rawArr.length == 40) {
            ControlUnit unit = new ControlUnit();
            byte[] softwarePatchArr = new byte[4];
            System.arraycopy(rawArr, 1, softwarePatchArr, 0, softwarePatchArr.length);
            unit.setSoftwarePatch(softwarePatchArr);
            unit.setHardwarePathch(converter.getShortArr(rawArr, 5));
            unit.setNumberOfFiles(converter.getInt(rawArr, 7));
            unit.setStorageRemain(converter.getInt(rawArr, 11));
            return unit;
        }
        return null;
    }

    @Override
    public TestParams getBasicTestParams(byte[] rawArr) {
        if (rawArr != null && rawArr.length == 40) {
            TestParams params = new TestParams();
            params.setIpAddress(converter.getString(rawArr, 1));
            byte[] portArr = new byte[2];
            System.arraycopy(rawArr, 5, portArr, 0, portArr.length);
            params.setPort(converter.getShortArr(rawArr, 5));
            params.setIntervalForUpload(rawArr[7]);
            params.setDeviceIpAddress(converter.getString(rawArr, 8));
            params.setSubnetMask(converter.getString(rawArr, 12));
            params.setGateway(converter.getString(rawArr, 16));
            params.setRestart(rawArr[20]);
            params.setHour(rawArr[21]);
            params.setMinute(rawArr[22]);
            params.setSyncType(rawArr[23]);
            params.setSycncFrequency(converter.getFloat(rawArr, 24));
            params.setTypeOfInternet(rawArr[28]);
            byte[] comAdd = {rawArr[29], rawArr[30]};
            short toshort = ConversionTool.toshort(comAdd);

            params.setComAdress(String.valueOf(toshort));
            return params;
        }
        return null;
    }

    //弃用
    @Override
    public PdParams getPDParams(byte[] rawArr) {
        PdParams pdParams = new PdParams();
        if (rawArr.length == 40) {
            pdParams.setFilterFrequency(rawArr[1]);
            pdParams.setThresholdValue(rawArr[2]);
            pdParams.setkValue(converter.getFloat(rawArr, 3));
            byte[] bValueArr = new byte[4];
            System.arraycopy(rawArr, 7, bValueArr, 0, bValueArr.length);
            pdParams.setBValue(ConversionTool.tofloat(bValueArr));
            pdParams.setAaSignalValue(rawArr[11]);
            byte[] aaKValueArr = new byte[4];
            System.arraycopy(rawArr, 12, aaKValueArr, 0, aaKValueArr.length);
            byte[] aaBValueArr = new byte[4];
            System.arraycopy(rawArr, 16, aaBValueArr, 0, aaBValueArr.length);
            pdParams.setTypeOfSync(rawArr[21]);
            byte[] syncFreArr = new byte[4];
            System.arraycopy(rawArr, 25, syncFreArr, 0, syncFreArr.length);
            pdParams.setSyncFrequency(ConversionTool.tofloat(syncFreArr));
            return pdParams;
        }

        return null;
    }

    public short toshort(byte[] data) {
        short temp = 0;
        if (data.length >= 2) {
            temp = (short) (temp | (data[0] & 0xff) << 0);
            temp = (short) (temp | (data[1] & 0xff) << 8);

        }
        return temp;
    }

    @Override
    public void getInfoFromLongPack() {

    }

    //获取设备基本信息
    //弃用
    @Override
    public DeviceInfo getDeviceInfo(byte[] body) {

        DeviceInfo info = new DeviceInfo();
        info.setState(body[1]);
        info.setCodeOfDevice(converter.getNormalString(body, 2));
        info.setNumberOfCells(body[10]);
        info.setNumberOfDetectedCells(body[11]);
        String code = info.getCodeOfDevice();
        char[] chars = code.toCharArray();
        switch (chars[2]) {
            case '0':
                info.setTypeOfMonitorDevice(Constants.enviromentDetectDevice);
                break;
            case '1':
                info.setTypeOfMonitorDevice(Constants.wirelessMainController);
                break;
            case '2':
                info.setTypeOfMonitorDevice(Constants.wirelessUnit);
                break;
            case '3':
                info.setTypeOfMonitorDevice(Constants.wirelessMultipleStateMonitor);
                break;
        }
        info.setPhysicAddress(converter.getString(body, 12));
        return info;
    }

    @Override
    public PdParamsUHF getUHFParams(byte[] body) {
        PdParamsUHF pdParamsUHF = new PdParamsUHF();
        pdParamsUHF.setFilterFrequency(body[1]);
        pdParamsUHF.setThresholdValue(body[2]);
        byte[] kValueArr = new byte[4];
        System.arraycopy(body, 3, kValueArr, 0, kValueArr.length);
        pdParamsUHF.setkValue(ConversionTool.tofloat(kValueArr));
        byte[] bValueArr = new byte[4];
        System.arraycopy(body, 7, bValueArr, 0, bValueArr.length);
        pdParamsUHF.setBValue(ConversionTool.tofloat(bValueArr));
        pdParamsUHF.setDischargeValue(body[12]);
        return pdParamsUHF;
    }

    @Override
    public LightParams getLightParams(byte[] body) {
        LightParams params = new LightParams();
        params.setResolution(body[1]);
        params.setLightness(body[2]);
        params.setConstract(body[3]);
        return params;

    }

    @Override
    public PdParamsAA getAaParams(byte[] body) {
        PdParamsAA pdParamsAA = new PdParamsAA();
        pdParamsAA.setAaSignalValue(body[1]);
        pdParamsAA.setAaKValue(converter.getFloat(body, 2));
        pdParamsAA.setAaBValue(converter.getFloat(body, 6));
        pdParamsAA.setDischargeValue(body[10]);
        return pdParamsAA;
    }

    @Override
    public String getCurrentDeviceSn(byte[] body) {
        int offset = 0;
        for (int i = 1; i < body.length; i++) {
            if (body[i] == 0) {
                offset = i;
                break;
            }
        }
        byte[] codeArr = new byte[offset];
        System.arraycopy(body, 1, codeArr, 0, codeArr.length);
        String reuslt = new String(codeArr);
        return reuslt;
    }

    @Override
    public WatchedDevice getTheWatchedDevice(byte[] body) {
        WatchedDevice watchedDevice = new WatchedDevice();
//        watchedDevice.set
        return watchedDevice;
    }

    @Override
    public String getJSONStr(byte[] body) {
        short length = converter.getShortArr(body, 5);
        byte[] jsonArr = new byte[length];
        System.arraycopy(body, 10, jsonArr, 0, jsonArr.length);

        return new String(jsonArr);
    }

    @Override
    public Packages getTwoDimenArray(byte[] finalResult) {
        Packages aPack = new Packages();
        long start = System.currentTimeMillis();
        int lengthOfData = converter.getInt(finalResult, 8);
        //辐射率offset 12 ,测试距离13 大气温度17，相对湿度18,反射温度22，温宽 26 30，
        //当前温度，最小温度，最大温度
        short currentTemp = converter.getShortArr(finalResult, 34);
        short minTemp;
        if (finalResult[36] == 0xff && finalResult[37] == 0xff) {
            minTemp = 0;
        } else {
            minTemp = converter.getShortArr(finalResult, 36);
        }
        short maxTemp;
        if (finalResult[38] == 0xff && finalResult[39] == 0xff) {
            maxTemp = 0;
        } else {
            maxTemp = converter.getShortArr(finalResult, 38);
        }
        short avg;
        if (finalResult[40] == 0xff && finalResult[41] == 0xff) {
            avg = 0;
        } else {
            avg = converter.getShortArr(finalResult, 40);
        }
        short minTempX = converter.getShortArr(finalResult, 42);
        short minTempY = converter.getShortArr(finalResult, 44);
        short maxTempX = converter.getShortArr(finalResult, 46);
        short maxTempY = converter.getShortArr(finalResult, 48);

        short width = converter.getShortArr(finalResult, 50);
        short height = converter.getShortArr(finalResult, 52);


        int offsetForColor = 54;
        int[][] result = new int[height][width];
        byte[] shortBytes = new byte[2];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                shortBytes[0] = finalResult[offsetForColor];
                shortBytes[1] = finalResult[offsetForColor + 1];

                short temp = 0;
                if (shortBytes.length >= 2) {
                    temp = (short) (temp | (shortBytes[0] & 0xff) << 0);
                    temp = (short) (temp | (shortBytes[1] & 0xff) << 8);

                }
                short nColor565 = temp;
                int r, g, b;
                r = (int) (nColor565 & 0xF800);
                r >>= 8;    // r >>= 11; r <<= 3

                g = (int) (nColor565 & 0x07E0);
                g >>= 3;    //g >>= 5; g <<= 2;

                b = (int) (nColor565 & 0x001F);
                b <<= 3;
                result[i][j] = Color.rgb(r, g, b);
//                result[i][j] = 0xFF000000;
//
                offsetForColor += 2;

            }
        }
        long end = System.currentTimeMillis();
        aPack.setDataSet(result);
        aPack.setCurrentTemp(currentTemp / 10.0f);
        if (minTemp != 0) {
            aPack.setMinTemp(minTemp / 10.0f);
            aPack.setMinTempX(minTempX);
            aPack.setMinTempY(minTempY);
        }
        if (maxTemp != 0) {
            aPack.setMaxTemp(maxTemp / 10.0f);
            aPack.setMaxTempX(maxTempX);
            aPack.setMaxTempY(maxTempY);
        }
        if (avg != 0) {
            aPack.setAvgTemp(avg / 10.0f);
        }

        System.out.println("读取数据耗时" + (end - start) + "毫秒");
        return aPack;
    }

    @Override
    public InfraredParams getInfraredParams(byte[] bytes) {
        InfraredParams params = new InfraredParams();
        params.setRate(converter.getFloat(bytes, 1));
        params.setDistance(bytes[5]);
        params.setAtoTemp(converter.getFloat(bytes, 6));
        params.setRelateTemp(bytes[10]);
        params.setReflectTemp(converter.getFloat(bytes, 11));


        return params;
    }

    public int ConvertRgb565(short nColor565) {
        int r, g, b;
        r = (int) (nColor565 & 0xF800);
        r >>= 8;    // r >>= 11; r <<= 3

        g = (int) (nColor565 & 0x07E0);
        g >>= 3;    //g >>= 5; g <<= 2;

        b = (int) (nColor565 & 0x001F);
        b <<= 3;

        return Color.rgb(r, g, b);

    }

}
