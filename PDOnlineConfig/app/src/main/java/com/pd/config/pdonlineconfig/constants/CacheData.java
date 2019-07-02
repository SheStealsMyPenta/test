package com.pd.config.pdonlineconfig.constants;

import com.pd.config.pdonlineconfig.net.ControlUnitReceiver;
import com.pd.config.pdonlineconfig.pojo.DeviceInfo;
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;
import com.pd.config.pdonlineconfig.pojo.PdParamsUHF;
import com.pd.config.pdonlineconfig.pojo.WatchedDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheData {
    public static String ip;
    public static boolean isFetching = false;
    public static boolean fetched = false;
    public static int port = 7282;
    public static int receivePort = 7283;
    public static String currentType = "特高频";
    public static ControlUnitReceiver receiver = null;
    public static Map<Byte, String> mapOfType = new HashMap<>();
    public static Map<Byte, String> mapOfTheOption = new HashMap<>();
    public static Map<String, Byte> mapOfTheOptionReverse = new HashMap<>();
    public static Map<String, Byte> mapOfTheOptionReverseCub = new HashMap<>();
    public static String codeOfDevice = "";
    public static DeviceInfo device;
    public static String currentPage = "test";
    public static final String UHF = "特高频";
    public static final String AA = "空气式\n超声";
    public static boolean isValidate = false;
    public static final String Light = "视频\n图像";
    public static final String infrared = "红外\n热像";
    public static List<MonitorDevice> listOfMonitorDevice = new ArrayList<>();
    public static List<WatchedDevice> listOfWatchedDevice = new ArrayList<>();
    public static String typeOfSend = "uhf";
    public static DeviceInfo currentUnit; //主机的信息
    public static DeviceInfo currentMonitorDevice;
    public static PdParamsUHF currentUHFParams = new PdParamsUHF();
    public static int sort = 0;
    public static short[][] staticList;
    public static String broadCastAddress="192.168.0.255";


    static {
        mapOfType.put((byte) 0x00, "UHF");
        mapOfType.put((byte) 0x04, "AA");
        mapOfType.put((byte) 0x01, "AE");
        mapOfType.put((byte) 0x07, "video");
        mapOfType.put((byte) 0x06, "light");
        mapOfType.put((byte) 0x05, "infrared");

        mapOfTheOption.put((byte) 0x00, "特高频局放监测");
        mapOfTheOption.put((byte) 0x01, "接触式超声监测");
        mapOfTheOption.put((byte) 0x02, "高频电流局放监测");
        mapOfTheOption.put((byte) 0x03, "暂态地电压局放监测");
        mapOfTheOption.put((byte) 0x04, "空气式超声局放监测");
        mapOfTheOption.put((byte) 0x05, "红外热像监测");
        mapOfTheOption.put((byte) 0x07, "视频图像监测");
        mapOfTheOption.put((byte) 0x09, "环境温湿度监测");
        mapOfTheOption.put((byte) 0x08, "气体监测");
        mapOfTheOption.put((byte) 0x84, "无线测温局放监测");
        mapOfTheOption.put((byte) 0x85, "断路器机械特性监测");
        mapOfTheOption.put((byte) 0x86, "柜内温湿度");
        mapOfTheOption.put((byte) 0x81, "特高频局放监测");
        mapOfTheOption.put((byte) 0x80, "空气式超声局放监测");
        mapOfTheOption.put((byte) 0x82, "高频电流局放监测");
        mapOfTheOption.put((byte) 0x83, "暂态地电压局放监测");
        mapOfTheOptionReverse.put("特高频局放监测", (byte) 0x00);
        mapOfTheOptionReverse.put("接触式超声监测", (byte) 0x01);
        mapOfTheOptionReverse.put("高频电流局放监测", (byte) 0x02);
        mapOfTheOptionReverse.put("暂态地电压局放监测", (byte) 0x03);
        mapOfTheOptionReverse.put("空气式超声局放监测", (byte) 0x04);
        mapOfTheOptionReverse.put("红外热像监测", (byte) 0x05);
        mapOfTheOptionReverse.put("视频图像监测", (byte) 0x07);
        mapOfTheOptionReverse.put("柜内温湿度", (byte) 0x86);
        mapOfTheOptionReverse.put("气体监测", (byte) 0x08);
        mapOfTheOptionReverse.put("无线测温局放监测", (byte) 0x84);
        mapOfTheOptionReverse.put("断路器机械特性监测", (byte) 0x85);
        mapOfTheOptionReverseCub.put("仪表仓温湿度", (byte) 0x86);
        mapOfTheOptionReverseCub.put("电缆仓温湿度", (byte) 0x86);
        mapOfTheOptionReverse.put("环境温湿度监测", (byte) 0x09);
        mapOfTheOptionReverseCub.put("特高频局放监测", (byte) 0x81);
        mapOfTheOptionReverseCub.put("空气式超声局放监测", (byte) 0x80);
        mapOfTheOptionReverseCub.put("高频电流局放监测", (byte) 0x82);
        mapOfTheOptionReverseCub.put("暂态地电压局放监测", (byte) 0x83);
//        MonitorDevice device1 = new MonitorDevice();
//        device1.setPhysicAddress("03");
//        device1.setCodeOfMonitor("A3000000");
//        device1.setTypeOfTheMonitor("开关柜监测装置");
//        device1.setSortNum((byte) 1);
//        MonitorDevice device = new MonitorDevice();
//        device.setPhysicAddress("-");
//        device.setCodeOfMonitor("A0000001");
//        device.setTypeOfTheMonitor("环境监测装置");
//        device1.setSortNum((byte) 2);
////        MonitorDevice device2 =new MonitorDevice();
////        device2.setPhysicAddress("-");
////        device2.setCodeOfMonitor("A0000002");
////        device2.setTypeOfTheMonitor("环境监测装置");
//        MonitorDevice device3 = new MonitorDevice();
//        device3.setSortNum((byte) 4);
//        device3.setTypeOfTheMonitor("无线测温装置");
//        device3.setCodeOfMonitor("A2000001");
//        device3.setPhysicAddress("12");
////        device2.setSortNum((byte) 3);
//        MonitorDevice device4 = new MonitorDevice();
//        device4.setSortNum((byte) 4);
//        device4.setTypeOfTheMonitor("开关柜监测装置");
//        device4.setCodeOfMonitor("A3000005");
//        device4.setPhysicAddress("02");
//        listOfMonitorDevice.add(device1);
////        listOfMonitorDevice.add(device2);
//        listOfMonitorDevice.add(device);
//        listOfMonitorDevice.add(device3);
//        listOfMonitorDevice.add(device4);
//        WatchedDevice wdevice = new WatchedDevice();
//        wdevice.setCodeOfWatchedDevice("3333333333222222222223333333333333");
//        wdevice.setNameOfWatchedDevice("开关柜1");
//        wdevice.setTypeOfWatchedDevice("开关柜");
//        WatchedDevice wdevice1 = new WatchedDevice();
//        wdevice1.setCodeOfWatchedDevice("111111113222222222223333333333333");
//        wdevice1.setNameOfWatchedDevice("环境1");
//        wdevice1.setTypeOfWatchedDevice("环境");
//        listOfWatchedDevice.add(wdevice);
//        listOfWatchedDevice.add(wdevice1);
    }

}
