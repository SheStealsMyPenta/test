package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;
import java.util.List;

public class DeviceInfo implements Serializable {
    private String codeOfDevice; //设备编码
    private byte numberOfCells; //监测单元
    private byte numberOfDetectedCells;//被测单元
    private String ipAddress; //ip地址
    private byte state; //当前状态
    private String physicAddress;  //探头的物理地址
    private String typeOfMonitorDevice;
    public String getPhysicAddress() {
        return physicAddress;
    }
    private List<String> supportType;

    public List<String> getSupportType() {
        return supportType;
    }

    public void setSupportType(List<String> supportType) {
        this.supportType = supportType;
    }

    public void setPhysicAddress(String physicAddress) {
        this.physicAddress = physicAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTypeOfMonitorDevice() {
        return typeOfMonitorDevice;
    }

    public void setTypeOfMonitorDevice(String typeOfMonitorDevice) {
        this.typeOfMonitorDevice = typeOfMonitorDevice;
    }

    private List<String> typesOfCells;

    public String getCodeOfDevice() {
        return codeOfDevice;
    }

    public void setCodeOfDevice(String codeOfDevice) {
        this.codeOfDevice = codeOfDevice;
    }

    public byte getNumberOfCells() {
        return numberOfCells;
    }

    public void setNumberOfCells(byte numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public List<String> getTypesOfCells() {
        return typesOfCells;
    }

    public void setTypesOfCells(List<String> typesOfCells) {
        this.typesOfCells = typesOfCells;
    }

    public byte getNumberOfDetectedCells() {
        return numberOfDetectedCells;
    }

    public void setNumberOfDetectedCells(byte numberOfDetectedCells) {
        this.numberOfDetectedCells = numberOfDetectedCells;
    }
    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

}
