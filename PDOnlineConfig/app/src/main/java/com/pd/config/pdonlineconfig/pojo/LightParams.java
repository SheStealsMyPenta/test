package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class LightParams implements Serializable {
    private byte resolution;
    private byte lightness;
    private byte constract;

    public byte getResolution() {
        return resolution;
    }

    public void setResolution(byte resolution) {
        this.resolution = resolution;
    }

    public byte getLightness() {
        return lightness;
    }

    public void setLightness(byte lightness) {
        this.lightness = lightness;
    }

    public byte getConstract() {
        return constract;
    }

    public void setConstract(byte constract) {
        this.constract = constract;
    }
}
