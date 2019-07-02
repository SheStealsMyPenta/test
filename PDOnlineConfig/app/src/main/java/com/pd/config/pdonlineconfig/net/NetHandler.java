package com.pd.config.pdonlineconfig.net;

import android.os.Handler;
import android.os.Message;

import com.pd.config.pdonlineconfig.interfaces.NetListener;

public class NetHandler extends Handler {
    private volatile NetListener listener ;
    public synchronized void setListener(NetListener listener){ this.listener=listener;}
    public NetHandler(NetListener listener){
        super();
        this.listener = listener;
    }

    @Override
    public synchronized void handleMessage(Message msg) {
        if(listener!=null){
            listener.handleMessage(msg);
            super.handleMessage(msg);
        }
    }
}
