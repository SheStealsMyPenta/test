package com.pd.config.pdonlineconfig.net;

import android.os.Bundle;
import android.os.Message;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.interfaces.InfoCreator;
import com.pd.config.pdonlineconfig.interfaces.InternetManager;
import com.pd.config.pdonlineconfig.pojo.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class ControlUnitMessager extends Thread {
    private InternetManager manager;
    private NetHandler netHandler;
    private String ip;
    public String typeOfCommand;
    private String typeOfTest;
    private String deviceSN;
    private String deviceCode;
    private byte currentSort;
    private String codeOfMonitorDevice;
    private String codeOfWatchedDevice; //被测对象编号
    private InfraredParams infraredParams;

    public InfraredParams getInfraredParams() {
        return infraredParams;
    }

    public void setInfraredParams(InfraredParams infraredParams) {
        this.infraredParams = infraredParams;
    }

    private String bindJson;
    private boolean photo=false;
    private IndexObj indexObj;

    public IndexObj getIndexObj() {
        return indexObj;
    }

    public void setIndexObj(IndexObj indexObj) {
        this.indexObj = indexObj;
    }

    public String getBindJson() {
        return bindJson;
    }

    public void setBindJson(String bindJson) {
        this.bindJson = bindJson;
    }

    public String getWatchedDevice() {
        return codeOfWatchedDevice;
    }

    public void setWatchedDevice(String watchedDevice) {
        this.codeOfWatchedDevice = watchedDevice;
    }

    public String getCodeOfMonitorDevice() {
        return codeOfMonitorDevice;
    }

    public void setCodeOfMonitorDevice(String codeOfMonitorDevice) {
        this.codeOfMonitorDevice = codeOfMonitorDevice;
    }

    public void setCurrentSort(byte currentSort) {
        this.currentSort = currentSort;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public void setTypeOfTest(String typeOfTest) {
        this.typeOfTest = typeOfTest;
    }

    public void setTypeOfCommand(String typeOfCommand) {
        this.typeOfCommand = typeOfCommand;
    }

    private boolean broadCast = false;
    private int port;
    private byte[] resultArr;
    private InfoCreator infoCreator;
    private TestParams params;
    private PdParamsAA pdParamsAA;
    private LightParams lightParams;
    private MonitorDevice monitorDevice;
    private WatchedDevice watchedDevice;
    private List<ResultObj> listOfResultObj;//绑定集合
    private String codeOfDevice;
    private int numberOfJson;

    public int getNumberOfJson() {
        return numberOfJson;
    }

    public void setNumberOfJson(int numberOfJson) {
        this.numberOfJson = numberOfJson;
    }

    public void setCodeOfDevice(String codeOfDevice) {
        this.codeOfDevice = codeOfDevice;
    }

    public void setListOfResultObj(List<ResultObj> listOfResultObj) {
        this.listOfResultObj = listOfResultObj;
    }

    public void setWatchedDevice(WatchedDevice watchedDevice) {
        this.watchedDevice = watchedDevice;
    }

    public void setMonitorDevice(MonitorDevice monitorDevice) {
        this.monitorDevice = monitorDevice;
    }

    DatagramSocket udpSocket;

    public void setBroadCast(boolean broadCast) {
        this.broadCast = broadCast;
    }

    private PdParamsUHF pdParamsUHF;

    public void setPdParamsUHF(PdParamsUHF pdParamsUHF) {
        this.pdParamsUHF = pdParamsUHF;
    }

    public void setParams(TestParams params) {
        this.params = params;
    }

    public void setInfoCreator(InfoCreator infoCreator) {
        this.infoCreator = infoCreator;
    }

    public void setManager(InternetManager manager) {
        this.manager = manager;
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public ControlUnitMessager(String ip, int port, String typeOfCommand) {
        this.typeOfCommand = typeOfCommand;
        this.ip = ip;
        this.port = port;
        resultArr = getResultArr(typeOfCommand);
    }

    public ControlUnitMessager(String ip, int port) {
        this.ip = ip;
        this.port = port;
        resultArr = getResultArr(typeOfCommand);
    }

    public ControlUnitMessager(int port) {
        this.port = port;
    }

    private byte[] getResultArr(String typeOfCommand) {

        return new byte[40];
    }

    @Override
    public void run() {
        try {
            if (!broadCast) {
                udpSocket = new DatagramSocket();
                if (manager != null) {
                    if (params != null) {
                        send(manager.makingCommandWithTestParams(params));
//                        if (deviceSN != null) {
//                            send(manager.makingCommandWithDeviceSN(deviceSN, (byte) 0x01));
//                        }
                    } else if (pdParamsUHF != null) {
                        send(manager.makePdParamsCommandUHF(pdParamsUHF));
                    } else if (pdParamsAA != null) {
                        send(manager.makePdParamsCommandAA(pdParamsAA));
                    } else if (lightParams != null) {
                        send(manager.makeLightParamsCommand(lightParams));
                    } else if (typeOfTest != null) {
                        byte[] bytes = manager.makingCommandTestStart(typeOfCommand, typeOfTest);
                        DatagramPacket udpPack = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(CacheData.ip), port);
                        udpSocket.send(udpPack);
//                    receiveFromUdpSocket(udpSocket);
                        return;
                    } else if (deviceCode != null && CommandTypes.SET_DEVICE_CODE.equals(typeOfCommand)) {
                        send(manager.makeCommandWithDeviceCode(deviceCode));
                    } else if (typeOfCommand.equals(CommandTypes.GET_BASIC_INFO)) {
                        send(manager.makeCommandToGetBasicInfo(currentSort));
                    } else if (monitorDevice != null && typeOfCommand.equals(CommandTypes.ADD_MONITOR_DEVICE)) {
                        send(manager.makeCommandToAddAMonitorDevice(monitorDevice));
                    } else if (typeOfCommand.equals(CommandTypes.GET_WATCHEDDEVICE_INFO)) {
                        send(manager.makeCommandToGetWatchedDevice(currentSort));
                    } else if (watchedDevice != null && typeOfCommand.equals(CommandTypes.ADD_WATCHED_DEVICE)) {
                        int length = manager.calculateThePackLength(watchedDevice);
                        if (length < 1030) {
                            send(manager.makeCommandToAddAWatchedDeviceInfo(watchedDevice));
                        } else {
                            //把数据分包传出去
                            List<byte[]> listOfCommandToBeSend = manager.sendseveralWatchedDevicePack(watchedDevice);
                            for (byte[] bytes : listOfCommandToBeSend) {
                                send(bytes);
                            }
                        }

                    } else if (typeOfCommand.equals(CommandTypes.BINDDEVICETOGETHER)) {
                        int length = manager.calculateBindProtocolLength(codeOfDevice, bindJson);
                        if(length<1030){
                            send(manager.makeCommandToBindMonitorDeviceAndWatchedDeviceTogether(codeOfDevice, bindJson, numberOfJson));
                        }else {
                            List<byte[]> listOfCommandToBeSend = manager.sendservaralBindProtocolPack(codeOfDevice,bindJson);
                        }


                    } else if (typeOfCommand.equals(CommandTypes.GET_MONITOR_LIST)) {
                        send(manager.makeCommandToGetMonitorInfo());
                    } else if (typeOfCommand.equals(CommandTypes.DELETEADEVICE)) {
                        send(manager.makeCommandToDeleteMonitorDevice(deviceCode));
                    } else if (typeOfCommand.equals(CommandTypes.DELETEAWATCHEDDEVICE)) {
                        send(manager.makeCommandToDeleteWatchedDevice(codeOfWatchedDevice));
                    } else if(typeOfCommand.equals(CommandTypes.TAKEPHOTO)&&photo){
                        send(manager.makeCommandToGetPhoto());
                    } else if(typeOfCommand.equals(CommandTypes.GET_TEMP_VALUE)&&indexObj!=null){
                        send(manager.makeCommandToGetTempValue(indexObj));
                    } else if(typeOfCommand.equals(CommandTypes.SET_INFRARED_PARAMS)&&infraredParams!=null){
                        send(manager.makeCommandToSetInfraredParams(infraredParams));
                    }
                    else {
                        byte[] bytes = manager.makingCommand(typeOfCommand);
                        DatagramPacket udpPack = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(CacheData.ip), port);
                        udpSocket.send(udpPack);
//                        if (typeOfCommand.equals(CommandTypes.GET_BASIC_TESTPARAMS)) {
//                            byte[] makingCommandWithDeviceSN = manager.makingCommandWithDeviceSN(CommandTypes.SET_DEVICE_SN, (byte) 0);
//                            DatagramPacket udpPack1 = new DatagramPacket(makingCommandWithDeviceSN, makingCommandWithDeviceSN.length, InetAddress.getByName(CacheData.ip), port);
//                            udpSocket.send(udpPack1);
//                        }
//                    receiveFromUdpSocket(udpSocket);
                        return;
                    }

                }
                udpSocket.close();
            } else {
                typeOfCommand = CommandTypes.GET_DEVICE_INFO;
                byte[] bytes = manager.makingCommand(typeOfCommand);
                //广播获取主控信息
                byte[] stopTest = manager.makeCommandToGetBasicInfo((byte) 0xff);

                DatagramPacket udpPackStop = new DatagramPacket(stopTest, stopTest.length, InetAddress.getByName(CacheData.broadCastAddress), CacheData.port);
                DatagramSocket udpSocket = new DatagramSocket();
                DatagramPacket udpPack = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(CacheData.broadCastAddress), CacheData.port);
                udpSocket.send(udpPackStop);
                udpSocket.send(udpPack);

                udpSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(byte[] bytes) throws IOException {
        DatagramPacket udpPack = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(CacheData.ip), port);
        udpSocket.send(udpPack);
    }


    private void notifyDataSetSucceed(String setBasicPdparamsSuccess) {
        Message msg = Message.obtain();
        if (setBasicPdparamsSuccess.equals(CommandTypes.SET_BASIC_PARAMS_SUCCESS)) {
            //设置成功0x03
            msg.what = 0x03;
            Bundle bundle = new Bundle();
            bundle.putString("succeed", "succeed");
            msg.setData(bundle);
        } else {
            msg.what = 0x04;
            Bundle bundle = new Bundle();
            bundle.putString("succeed", "succeed");
            msg.setData(bundle);
        }
        netHandler.sendMessage(msg);
    }

    private void sendPDParamsToMainThread(PdParams pdParams) {
        //通过Message把消息发到主线程
        Message msg = Message.obtain();
        msg.what = 0x03;
        Bundle bundle = new Bundle();
        bundle.putSerializable("pdParamsUHF", pdParams);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendTestParamToMainThread(TestParams basicTestParams) {
        //通过Message把消息发到主线程
        Message msg = Message.obtain();
        msg.what = 0x02;
        Bundle bundle = new Bundle();
        bundle.putSerializable("basicTestParams", basicTestParams);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendInfoToMainThread(ControlUnit deviceBasicInfo) {
        //通过Message把消息发到主线程
        Message msg = Message.obtain();
        msg.what = 0x01;
        Bundle bundle = new Bundle();
        bundle.putSerializable("deviceBasicInfo", deviceBasicInfo);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    public void setPdParamsAA(PdParamsAA data) {
        this.pdParamsAA = data;
    }

    public void setLightParams(LightParams data) {
        this.lightParams = data;
    }
}
