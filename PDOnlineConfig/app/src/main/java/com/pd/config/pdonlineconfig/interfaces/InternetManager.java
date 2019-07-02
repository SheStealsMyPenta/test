package com.pd.config.pdonlineconfig.interfaces;

import com.pd.config.pdonlineconfig.pojo.*;

import java.util.List;

public interface InternetManager {


    //制作获取命令
    byte[] makingCommand(String type);
    //制作带有基本参数的命令
    byte[] makingCommandWithTestParams(TestParams params);

    byte[] notifyDataReceiveSuccess(byte[] currenPack);
    ///解析短数据帧
    byte[] resolveArrayFromShortPack(byte[] resultArr);
// 解析长数据帧
    byte[] resolveArrayFromLongPack(byte[] resultArr);
    //封装局方UHF命令
    byte[] makePdParamsCommandUHF(PdParamsUHF params);

    byte[] makePdParamsCommandAA(PdParamsAA pdParamsAA);

    byte[] makeLightParamsCommand(LightParams lightParams);

    byte[] makeCommandToConfigTEVParams(TEVParams params);

    byte[] makeCommandToConfigHFCTParams(PdParamsUHF params);

    byte[] makingCommandTestStart(String typeOfCommand, String typeOfTest);

    byte[] makingCommandWithDeviceSN(String setDeviceSn,byte command);

    byte[] makeCommandWithDeviceCode(String deviceCode);

    byte[]  makeCommandToGetBasicInfo(byte sortNum);

    byte[] makeCommandToAddAMonitorDevice(MonitorDevice monitorDevice);

    byte[] makeCommandToGetWatchedDevice(byte currentSort);

    byte[] makeCommandToAddAWatchedDeviceInfo(WatchedDevice watchedDevice);

    byte[] makeCommandToBindMonitorDeviceAndWatchedDeviceTogether(String name,String json,int numberOfJson);

    byte[] makeCommandToDeleteMonitorDevice(String codeOfMonitorDevice);

    byte[] makeCommandToGetMonitorInfo();

    byte[] makeCommandToDeleteWatchedDevice(String codeOfWatchedDevice);

    int calculateThePackLength(WatchedDevice watchedDevice);

    List<byte[]> sendseveralWatchedDevicePack(WatchedDevice watchedDevice);

    int calculateBindProtocolLength(String codeOfDevice, String bindJson);

    List<byte[]> sendservaralBindProtocolPack(String codeOfDevice, String bindJson);

    byte[] makeCommandToGetPhoto();

    byte[] makeCommandToGetTempValue(IndexObj indexObj);

    byte[] makeCommandToSetInfraredParams(InfraredParams infraredParams);
}

