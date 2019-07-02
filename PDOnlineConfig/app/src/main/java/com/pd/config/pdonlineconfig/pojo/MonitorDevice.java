package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;
import java.util.List;

public class MonitorDevice extends DeviceInfo implements Serializable {
    private String codeOfMonitor;
    private String physicAddress;
    private String typeOfTheMonitor;
    private byte sortNum;
    private List<String> listOfSupportType;
    private int numberOfChannel;

    public int getNumberOfChannel() {
        return numberOfChannel;
    }

    public void setNumberOfChannel(int numberOfChannel) {
        this.numberOfChannel = numberOfChannel;
    }

    public List<String> getListOfSupportType() {
        return listOfSupportType;
    }

    public void setListOfSupportType(List<String> listOfSupportType) {
        this.listOfSupportType = listOfSupportType;
    }

    public byte getSortNum() {
        return sortNum;
    }

    public void setSortNum(byte sortNum) {
        this.sortNum = sortNum;
    }

    public String getCodeOfMonitor() {
        return codeOfMonitor;
    }

    public void setCodeOfMonitor(String codeOfMonitor) {
        this.codeOfMonitor = codeOfMonitor;
    }

    public String getPhysicAddress() {
        return physicAddress;
    }

    public void setPhysicAddress(String physicAddress) {
        this.physicAddress = physicAddress;
    }

    public String getTypeOfTheMonitor() {
        return typeOfTheMonitor;
    }

    public void setTypeOfTheMonitor(String typeOfTheMonitor) {
        this.typeOfTheMonitor = typeOfTheMonitor;
    }
}
