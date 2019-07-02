package com.pd.config.pdonlineconfig.impls;

import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.interfaces.InternetManager;
import com.pd.config.pdonlineconfig.pojo.*;
import com.pd.config.pdonlineconfig.utils.ConversionTool;
import com.pd.config.pdonlineconfig.utils.SwapRandCipher;

import java.util.ArrayList;
import java.util.List;


public class InternetManagerImpl implements InternetManager {
    private ByteAndObjConverter converter;

    public InternetManagerImpl() {
        converter = new ByteAndObjConverter();
    }

    @Override
    public byte[] makingCommand(String type) {
        switch (type) {
            case CommandTypes.GET_DEVICE_INFO: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.GET_DEVICE_INFO);
                calculateCrcAndEncrptArr(command);
                return command;
            }

            case CommandTypes.GET_BASIC_INFO: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.GET_BASIC_INFO);
                calculateCrcAndEncrptArr(command);
                //加密后返回
                return command;
            }

            case CommandTypes.GET_BASIC_TESTPARAMS: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.GET_BASIC_TESTPARAMS);
                calculateCrcAndEncrptArr(command);
                return command;

            }
            case CommandTypes.GET_UHF_PARAMS: {
                byte[] command;
                if (CacheData.typeOfSend.equals("uhf")) {
                    command = getARawArray(Command.ENCRIPT, Command.GET_UHF_PARAMS);
                } else {
                    command = getARawArray(Command.ENCRIPT, Command.GET_HF_PD_PARAMS);
                }
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
//                converter.appendAByte((byte) CacheData.sort, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CommandTypes.GET_INFRARED_PARAMS: {

                byte[] command = getARawArray(Command.ENCRIPT, Command.GET_INFRARED_PARAMS);
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
//                converter.appendAByte((byte) CacheData.sort, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CommandTypes.GET_AA_PARAMS: {
                byte[] command;
                if (CacheData.typeOfSend.equals("ae")) {
                    command = getARawArray(Command.ENCRIPT, Command.GET_AA_PARAMS);
                } else {
                    command = getARawArray(Command.ENCRIPT, Command.GET_TEV_PD_PARAMS);
                }

                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
//                converter.appendAByte((byte) CacheData.sort, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CommandTypes.STARTTEST: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.STARTTEST);
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
                offset += converter.appendAByte(Command.START, command, offset);
                converter.appendAByte(Command.UHF, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CommandTypes.GET_LIGHT_PARAMS: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.GET_LIGHT_PARAMS);
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CommandTypes.ExitCommandMode: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.ExitCommandMode);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CommandTypes.STOPTEST: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.STOPTEST);
                int offset = 6;
                offset += converter.appendAByte(Command.MAINCONTROLCELL, command, offset);
                offset += converter.appendAByte(Command.STOP, command, offset);
                converter.appendAByte(Command.UHF, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;

            }
            case CommandTypes.SET_DEVICE_SN:
                //封装头部
                return getARawArray(Command.ENCRIPT, Command.SET_DEVICE_SN);
            case CommandTypes.GET_LOG:

                break;
            case CommandTypes.DELETE_DATA:
                return getCommandArr(true, Command.DELETE_DATA);
            case CommandTypes.DELETE_LOG:
                return getCommandArr(true, Command.DELETE_LOG);

        }
        return new byte[0];
    }

    //运行参数设置
    @Override
    public byte[] makingCommandWithTestParams(TestParams params) {
        byte[] command = getARawArray((byte) 0x01, (byte) 0x04);
        //设置Ip地址
        int offset = 6;
        offset += converter.appendStringIpFormat(params.getIpAddress(), command, offset);
        offset += converter.appendShort(params.getPort(), command, offset);
        //设置数据上传时间
        offset += converter.appendAByte(params.getIntervalForUpload(), command, offset);
        //设置设备Ip地址
        offset += converter.appendStringIpFormat(params.getDeviceIpAddress(), command, offset);
        //子网掩码
        offset += converter.appendStringIpFormat(params.getSubnetMask(), command, offset);
        //网关
        offset += converter.appendStringIpFormat(params.getGateway(), command, offset);
        //定时重启
        offset += converter.appendAByte(params.getRestart(), command, offset);
        offset += converter.appendAByte(params.getHour(), command, offset);
        offset += converter.appendAByte(params.getMinute(), command, offset);
        offset += converter.appendAByte(params.getSyncType(), command, offset);
        //最后一位不需要得到偏移值
        offset += converter.appendFloat(params.getSycncFrequency(), command, offset);
        offset += converter.appendAByte((byte) 0, command, offset);
        short address = Short.parseShort(params.getComAdress());
        converter.appendShort(address, command, offset);
        //计算CRC和加密
        calculateCrcAndEncrptArr(command);
        return command;
    }

    private void calculateCrcAndEncrptArr(byte[] command) {
        byte[] crcArr = crcSumFunc(command, 3, 42);//校验
        command[45] = crcArr[0];
        command[46] = crcArr[1];
        SwapRandCipher.encrypt(command, 5, 42, command[4]);//加密
    }

    private void calculateCrcAndEncrptArrLongPack(byte[] command) {
        byte[] crcArr = crcSumFunc(command, 3, 1032);//校验
        command[1035] = crcArr[0];
        command[1036] = crcArr[1];
        SwapRandCipher.encrypt(command, 5, 1032, command[4]);//加密
    }

    @Override
    public byte[] notifyDataReceiveSuccess(byte[] currenPack) {
        return new byte[0];
    }

    @Override
    public byte[] resolveArrayFromShortPack(byte[] resultArr) {
        if ((char) resultArr[0] != 'B' ||
                (char) resultArr[1] != 'E' ||
                (char) resultArr[2] != 'G' ||
                (char) resultArr[47] != 'E' ||
                (char) resultArr[48] != 'N' ||
                (char) resultArr[49] != 'D') {
            return new byte[0];
        }
        if (resultArr[3] == 0x01) {
            //解密
            byte code = resultArr[4];
            byte[] body = new byte[40];
            System.arraycopy(resultArr, 5, body, 0, body.length);
            SwapRandCipher.decrypt(body, 0, body.length, (int) code);
            return body;
        } else {
            byte code = resultArr[4];
            byte[] body = new byte[40];
            System.arraycopy(resultArr, 5, body, 0, body.length);
            SwapRandCipher.decrypt(body, 0, body.length, (int) code);
            return body;
        }
    }

    @Override
    public byte[] resolveArrayFromLongPack(byte[] resultArr) {

        if ((char) resultArr[0] != 'B' ||
                (char) resultArr[1] != 'E' ||
                (char) resultArr[2] != 'G' ||
                (char) resultArr[1037] != 'E' ||
                (char) resultArr[1038] != 'N' ||
                (char) resultArr[1039] != 'D') {
            return new byte[0];
        }
        //解密

        byte[] body = new byte[1030];
        System.arraycopy(resultArr, 5, body, 0, body.length);
        if (resultArr[3] == 0x01) {
            byte code = resultArr[4];
            SwapRandCipher.decrypt(body, 0, body.length, (int) code);
        }

        return body;

        //body是可用的数据包
    }

    private byte[] getCommandArr(boolean encrpt, byte order) {
        byte[] command = new byte[50];
        //封装头部
        command[0] = 'B';
        command[1] = 'E';
        command[2] = 'G';
        command[3] = 0x01;//加密
        command[4] = (byte) SwapRandCipher.getPublicKey();
        command[5] = order;
        byte[] crcArr = crcSumFunc(command, 3, 42);//校验
        command[45] = crcArr[0];
        command[46] = crcArr[1];
        command[47] = 'E';
        command[48] = 'N';
        command[49] = 'D';
        if (encrpt) {
            SwapRandCipher.encrypt(command, 5, 42, command[4]);//加密
        }
        return command;
    }

    @Override
    public byte[] makePdParamsCommandUHF(PdParamsUHF params) {
        byte[] command;
        if (CacheData.typeOfSend.equals("uhf")) {
            command = getARawArray(Command.ENCRIPT, Command.SET_UHF_CONFIG);
        } else {
            command = getARawArray(Command.ENCRIPT, Command.SET_HF_CONFIG);
        }
        byte[] commandBody = new byte[40];
        //包装头部
        commandBody[0] = Command.SET_UHF_CONFIG;
        //初始化偏移值
        int offset = 1;
        offset += converter.appendNormalStringArr(commandBody, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendAByte(params.getFilterFrequency(), commandBody, offset);
        offset += converter.appendAByte(params.getThresholdValue(), commandBody, offset);
        offset += converter.appendFloat(params.getkValue(), commandBody, offset);
        //最后一个数组不需要返回偏移值
        offset += converter.appendFloat(params.getBValue(), commandBody, offset);
        offset += converter.appendAByte(params.getDischargeValue(), commandBody, offset);
        converter.appendAByte(params.getChartValue(), commandBody, offset);
        //把命令字节数组放回原素组
        System.arraycopy(commandBody, 0, command, 5, commandBody.length);
        calculateCrcAndEncrptArr(command);
        return command;

    }

    @Override
    public byte[] makePdParamsCommandAA(PdParamsAA pdParamsAA) {
        byte[] command;
        if (CacheData.typeOfSend.equals("ae")) {
            command = getARawArray(Command.ENCRIPT, Command.SET_AA_CONFIG);
        } else {
            command = getARawArray(Command.ENCRIPT, Command.SET_TEV_PD_PARAMS);
        }

        byte[] commandBody = new byte[40];
        //初始化头部
        commandBody[0] = Command.SET_AA_CONFIG;
        int offset = 1;
        offset += converter.appendNormalStringArr(commandBody, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendAByte(pdParamsAA.getAaSignalValue(), commandBody, offset);
        offset += converter.appendFloat(pdParamsAA.getAaKValue(), commandBody, offset);
        //最后的数组不需要返回offset
        offset += converter.appendFloat(pdParamsAA.getAaBValue(), commandBody, offset);
        converter.appendAByte(pdParamsAA.getDischargeValue(), commandBody, offset);
        System.arraycopy(commandBody, 0, command, 5, commandBody.length);
        calculateCrcAndEncrptArr(command);

        return command;
    }

    @Override
    public byte[] makeLightParamsCommand(LightParams lightParams) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.SET_LIGNT_CONFIG);
        byte[] commandBody = new byte[40];
        //封装头部
        commandBody[0] = Command.SET_LIGNT_CONFIG;
        int offset = 1;
        offset += converter.appendNormalStringArr(commandBody, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendAByte(lightParams.getResolution(), commandBody, offset);
        offset += converter.appendAByte(lightParams.getLightness(), commandBody, offset);
        //最后数组不需要返回偏移量
        converter.appendAByte(lightParams.getConstract(), commandBody, offset);
        System.arraycopy(commandBody, 0, command, 5, commandBody.length);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToConfigTEVParams(TEVParams params) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.TEVCONFIG);
        int offset = 6;
        offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendAByte(params.getTevMonitorThreathreshold(), command, offset);
        offset += converter.appendFloat(params.getTevKValue(), command, offset);
        offset += converter.appendFloat(params.getTevBValue(), command, offset);
        offset += converter.appendAByte(params.getTevDiaschargeThreathreshold(), command, offset);
        return command;
    }

    @Override
    public byte[] makeCommandToConfigHFCTParams(PdParamsUHF params) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.HFCTCONFIG);
        int offset = 6;
        offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendAByte((byte) 0x01, command, offset);
        offset += converter.appendAByte(params.getFilterFrequency(), command, offset);
        offset += converter.appendAByte(params.getThresholdValue(), command, offset);
        offset += converter.appendFloat(params.getkValue(), command, offset);
        offset += converter.appendFloat(params.getBValue(), command, offset);
        offset += converter.appendAByte(params.getDischargeValue(), command, offset);
        return command;
    }

    @Override
    public byte[] makingCommandTestStart(String typeOfCommand, String typeOfTest) {
        switch (typeOfTest) {
            case CacheData.UHF: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.STARTTEST);
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
                offset += converter.appendAByte(Command.START, command, offset);
                converter.appendAByte(Command.UHF, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CacheData.AA: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.STARTTEST);
                //封装头部
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
                offset += converter.appendAByte(Command.START, command, offset);
                converter.appendAByte(Command.AA, command, offset);
                calculateCrcAndEncrptArr(command);
                //加密后返回
                return command;
            }
            case CacheData.Light: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.STARTTEST);
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
                offset += converter.appendAByte(Command.START, command, offset);
                converter.appendAByte(Command.UHF, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }
            case CacheData.infrared: {
                byte[] command = getARawArray(Command.ENCRIPT, Command.STARTTEST);
                int offset = 6;
                offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
                offset += converter.appendAByte(Command.START, command, offset);
                converter.appendAByte((byte) 0x05, command, offset);
                calculateCrcAndEncrptArr(command);
                return command;
            }

        }
        return new byte[0];
    }

    @Override
    public byte[] makingCommandWithDeviceSN(String setDeviceSn, byte code) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.SET_DEVICE_SN);
        command[6] = code;
        byte[] bytes = setDeviceSn.getBytes();
        System.arraycopy(bytes, 0, command, 7, bytes.length);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandWithDeviceCode(String deviceCode) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.SET_DEVICE_CODE);
        command[6] = Command.SET;
        byte[] bytes = deviceCode.getBytes();
        System.arraycopy(bytes, 0, command, 7, bytes.length);
        calculateCrcAndEncrptArr(command);
        return command;

    }

    @Override
    public byte[] makeCommandToGetBasicInfo(byte sortNum) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.GET_BASIC_INFO);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToAddAMonitorDevice(MonitorDevice monitorDevice) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.ADD_MONITOR_DEVICE);
        byte[] commandBody = new byte[40];
        commandBody[0] = Command.ADD_MONITOR_DEVICE;
        int offset = 1;
        offset += converter.appendAByte(Command.ADD, commandBody, offset);
        offset += converter.appendNormalStringArr(commandBody, monitorDevice.getCodeOfMonitor(), offset);
        String ips = monitorDevice.getPhysicAddress();
        if (!ips.equals("")) {
            short ipShort = Short.parseShort(ips);
            converter.appendShort(ipShort, commandBody, offset);
        }
        System.arraycopy(commandBody, 0, command, 5, commandBody.length);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToGetWatchedDevice(byte currentSort) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.GET_WATCHED_INFO);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToAddAWatchedDeviceInfo(WatchedDevice watchedDevice) {
        byte[] command = getALongRawArray(Command.ENCRIPT, Command.ADD_WATCHED);
        byte type = 0x00;
        if (watchedDevice.getTypeOfWatchedDevice().equals("环境")) {
            type = 0x00;
        } else {
            type = 0x01;
        }
        int offset = 6;
        offset += converter.appendAByte(Command.ADD, command, offset);
        offset += converter.appendString32(command, watchedDevice.getCodeOfWatchedDevice(), offset);
        offset += converter.appendString32(command, watchedDevice.getNameOfWatchedDevice(), offset);
        offset += converter.appendAByte(type, command, offset);
        offset += converter.appendAByte((byte) watchedDevice.getNumberOfWatchedKind(), command, offset);
        offset += converter.appendShort((short) watchedDevice.getJsonStr().getBytes().length, command, offset);
        offset += converter.appendAJsonStr(watchedDevice.getJsonStr(), command, offset);
        calculateCrcAndEncrptArrLongPack(command);
        return command;
    }

    @Override
    public byte[] makeCommandToBindMonitorDeviceAndWatchedDeviceTogether(String codeOfWatchedDevice, String json, int numberOfJson) {
        byte[] command = getALongRawArray(Command.ENCRIPT, Command.BIND_MONITORANDWATCHEDDEVICE);
        int offset = 6;
        offset += converter.appendString32(command, codeOfWatchedDevice, offset);
        offset += converter.appendAByte((byte) numberOfJson, command, offset);
        short length = (short) json.getBytes().length;
        offset += converter.appendShort(length, command, offset);
        offset += converter.appendNormalStringArr(command, json, offset);
        calculateCrcAndEncrptArrLongPack(command);
        return command;


    }

    @Override
    public byte[] makeCommandToDeleteMonitorDevice(String codeOfMonitorDevice) {
        byte[] command = getARawArray(Command.ENCRIPT, (byte) 0x0E);
        int offset = 6;
        offset += converter.appendAByte(Command.DELETE, command, offset);
        converter.appendNormalStringArr(command, codeOfMonitorDevice, offset);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToGetMonitorInfo() {
        byte[] command = getARawArray(Command.ENCRIPT, Command.GET_MONITOR_DEVICE);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToDeleteWatchedDevice(String codeOfWatchedDevice) {
        byte[] command = getARawArray(Command.ENCRIPT, (byte) 0x0F);
        int offset = 6;
        offset += converter.appendAByte(Command.DELETE, command, offset);
        converter.appendNormalStringArr(command, codeOfWatchedDevice, offset);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public int calculateThePackLength(WatchedDevice watchedDevice) {
        int lengthOfAll = 0;

        lengthOfAll += watchedDevice.getCodeOfWatchedDevice().getBytes().length;
        lengthOfAll += watchedDevice.getJsonStr().getBytes().length;

        return lengthOfAll;
    }

    @Override
    public List<byte[]> sendseveralWatchedDevicePack(WatchedDevice watchedDevice) {
        byte[] bytesForCode = watchedDevice.getCodeOfWatchedDevice().getBytes();
        byte[] bytesForJson = watchedDevice.getJsonStr().getBytes();
        byte[] bytesOfAll = new byte[bytesForCode.length + bytesForJson.length];
        System.arraycopy(bytesForCode, 0, bytesOfAll, 0, bytesForCode.length);
        System.arraycopy(bytesForJson, 0, bytesOfAll, bytesForCode.length, bytesForJson.length);
        ArrayList<byte[]> listOfBytes = new ArrayList<>();
        int remain = bytesOfAll.length;
        int offset = 0;
        short jsonLen;
        int numberOfPack = remain / 1028 + 1;

        for (int i = 0; i < numberOfPack - 1; i++) {
            byte[] command = getALongRawArray(Command.ENCRIPT, Command.ADD_WATCHED_LONG_PACK);
            command[6] = (byte) numberOfPack;
            command[7] = (byte) i;
            System.arraycopy(bytesOfAll, offset, command, 8, 1028);
            calculateCrcAndEncrptArrLongPack(command);
            remain = remain - 1028;
            offset += 1028;
            listOfBytes.add(command);
        }
        if (remain != 0) {
            byte[] command = getALongRawArray(Command.ENCRIPT, Command.ADD_WATCHED_LONG_PACK);
            command[6] = (byte) numberOfPack;
            command[7] = (byte) numberOfPack;
            System.arraycopy(bytesOfAll, offset, command, 0, remain);
            calculateCrcAndEncrptArrLongPack(command);
            listOfBytes.add(command);
        }

        return listOfBytes;
    }

    @Override
    public int calculateBindProtocolLength(String codeOfDevice, String bindJson) {

        return 35 + bindJson.getBytes().length;
    }

    @Override
    public List<byte[]> sendservaralBindProtocolPack(String codeOfDevice, String bindJson) {
        byte[] bytesForCode = codeOfDevice.getBytes();
        byte[] bytesForJson = bindJson.getBytes();
        byte[] bytesOfAll = new byte[bytesForCode.length + bytesForJson.length];
        System.arraycopy(bytesForCode, 0, bytesOfAll, 0, bytesForCode.length);
        System.arraycopy(bytesForJson, 0, bytesOfAll, bytesForCode.length, bytesForJson.length);
        ArrayList<byte[]> listOfBytes = new ArrayList<>();
        int remain = bytesOfAll.length;
        int offset = 0;
        int numberOfPack = remain / 1028 + 1;

        for (int i = 0; i < numberOfPack - 1; i++) {
            byte[] command = getALongRawArray(Command.ENCRIPT, Command.BIND_MONITORANDWATCHEDDEVICE);
            command[6] = (byte) numberOfPack;
            command[7] = (byte) i;
            System.arraycopy(bytesOfAll, offset, command, 8, 1028);
            calculateCrcAndEncrptArrLongPack(command);
            remain = remain - 1028;
            offset += 1028;
            listOfBytes.add(command);
        }
        if (remain != 0) {
            byte[] command = getALongRawArray(Command.ENCRIPT, Command.BIND_MONITORANDWATCHEDDEVICE);
            command[6] = (byte) numberOfPack;
            command[7] = (byte) numberOfPack;
            System.arraycopy(bytesOfAll, offset, command, 0, remain);
            calculateCrcAndEncrptArrLongPack(command);
            listOfBytes.add(command);
        }

        return listOfBytes;
    }

    @Override
    public byte[] makeCommandToGetPhoto() {
        byte[] command = getARawArray(Command.ENCRIPT, Command.STARTTEST);

        int offset = 6;
        offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendAByte(Command.START, command, offset);
        offset += converter.appendAByte(Command.LIGHT, command, offset);
        offset += converter.appendAByte((byte) 0x01, command, offset);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToGetTempValue(IndexObj indexObj) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.GET_TEM_VALUE);
        int offset = 6;
        offset += converter.appendShort(indexObj.getxIndex(), command, offset);
        converter.appendShort(indexObj.getyIndex(), command, offset);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    @Override
    public byte[] makeCommandToSetInfraredParams(InfraredParams infraredParams) {
        byte[] command = getARawArray(Command.ENCRIPT, Command.SET_INFRARED_PARAMS);
        int offset = 6;
        offset += converter.appendNormalStringArr(command, CacheData.currentMonitorDevice.getCodeOfDevice(), offset);
        offset += converter.appendFloat(infraredParams.getRate(), command, offset);
        offset += converter.appendAByte(infraredParams.getDistance(), command, offset);
        offset += converter.appendFloat(infraredParams.getAtoTemp(), command, offset);
        offset += converter.appendAByte(infraredParams.getRelateTemp(), command, offset);
        converter.appendFloat(infraredParams.getReflectTemp(), command, offset);
        calculateCrcAndEncrptArr(command);
        return command;
    }

    private byte[] crcSumFunc(byte[] data, int offset, int len) {
        byte[] temp = new byte[len];
        System.arraycopy(data, offset, temp, 0, len);
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            sum += temp[i] & 0xff;
        }
        sum &= 0xffff;
        return ConversionTool.short2bits((short) sum);

    }

    private byte[] ipStr2ByteArr(String ipStr) {
        String[] ipStrs = ipStr.split("[.]");
        byte[] ipArr = new byte[4];
        for (int i = 0; i < ipStrs.length; i++) {
            ipArr[i] = (byte) Integer.parseInt(ipStrs[i]);
        }
        return ipArr;
    }

    private byte[] getARawArray(byte encrpt, byte head) {
        byte[] command = new byte[50];
        command[0] = 'B';
        command[1] = 'E';
        command[2] = 'G';
        command[3] = encrpt;
        command[5] = head;
        if (encrpt == 0x01) {
            command[4] = (byte) SwapRandCipher.getPublicKey();
        }
        command[47] = 'E';
        command[48] = 'N';
        command[49] = 'D';
        return command;
    }

    private byte[] getALongRawArray(byte encrpt, byte head) {
        byte[] command = new byte[1040];
        command[0] = 'B';
        command[1] = 'E';
        command[2] = 'G';
        command[3] = encrpt;
        command[5] = head;
        if (encrpt == 0x01) {
            command[4] = (byte) SwapRandCipher.getPublicKey();
        }
        command[1037] = 'E';
        command[1038] = 'N';
        command[1039] = 'D';

        return command;
    }

}
