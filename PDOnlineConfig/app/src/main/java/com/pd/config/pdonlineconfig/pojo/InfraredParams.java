package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class InfraredParams implements Serializable {
private float rate;

    public byte getDistance() {
        return distance;
    }

    public void setDistance(byte distance) {
        this.distance = distance;
    }

    private byte distance;
private float atoTemp;
private byte relateTemp;
private float reflectTemp;

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


    public float getAtoTemp() {
        return atoTemp;
    }

    public void setAtoTemp(float atoTemp) {
        this.atoTemp = atoTemp;
    }

    public byte getRelateTemp() {
        return relateTemp;
    }

    public void setRelateTemp(byte relateTemp) {
        this.relateTemp = relateTemp;
    }

    public float getReflectTemp() {
        return reflectTemp;
    }

    public void setReflectTemp(float reflectTemp) {
        this.reflectTemp = reflectTemp;
    }
}
