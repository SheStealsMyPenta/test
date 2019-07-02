package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class PdParamsUHF implements Serializable {
    private byte filterFrequency;
    private byte thresholdValue;
    private float kValue;
    private float BValue;
    private byte dischargeValue;
    private byte sort;
    private byte chartValue;

    public byte getChartValue() {
        return chartValue;
    }

    public void setChartValue(byte chartValue) {
        this.chartValue = chartValue;
    }

    public byte getDischargeValue() {
        return dischargeValue;
    }

    public void setDischargeValue(byte dischargeValue) {
        this.dischargeValue = dischargeValue;
    }

    public byte getSort() {
        return sort;
    }

    public void setSort(byte sort) {
        this.sort = sort;
    }

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


    public boolean equals(   PdParamsUHF obj) {
        if(this.getThresholdValue()!= obj.thresholdValue) return false;
        if(this.getFilterFrequency()!= obj.getFilterFrequency()) return false;
        if(this.getkValue()!= obj.getkValue()) return false;
        if(this.getBValue()!= obj.getBValue()) return false;
        return true;
    }
}
