package com.pd.config.pdonlineconfig.activities;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.fragment.ControlUnitBasicParamFragment;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.TestParams;
import com.pd.config.pdonlineconfig.utils.ConversionTool;
import com.pd.config.pdonlineconfig.vies.SuperEditText;

public class SystemConfigActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, NetListener {
    @BindView(R.id.timePicker)
    TextView timePicker;
    @BindView(R.id.updater)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ipAddress)
    SuperEditText ipAddress;
    @BindView(R.id.port)
    EditText port;
    @BindView(R.id.uploadInterval)
    Spinner uploadInterval;
    @BindView(R.id.deviceIpAddress)
    SuperEditText deviceIpAddress;
    @BindView(R.id.subnetMask)
    SuperEditText subnetInternet;
    @BindView(R.id.gateWay)
    SuperEditText gateWay;
    @BindView(R.id.isRestart)
    Spinner isRestart;
    @BindView(R.id.syncFrequency)
    EditText syncFrequency;
    @BindView(R.id.syncType)
    Spinner syncType;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.address)
    EditText comAddress;
    private View view;
    private ControlUnitBasicParamFragment self;
    @BindView(R.id.confirmBtn)
    Button confirmBtn;
    private NetHandler handler;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //变量
    private int hourOfDay;
    private volatile boolean fetchingData = false;
    private volatile boolean setting = false;
    private int minute;
    private InternetService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_config);
        ButterKnife.bind(this);
        view = getWindow().getDecorView();
        initInternet();
        initListner();
        //初始化页面的时候获取一次
        swipeRefreshLayout.setRefreshing(true);
        service.getSystemConfig(handler);

    }

    private void initListner() {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(() ->

                {
                    fetchingData = true;
                    service.getSystemConfig(handler);

                }
        );
        timePicker.setOnClickListener(v -> callOutTimePicker(view));
        confirmBtn.setOnClickListener(v -> {
            if (checkValue()) {
                setting = true;
                service.setSystemConfig(getValues(), handler);
                swipeRefreshLayout.setRefreshing(true);
            } else {
                Toast.makeText(this, "输入格式不正确！", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initInternet() {
        handler = new NetHandler(this);
        service = new InternetService();
        CacheData.receiver.setNetHandler(handler);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Command
                    .GET_PD_PARAMS_SUCCESS:
                fetchingData = false;
                swipeRefreshLayout.setRefreshing(false);
                TestParams testParams = (TestParams) msg.getData().getSerializable("basicTestParams");
                setData(testParams);
                break;
            case Command
                    .CHECK_PDPARAMS_OVERTIME:
                if (swipeRefreshLayout.isRefreshing() || fetchingData) {
                    Toast.makeText(this, "获取失败", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case Command.SET_SYSTEM_CONFIG_SUCCESS:
                if (setting) {
                    setting = false;
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(this, "设置参数成功", Toast.LENGTH_LONG).show();
                }
                break;
            case Command.CHECK_SETSYSTEM_CONFIG_OVERTIME:
                if (setting) {
                    setting = false;
                    Toast.makeText(this, "设置参数失败", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;


        }
    }

    //私有方法
    private String getIPStr(String[] ips) {
        StringBuilder ipStr = new StringBuilder();
        for (int i = 0; i < ips.length; i++) {
            if (i < ips.length - 1) {
                ipStr.append(ips[i]).append(".");
            } else {
                ipStr.append(ips[i]);
            }
        }
        return ipStr.toString();
    }

    private TestParams getValues() {
        TestParams params = new TestParams();
        String[] ips = ipAddress.getSuperEditTextValue();
        StringBuilder ipStr = new StringBuilder();
        for (int i = 0; i < ips.length; i++) {
            if (i < ips.length - 1) {
                ipStr.append(ips[i]).append(".");
            } else {
                ipStr.append(ips[i]);
            }
        }
        String restart = (String) isRestart.getSelectedItem();
        if (restart.equals("开启")) {
            params.setRestart((byte) 1);
        } else {
            params.setRestart((byte) 0);
        }
        params.setIpAddress(ipStr.toString());
        String portStr = port.getText().toString();
        params.setPort(Short.parseShort(portStr));
        String interval = (String) uploadInterval.getSelectedItem();
        switch (interval) {
            case "60分钟":
                params.setIntervalForUpload((byte) 60);
                break;
            case "10分钟":
                params.setIntervalForUpload((byte) 10);
                break;
            case "30分钟":
                params.setIntervalForUpload((byte) 30);
                break;
        }
        String[] deviceIp = deviceIpAddress.getSuperEditTextValue();
        params.setDeviceIpAddress(getIPStr(deviceIp));
        String[] subnetMaskArr = subnetInternet.getSuperEditTextValue();
        params.setSubnetMask(getIPStr(subnetMaskArr));
        String[] gatewayArr = gateWay.getSuperEditTextValue();
        params.setGateway(getIPStr(gatewayArr));
        String time = timePicker.getText().toString();
        String[] split = time.split(":");
        params.setHour((byte) Integer.parseInt(split[0]));
        params.setMinute((byte) Integer.parseInt(split[1]));
//        String item = (String) typeOfInternet.getSelectedItem();
//        if (item.equals("4G")) {
//
//            params.setTypeOfInternet((byte) 0x00);
//        } else {
//            params.setTypeOfInternet((byte) 0x01);
//        }
        String sync = (String) syncType.getSelectedItem();
        if (sync.equals("外同步")) {
            params.setSyncType((byte) 1);
        } else {
            params.setSyncType((byte) 0);
        }
        params.setSycncFrequency(Float.parseFloat(syncFrequency.getText().toString()));
        params.setComAdress(comAddress.getText().toString());
        return params;
    }

    private void callOutTimePicker(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setPositiveButton("设置", (dialog, which) -> {
            String hourStr;
            String minuteStr;
            if (hourOfDay < 10) {
                hourStr = "0" + hourOfDay;
            } else {
                hourStr = String.valueOf(hourOfDay);
            }
            if (minute < 10) {
                minuteStr = "0" + minute;
            } else {
                minuteStr = String.valueOf(minute);
            }
            timePicker.setText(hourStr + ":" + minuteStr);
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(view.getContext(), R.layout.dialog_date, null);
        final TimePicker datePicker = dialogView.findViewById(R.id.timePicker);
        String currentTime = timePicker.getText().toString();
        if (!currentTime.equals("")) {
            String currentHour = currentTime.split(":")[0];
            String currentMinute = currentTime.split(":")[1];
            datePicker.setHour(Integer.parseInt(currentHour));
            datePicker.setMinute(Integer.parseInt(currentMinute));
        }
        datePicker.setOnTimeChangedListener(this);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);

        dialog.show();
    }

    private boolean checkValue() {
//        String deviceSn = deviceSN.getText().toString();
//        if(deviceSn.equals("")) return false;
//        if(deviceSn.getBytes().length>32)return  false;
//        String[] editTextValue = ipAddress.getSuperEditTextValue();
//        for (String ipValue : editTextValue) {
//            if (ipValue.equals("")) {
//                return false;
//            }
//            if ((Integer.parseInt(ipValue) > 255)) {
//                return false;
//            }
//        }
        String portStr = port.getText().toString();
        if (!portStr.equals("")) {
            int i = Integer.parseInt(portStr);
            if (i > 65535) {
                System.out.println("错误");
                return false;
            }
        }
        String[] deviceIp = deviceIpAddress.getSuperEditTextValue();

        for (String ipValue : deviceIp) {
            if (ipValue.equals("")) {
                return false;
            }
            if ((Integer.parseInt(ipValue) > 255)) {
                return false;
            }
        }
        String[] subnetMask = subnetInternet.getSuperEditTextValue();

        for (String ipValue : subnetMask) {
            if (ipValue.equals("")) {
                return false;
            }
            if ((Integer.parseInt(ipValue) > 255)) {
                return false;
            }
        }
        String[] gateWayArr = gateWay.getSuperEditTextValue();

        for (String ipValue : gateWayArr) {
            if (ipValue.equals("")) {
                return false;
            }
            if ((Integer.parseInt(ipValue) > 255)) {
                return false;
            }
        }
        String time = timePicker.getText().toString();
        if (time.equals("")) {
            return false;
        }
        String s = comAddress.getText().toString();
        if (!s.equals("")) {
            try{
                short i = Short.parseShort(s);
                if (ConversionTool.short2bits(i).length > 2) {
                    return false;
                }
            }catch (Exception e){
                return  false;
            }

        } else {
            return false;
        }

//        String frequency = syncFrequency.getText().toString();
//        if (frequency.equals("")) {
//            return false;
//        }
        return true;
    }

    private void setData(TestParams testParams) {
        String[] ipStr = testParams.getIpAddress().split("[.]");
        if (ipStr.length == 4) {
            ipAddress.setSuperEdittextValue(ipStr);
        }
        port.setText(testParams.getPort() + "");
        if (testParams.getIntervalForUpload() == 10) {
            uploadInterval.setSelection(0);
        } else if (testParams.getIntervalForUpload() == 30) {
            uploadInterval.setSelection(1);
        } else {
            uploadInterval.setSelection(2);
        }
        String[] deviceIp = testParams.getDeviceIpAddress().split("[.]");
        if (deviceIp.length == 4) {
            deviceIpAddress.setSuperEdittextValue(deviceIp);
        }
        String[] subMask = testParams.getSubnetMask().split("[.]");
        if (subMask.length == 4) {
            subnetInternet.setSuperEdittextValue(subMask);
        }
        String[] gateWayArr = testParams.getGateway().split("[.]");
        if (gateWayArr.length == 4) {
            gateWay.setSuperEdittextValue(gateWayArr);
        }
        String hourStr = "";
        String minuteStr = "";
        byte hour = testParams.getHour();
        if (hour < 10) {
            hourStr += "0" + hour;
        } else {
            hourStr += hour;
        }
        byte minute = testParams.getMinute();
        if (minute < 10) {
            minuteStr += "0" + minute;
        } else {
            minuteStr += minute;
        }
        timePicker.setText(hourStr + ":" + minuteStr);
        syncType.setSelection(testParams.getSyncType());
        syncFrequency.setText(testParams.getSycncFrequency() + "");
        isRestart.setSelection(testParams.getRestart());
        comAddress.setText(testParams.getComAdress());


    }
}
