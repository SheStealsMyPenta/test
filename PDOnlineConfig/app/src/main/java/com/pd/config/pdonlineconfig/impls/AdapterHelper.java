package com.pd.config.pdonlineconfig.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.pojo.ResultObj;
import com.pd.config.pdonlineconfig.pojo.WatchedDevice;

import java.util.ArrayList;
import java.util.List;

public class AdapterHelper {
    public List<ResultObj> getListOfInsepctByJsonStr(String jsonStr, String type) {
        ArrayList<ResultObj> listOfInspectObj = new ArrayList<>();
        if (type.equals("环境")) {
            String relationShipMap = jsonStr;
            JSONArray array = JSON.parseArray(relationShipMap);
            int length = array.getInteger(0);
            for (int i = 1; i < length + 1; i++) {
                //每一项都是一个测试相测试参数
                JSONObject eachConfig = array.getJSONObject(i);

                ResultObj newObj = new ResultObj();

                Integer status = eachConfig.getInteger("item_status");
                if (status == 0) {
                    newObj.setOpen(false);
                } else {
                    newObj.setOpen(true);
                }
                Byte typeByte = eachConfig.getByte("item_type");
                if (typeByte == 0x84) {
                    //无线测温
                    Byte id = eachConfig.getByte("item_id");

                    switch (id) {
                        case 0:
                            newObj.setTypeOfMonitor("开关上触头A相温度监测");
                            break;
                        case 1:
                            newObj.setTypeOfMonitor("开关上触头B相温度监测");
                            break;
                        case 2:
                            newObj.setTypeOfMonitor("开关上触头C相温度监测");
                            break;
                        case 3:
                            newObj.setTypeOfMonitor("开关下触头A相温度监测");
                            break;
                        case 4:
                            newObj.setTypeOfMonitor("开关下触头B相温度监测");
                            break;
                        case 5:
                            newObj.setTypeOfMonitor("开关下触头C相温度监测");
                            break;
                        case 6:
                            newObj.setTypeOfMonitor("电缆连接头A相温度监测");
                            break;
                        case 7:
                            newObj.setTypeOfMonitor("电缆连接头B相温度监测");
                            break;
                        case 8:
                            newObj.setTypeOfMonitor("电缆连接头C相温度监测");
                            break;
                    }
                } else if (typeByte == 0x86) {
                    if (eachConfig.getByte("item_id") == 0) {
                        newObj.setTypeOfMonitor("电缆仓温湿度监测");
                    } else {
                        newObj.setTypeOfMonitor("仪表仓温湿度监测");
                    }
                } else {
                    newObj.setTypeOfMonitor(CacheData.mapOfTheOption.get(typeByte));
                }
                String itemNum = eachConfig.getString("item_num");
                newObj.setChannel(String.valueOf(eachConfig.getByte("item_chn")));
                newObj.setNameOfMoniorDevice(eachConfig.getString("item_num"));
                listOfInspectObj.add(newObj);


            }
            return listOfInspectObj;
//           ResultObj obj = new ResultObj();
//            ResultObj obj1 = new ResultObj();
//            ResultObj obj2 = new ResultObj();
//            ResultObj obj3 = new ResultObj();
//            ResultObj obj4 = new ResultObj();
//            obj.setNameOfMoniorDevice("特高频局放监测");
//            obj1.setNameOfMoniorDevice("空气式超声局放监测");
//            obj2.setNameOfMoniorDevice("红外热像监测");
//            obj3.setNameOfMoniorDevice("视频图像监测");
//            obj4.setNameOfMoniorDevice("气体监测");
//            ArrayList<ResultObj> list = new ArrayList<>();
//            listOfInspectObj = list;
//            list.add(obj);
//            list.add(obj1);
//            list.add(obj2);
//            list.add(obj3);
//            list.add(obj4);
        } else {

        }

        return listOfInspectObj;
    }

    public List<WatchedDevice> getListOfWatchedDeviceByJsonStr(String jsonStr) {
        JSONObject object = JSON.parseObject(jsonStr);
        //json对象
        System.out.println(object);
        //被测对象的个数
        Integer numberOfWatchedObj = object.getInteger("mcc_obj_cnt");
        //循环遍历每一个被测对象及下面的被测项的状态和数据
        String watchedRaw = "mcc_obj";
        ArrayList<WatchedDevice> listOfWatchedDevice = new ArrayList<>();
        for (int i = 0; i < numberOfWatchedObj; i++) {
            //每一个被测对象

            JSONObject eachWatched = object.getJSONObject(watchedRaw + i);
            WatchedDevice device = new WatchedDevice();
            device.setNameOfWatchedDevice(eachWatched.getString("obj_name"));
            device.setCodeOfWatchedDevice(eachWatched.getString("obj_num"));
            Integer type1 = eachWatched.getInteger("obj_type");
            if(type1==0){
                device.setTypeOfWatchedDevice("环境");
            }else {
                device.setTypeOfWatchedDevice("开关柜");
            }
            device.setNumberOfWatchedKind(eachWatched.getInteger("obj_item_cnt"));
            //在每一个被测设备里遍历每一个测试项的状态
            String testRaw = "obj_item";
            ArrayList<ResultObj> listOfResultObj = new ArrayList<>();
            for (int j = 0; j < device.getNumberOfWatchedKind(); j++) {
                JSONObject eachTestObj =eachWatched.getJSONObject("obj_item" + j);
                ResultObj obj = new ResultObj();
                obj.setOpen(eachTestObj.getInteger("item_status") == 1);
                obj.setNameOfMoniorDevice(eachTestObj.getString("item_num"));
                String type = CacheData.mapOfTheOption.get(Byte.parseByte(eachTestObj.getString("item_type")));
                if(type!=null){
                    if (type.equals("柜内测温局放监测")) {

                        switch (eachTestObj.getInteger("item_id")) {
                            case 0:
                                obj.setTypeOfMonitor("开关上触头A相温度监测");
                                break;
                            case 1:
                                obj.setTypeOfMonitor("开关上触头B相温度监测");
                                break;
                            case 2:
                                obj.setTypeOfMonitor("开关上触头C相温度监测");
                                break;
                            case 3:
                                obj.setTypeOfMonitor("开关下触头A相温度监测");
                                break;
                            case 4:
                                obj.setTypeOfMonitor("开关下触头B相温度监测");
                                break;
                            case 5:
                                obj.setTypeOfMonitor("开关下触头C相温度监测");
                                break;
                            case 6:
                                obj.setTypeOfMonitor("电缆连接头A相温度监测");
                                break;
                            case 7:
                                obj.setTypeOfMonitor("电缆连接头B相温度监测");
                                break;
                            case 8:
                                obj.setTypeOfMonitor("电缆连接头C相温度监测");
                                break;

                        }
                    } else if (type.equals("柜内温湿度")) {
                        switch (eachTestObj.getInteger("item_id")) {
                            case 0:
                                obj.setTypeOfMonitor("电缆仓温湿度监测");
                                break;
                            case 1:
                                obj.setTypeOfMonitor("仪表仓温湿度监测");
                                break;
                        }
                    } else {
                        obj.setTypeOfMonitor(type);
                    }
                    listOfResultObj.add(obj);
                }
            }
            device.setListOfInspectObj(listOfResultObj);


            listOfWatchedDevice.add(device);
        }
        return listOfWatchedDevice;
    }
}
