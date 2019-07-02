package com.pd.config.pdonlineconfig.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.fizzer.doraemon.passwordinputdialog.PassWordDialog;
import com.fizzer.doraemon.passwordinputdialog.impl.DialogCompleteListener;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.activities.MainActivity;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InfoCreateImpl;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.ControlUnit;
import com.pd.config.pdonlineconfig.utils.ConversionTool;

public class ControlUnitBasicInfo extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView mCodeOfDevice;
    private TextView mSoftwarePatch;
    private TextView mHardWarePatch;
    private TextView mNumberOfFiles;
    private TextView mStorageRemain;
    private TextView mNumberOfUnit;
    private MainActivity activity;
    private Button mDeleteData;
    private Button mDeleteLog;
    private NetHandler handler;
    private int count = 0;
    private ControlUnitBasicInfo controlUnitBasicInfo;
    public boolean deletingLog = false;
    public boolean deletingData = false;
    public boolean fetching = false;
    public boolean setting = false;
  private String currentDeviceCode= "";
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setHandler(NetHandler handler) {

        this.handler = handler;
    }

    public static ControlUnitBasicInfo newInstance(NetHandler netHandler) {
        ControlUnitBasicInfo fragment = new ControlUnitBasicInfo();
        Bundle bundle = new Bundle();
        bundle.putString("init", "init");
        fragment.setHandler(netHandler);
        fragment.setArguments(bundle);
        return fragment;


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_unit_laout, null);
        initComponent(view);
        controlUnitBasicInfo = this;
        setListners();
        return view;
    }


    private void initComponent(View view) {
        mDeleteData = view.findViewById(R.id.clearDataBtn);
        mDeleteLog = view.findViewById(R.id.clearLogBtn);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayoutInfo);
        mCodeOfDevice = view.findViewById(R.id.codeOfDevice);
        mHardWarePatch = view.findViewById(R.id.hardwarePatch);
        mSoftwarePatch = view.findViewById(R.id.softwarePatch);
        mNumberOfFiles = view.findViewById(R.id.numberOfFiles);
        mStorageRemain = view.findViewById(R.id.storageRemain);
        mNumberOfUnit = view.findViewById(R.id.numberOfUnit);
    }

    private void setListners() {
        swipeRefreshLayout.setOnRefreshListener(this::getBasicInfo);
        mDeleteLog.setOnClickListener(v -> clearLog());
        mDeleteData.setOnClickListener(v -> clearData());
        mCodeOfDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count % 5 == 0) {
                    PassWordDialog wordDialog = new PassWordDialog(getActivity()).setTitle("请输入密码").setSubTitle("提示").setMoney("内部参数谨慎修改").setCompleteListener(new DialogCompleteListener() {
                        @Override
                        public void dialogCompleteListener(String money, String pwd) {

                            if (pwd.equals(getResources().getString(R.string.password))) {
                                Activity activity = getActivity();

                                if (activity != null) {
                                    final EditText editText = new EditText(activity);
                                    AlertDialog.Builder inputDialog =
                                            new AlertDialog.Builder(activity);
                                    inputDialog.setTitle("请输入设备编号").setView(editText);
                                    inputDialog.setPositiveButton("确定",
                                            (dialog, which) -> {
                                                String s = editText.getText().toString();
                                                if (s.getBytes().length <= 16 && !s.equals("")) {
                                                    setDeivceInfo(s);
                                                    currentDeviceCode = s;
                                                } else {
                                                    Toast.makeText(getActivity(), "输入内容不合法请重试", Toast.LENGTH_LONG).show();
                                                }
                                            }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();


                                }

                            } else {
                                Toast.makeText(getActivity(), "密码错误请重试", Toast.LENGTH_LONG).show();
                            }
                        }


                    });

                    wordDialog.show();
                }
            }
        });
    }

    private void setDeivceInfo(String code) {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.SET_DEVICE_CODE);
        messager.setManager(new InternetManagerImpl());
        messager.setNetHandler(handler);
        messager.setDeviceCode(code);
        setting = true;
        messager.start();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = 0x11;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getBasicInfo() {
        if (CacheData.ip == null) {
            ControlUnitMessager manager = new ControlUnitMessager(CacheData.port);
            manager.setManager(new InternetManagerImpl());
            manager.setInfoCreator(new InfoCreateImpl());
            manager.setNetHandler(handler);
            new Thread(manager).start();
        } else {
            ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_BASIC_INFO);
            manager.setManager(new InternetManagerImpl());
            manager.setInfoCreator(new InfoCreateImpl());
            manager.setNetHandler(handler);
            manager.start();
            fetching = true;
//        new Thread(manager).start();
        }

        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = 0x06;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void clearLog() {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.DELETE_LOG);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.start();
        deletingLog = true;
        swipeRefreshLayout.setRefreshing(true);
        //检查是否设置成功
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = 0x08;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void clearData() {
        ControlUnitMessager manager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.DELETE_DATA);
        manager.setManager(new InternetManagerImpl());
        manager.setNetHandler(handler);
        manager.start();
        deletingData = true;
        swipeRefreshLayout.setRefreshing(true);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = 0x09;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void cancelSwiping() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setData(ControlUnit basicInfo) {
        mCodeOfDevice.setText(CacheData.currentUnit.getCodeOfDevice() + "");
        byte[] softwaareArr = basicInfo.getSoftwarePatch();
        String softwareStr = String.format("%d.%d.%d", softwaareArr[0], softwaareArr[1], ConversionTool.toshort(new byte[]{softwaareArr[2], softwaareArr[3]}));
        int hardware = basicInfo.getHardwarePathch();
        mHardWarePatch.setText(intToHex(hardware));
        mSoftwarePatch.setText(softwareStr);
        mNumberOfFiles.setText(basicInfo.getNumberOfFiles() + "");
        mStorageRemain.setText(basicInfo.getStorageRemain() + "");
        mNumberOfUnit.setText(CacheData.currentUnit.getNumberOfCells() + "个");
    }

    private String intToHex(int n) {
        //StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        return a;
    }

    public String getCurrentDeviceCode() {

        return currentDeviceCode;
    }

    public void setDeviceCode(String currentDeviceCode) {
        mCodeOfDevice.setText(currentDeviceCode + "");
    }
}
