package com.pd.config.pdonlineconfig.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.net.UDPSender;
import com.pd.config.pdonlineconfig.pojo.TestParams;
import com.pd.config.pdonlineconfig.vies.SuperEditText;

public class ControlUnitBasicParamFragment extends Fragment implements TimePicker.OnTimeChangedListener {
    private TextView timePicker;
    private int hourOfDay;
    private volatile boolean fetchingData = false;
    private volatile boolean setting = false;
    private int minute;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SuperEditText ipAddress;
    private EditText port;
    private Spinner uploadInterval;
    private SuperEditText deviceIpAddress;
    private SuperEditText subnetInternet;
    private SuperEditText gateWay;
    private Spinner isRestart;
    private EditText syncFrequency;
    private Spinner syncType;
    private View view;
    private EditText deviceSN;
    private TextView restartTime;
    private Spinner typeOfInternet;
    private ControlUnitBasicParamFragment self;
    private TimePickerDialog mTimePicker;
    private Button settingParamsBtn;
    private NetHandler handler;
    private Button confirmBtn;

    public boolean isFetchingData() {
        return fetchingData;
    }

    public void setFetchingData(boolean fetchingData) {
        this.fetchingData = fetchingData;
    }


    private UDPSender udpSender;

    public void setHandler(NetHandler handler) {
        this.handler = handler;
    }

    public void setUdpSender(UDPSender udpSender) {
        this.udpSender = udpSender;
    }

    public static ControlUnitBasicParamFragment newInstance(NetHandler handler) {
        ControlUnitBasicParamFragment fragment = new ControlUnitBasicParamFragment();
        fragment.setHandler(handler);
        UDPSender udpSender = new UDPSender();

        udpSender.setManager(new InternetManagerImpl());
        udpSender.setNetHandler(handler);
        fragment.setUdpSender(udpSender);
        return fragment;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basic_params_layout, null);
        self = this;
        initComponent(view);
        initListener();
        this.view = view;
        return view;
    }

    private void initListener() {
          swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            fetchingData = true;
            getBasicInfo();
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

    }

    public boolean isSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {

        this.setting = setting;
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
        if (restart.equals("重启")) {
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
        String item = (String) typeOfInternet.getSelectedItem();
        if (item.equals("4G")) {

            params.setTypeOfInternet((byte) 0x00);
        } else {
            params.setTypeOfInternet((byte) 0x01);
        }
        String sync = (String) syncType.getSelectedItem();
        if (sync.equals("外同步")) {
            params.setSyncType((byte) 1);
        } else {
            params.setSyncType((byte) 0);
        }
        params.setSycncFrequency(Float.parseFloat(syncFrequency.getText().toString()));
        return params;
    }

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
//        String frequency = syncFrequency.getText().toString();
//        if (frequency.equals("")) {
//            return false;
//        }
        return true;
    }

    private void initComponent(View view) {
//        deviceSN = view.findViewById(R.id.deviceSN);
        confirmBtn = view.findViewById(R.id.confirmBtn);
        syncFrequency = view.findViewById(R.id.syncFrequency);
        syncType = view.findViewById(R.id.syncType);
        timePicker = view.findViewById(R.id.timePicker);
        ipAddress = view.findViewById(R.id.ipAddress);
        port = view.findViewById(R.id.port);
        uploadInterval = view.findViewById(R.id.uploadInterval);
        deviceIpAddress = view.findViewById(R.id.deviceIpAddress);
        subnetInternet = view.findViewById(R.id.subnetMask);
        gateWay = view.findViewById(R.id.gateWay);
        isRestart = view.findViewById(R.id.isRestart);
        typeOfInternet = view.findViewById(R.id.typeOfInternet);
        timePicker.setOnClickListener(v -> callOutTimePicker(view));
        settingParamsBtn = view.findViewById(R.id.settingBtn);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
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

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public void getBasicInfo() {
        udpSender.setMessager(new ControlUnitMessager(CacheData.ip, CacheData.port)).setCommandType(CommandTypes.GET_BASIC_TESTPARAMS).setNotifyNumber(Command.CHECK_GETBASICPARAMS_OVERTIME).send();
    }

    public void setData(String s) {
        syncFrequency.setText(s);
        fetchingData = false;

    }

    public void cancelRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }


    public boolean sendData() {
        boolean b = checkValue();
        if (b) {
            swipeRefreshLayout.setRefreshing(true);
            setSetting(true);
            TestParams values = getValues();
            udpSender.setMessager(new ControlUnitMessager(CacheData.ip, CacheData.port)).setNotifyNumber(Command.CHECK_SETRUNTIME_OVERTIME).send();
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Toast.makeText(activity, "参数不符合规范，请检查", Toast.LENGTH_LONG).show();
            }

        }
        return false;
    }

    public void setData(TestParams testParams) {
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

    }

    public void setSNCode(String code1) {
        if (deviceSN != null) {
            deviceSN.setText(code1);
        }

    }
}
