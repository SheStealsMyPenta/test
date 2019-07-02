package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class PdParamsAA implements Serializable {
    private byte  aaSignalValue;
    private float aaKValue;
    private float aaBValue;
    private byte dischargeValue;
   private  byte chartValue;

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
}
