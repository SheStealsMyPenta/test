package com.pd.config.pdonlineconfig.activities;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alibaba.fastjson.JSON;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.InspectateDeviceAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.impls.AdapterHelper;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.BindObj;
import com.pd.config.pdonlineconfig.pojo.ResultObj;
import com.pd.config.pdonlineconfig.pojo.WatchedDevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindMonitorDeviceAndWatchedDeviceActivity extends AppCompatActivity implements NetListener {
    @BindView(R.id.listOfConfig)
    ListView listOfConfig;
    @BindView(R.id.codeOfWatchedDevice)
    TextView codeOfWatchedDevice;
    @BindView(R.id.nameOfWatchedDevice)
    TextView nameOfWatchedDevice;
    @BindView(R.id.save)
    Button saveBtn;
    @BindView(R.id.back)
    Button backBtn;
    private WatchedDevice currentWatchedDeivce;
    private InspectateDeviceAdapter adapter;
    private InternetService service;
    private NetHandler handler;
    private AdapterHelper adapterHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspectate_device);
        ButterKnife.bind(this);
        getWatchedDeviceFromIntent();
//       .
        bindClickListner();
        adapterHelper = new AdapterHelper();
        service = new InternetService();
        initInternet();
    }

    private void initInternet() {
        //把消息通知放到本类
        NetHandler handler = new NetHandler(this);
        CacheData.receiver.setNetHandler(handler);
        this.handler = handler;
    }

    private void bindClickListner() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    Map<String, Object> mapOfResult = new HashMap<>();
                    List<ResultObj> list = adapter.getResultList();
                    if (adapter.getCurrentType().equals("环境监测装置")) {
                        String raw = "dev_func";
                        for (int i = 0; i < list.size(); i++) {
                            //测试
                            String key = raw + i;
                            //监测名称
                            BindObj obj = new BindObj();
                            obj.setItem_type(CacheData.mapOfTheOptionReverse.get(list.get(i).getTypeOfMonitor()));
                            obj.setItem_num(list.get(i).getNameOfMoniorDevice());
                            obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            obj.setItem_id((byte) 0);
                            mapOfResult.put(key, obj);
                        }
                    } else {
                        String raw = "dev_func";
                        for (int i = 0; i < list.size(); i++) {
                            //测试
                            String key = raw + i;
                            //监测名称
                            BindObj obj = new BindObj();
                            if (list.get(i).getTypeOfMonitor().equals("电缆仓温湿度")) {
                                obj.setItem_type((byte) 0x86);
                                obj.setItem_id((byte) 0);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("仪表仓温湿度")) {
                                obj.setItem_type((byte) 0x86);
                                obj.setItem_id((byte) 1);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("开关上触头A相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 0);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("开关上触头B相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 1);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("开关上触头C相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 2);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("开关下触头A相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 3);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("开关下触头B相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 4);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("开关下触头C相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 5);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("电缆连接头A相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 6);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("电缆连接头B相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 7);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else if (list.get(i).getTypeOfMonitor().equals("电缆连接头C相温度监测")) {
                                obj.setItem_type((byte) 0x84);
                                obj.setItem_id((byte) 8);
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                            } else {
                                obj.setItem_type(CacheData.mapOfTheOptionReverseCub.get(list.get(i).getTypeOfMonitor()));
                                obj.setItem_num(currentWatchedDeivce.getCodeOfWatchedDevice());
                                obj.setItem_status((byte) (list.get(i).isOpen() ? 1 : 0));
                                obj.setItem_id((byte) 0);
                            }

                            mapOfResult.put(key, obj);
                        }

                    }
                    String s = JSON.toJSONString(mapOfResult);
                    System.out.println(s.getBytes().length);
                    service.bindDeviceTogether(currentWatchedDeivce.getCodeOfWatchedDevice(), s, handler, list.size());
//                    ArrayList<ResultObj> listOfItemCanBeAdd = new ArrayList<>();
//                    for (int i = 0; i < list.size(); i++) {
//                        if (list.get(i).isOpen()) {
//                            if (!list.get(i).getChannel().equals("")) {
//                                listOfItemCanBeAdd.add(list.get(i));
//                            }
//                        }
//                    }
//                    if (listOfItemCanBeAdd.size() != 0) {
//                        service.bindDeviceTogether(currentWatchedDeivce.getCodeOfWatchedDevice(), listOfItemCanBeAdd, handler);
//                    }


                }

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getWatchedDeviceFromIntent() {
        currentWatchedDeivce = (WatchedDevice) getIntent().getExtras().getSerializable("watchedDevice");
        nameOfWatchedDevice.setText(currentWatchedDeivce.getNameOfWatchedDevice());
        codeOfWatchedDevice.setText(currentWatchedDeivce.getCodeOfWatchedDevice());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (currentWatchedDeivce.getTypeOfWatchedDevice().equals("环境")) {
            List<ResultObj> list = currentWatchedDeivce.getListOfInspectObj();
            adapter = new InspectateDeviceAdapter(this, R.layout.inspect_list_adapter, list, "环境监测装置");
            listOfConfig.setAdapter(adapter);
        } else {
            List<ResultObj> list = currentWatchedDeivce.getListOfInspectObj();

            adapter = new InspectateDeviceAdapter(this,R.layout.inspect_list_adapter,list,"开关柜监测装置");
            listOfConfig.setAdapter(adapter);
//            ResultObj obj = new ResultObj();
//            ResultObj obj1 = new ResultObj();
//            ResultObj obj2 = new ResultObj();
//            ResultObj obj3 = new ResultObj();
//            ResultObj obj4 = new ResultObj();
//            ResultObj obj5 = new ResultObj();
//            ResultObj obj6 = new ResultObj();
//            ResultObj obj7 = new ResultObj();
//            ResultObj obj8 = new ResultObj();
//            ResultObj obj9 = new ResultObj();
//            ResultObj obj10 = new ResultObj();
//            ResultObj obj11 = new ResultObj();
//            ResultObj obj12 = new ResultObj();
//            ResultObj obj13 = new ResultObj();
//            ResultObj obj14 = new ResultObj();
//            ResultObj obj15 = new ResultObj();
//            obj.setNameOfMoniorDevice("暂态地电压局放监测");
//            obj1.setNameOfMoniorDevice("空气式超声局放监测");
//            obj2.setNameOfMoniorDevice("特高频局放监测");
//            obj3.setNameOfMoniorDevice("高频电流局放监测");
//            obj4.setNameOfMoniorDevice("电缆仓温湿度监测");
//            obj5.setNameOfMoniorDevice("仪表仓温湿度监测");
//            obj6.setNameOfMoniorDevice("开关上触头A相温度监测");
//            obj7.setNameOfMoniorDevice("开关上触头B相温度监测");
//            obj8.setNameOfMoniorDevice("开关上触头C相温度监测");
//            obj9.setNameOfMoniorDevice("开关下触头A相温度监测");
//            obj10.setNameOfMoniorDevice("开关下触头B相温度监测");
//            obj11.setNameOfMoniorDevice("开关下触头C相温度监测");
//            obj12.setNameOfMoniorDevice("电缆连接头A相温度监测");
//            obj13.setNameOfMoniorDevice("电缆连接头B相温度监测");
//            obj14.setNameOfMoniorDevice("电缆连接头C相温度监测");
//            obj15.setNameOfMoniorDevice("断路器机械特性监测");
//            ArrayList<ResultObj> list = new ArrayList<>();
//            list.add(obj);
//            list.add(obj1);
//            list.add(obj2);
//            list.add(obj3);
//            list.add(obj4);
//            list.add(obj5);
//            list.add(obj6);
//            list.add(obj7);
//            list.add(obj8);
//            list.add(obj9);
//            list.add(obj10);
//            list.add(obj11);
//            list.add(obj12);
//            list.add(obj13);
//            list.add(obj14);
//            list.add(obj15);
//            adapter = new InspectateDeviceAdapter(this, R.layout.inspect_list_adapter, list, "开关柜监测装置");
//            listOfConfig.setAdapter(adapter);

        }

    }

    @Override
    public void handleMessage(Message msg) {

    }
}
