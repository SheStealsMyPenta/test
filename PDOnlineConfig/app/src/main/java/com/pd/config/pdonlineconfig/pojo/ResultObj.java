package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class ResultObj implements Serializable {
    private String typeOfMonitor; //监测设备的类型
    private String nameOfMoniorDevice; //监测设备编号
    private String channel; //数据通道
    private String typeOfWatchedDevice; //被测设备的类型
    private boolean isOpen;

    public ResultObj() {
    }

    public ResultObj(String typeOfMonitor, String nameOfMoniorDevice, String channel, String typeOfWatchedDevice, boolean isOpen) {
        this.typeOfMonitor = typeOfMonitor;
        this.nameOfMoniorDevice = nameOfMoniorDevice;
        this.channel = channel;
        this.typeOfWatchedDevice = typeOfWatchedDevice;
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public ResultObj(String typeOfMonitor, String nameOfMoniorDevice, String channel, String typeOfWatchedDevice) {
        this.typeOfMonitor = typeOfMonitor;
        this.nameOfMoniorDevice = nameOfMoniorDevice;
        this.channel = channel;
        this.typeOfWatchedDevice = typeOfWatchedDevice;
    }

    public String getTypeOfWatchedDevice() {
        return typeOfWatchedDevice;
    }

    public void setTypeOfWatchedDevice(String typeOfWatchedDevice) {
        this.typeOfWatchedDevice = typeOfWatchedDevice;
    }

    private int currentMonitorDevicePosition;
    private int currentChannelPosition;

    public int getCurrentMonitorDevicePosition() {
        return currentMonitorDevicePosition;
    }

    public void setCurrentMonitorDevicePosition(int currentMonitorDevicePosition) {
        this.currentMonitorDevicePosition = currentMonitorDevicePosition;
    }

    public int getCurrentChannelPosition() {
        return currentChannelPosition;
    }

    public void setCurrentChannelPosition(int currentChannelPosition) {
        this.currentChannelPosition = currentChannelPosition;
    }

    public ResultObj(String typeOfMonitor, String nameOfMoniorDevice, String channel) {
        this.typeOfMonitor = typeOfMonitor;
        this.nameOfMoniorDevice = nameOfMoniorDevice;
        this.channel = channel;
    }

    public String getTypeOfMonitor() {
        return typeOfMonitor;
    }

    public void setTypeOfMonitor(String typeOfMonitor) {
        this.typeOfMonitor = typeOfMonitor;
    }

    public String getNameOfMoniorDevice() {
        return nameOfMoniorDevice;
    }

    public void setNameOfMoniorDevice(String nameOfMoniorDevice) {
        this.nameOfMoniorDevice = nameOfMoniorDevice;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    @Override
    public String toString() {
        String result = this.channel + this.nameOfMoniorDevice + this.typeOfMonitor;
        return result;
    }
}
