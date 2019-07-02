package com.pd.config.pdonlineconfig.impls;

import android.os.Message;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.interfaces.InternetManager;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.*;

public class InternetService {
    //发送命令获取每个监测装置的编号
    public void getMonitorDeviceBySortNumber(byte number, NetHandler handler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        InternetManager manager = new InternetManagerImpl();
        messager.setManager(manager);
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.GET_BASIC_INFO);
        messager.setCurrentSort(number);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = Command.Check_GETBASIC_INFO_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addANewMonitorDevice(MonitorDevice monitorDevice, NetHandler handler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        InternetManager manager = new InternetManagerImpl();
        messager.setManager(manager);
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.ADD_MONITOR_DEVICE);
        messager.setMonitorDevice(monitorDevice);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = Command.Check_BIND_DEVICEINFO_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getWatchedDeviceBySortNumber(byte number, NetHandler handler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        InternetManager manager = new InternetManagerImpl();
        messager.setManager(manager);
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.GET_WATCHEDDEVICE_INFO);
        messager.setCurrentSort(number);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = Command.Check_GETBASIC_INFO_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addANewWatchedDevice(WatchedDevice device, NetHandler netHandler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        InternetManager manager = new InternetManagerImpl();
        messager.setManager(manager);
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.ADD_WATCHED_DEVICE);
        messager.setWatchedDevice(device);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_ADD_WATCHED_INFO_OVERTIME;
                netHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getSystemConfig(NetHandler handler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.GET_BASIC_TESTPARAMS);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_PDPARAMS_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setSystemConfig(TestParams values, NetHandler handler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.SET_BASIC_TESTPARAMS);
        messager.setParams(values);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_SETSYSTEM_CONFIG_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void  bindDeviceTogether(String codeOfDevice,String json, NetHandler handler,int numberOfJson) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.BINDDEVICETOGETHER);
        messager.setBindJson(json);
        messager.setNumberOfJson(numberOfJson);
        messager.setCodeOfDevice(codeOfDevice);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_BIND_MONITOR_AND_WATCHED_DEVICE_SUCCESS;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getSystemInfo(NetHandler mHandler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.GET_BASIC_INFO);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_GETBASICPARAMS_OVERTIME;
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void deleteData(NetHandler handler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.DELETE_DATA);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.start();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_DELETE_DATA_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void deleteLog(NetHandler mHandler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.DELETE_LOG);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(mHandler);
        manager.start();

        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what =Command.CHECK_DELETE_LOG_OVERTIME;
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getDataByType(NetHandler netHandler, String type) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_CUBICLE_DATA);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(netHandler);
        manager.start();

        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what =Command.CHECK_GET_CUBICLE_DATA_OVERTIME;
                netHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void saveAEParams(PdParamsAA pdParamsAA) {


    }

    public void getMonitorJSONStr(NetHandler handler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_MONITOR_LIST);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.start();

        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what =Command.CHECK_GETMONITOR_OVERTIME;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void deleteAMonitorDevice(String codeOfDevice, NetHandler handler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.DELETEADEVICE);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.setDeviceCode(codeOfDevice);
        manager.start();
        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what =Command.CHECK_DELETE_MONITOR_DEVICE_SUCCESS;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void deleteAWatchedDeviceByName(String codeOfWatchedDevice,NetHandler handler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.DELETEAWATCHEDDEVICE);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.setWatchedDevice(codeOfWatchedDevice);
        manager.start();
        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what =Command.CHECK_DELETE_WATCHED_DEVICE_SUCCESS;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void takeAPhoto(NetHandler handler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.TAKEPHOTO);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.start();
    }

    public void getTempValueByIndex(short xIndex, short yIndex,NetHandler handler) {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_TEMP_VALUE);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.setIndexObj(new IndexObj(xIndex,yIndex));
        manager.start();
    }

    public void notifyCoordinate(NetHandler handler,float xCoordinate,float yCoordinate) {

    }

    public void setInfraredConfigFragment(InfraredParams infraredParams, NetHandler netHandler) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.SET_INFRARED_PARAMS);
        messager.setInfraredParams(infraredParams);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what =0x03;
                netHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void getInfraredParams(NetHandler netHandler) {

        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(false);
        messager.setTypeOfCommand(CommandTypes.GET_INFRARED_PARAMS);
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_INFRARED_OVERTIME;
                netHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
