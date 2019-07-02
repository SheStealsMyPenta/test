package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;
import java.util.List;

public class WatchedDevice implements Serializable {
    private String nameOfWatchedDevice;
    private String codeOfWatchedDevice; //32bytes
    private String typeOfWatchedDevice;
    private byte sort;
    private int numberOfWatchedKind;
    private String jsonStr;

    public List<ResultObj> getListOfInspectObj() {
        return listOfInspectObj;
    }

    public void setListOfInspectObj(List<ResultObj> listOfInspectObj) {
        this.listOfInspectObj = listOfInspectObj;
    }

    private List<ResultObj> listOfInspectObj;

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public int getNumberOfWatchedKind() {
        return numberOfWatchedKind;
    }

    public void setNumberOfWatchedKind(int numberOfWatchedKind) {
        this.numberOfWatchedKind = numberOfWatchedKind;
    }

    public byte getSort() {
        return sort;
    }

    public void setSort(byte sort) {
        this.sort = sort;
    }

    public String getNameOfWatchedDevice() {
        return nameOfWatchedDevice;
    }

    public void setNameOfWatchedDevice(String nameOfWatchedDevice) {
        this.nameOfWatchedDevice = nameOfWatchedDevice;
    }

    public String getCodeOfWatchedDevice() {
        return codeOfWatchedDevice;
    }

    public void setCodeOfWatchedDevice(String codeOfWatchedDevice) {
        this.codeOfWatchedDevice = codeOfWatchedDevice;
    }

    public String getTypeOfWatchedDevice() {
        return typeOfWatchedDevice;
    }

    public void setTypeOfWatchedDevice(String typeOfWatchedDevice) {
        this.typeOfWatchedDevice = typeOfWatchedDevice;
    }
}
