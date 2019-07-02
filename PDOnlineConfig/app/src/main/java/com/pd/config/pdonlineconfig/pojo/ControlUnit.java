package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;
import java.util.List;

public class ControlUnit implements Serializable {
    private String codeOfDevice;
    private byte[] softwarePatch;
    private short hardwarePathch;
    private int numberOfFiles;
    private int storageRemain;
    private byte numberOfUnit;
    private String ipAddress;
    private List<String> listOfTypes;

    public List<String> getListOfTypes() {
        return listOfTypes;
    }

    public void setListOfTypes(List<String> listOfTypes) {
        this.listOfTypes = listOfTypes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCodeOfDevice() {
        return codeOfDevice;
    }

    public void setCodeOfDevice(String codeOfDevice) {
        this.codeOfDevice = codeOfDevice;
    }

    public byte[] getSoftwarePatch() {
        return softwarePatch;
    }

    public void setSoftwarePatch(byte[] softwarePatch) {
        this.softwarePatch = softwarePatch;
    }

    public short getHardwarePathch() {
        return hardwarePathch;
    }

    public void setHardwarePathch(short hardwarePathch) {
        this.hardwarePathch = hardwarePathch;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public int getStorageRemain() {
        return storageRemain;
    }

    public void setStorageRemain(int storageRemain) {
        this.storageRemain = storageRemain;
    }

    public byte getNumberOfUnit() {
        return numberOfUnit;
    }

    public void setNumberOfUnit(byte numberOfUnit) {
        this.numberOfUnit = numberOfUnit;
    }
}
