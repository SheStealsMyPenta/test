package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class TestParams implements Serializable {
    private String ipAddress;
    private short port;
    private byte IntervalForUpload;
    private String deviceIpAddress;
    private String subnetMask;
    private String gateway;
    private byte restart; //0为不定时重启
    private byte hour;
    private byte syncType;
    private String comAdress;

    public String getComAdress() {
        return comAdress;
    }

    public void setComAdress(String comAdress) {
        this.comAdress = comAdress;
    }

    public float getSycncFrequency() {
        return sycncFrequency;
    }

    public void setSycncFrequency(float sycncFrequency) {
        this.sycncFrequency = sycncFrequency;
    }

    private float sycncFrequency;
    public byte getSyncType() {
        return syncType;
    }

    public void setSyncType(byte syncType) {
        this.syncType = syncType;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getTypeOfInternet() {
        return typeOfInternet;
    }

    public void setTypeOfInternet(byte typeOfInternet) {
        this.typeOfInternet = typeOfInternet;
    }

    private byte minute;
    private byte typeOfInternet;
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public byte getIntervalForUpload() {
        return IntervalForUpload;
    }

    public void setIntervalForUpload(byte intervalForUpload) {
        IntervalForUpload = intervalForUpload;
    }

    public String getDeviceIpAddress() {
        return deviceIpAddress;
    }

    public void setDeviceIpAddress(String deviceIpAddress) {
        this.deviceIpAddress = deviceIpAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public byte getRestart() {
        return restart;
    }

    public void setRestart(byte restart) {
        this.restart = restart;
    }
}
