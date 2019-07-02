package com.pd.config.pdonlineconfig.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.MonitorDeviceManagerAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.DeviceInfo;
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;
import com.pd.config.pdonlineconfig.utils.ConversionTool;
import com.pd.config.pdonlineconfig.vies.CommonDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MonitorDeviceManageActivity extends AppCompatActivity implements NetListener {
    NetHandler handler;
    private InternetService internetService;
    private DeviceInfo currentInfo;
    private List<MonitorDevice> listOfDevices;
    private MonitorDeviceManagerAdapter adapterForMonitorDevice;
    private DeviceInfo mainDevice;
    private MonitorDevice currentItem;
    private MonitorDevice currentAdd;
    private boolean isDeleting = false;
    @BindView(R.id.listOfMonitorDevice)
    ListView listOfMonitorDevice;
    @BindView(R.id.addBtn)
    Button addBtn;
    @BindView(R.id.delBtn)
    Button delBtn;
    @BindView(R.id.updater)
    SwipeRefreshLayout updater;
    @BindView(R.id.back)
    Button backBtn;
    @BindView(R.id.choose)
    Button chooseBtn;
    private int currentPosition = -1;

    @OnClick({R.id.addBtn, R.id.delBtn, R.id.back, R.id.choose})
    public void invokeBtn(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                invokeAdd();
                break;
            case R.id.back:
//                Intent intentBack = new Intent();
//                intentBack.setClass(MonitorDeviceManageActivity.this, MainControllerActivity.class);
//                startActivity(intentBack);
                finish();
                break;
            case R.id.choose:
                if (adapterForMonitorDevice.getCurrentPosition() != -1) {
                    currentInfo = adapterForMonitorDevice.getADeviceInfo(adapterForMonitorDevice.getCurrentPosition());

                    if (currentInfo.getTypeOfMonitorDevice().equals("环境监测装置")) {
                        CacheData.currentMonitorDevice = currentInfo;
                        Intent intent = new Intent();
                        intent.setClass(MonitorDeviceManageActivity.this, TestActivity.class);
                        intent.putExtra("activity", "fromList");
                        intent.putExtra("currentDevice", currentInfo);
                        intent.putExtra("currentType", "特高频");
                        startActivity(intent);
                        CacheData.sort = 0;
                    } else if (currentInfo.getTypeOfMonitorDevice().equals("开关柜监测装置")) {
                        CacheData.currentMonitorDevice = currentInfo;
                        Intent intent = new Intent();
                        intent.setClass(MonitorDeviceManageActivity.this, CubicleTestActivity.class);
                        intent.putExtra("currentDevice", currentInfo);
                        intent.putExtra("currentType", "特高频");
                        startActivity(intent);
                    }
                }
                break;
            case R.id.delBtn:
                if (adapterForMonitorDevice.getCurrentPosition() != -1 && listOfDevices.size() > 0) {
                    currentInfo = adapterForMonitorDevice.getADeviceInfo(adapterForMonitorDevice.getCurrentPosition());
                    internetService.deleteAMonitorDevice(currentInfo.getCodeOfDevice(), handler);
                    updater.setRefreshing(true);
                    isDeleting = true;
                    break;
                }

        }
    }

    @OnItemClick(R.id.listOfMonitorDevice)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapterForMonitorDevice.setCurrentPosition(position);
        adapterForMonitorDevice.notifyDataSetChanged();
        currentPosition = position;
    }

    private String exChange2(String str) {
        for (int i = 0; i < str.length(); i++) {
            //如果是小写
            if (str.substring(i, i + 1).equals(str.substring(i, i + 1).toLowerCase())) {
                str.substring(i, i + 1).toUpperCase();
            } else {
                str.substring(i, i + 1).toLowerCase();
            }
        }
        return str;
    }

    private void invokeAdd() {
        new CommonDialog(this, R.style.dialog, "您确定删除此信息？", (dialog, confirm) -> {
            MonitorDevice monitorDevice = new MonitorDevice();

            //点击确定以后获取当前dialog的值
            LinearLayout linearLayout = dialog.findViewById(R.id.addressLayout);
            EditText codeStr = dialog.findViewById(R.id.codeOfDevice);
            Spinner spinner = dialog.findViewById(R.id.typeOfDevice);
            EditText address = dialog.findViewById(R.id.physicAddress);
//            String[] ips = superEditText.getSuperEditTextValue();
//            StringBuilder ipStr = new StringBuilder();
//            for (int i = 0; i < ips.length; i++) {
//                if (i < ips.length - 1) {
//                    ipStr.append(ips[i]).append(".");
//                } else {
//                    ipStr.append(ips[i]);
//                }
//            }


            if (checkValue(codeStr, address.getText().toString(), spinner.getSelectedItem().toString())) {
                boolean canBeAddedEnv = true;
                boolean canBeAddedCode = true;
                boolean canBeAddedAddress = true;
                String code = codeStr.getText().toString();

                Iterator<MonitorDevice> iterator = listOfDevices.iterator();
                while (iterator.hasNext()) {
                    MonitorDevice next = iterator.next();
                    if (code.substring(0, 2).equals("A0")) {
                        if (next.getCodeOfMonitor().substring(0, 2).equals("A0")) {
                            canBeAddedEnv = false;
                        }
                    }

                    if (next.getCodeOfMonitor().equals(code)) {
                        canBeAddedCode = false;
                    }
                    if (next.getPhysicAddress().equals(address.getText().toString())) {
                        canBeAddedAddress = false;
                    }


                }
                if (!canBeAddedAddress) {
                    Toast.makeText(getBaseContext(), "设备地址已存在 ，请勿重复添加！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!canBeAddedCode) {
                    Toast.makeText(getBaseContext(), "装置已存在，请勿重复添加！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!canBeAddedEnv) {
                    Toast.makeText(getBaseContext(), "环境装置已存在，请勿重复添加！", Toast.LENGTH_LONG).show();
                    return;
                }


                monitorDevice.setPhysicAddress(address.getText().toString());
                monitorDevice.setTypeOfTheMonitor(spinner.getSelectedItem().toString());
                monitorDevice.setCodeOfMonitor(exChange2(codeStr.getText().toString()));
                currentAdd = monitorDevice;
                internetService.addANewMonitorDevice(monitorDevice, handler);
                updater.setRefreshing(true);


            } else {
                Toast.makeText(getBaseContext(), "输入不合法", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
        })
                .setTitle("提示").show();

    }

    private boolean checkValue(EditText codeStr, String ips, String typeOfDevice) {
//        for (String ipValue : ips) {
//            if (ipValue.equals("")) {
//                return false;
//            }
//            if ((Integer.parseInt(ipValue) > 255)) {
//                return false;
//            }
//        }\


        if (codeStr.getText().toString().getBytes().length != 8) return false;
        String type = codeStr.getText().toString().substring(0, 2);
        if (!type.equals("A0")) {
            if (!ips.equals("")) {
                short i = Short.parseShort(ips);
                if (ConversionTool.short2bits(i).length > 2) return false;
            } else {
                return false;
            }
        }

        if (typeOfDevice.equals("环境监测")) {
            if (!type.equals("A0")) {
                return false;
            }
        }
        if (typeOfDevice.equals("开关柜监测")) {
            if (!type.equals("A3")) {
                return false;
            }
        }
        if (typeOfDevice.equals("无线测温")) {
            if (!type.equals("A2")) {
                return false;
            }
        }
        return !codeStr.getText().toString().equals("");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moniter_device);
        ButterKnife.bind(this);
        initInternet();
        initSwipeLayout();
        getEachDeviceInfo();
        simulateSomeData();
        initItemClickListner();
        getAMonitorDeviceInfo();
    }

    private void initItemClickListner() {

    }

    private void simulateSomeData() {
        mainDevice = CacheData.currentUnit;
        listOfDevices = CacheData.listOfMonitorDevice;
        adapterForMonitorDevice = new MonitorDeviceManagerAdapter(this, R.layout.used_bt_task_format_tri, listOfDevices);
        listOfMonitorDevice.setAdapter(adapterForMonitorDevice);

    }

    private void initSwipeLayout() {
        updater.setOnRefreshListener(() -> getAMonitorDeviceInfo());
    }

    private void getAMonitorDeviceInfo() {
        //每次刷新清空设备列表从0x00开始获取
        listOfDevices.clear();
        internetService.getMonitorJSONStr(handler);
//        byte numberOfCells = mainDevice.getNumberOfCells();
//        for (int i = 0; i < numberOfCells; i++) {
//            internetService.getMonitorDeviceBySortNumber((byte) i, handler);
//        }

    }

    private void getEachDeviceInfo() {
    }

    private void initInternet() {
        handler = new NetHandler(this);
        CacheData.receiver.setNetHandler(handler);
        internetService = new InternetService();
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Command
                    .SendMonitorJSON:
                updater.setRefreshing(false);
                //json字符串
                String str = (String) msg.getData().get("jsonStr");
//                JSONArray jsonArray = JSON.(str);
                JSONObject object = JSON.parseObject(str);
                if (object != null) {
                    Integer integer = object.getInteger("mcc_dev_cnt");
                    if (integer == 0) {
                        Toast.makeText(this, "当前监测设备为0，请添加", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < integer; i++) {
                            String name = "mcc_dev";
                            JSONObject jsonObject = object.getJSONObject(name + i);
                            if (jsonObject != null) {
                                MonitorDevice monitorDevice = new MonitorDevice();

                                String code = jsonObject.getString("dev_num");
                                monitorDevice.setCodeOfMonitor(code);
                                monitorDevice.setPhysicAddress(jsonObject.getString("dev_addr"));
                                monitorDevice.setNumberOfChannel(jsonObject.getInteger("dev_chn_cnt"));
                                if (code.substring(0, 2).equals("A0")) {
                                    monitorDevice.setTypeOfTheMonitor("环境监测装置");
                                } else if (code.substring(0, 2).equals("A2")) {
                                    monitorDevice.setTypeOfTheMonitor("无线测温监测");

                                } else if (code.substring(0, 2).equals("A3")) {
                                    monitorDevice.setTypeOfTheMonitor("开关柜监测装置");
                                }
                                int numberOfChannel = monitorDevice.getNumberOfChannel();
                                List<String> supportType = new ArrayList<>();
//                        if (code.substring(0, 2).equals("A0")) {
//                            for (int j = 0; j < numberOfChannel; j++) {
//                                String key = "dev_func" + j;
//                                Integer state = jsonObject.getInteger(key);
//                                if (state == 1) {
//                                    supportType.add(String.valueOf(j));
//                                }
//                            }
//                        }
//                        else if (code.substring(0, 2).equals("A3")) {
//                            for (int j = 0; j < 7; j++) {
//                                String key = "dev_func" + j;
//                                Integer state = jsonObject.getInteger(key);
//                                if (state == 1) {
//                                    supportType.add(String.valueOf(j));
//                                }
//                            }
//                        }
                                listOfDevices.add(monitorDevice);
                                adapterForMonitorDevice.notifyDataSetChanged();
                            }

                        }

                    }

                }


                break;
            case Command.BIND_DEVICE_SUCCESS:
                updater.setRefreshing(false);
                Toast.makeText(this, "绑定设备成功", Toast.LENGTH_LONG).show();
                getAMonitorDeviceInfo();
//                //添加完成重新刷新一次列表
//                listOfDevices.add(currentAdd);
//                adapterForMonitorDevice.notifyDataSetChanged();
                break;
            case Command.Check_GETBASIC_INFO_OVERTIME:
                //若没有网络返回则自动停止loading
                updater.setRefreshing(false);
                break;
            case Command.Check_BIND_DEVICEINFO_OVERTIME:
                if (updater.isRefreshing()) {
                    Toast.makeText(this, "绑定设备超时", Toast.LENGTH_LONG).show();
                    updater.setRefreshing(false);
                }
                break;
            case Command.CHECK_DELETE_MONITOR_DEVICE_SUCCESS:
                if (updater.isRefreshing()) {
                    Toast.makeText(this, "删除设备超时", Toast.LENGTH_LONG).show();
                    updater.setRefreshing(false);
                    isDeleting = false;
                }
            case Command.DELETE_MONITOR_DEVICE_SUCCESS:
                if (isDeleting) {
                    if (currentPosition != -1) {
                        listOfDevices.remove(currentPosition);
                        adapterForMonitorDevice.setCurrentPosition(-1);
                        adapterForMonitorDevice.notifyDataSetChanged();
                        updater.setRefreshing(false);
                        Toast.makeText(this, "删除设备成功", Toast.LENGTH_LONG).show();
                    }

                    isDeleting = false;
                }

                break;
            case Command.CHECK_GETMONITOR_OVERTIME:
                if (updater.isRefreshing()) {
                    Toast.makeText(this, "获取设备超时", Toast.LENGTH_LONG).show();
                    updater.setRefreshing(false);
                }

                break;
        }
    }

    private void initAdapter() {
        adapterForMonitorDevice = new MonitorDeviceManagerAdapter(this, R.layout.used_bt_task_format_tri, listOfDevices);
        listOfMonitorDevice.setAdapter(adapterForMonitorDevice);
    }

    private void notifyDataSetChange() {

    }

    private boolean checkExist(DeviceInfo deviceInfo) {
        if (listOfDevices.size() > 0) {
            for (int i = 0; i < listOfDevices.size(); i++) {
                if (listOfDevices.get(i).getCodeOfDevice().equals(deviceInfo.getCodeOfDevice())) {
                    return true;
                }
            }
        }
        return false;
    }

}
