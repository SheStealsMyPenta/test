package com.pd.config.pdonlineconfig.net;

import android.os.Message;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.interfaces.InternetManager;
import com.pd.config.pdonlineconfig.pojo.LightParams;
import com.pd.config.pdonlineconfig.pojo.PdParamsAA;
import com.pd.config.pdonlineconfig.pojo.PdParamsUHF;
import com.pd.config.pdonlineconfig.pojo.TestParams;

public class UDPSender implements UDPSenderInterface {
    private String typeOfCommand;
    private ControlUnitMessager messager;
    private InternetManager manager;
    private int notifyNumber;
    private NetHandler netHandler;

    public UDPSender setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
        return this;
    }

    public UDPSender setMessager(ControlUnitMessager messager) {
        this.messager = messager;
        return this;
    }

    public UDPSender setManager(InternetManager manager) {
        this.manager = manager;
        messager.setManager(manager);
        return this;
    }

    public UDPSender setNotifyNumber(int notifyNumber) {
        this.notifyNumber = notifyNumber;
        return this;
    }

    public UDPSender setCommandType(String type) {
        messager.setTypeOfCommand(type);
        return this;
    }

    @Override
    public void send() {
        if (checkNull()) {
            messager.start();
            startNotifyThread();
        }
    }

    private void startNotifyThread() {
        new Thread(() -> {
            try {
                Thread.sleep(Command.OVERTIME);
                Message msg = Message.obtain();
                msg.what = notifyNumber;
                netHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean checkNull() {
        if (messager == null) return false;
        return manager != null;
    }

    public UDPSender setUHFParams(PdParamsUHF pdUHf) {
        messager.setPdParamsUHF(pdUHf);
        return this;

    }

    public UDPSender setAAParams(PdParamsAA paramsAA) {
        messager.setPdParamsAA(paramsAA);
        return this;
    }

    public UDPSender setRunTimeConfig(TestParams values) {
      messager.setParams(values);
      return this;

    }

    public UDPSender setDeviceSn(String s) {
        messager.setDeviceSN(s);
        return this;
    }

    public UDPSender setLightParams(LightParams data) {
      messager.setLightParams(data);
       return this;
    }
}
