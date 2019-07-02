package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class HFCTParams implements Serializable {
    private byte HFCTfilaterFrequency;
    private byte HFCTMonitorThreaholdValue;
    private float HFCTKValue;
    private float HFCTBValue;
    private byte  dischargeThreaholdValue;

    public byte getHFCTfilaterFrequency() {
        return HFCTfilaterFrequency;
    }

    public void setHFCTfilaterFrequency(byte HFCTfilaterFrequency) {
        this.HFCTfilaterFrequency = HFCTfilaterFrequency;
    }

    public byte getHFCTMonitorThreaholdValue() {
        return HFCTMonitorThreaholdValue;
    }

    public void setHFCTMonitorThreaholdValue(byte HFCTMonitorThreaholdValue) {
        this.HFCTMonitorThreaholdValue = HFCTMonitorThreaholdValue;
    }

    public float getHFCTKValue() {
        return HFCTKValue;
    }

    public void setHFCTKValue(float HFCTKValue) {
        this.HFCTKValue = HFCTKValue;
    }

    public float getHFCTBValue() {
        return HFCTBValue;
    }

    public void setHFCTBValue(float HFCTBValue) {
        this.HFCTBValue = HFCTBValue;
    }

    public byte getDischargeThreaholdValue() {
        return dischargeThreaholdValue;
    }

    public void setDischargeThreaholdValue(byte dischargeThreaholdValue) {
        this.dischargeThreaholdValue = dischargeThreaholdValue;
    }
}
