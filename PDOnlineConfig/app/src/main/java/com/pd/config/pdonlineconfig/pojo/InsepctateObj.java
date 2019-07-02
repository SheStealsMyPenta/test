package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;
import java.util.List;

public class InsepctateObj implements Serializable {
private String nameOfTheMonitor;
private boolean open ;
private List listOfMonitorDevice;
private List channel;
private ResultObj resultObj;

    public ResultObj getResultObj() {
        return resultObj;
    }

    public void setResultObj(ResultObj resultObj) {
        this.resultObj = resultObj;
    }

    public String getNameOfTheMonitor() {
        return nameOfTheMonitor;
    }

    public void setNameOfTheMonitor(String nameOfTheMonitor) {
        this.nameOfTheMonitor = nameOfTheMonitor;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List getListOfMonitorDevice() {
        return listOfMonitorDevice;
    }

    public void setListOfMonitorDevice(List listOfMonitorDevice) {
        this.listOfMonitorDevice = listOfMonitorDevice;
    }

    public List getChannel() {
        return channel;
    }

    public void setChannel(List channel) {
        this.channel = channel;
    }
}
