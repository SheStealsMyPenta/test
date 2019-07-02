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
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.WatchedDeviceManagerAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.impls.AdapterHelper;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.DeviceInfo;
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;
import com.pd.config.pdonlineconfig.pojo.WatchedDevice;
import com.pd.config.pdonlineconfig.pojo.WatchedObj;
import com.pd.config.pdonlineconfig.vies.Add_WatchedDevice_Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchedDeviceActivity extends AppCompatActivity implements NetListener {
    private InternetService service;
    private List<WatchedDevice> listOfWatchedDevice;
    private WatchedDeviceManagerAdapter adapterForWatchedDevice;
    private NetHandler netHandler;
    private DeviceInfo mainController;
    private AdapterHelper helper;
    private boolean adding = false;
    private boolean deleting = false;
    private int currentDelPosition = -1;
    private WatchedDevice currentAddDevice;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listOfMonitorDevice)
    ListView listOfMonitorDevice;
    @BindView(R.id.addBtn)
    Button addBtn;
    @BindView(R.id.updater)
    SwipeRefreshLayout updater;
    @BindView(R.id.choose)
    Button chooseBtn;
    private boolean isWatchedDevicePrepared = false;

    @OnItemClick(R.id.listOfMonitorDevice)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapterForWatchedDevice.setCurrentPosition(position);
        adapterForWatchedDevice.notifyDataSetChanged();
    }

    @OnClick({R.id.addBtn, R.id.delBtn, R.id.back, R.id.choose})
    public void invokeBtn(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                invokeAdd();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.choose:
                if (adapterForWatchedDevice.getCurrentPosition() != -1) {
                    WatchedDevice item = adapterForWatchedDevice.getItem(adapterForWatchedDevice.getCurrentPosition());
                    Intent intent = new Intent();
                    intent.putExtra("watchedDevice", item);
                    intent.setClass(WatchedDeviceActivity.this, BindMonitorDeviceAndWatchedDeviceActivity.class);
                    adapterForWatchedDevice.setCurrentPosition(-1);
                    adapterForWatchedDevice.notifyDataSetChanged();
                    startActivity(intent);
                }
                break;
            case R.id.delBtn:
                if (adapterForWatchedDevice.getCurrentPosition() != -1) {
                    WatchedDevice currentWatchedDevice = adapterForWatchedDevice.getItem(adapterForWatchedDevice.getCurrentPosition());
                    currentDelPosition = adapterForWatchedDevice.getCurrentPosition();
                    service.deleteAWatchedDeviceByName(currentWatchedDevice.getCodeOfWatchedDevice(), netHandler);
                    adapterForWatchedDevice.setCurrentPosition(-1);
                    deleting = true;
                }
                break;
        }
    }

    private void invokeAdd() {
        Add_WatchedDevice_Dialog dialog = new Add_WatchedDevice_Dialog(this, R.style.dialog, "您确定删除此信息？", (dialog1, confirm) -> {
            MonitorDevice monitorDevice = new MonitorDevice();
            //点击确定以后获取当前dialog的值
            EditText codeStr = dialog1.findViewById(R.id.codeOfWatchedDevice);
            Spinner spinner = dialog1.findViewById(R.id.typeOfWatchedDevice);
            EditText nameOfWatchedDevice = dialog1.findViewById(R.id.nameOfWatchedDevice);
            WatchedDevice device = new WatchedDevice();
            device.setCodeOfWatchedDevice(codeStr.getText().toString());
            device.setTypeOfWatchedDevice(spinner.getSelectedItem().toString());
            device.setNameOfWatchedDevice(nameOfWatchedDevice.getText().toString());
            ArrayList<WatchedObj> listOfResultObj = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            String raw = "obj_item";
            if (spinner.getSelectedItem().toString().equals("环境")) {
                //如果是环境
                for (int i = 0; i < 7; i++) {
                    if (i == 6) {
                        map.put("obj_item_cnt", 5);
                    } else {
                        switch (i) {
                            case 0:
                                map.put(raw + i, new WatchedObj(0, "", 0, 0x00, 0));
                                break;
                            case 1:
                                map.put(raw + i, new WatchedObj(0, "", 0, 0x04, 0));
                                break;
                            case 2:
                                map.put(raw + i, new WatchedObj(0, "", 0, 0x05, 0));
                                break;
                            case 3:
                                map.put(raw + i, new WatchedObj(0, "", 0, 0x07, 0));
                                break;
                            case 4:
                                map.put(raw + i, new WatchedObj(0, "", 0, 0x08, 0));
                                break;
                            case 5:
                                map.put(raw + i, new WatchedObj(0, "", 0, 0x09, 0));


                        }

                    }

                }
            } else if (spinner.getSelectedItem().toString().equals("开关柜")) {
                for (int i = 0; i < 16; i++) {
                    if (i == 15) {
                        map.put("obj_item_cnt", 15);
                    } else {

                    }
                    switch (i) {
                        case 0:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x83, 0));
                            break;
                        case 1:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x80, 0));
                            break;
                        case 2:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x81, 0));
                            break;
                        case 3:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x82, 0));
                            break;
                        case 4:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x86, 0));
                            break;
                        case 5:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x86, 1));
                            break;
                        case 6:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x85, 1));
                            break;
                        case 7:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 0));
                            break;
                        case 8:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 1));
                            break;
                        case 9:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 2));
                            break;
                        case 10:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 3));
                            break;
                        case 11:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 4));
                            break;
                        case 12:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 5));
                            break;
                        case 13:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 6));
                            break;
                        case 14:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 7));
                            break;
                        case 15:
                            map.put(raw + i, new WatchedObj(0, "", 0, 0x84, 8));
                            break;

                    }
                }
            }
            String json = JSON.toJSONString(map);
            device.setJsonStr(json);
            if (device.getTypeOfWatchedDevice().equals("环境")) {
                device.setNumberOfWatchedKind(6);
            } else {

            }
            int length = json.getBytes().length;
            if (checkValue(device)) {
                service.addANewWatchedDevice(device, netHandler);
                currentAddDevice = device;
            } else {
                Toast.makeText(this, "输入不合法", Toast.LENGTH_LONG).show();
            }

            adding = true;
            dialog1.dismiss();
        })
                .setTitle("添加被测设备");
        dialog.create();

        dialog.show();
    }

    private boolean checkValue(WatchedDevice device) {
        if (device.getCodeOfWatchedDevice().getBytes().length > 32) return false;
        if (device.getNameOfWatchedDevice().getBytes().length > 32) return false;
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moniter_device);
        mainController = CacheData.currentUnit;
        listOfWatchedDevice = CacheData.listOfWatchedDevice;
        ButterKnife.bind(this);

        helper = new AdapterHelper();
        initView();
        initInternet();
        simulateSomeData();
        initListner();
