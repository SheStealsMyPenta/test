package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;
import java.util.List;

public class MainControllerDetailInfo implements Serializable {


    private String mcc_num; //设备编号
    private String mcc_dev_cnt; //监测设备个数
    private String mcc_obj_cnt; //被测设备个数
    private List<MonitorDevice> listOfMonitorDevice;
    private List<WatchedDevice> listOfWatchedDevice;

    public String getMcc_num() {
        return mcc_num;
    }

    public void setMcc_num(String mcc_num) {
        this.mcc_num = mcc_num;
    }

    public String getMcc_dev_cnt() {
        return mcc_dev_cnt;
    }

    public void setMcc_dev_cnt(String mcc_dev_cnt) {
        this.mcc_dev_cnt = mcc_dev_cnt;
    }

    public String getMcc_obj_cnt() {
        return mcc_obj_cnt;
    }

    public void setMcc_obj_cnt(String mcc_obj_cnt) {
        this.mcc_obj_cnt = mcc_obj_cnt;
    }

    public List<MonitorDevice> getListOfMonitorDevice() {
        return listOfMonitorDevice;
    }

    public void setListOfMonitorDevice(List<MonitorDevice> listOfMonitorDevice) {
        this.listOfMonitorDevice = listOfMonitorDevice;
    }

    public List<WatchedDevice> getListOfWatchedDevice() {
        return listOfWatchedDevice;
    }

    public void setListOfWatchedDevice(List<WatchedDevice> listOfWatchedDevice) {
        this.listOfWatchedDevice = listOfWatchedDevice;
    }
}
