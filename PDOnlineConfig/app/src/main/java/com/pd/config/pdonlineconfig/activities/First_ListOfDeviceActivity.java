package com.pd.config.pdonlineconfig.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.DevicesAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InfoCreateImpl;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.ControlUnitReceiver;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.DeviceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class First_ListOfDeviceActivity extends AppCompatActivity implements NetListener {
    private ListView listOfDevice;
    private Toolbar toolbar;
    private First_ListOfDeviceActivity currentActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<DeviceInfo> listOfControlUnits;
    private DevicesAdapter adapter;
    private String searchIp;
    private Button refresh;

    private Button chooseBtn;
    private Button backBtn;
    private int clickCount = 0;
    private TextView codeOfDevice;
    private TextView ipAddress;
    private boolean fetching = false;
    private String currentSearchIp = "";
    private NetHandler netHandler;
    private boolean fromTest = false;
    @BindView(R.id.addBtn)
    Button addBtn;

    @OnClick(R.id.addBtn)
    public void onClick(Button button) {
        showInputDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_first);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromTest = extras.getBoolean("fromTest");
        }
        ButterKnife.bind(this);
        currentActivity = this;
        listOfControlUnits = new ArrayList<>();
        initView();
        initInternet();

//        if(CacheData.ip!=null){
//            ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.STOPTEST);
//            messager.setManager(new InternetManagerImpl());
//            messager.start();
//        }else {
//
//        }
        scanListInTheBeginningOfTheTest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        CacheData.receiver.setNetHandler(new NetHandler(this));
        if (CacheData.currentUnit != null) {
            String address = CacheData.currentUnit.getIpAddress();
            for (int i = 0; i < adapter.getListOfControlUnit().size(); i++) {
                if (adapter.getListOfControlUnit().get(i).getIpAddress().equals(address)) {
                    adapter.setCurrentPosition(i);
                    ipAddress.setText(listOfControlUnits.get(i).getIpAddress());
                    codeOfDevice.setText(listOfControlUnits.get(i).getCodeOfDevice());
                    clickCount++;
                }
            }

        }
    }

    private void scanListInTheBeginningOfTheTest() {
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_DEVICE_INFO);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(true);
        messager.start();
    }

    private void initInternet() {
        if (CacheData.receiver == null) {
            getIp();
            ControlUnitReceiver receiver = new ControlUnitReceiver(CacheData.receivePort);
            netHandler = new NetHandler(currentActivity);
            receiver.setNetHandler(netHandler);
            receiver.setNetHandler(netHandler);
            receiver.setManager(new InternetManagerImpl());
            receiver.setInfoCreator(new InfoCreateImpl());
            receiver.start();
            CacheData.receiver = receiver;
        } else {
            CacheData.receiver.setNetHandler(new NetHandler(this));
        }
    }

    private void getIp() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        CacheData.broadCastAddress =(ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."+(ipAddress>>16 & 0xff) +"."+"255";
    }

    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(First_ListOfDeviceActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(First_ListOfDeviceActivity.this);
        inputDialog.setTitle("请输入IP地址").setView(editText);
        inputDialog.setPositiveButton("确定",
                (dialog, which) -> {
                    boolean exist = false;
                    for (DeviceInfo unit : listOfControlUnits) {
                        if (unit.getIpAddress().equals(editText.getText().toString())) {
                            exist = true;
                        }
                    }
                    if (testIP(editText.getText().toString())) {
                        if (exist) {
                            Toast.makeText(First_ListOfDeviceActivity.this,
                                    "ip地址已经存在",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            searchIp = editText.getText().toString();
                            currentSearchIp = searchIp;
                            ControlUnitMessager messager = new ControlUnitMessager(editText.getText().toString(), CacheData.port, CommandTypes.GET_DEVICE_INFO);
                            messager.setManager(new InternetManagerImpl());
                            messager.start();
                            swipeRefreshLayout.setRefreshing(true);
                            fetching = true;
                            new Thread(() -> {
                                try {
                                    Thread.sleep(2000);
                                    Message msg = Message.obtain();
                                    msg.what = 0x02;
//                                    NetHandler handler1 = new NetHandler(currentActivity);
                                    netHandler.sendMessage(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    } else {
                        Toast.makeText(First_ListOfDeviceActivity.this,
                                "ip地址不规范！",
                                Toast.LENGTH_SHORT).show();
                    }

                }).show();
    }

    private boolean testIP(String ip) {
        Pattern pattern = Pattern.compile("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }

    private void initView() {
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> {
            swipeRefreshLayout.setRefreshing(true);
            getListOfDevice();

        });
        backBtn = findViewById(R.id.back);
        codeOfDevice = findViewById(R.id.codeOfDevice);
        ipAddress = findViewById(R.id.ipAddress);
//        addBtn = findViewById(R.id.addBtn);
        chooseBtn = findViewById(R.id.choose);
        listOfDevice = findViewById(R.id.listOfDevices);

        if (CacheData.ip != null) {
            backBtn.setVisibility(View.VISIBLE);
        }
        backBtn.setOnClickListener(v -> {
            if (fromTest) {
                finish();
            } else {
                Intent intent = new Intent(First_ListOfDeviceActivity.this, TestActivity.class);
                intent.putExtra("message", "fromNoWhere");
                startActivity(intent);
            }
        });
//        ArrayList<DeviceInfo> listOfDevices = new ArrayList();
//        DeviceInfo info = new DeviceInfo();
//        info.setCodeOfDevice("123123");
//        info.setIpAddress("192.199.0123.1");
//
        DeviceInfo info1 = new DeviceInfo();
        info1.setCodeOfDevice("A0000001");
        info1.setIpAddress("192.12.013.1");
//        listOfDevices.add(info);
//        listOfDevices.add(info1);
        listOfControlUnits.add(info1);
        adapter = new DevicesAdapter(this, R.layout.device_list_first, listOfControlUnits);
        listOfDevice.setAdapter(adapter);
        listOfDevice.setOnItemClickListener((parent, view, position, id) -> {
            int currentPosition = adapter.getCurrentPosition();
            if (listOfControlUnits.size() != 0) {
                ipAddress.setText(listOfControlUnits.get(position).getIpAddress());
                codeOfDevice.setText(listOfControlUnits.get(position).getCodeOfDevice());
                if (currentPosition != -1) {
                    clickCount++;
                    if (currentPosition == position) {
                        if (clickCount % 2 == 0) {
                            clickCount = 0;
                            CacheData.currentUnit = listOfControlUnits.get(position); //当前的设备
                            Intent intent = new Intent(First_ListOfDeviceActivity.this, MainControllerActivity.class);
//                            intent.putExtra("currentDevice", currentDevice);
                            CacheData.ip = listOfControlUnits.get(position).getIpAddress();
                            startActivity(intent);
                        }
                    } else {
                        clickCount = 1;
                        adapter.setCurrentPosition(position);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    clickCount++;
                    adapter.setCurrentPosition(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        chooseBtn.setOnClickListener(v -> {
            int position = adapter.getCurrentPosition();
            if (position != -1) {
                CacheData.ip=listOfControlUnits.get(position).getIpAddress();
                CacheData.currentUnit = listOfControlUnits.get(position); //当前的设备
                Intent intent = new Intent(First_ListOfDeviceActivity.this, MainControllerActivity.class);
                startActivity(intent);

            }
        });
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getListOfDevice);
    }

    private void getListOfDevice() {
        getIp();
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.port);
        messager.setTypeOfCommand(CommandTypes.GET_DEVICE_INFO);
        messager.setManager(new InternetManagerImpl());
        messager.setBroadCast(true);
        messager.start();
        NetHandler handler1 = new NetHandler(currentActivity);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = 0x04;
                handler1.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void handleMessage(Message msg) {
        int code = msg.what;
        switch (code) {
            case 0x01:
                Bundle data = msg.getData();
                DeviceInfo info = (DeviceInfo) data.getSerializable("deviceBasicInfo");

                if (info != null) {
                    if (info.getIpAddress().equals(currentSearchIp)) {
                        fetching = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if (listOfControlUnits.size() == 0) {
                        listOfControlUnits.add(info);
                    } else {
                        boolean exist = false;
                        for (DeviceInfo oldInfo : listOfControlUnits) {
                            if (info.getIpAddress().equals(oldInfo.getIpAddress())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            listOfControlUnits.add(info);
                        }
                    }

                    if (adapter != null) {
                        adapter.setListOfControlUnit(listOfControlUnits);
                        adapter.notifyDataSetChanged();
                        if (listOfControlUnits.size() != 0) {
                            if (CacheData.currentUnit != null) {
                                String address = CacheData.currentUnit.getIpAddress();
                                for (int i = 0; i < adapter.getListOfControlUnit().size(); i++) {
                                    if (adapter.getListOfControlUnit().get(i).getIpAddress().equals(address)) {
                                        adapter.setCurrentPosition(i);
                                        ipAddress.setText(listOfControlUnits.get(i).getIpAddress());
                                        codeOfDevice.setText(listOfControlUnits.get(i).getCodeOfDevice());
                                    }
                                }
                            } else {
                                adapter.setCurrentPosition(0);
                            }
                            clickCount++;
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;

            case 0x02:
                if (fetching) {
                    swipeRefreshLayout.setRefreshing(false);
                    fetching = false;
                    Toast.makeText(getApplicationContext(), "未检测到此设备", Toast.LENGTH_LONG).show();
                }

                break;

            case 0x03:
                boolean success = false;
                for (DeviceInfo unit1 : listOfControlUnits) {
                    if (unit1.getIpAddress().equals(searchIp)) {
                        success = true;
                    }
                }
                if (!success) {
                    Toast.makeText(getApplicationContext(), "未检测到此设备", Toast.LENGTH_LONG).show();
                }
                fetching = false;
                swipeRefreshLayout.setRefreshing(false);
                break;
            case 0x04:
                swipeRefreshLayout.setRefreshing(false);
                break;


        }
    }
}