//        updateList();

    }

    private void initListner() {
        updater.setOnRefreshListener(() -> {
            updateList();
        });

    }

    private void updateList() {
        listOfWatchedDevice.clear();
        service.getWatchedDeviceBySortNumber((byte) 0x01, netHandler);

    }


    private void simulateSomeData() {
//        WatchedDevice device = new WatchedDevice();
//        device.setCodeOfWatchedDevice("3333333333222222222223333333333333");
//        device.setNameOfWatchedDevice("开关柜1");
//        device.setTypeOfWatchedDevice("开关柜");
//        WatchedDevice device1 = new WatchedDevice();
//        device1.setCodeOfWatchedDevice("111111113222222222223333333333333");
//        device1.setNameOfWatchedDevice("环境1");
//        device1.setTypeOfWatchedDevice("环境");
//        listOfWatchedDevice.add(device);
//        listOfWatchedDevice.add(device1);
        adapterForWatchedDevice = new WatchedDeviceManagerAdapter(this, R.layout.used_bt_task_format_tri, listOfWatchedDevice);
        listOfMonitorDevice.setAdapter(adapterForWatchedDevice);
    }

    private void initInternet() {
        netHandler = new NetHandler(this);
        CacheData.receiver.setNetHandler(netHandler);
        service = new InternetService();
    }

    private void initView() {
        title.setText("被测设备管理");
        updater.setOnRefreshListener(() -> service.getWatchedDeviceBySortNumber((byte) 0x00, netHandler));
    }

    @Override
    protected void onResume() {
        super.onResume();
        CacheData.receiver.setNetHandler(netHandler);
//        updateList();

    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Command.GET_WATCHED_INFO_SUCCESS:
                updater.setRefreshing(false);
                String json = (String) msg.getData().getSerializable("jsonStr");
                List<WatchedDevice> watchedDevices = helper.getListOfWatchedDeviceByJsonStr(json);
                for (WatchedDevice device : watchedDevices) {
                    listOfWatchedDevice.add(device);
                }
                adapterForWatchedDevice.notifyDataSetChanged();
                //获取出来的是一个Json字符串

                break;
            case Command.Check_GETBASIC_INFO_OVERTIME:
                if (updater.isRefreshing()) {
                    Toast.makeText(this, "获取超时", Toast.LENGTH_LONG).show();
                    updater.setRefreshing(false);
                }
                break;
            case Command.ADD_WATCHED_INFO_SUCCESS:
                adding = false;
                updateList();
                Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
                break;
            case Command.CHECK_ADD_WATCHED_INFO_OVERTIME:
                if (adding) {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_LONG).show();
                    adding = false;
                }

                break;
            case Command.CHECK_DELETE_WATCHED_DEVICE_SUCCESS:
                if (deleting) {
                    Toast.makeText(this, "删除失败", Toast.LENGTH_LONG).show();
                    deleting = false;
                }
                break;
            case Command.DEL_WATCHED_INFO_SUCCESS:
                Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
                deleting = false;
                updateList();
                break;

        }
    }

    private boolean checkWatchedDeviceExist(WatchedDevice watchedDevice) {
        if (listOfWatchedDevice.size() > 0) {
            for (int i = 0; i < listOfWatchedDevice.size(); i++) {
                if (listOfWatchedDevice.get(i).getCodeOfWatchedDevice().equals(watchedDevice.getCodeOfWatchedDevice())) {
                    return true;
                }
            }
        }
        return false;
    }
}
