package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class BindObj implements Serializable {
    private byte item_status;
    private String item_num;
    private byte item_chn;
    private byte item_type;
    private byte item_id;

    public byte getItem_status() {
        return item_status;
    }

    public void setItem_status(byte item_status) {
        this.item_status = item_status;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public byte getItem_chn() {
        return item_chn;
    }

    public void setItem_chn(byte item_chn) {
        this.item_chn = item_chn;
    }

    public byte getItem_type() {
        return item_type;
    }

    public void setItem_type(byte item_type) {
        this.item_type = item_type;
    }

    public byte getItem_id() {
        return item_id;
    }

    public void setItem_id(byte item_id) {
        this.item_id = item_id;
    }
}
