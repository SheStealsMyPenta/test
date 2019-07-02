package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class PdParams implements Serializable {
    private byte filterFrequency;
    private byte thresholdValue;
    private float kValue;
    private float BValue;
    private byte  aaSignalValue;
    private float aaKValue;
    private float aaBValue;
    private byte typeOfSync;
    private float syncFrequency;

    public byte getFilterFrequency() {
        return filterFrequency;
    }

    public void setFilterFrequency(byte filterFrequency) {
        this.filterFrequency = filterFrequency;
    }

    public byte getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(byte thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public float getkValue() {
        return kValue;
    }

    public void setkValue(float kValue) {
        this.kValue = kValue;
    }

    public float getBValue() {
        return BValue;
    }

    public void setBValue(float BValue) {
        this.BValue = BValue;
    }

    public byte getAaSignalValue() {
        return aaSignalValue;
    }

    public void setAaSignalValue(byte aaSignalValue) {
        this.aaSignalValue = aaSignalValue;
    }

    public float getAaKValue() {
        return aaKValue;
    }

    public void setAaKValue(float aaKValue) {
        this.aaKValue = aaKValue;
    }

    public float getAaBValue() {
        return aaBValue;
    }

    public void setAaBValue(float aaBValue) {
        this.aaBValue = aaBValue;
    }

    public byte getTypeOfSync() {
        return typeOfSync;
    }

    public void setTypeOfSync(byte typeOfSync) {
        this.typeOfSync = typeOfSync;
    }

    public float getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(float syncFrequency) {
        this.syncFrequency = syncFrequency;
    }
}
