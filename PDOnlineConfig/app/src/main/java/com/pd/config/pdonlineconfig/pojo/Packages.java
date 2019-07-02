package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class Packages implements Serializable {
    private int[][] dataSet ;
    private float currentTemp;
    private float minTemp;
    private float maxTemp;
    private float avgTemp;

    public float getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(float avgTemp) {
        this.avgTemp = avgTemp;
    }

    private short minTempX;
    private short minTempY;
    private short maxTempY;
    private short maxTempX;

    public short getMinTempX() {
        return minTempX;
    }

    public void setMinTempX(short minTempX) {
        this.minTempX = minTempX;
    }

    public short getMinTempY() {
        return minTempY;
    }

    public void setMinTempY(short minTempY) {
        this.minTempY = minTempY;
    }

    public short getMaxTempY() {
        return maxTempY;
    }

    public void setMaxTempY(short maxTempY) {
        this.maxTempY = maxTempY;
    }

    public short getMaxTempX() {
        return maxTempX;
    }

    public void setMaxTempX(short maxTempX) {
        this.maxTempX = maxTempX;
    }

    public float getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int[][] getDataSet() {
        return dataSet;
    }

    public void setDataSet(int[][] dataSet) {
        this.dataSet = dataSet;
    }
}
