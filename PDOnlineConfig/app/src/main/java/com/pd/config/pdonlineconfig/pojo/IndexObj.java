package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class IndexObj implements Serializable {
    private short xIndex;
    private short yIndex;

    public IndexObj() {
    }

    public short getxIndex() {
        return xIndex;
    }

    public void setxIndex(short xIndex) {
        this.xIndex = xIndex;
    }

    public short getyIndex() {
        return yIndex;
    }

    public void setyIndex(short yIndex) {
        this.yIndex = yIndex;
    }

    public IndexObj(short xIndex, short yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }
}
