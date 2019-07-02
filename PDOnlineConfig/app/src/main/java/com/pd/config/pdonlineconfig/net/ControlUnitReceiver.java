package com.pd.config.pdonlineconfig.net;

import android.os.Bundle;
import android.os.Message;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.interfaces.InfoCreator;
import com.pd.config.pdonlineconfig.interfaces.InternetManager;
import com.pd.config.pdonlineconfig.pojo.*;
import com.pd.config.pdonlineconfig.utils.ConversionTool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ControlUnitReceiver extends Thread {
    private InternetManager manager;
    private volatile NetHandler netHandler;
    private String ip;
    public String typeOfCommand;
    private int port;
    private long start;
    private long end;
    private byte[] bufferData;
    private InfoCreator infoCreator;
    private LongPackObj obj;

    public ControlUnitReceiver(String ip, int port) {
        this.ip = ip;
        this.port = port;
        obj = new LongPackObj();
    }

    public ControlUnitReceiver(int port) {
        this.port = port;
        obj = new LongPackObj();
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public void setInfoCreator(InfoCreator infoCreator) {
        this.infoCreator = infoCreator;
    }

    public void setManager(InternetManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        super.run();
        DatagramSocket udpReceiver = null;
        try {
            udpReceiver = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (true) {
            byte[] bytes = new byte[1024];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            try {
                if (udpReceiver != null) {
                    receiveFromUdpSocket(udpReceiver);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            netHandler.sendEmptyMessage(0x01);
        }
    }

    private void receiveFromUdpSocket(DatagramSocket udpSocket) throws IOException {
        while (true) {
            byte[] resultArr = new byte[2000];
            DatagramPacket receivePack = new DatagramPacket(resultArr, resultArr.length);

            udpSocket.receive(receivePack);
            int lengthOfPack = receivePack.getLength();
            if (lengthOfPack > 0) {
                //判断是否帧头帧尾是否为小包
                if ((char) resultArr[0] == 'B' &&
                        (char) resultArr[1] == 'E' &&
                        (char) resultArr[2] == 'G' &&
                        (char) resultArr[47] == 'E' &&
                        (char) resultArr[48] == 'N' &&
                        (char) resultArr[49] == 'D') {
                    //得到数据包
                    byte[] body = manager.resolveArrayFromShortPack(resultArr);
                    if (body[0] == Command.GET_DEVICE_INFO) {
                        InetAddress address = receivePack.getAddress();
                        DeviceInfo info = infoCreator.getDeviceInfo(body);
                        info.setIpAddress(address.toString().split("/")[1]);
                        sendDeviceInfoIntoMainThread(info);

                    } else if (body[0] == Command.GET_BASIC_INFO) {
                        if (CacheData.ip == null) {
                            InetAddress address = receivePack.getAddress();
                            ControlUnit deviceBasicInfo = infoCreator.getBasicInfo(body);
                            deviceBasicInfo.setIpAddress(address.toString());
                            sendInfoToMainThread(deviceBasicInfo);
                        }
                        if (infoCreator != null) {
                            ControlUnit DeviceBasicInfo = infoCreator.getBasicInfo(body);
                            sendInfoToMainThread(DeviceBasicInfo);
                        }
                    } else if (body[0] == Command.GET_BASIC_TESTPARAMS) {
                        sendTestParamToMainThread(infoCreator.getBasicTestParams(body));
                    } else if (body[0] == Command.SET_RUNTIME_CONFIG) {
                        notifyDataSetSucceed(CommandTypes.SET_BASIC_PARAMS_SUCCESS);
                    } else if (body[0] == Command.GET_UHF_PARAMS) {
                        //uhf参数获取
                        PdParamsUHF uhfParams = infoCreator.getUHFParams(body);
                        sendUHFParamsIntoMainThread(uhfParams);
                    } else if (body[0] == Command.SET_UHF_CONFIG || body[0] == 0x08) {
                        //设置特高频参数成功
                        notifyDataSetSucceed(CommandTypes.SET_UHF_PARAMS_SUCCESS);
                    } else if (body[0] == Command.GET_AA_PARAMS) {
                        sendAAParamsIntoMainThread(infoCreator.getAaParams(body));
                    } else if (body[0] == Command.STARTTEST) {
                        byte result = -1;
                        if (body[1] == 0) {
                            result = 0;
                        } else if (body[1] == 1) {
                            result = 1;
                        } else {
                            result = 2;
                        }
                        if (CacheData.currentType.equals(CacheData.Light)) {
                            int position = -1;
                            if (body[2] == 0x00) {
                                notifyStartLightUrl("filename");
                            } else {
                                for (int i = 2; i < body.length; i++) {
                                    if (body[i] == 0x00) {
                                        position = i - 2;
                                        break;
                                    }
                                }
                                byte[] fileName = new byte[position];
                                System.arraycopy(body, 2, fileName, 0, position);
                                System.out.println(fileName);
                                String fileNameStr = new String(fileName, "UTF-8");

                                notifyStartLightUrl(fileNameStr);
                            }

                        } else {
//                            if (body[2]==0){
//                                notifyEndTestSuccess();
//                            }else {
                            notifyStartTestSuccess(result);
//                            }
                        }

                    } else if (body[0] == 0x12) {
                        LightParams params = infoCreator.getLightParams(body);
                        sendLightParamsIntoMainThread(params);
                    } else if (body[0] == 0x13) {
                        notifyDataSetSucceed(CommandTypes.SET_UHF_PARAMS_SUCCESS);
                    } else if (body[0] == 0x0B) {
                        notifyClearDataSuccess();
                    } else if (body[0] == 0x0C) {
                        notifyClearLogSuccess();

                    } else if (body[0] == 0x14) {
                        boolean success = body[1] == 1;
                        notifySetDeviceCodeSucceed(success);
                    } else if (body[0] == 0x19) {
                        String jsonStr = infoCreator.getJSONStr(body);
                        sendJSONStrToMainThread(jsonStr, "monitor");

                    } else if (body[0] == Command.BIND_DEVICE_SUCCESS) {
                        if (body[1] == 1) {
                            if (body[2] == 0) {
                                notifyDelSuccess(Command.DELETE_MONITOR_DEVICE_SUCCESS);
                            } else {
                                notifyAddSuccess(Command.BIND_DEVICE_SUCCESS);
                            }
                        }


                    } else if (body[0] == 0x1A) {
                        if (body[2] == 0x01) {
                            //第一个包
                            obj = new LongPackObj();
                            obj.setNumberOfPack(body[3]);
                            obj.setListOfBytes(new ArrayList<>());
                            obj.setOffset(0);
                        }
                        byte[] lenthOfDataArr = {body[4], body[5]};
                        short lenthOfData = ConversionTool.toshort(lenthOfDataArr);
                        obj.getDataFromSource(body, lenthOfData);
                        if (obj.isSucceed()) {
                            byte[] finalResult = new byte[obj.getLengthOfAll()];
                            int count = 0;
                            for (int i = 0; i < obj.getListOfBytes().size(); i++) {
                                if (i == 0) {
                                    System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, 0, obj.getListOfBytes().get(i).length);
                                } else {
                                    System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, count, obj.getListOfBytes().get(i).length);
                                }
                                count += obj.getListOfBytes().get(i).length;
                            }
                            //finalResult就是整个数据包
                            String jsonStr = new String(finalResult);

                            sendJSONStrToMainThread(jsonStr, "watched");
                        }
                    } else if (body[0] == 0x0F) {
                        //添加被测设备成功
                        if (body[1] == 1) {
                            if (body[2] == 0) {
                                notifyDelSuccess(Command.DEL_WATCHED_INFO_SUCCESS);
                            } else {
                                notifyAddSuccess(Command.ADD_WATCHED_INFO_SUCCESS);
                            }
                        }
                    } else if (body[0] == 0x16) {
                        notifyChangeRelationshipSuccess();
                    } else if (body[0] == 0x0A) {
                        byte[] bytes = body;
                        short currentPack = ConversionTool.toshort(new byte[]{bytes[1], bytes[2]});
                        if (currentPack == 1) {
                            start = System.currentTimeMillis();
                            obj = new LongPackObj();
                            //报个数也改为两个字的short；
                            short numberOfpack = ConversionTool.toshort(new byte[]{bytes[3], bytes[4]});
                            obj.setNumberOfPack(numberOfpack);
                            obj.setListOfBytes(new ArrayList<>());
                            obj.setOffset(0);
                        }
                        byte[] lenthOfDataArr = {bytes[5], bytes[6]};
                        short lenthOfData = ConversionTool.toshort(lenthOfDataArr);
                        boolean succeed = obj.getDataFromSource(bytes, lenthOfData);
                        if (succeed) {
                            if (obj.isSucceed()) {
                                System.out.println("网络传输耗时" + (end - start) + "毫秒");
                                start = System.currentTimeMillis();
                                //传到主线程
                                int count = 0;
                                byte[] finalResult = new byte[obj.getLengthOfAll()];
                                for (int i = 0; i < obj.getListOfBytes().size(); i++) {
                                    if (i == 0) {
                                        System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, 0, obj.getListOfBytes().get(i).length);
                                    } else {
                                        System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, count, obj.getListOfBytes().get(i).length);
                                    }
                                    count += obj.getListOfBytes().get(i).length;

                                }
                                end = System.currentTimeMillis();
                                System.out.println("网络传输后处理数据" + (end - start) + "毫秒");
                                //发送到组线程
                                if (bytes[0] == 0x0A) {
                                    //实时采集数据
                                    if (CacheData.currentType.equals(CacheData.UHF) || CacheData.currentType.equals(CacheData.AA)) {
                                        sendLongPackDataToMainThread(finalResult);
                                    } else {
                                        Packages packages = infoCreator.getTwoDimenArray(finalResult);
//                                        packages.setDataSet();

                                        sendListOfResultToMainThread(packages);
                                    }

                                } else if (bytes[0] == 0x19) {
                                    String jsonStr = new String(finalResult);
                                    sendJSONStrToMainThread(jsonStr, "monitor");
                                } else if (bytes[1] == 0x1A) {
                                    String jsonStr = new String(finalResult);
                                    sendJSONStrToMainThread(jsonStr, "watched");
                                }
                                obj = new LongPackObj();

                            }
                        }

                    } else if (body[0] == 0x1D) {
                        //获取红外参数成功
                        InfraredParams params = infoCreator.getInfraredParams(body);
                        sendInfrardParamsToMainThread(params);
                    } else if(body[0]==0x1C){
                        notifySetInfraredSucceed();
                    }
                } else if ((char) resultArr[0] == 'B' &&
                        (char) resultArr[1] == 'E' &&
                        (char) resultArr[2] == 'G' &&
                        (char) resultArr[1037] == 'E' &&
                        (char) resultArr[1038] == 'N' &&
                        (char) resultArr[1039] == 'D'
                ) {
                    //为长包

                    byte[] bytes = manager.resolveArrayFromLongPack(resultArr);
                    if (bytes[0] == 0x0A) {
                        //包长度改为2个字节的长度
                        short currentPack = ConversionTool.toshort(new byte[]{bytes[1], bytes[2]});
                        if (currentPack == 1) {
                            start = System.currentTimeMillis();
                            obj = new LongPackObj();
                            //报个数也改为两个字的short；
                            short numberOfpack = ConversionTool.toshort(new byte[]{bytes[3], bytes[4]});
                            obj.setNumberOfPack(numberOfpack);
                            obj.setListOfBytes(new ArrayList<>());
                            obj.setOffset(0);
                        }
                        byte[] lenthOfDataArr = {bytes[5], bytes[6]};
                        short lenthOfData = ConversionTool.toshort(lenthOfDataArr);
                        boolean succeed = obj.getDataFromSource(bytes, lenthOfData);
                        if (succeed) {
                            if (obj.isSucceed()) {
                                end = System.currentTimeMillis();
                                System.out.println("网络传输耗时" + (end - start) + "毫秒");
                                start = System.currentTimeMillis();
                                //传到主线程
                                int count = 0;
                                byte[] finalResult = new byte[obj.getLengthOfAll()];
                                for (int i = 0; i < obj.getListOfBytes().size(); i++) {
                                    if (i == 0) {
                                        System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, 0, obj.getListOfBytes().get(i).length);
                                    } else {
                                        System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, count, obj.getListOfBytes().get(i).length);
                                    }
                                    count += obj.getListOfBytes().get(i).length;

                                }
                                end = System.currentTimeMillis();
                                System.out.println("网络传输后处理数据" + (end - start) + "毫秒");
                                //发送到组线程
                                if (bytes[0] == 0x0A) {
                                    //实时采集数据
                                    if (CacheData.currentType.equals(CacheData.UHF) || CacheData.currentType.equals(CacheData.AA)) {
                                        sendLongPackDataToMainThread(finalResult);
                                    } else {
                                        Packages packages = infoCreator.getTwoDimenArray(finalResult);

                                        sendListOfResultToMainThread(packages);


                                    }

                                } else if (bytes[0] == 0x19) {
                                    String jsonStr = new String(finalResult);
                                    sendJSONStrToMainThread(jsonStr, "monitor");
                                } else if (bytes[1] == 0x1A) {
                                    String jsonStr = new String(finalResult);
                                    sendJSONStrToMainThread(jsonStr, "watched");
                                }
                                obj = new LongPackObj();

                            }
                        }

                    } else if (bytes[0] == 0x19) {
                        String str = infoCreator.getJSONStr(bytes);
                        sendJSONStrToMainThread(str, "");
                    } else if (bytes[0] == 0x1A) {
                        //有可能分包传输
                        short currentPack = ConversionTool.toshort(new byte[]{bytes[1], bytes[2]});
                        if (currentPack == 0x01) {
                            //第一个包
                            obj = new LongPackObj();
                            short numberOfpack = ConversionTool.toshort(new byte[]{bytes[3], bytes[4]});
                            obj.setNumberOfPack(numberOfpack);
                            obj.setListOfBytes(new ArrayList<>());
                            obj.setOffset(0);
                        }

                        byte[] lenthOfDataArr = {bytes[5], bytes[6]};
                        short lenthOfData = ConversionTool.toshort(lenthOfDataArr);
                        obj.getDataFromSource(bytes, lenthOfData);
                        if (obj.isSucceed()) {
                            byte[] finalResult = new byte[obj.getLengthOfAll()];
                            int count = 0;
                            for (int i = 0; i < obj.getListOfBytes().size(); i++) {
                                if (i == 0) {
                                    System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, 0, obj.getListOfBytes().get(i).length);
                                } else {
                                    System.arraycopy(obj.getListOfBytes().get(i), 0, finalResult, count, obj.getListOfBytes().get(i).length);
                                }
                                count += obj.getListOfBytes().get(i).length;
                            }
                            //finalResult就是整个数据包
                            String jsonStr = new String(finalResult);

                            sendJSONStrToMainThread(jsonStr, "watched");
                        }
                    } else if (bytes[0] == 0x18) {
                        //获取或者设置
                    }


                } else {
                    //发生错误

                }
            }

        }
    }

    private void notifySetInfraredSucceed() {
        Message msg = Message.obtain();
        msg.what = 0x0C;
        netHandler.sendMessage(msg);
    }

    private void sendInfrardParamsToMainThread(InfraredParams params) {
        Message msg = Message.obtain();
        msg.what = 0x01;
        Bundle bundle = new Bundle();
        bundle.putSerializable("infrared", params);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendListOfResultToMainThread(Packages listOfResult) {
        Message msg = Message.obtain();
        msg.what = 0x07;
        Bundle bundle = new Bundle();
        bundle.putSerializable("packages", listOfResult);
//        bundle.putShort("width", (Short) listOfResult.get(0));
//        bundle.putShort("height", (Short) listOfResult.get(1));
//        bundle.putS("colorArray", (short[][]) listOfResult.get(2));
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

//    private List sendTwo_dimenArryToMain(byte[] finalResult) {
////        List listOfResult = infoCreator.getTwoDimenArray(finalResult);
//        return listOfResult;
//    }

    private void notifyChangeRelationshipSuccess() {
        Message msg = Message.obtain();
        msg.what = 0x01;
        netHandler.sendMessage(msg);
    }

    private void notifyDelSuccess(int typeOfCommand) {
        Message msg = Message.obtain();

        msg.what = typeOfCommand;
        netHandler.sendMessage(msg);

    }

    private void notifyAddSuccess(int typeOfCommand) {
        Message msg = Message.obtain();
        msg.what = typeOfCommand;
        netHandler.sendMessage(msg);
    }

    private void sendJSONStrToMainThread(String jsonStr, String monitor) {

        Message msg = Message.obtain();
        msg.what = Command.SendMonitorJSON;
        Bundle bundle = new Bundle();
        bundle.putString("jsonStr", jsonStr);
        msg.setData(bundle);
        netHandler.sendMessage(msg);

    }

    private void sendDeviceSNToMainThread(String code) {
        Message msg = Message.obtain();
        msg.what = 0x0F;
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifySetDeviceCodeSucceed(boolean success) {
        Message msg = Message.obtain();
        msg.what = 0x10;
        Bundle bundle = new Bundle();
        bundle.putBoolean("success", success);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifyClearDataSuccess() {
        Message msg = Message.obtain();
        msg.what = 0x0B;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifyClearLogSuccess() {
        Message msg = Message.obtain();
        msg.what = 0x0C;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifyEndTestSuccess() {
        Message msg = Message.obtain();
        msg.what = 0x11;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifyStartLightUrl(String url) {
        Message msg = Message.obtain();
        msg.what = 0x10;
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendAAParamsIntoMainThread(PdParamsAA aaParams) {
        Message msg = Message.obtain();
        msg.what = 0x01;
        Bundle bundle = new Bundle();
        bundle.putSerializable("aaParams", aaParams);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendLightParamsIntoMainThread(LightParams params) {
        Message msg = Message.obtain();
        msg.what = 0x01;
        Bundle bundle = new Bundle();
        bundle.putSerializable("LightParams", params);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifyStartTestSuccess(byte result) {
        Message msg = Message.obtain();
        msg.what = 0x09;
        Bundle bundle = new Bundle();
        bundle.putByte("result", result);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendUHFParamsIntoMainThread(PdParamsUHF uhfParams) {
        Message msg = Message.obtain();
        msg.what = Command.CONFIG_FETCH_SUCCESS;
        Bundle bundle = new Bundle();
        bundle.putSerializable("UHFParams", uhfParams);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendDeviceInfoIntoMainThread(DeviceInfo info) {
        Message msg = Message.obtain();
        msg.what = 0x01;
        Bundle bundle = new Bundle();
        bundle.putSerializable("deviceBasicInfo", info);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendLongPackDataToMainThread(byte[] data) {
        Message msg = Message.obtain();
        msg.what = 0x07;
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", data);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendPDParamsToMainThread(PdParams pdParams) {
        //通过Message把消息发到主线程
        Message msg = Message.obtain();
        msg.what = 0x03;
        Bundle bundle = new Bundle();
        bundle.putSerializable("pdParams", pdParams);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void notifyDataSetSucceed(String setBasicPdparamsSuccess) {
        Message msg = Message.obtain();
        if (setBasicPdparamsSuccess.equals(CommandTypes.SET_BASIC_PARAMS_SUCCESS)) {
            msg.what = 0x02;
            Bundle bundle = new Bundle();
            bundle.putString("succeed", "succeed");
            msg.setData(bundle);
        } else if (setBasicPdparamsSuccess.equals(CommandTypes.SET_UHF_PARAMS_SUCCESS) || setBasicPdparamsSuccess.equals(CommandTypes.SET_AA_PARAMS_SUCCESS)) {
            msg.what = Command.SET_RUNTIME_CONFIG_SUCCUSS;
            Bundle bundle = new Bundle();
            bundle.putString("succeed", "succeed");
            msg.setData(bundle);
        }
        netHandler.sendMessage(msg);
    }

    private void sendTestParamToMainThread(TestParams basicTestParams) {
        //通过Message把消息发到主线程
        Message msg = Message.obtain();
        msg.what = Command.GET_BASIC_TESTPARAMS_SUCCESS;
        Bundle bundle = new Bundle();
        bundle.putSerializable("basicTestParams", basicTestParams);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }

    private void sendInfoToMainThread(ControlUnit deviceBasicInfo) {
        //通过Message把消息发到主线程
        Message msg = Message.obtain();
        msg.what = Command.GET_BASIC_TESTPARAMS_SUCCESS;
        Bundle bundle = new Bundle();

        bundle.putSerializable("deviceBasicInfo", deviceBasicInfo);
        msg.setData(bundle);
        netHandler.sendMessage(msg);
    }
}
