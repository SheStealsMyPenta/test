package com.pd.config.pdonlineconfig.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.DeviceInfo;
import com.pd.config.pdonlineconfig.pojo.MonitorDevice;
import com.pd.config.pdonlineconfig.pojo.WatchedDevice;
import com.pd.config.pdonlineconfig.vies.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class MainControllerActivity extends AppCompatActivity implements NetListener {
    @BindView(R.id.codeOfDevice)
    TextView codeOfDevice;
    @BindView(R.id.ipAddress)
    TextView ipAddress;

    @BindView(R.id.mainControllerConfig)
    LinearLayout systemConfig;
    @BindView(R.id.mainControllerInfo)
    LinearLayout mainConrollerInfo;
    @BindView(R.id.monitorDeviceMange)
    LinearLayout monitorDeviceManage;
    @BindView(R.id.detectedDeviceManage)
    LinearLayout detectedDeviceManage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private LoadingDialog loadingDialog;
    private Long preventDoubleClick;
    private boolean isFinisheLoading = false;
    private InternetService mInternetService;
    private NetHandler mHandler;
    private DeviceInfo currentDevice;
    private List<MonitorDevice> listOfMonitorDevices;
    private List<WatchedDevice> listOfWatchedDevices;
    private boolean isWatchedDevicePrepared = false;
    private boolean isMonitorDevicePrepared = false;
    private boolean isFetchingData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_controller_config);
        //绑定视图层到Model层
        ButterKnife.bind(this);
        preventDoubleClick = System.currentTimeMillis();
        getInfoFromBundle();
        componentDidMount();
        loadingToInitInfo();

    }

    private void loadingToInitInfo() {
        loadingDialog = new LoadingDialog(this, "数据初始化中...");
//显示Dialog
//        isFetchingData =true;
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = Command.CHECK_MAIN_CONTROLLER_GETINFO_SUCCESS;
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
//        loadingDialog.show();
        listOfMonitorDevices = CacheData.listOfMonitorDevice;
        listOfWatchedDevices = CacheData.listOfWatchedDevice;
        currentDevice = CacheData.currentUnit;
        getMonitorDevices();

    }

    private void getMonitorDevices() {
        mInternetService.getMonitorJSONStr(mHandler);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void componentDidMount() {
//        codeOfDevice.setText(currentDevice.getCodeOfDevice());
//        ipAddress.setText(currentDevice.getIpAddress())
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initOnclickLister();
        mInternetService = new InternetService();
        mHandler = new NetHandler(this);
        CacheData.receiver.setNetHandler(mHandler);
    }

    private void initOnclickLister() {
        mainConrollerInfo.setOnClickListener(v -> {

            long last = preventDoubleClick;
            preventDoubleClick = System.currentTimeMillis();
            if (preventDoubleClick - last < 500) {
                return;
            } else {
                Intent intent = new Intent();
                intent.setClass(MainControllerActivity.this, SystemInfoActivity.class);
                startActivity(intent);
            }


        });
        monitorDeviceManage.setOnClickListener(v -> {
            long last = preventDoubleClick;
            preventDoubleClick = System.currentTimeMillis();
            if (preventDoubleClick - last < 500) {
                return;
            } else {
                Intent intent = new Intent();
                intent.setClass(MainControllerActivity.this, MonitorDeviceManageActivity.class);
                startActivity(intent);
            }

//            new CommonDialog(this, R.style.dialog, "您确定删除此信息？", new CommonDialog.OnCloseListener() {
//                @Override
//                public void onClick(Dialog dialog, boolean confirm) {
//                    Toast.makeText(getApplicationContext(),"点击确定", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//
//            })
//                    .setTitle("提示").show();

        });
        systemConfig.setOnClickListener(v -> {
            long last = preventDoubleClick;
            preventDoubleClick = System.currentTimeMillis();
            if (preventDoubleClick - last < 500) {
                return;
            } else {
                Intent intent = new Intent();
                intent.setClass(MainControllerActivity.this, SystemConfigActivity.class);
                startActivity(intent);
            }


        });
        detectedDeviceManage.setOnClickListener(v -> {
            long last = preventDoubleClick;
            preventDoubleClick = System.currentTimeMillis();
            if (preventDoubleClick - last < 500) {
                return;
            } else {
                Intent intent = new Intent();
                intent.setClass(MainControllerActivity.this, WatchedDeviceActivity.class);
                startActivity(intent);
            }

        });


    }

    private void getInfoFromBundle() {
        DeviceInfo info = CacheData.currentUnit;
        ipAddress.setText(info.getIpAddress());
        codeOfDevice.setText(info.getCodeOfDevice());

    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Command.GET_DEVICE_INFO:
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
                                listOfMonitorDevices.add(monitorDevice);
                            }
                        }
                    }
                }

                break;
            case Command.GET_WATCHED_INFO:
                WatchedDevice watchedDevice = (WatchedDevice) msg.getData().getSerializable("watchedDevice");
                if (watchedDevice.getSort() == currentDevice.getNumberOfDetectedCells()) {
                    isWatchedDevicePrepared = true;
                }
                if (checkWatchedDeviceExist(watchedDevice)) {
                    listOfWatchedDevices.add(watchedDevice);
                }
                break;
            case Command.CHECK_MAIN_CONTROLLER_GETINFO_SUCCESS:
                if (isFetchingData) {
                    this.finish();
                }
                break;
        }
    }

    private boolean checkWatchedDeviceExist(WatchedDevice watchedDevice) {
        if (listOfWatchedDevices.size() > 0) {
            for (int i = 0; i < listOfWatchedDevices.size(); i++) {
                if (listOfWatchedDevices.get(i).getCodeOfWatchedDevice().equals(watchedDevice.getCodeOfWatchedDevice())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkExist(DeviceInfo deviceInfo) {
        if (listOfMonitorDevices.size() > 0) {
            for (int i = 0; i < listOfMonitorDevices.size(); i++) {
                if (listOfMonitorDevices.get(i).getCodeOfDevice().equals(deviceInfo.getCodeOfDevice())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void cancelLoading() {
        //清除加载
        if (isWatchedDevicePrepared && isMonitorDevicePrepared) {
            loadingDialog.close();
        }
    }
}
