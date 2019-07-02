package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class TEVParams implements Serializable {
    private byte tevMonitorThreathreshold;
    private float tevKValue;
    private float tevBValue;
    private byte tevDiaschargeThreathreshold;

    public byte getTevMonitorThreathreshold() {
        return tevMonitorThreathreshold;
    }

    public void setTevMonitorThreathreshold(byte tevMonitorThreathreshold) {
        this.tevMonitorThreathreshold = tevMonitorThreathreshold;
    }

    public float getTevKValue() {
        return tevKValue;
    }

    public void setTevKValue(float tevKValue) {
        this.tevKValue = tevKValue;
    }

    public float getTevBValue() {
        return tevBValue;
    }

    public void setTevBValue(float tevBValue) {
        this.tevBValue = tevBValue;
    }

    public byte getTevDiaschargeThreathreshold() {
        return tevDiaschargeThreathreshold;
    }

    public void setTevDiaschargeThreathreshold(byte tevDiaschargeThreathreshold) {
        this.tevDiaschargeThreathreshold = tevDiaschargeThreathreshold;
    }
}
