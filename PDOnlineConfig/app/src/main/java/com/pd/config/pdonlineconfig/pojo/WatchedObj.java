package com.pd.config.pdonlineconfig.pojo;

import java.io.Serializable;

public class WatchedObj implements Serializable {
    private int item_status;
    private String item_num;
    private int item_chn;
    private int item_type;
    private int item_id;

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public WatchedObj(int item_status, String item_num, int item_chn, int item_type, int item_id) {
        this.item_status = item_status;
        this.item_num = item_num;
        this.item_chn = item_chn;
        this.item_type = item_type;
        this.item_id = item_id;
    }

    public WatchedObj(int item_status, String item_num, int item_chn) {
        this.item_status = item_status;
        this.item_num = item_num;
        this.item_chn = item_chn;
    }

    public int getItem_status() {
        return item_status;
    }

    public void setItem_status(int item_status) {
        this.item_status = item_status;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public int getItem_chn() {
        return item_chn;
    }

    public void setItem_chn(int item_chn) {
        this.item_chn = item_chn;
    }
}
