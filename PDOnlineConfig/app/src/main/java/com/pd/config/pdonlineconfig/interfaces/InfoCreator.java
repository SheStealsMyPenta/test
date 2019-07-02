package com.pd.config.pdonlineconfig.interfaces;


import com.pd.config.pdonlineconfig.pojo.*;

public interface InfoCreator {
    ControlUnit getBasicInfo(byte[] rawArr);

    TestParams getBasicTestParams(byte[] rawArr);

    PdParams getPDParams(byte[] rawArr);

    void getInfoFromLongPack();

    DeviceInfo getDeviceInfo(byte[] body);

    PdParamsUHF getUHFParams(byte[] body);

    LightParams getLightParams(byte[] body);

    PdParamsAA getAaParams(byte[] body);

    String getCurrentDeviceSn(byte[] body);

    WatchedDevice getTheWatchedDevice(byte[] body);

    String getJSONStr(byte[] body);

    Packages getTwoDimenArray(byte[] finalResult);

    InfraredParams getInfraredParams(byte[] bytes);

}
