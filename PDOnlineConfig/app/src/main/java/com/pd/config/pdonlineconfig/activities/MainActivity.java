package com.pd.config.pdonlineconfig.activities;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.FragmentAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.fragment.ControlUnitBasicInfo;
import com.pd.config.pdonlineconfig.fragment.ControlUnitBasicParamFragment;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.ControlUnitReceiver;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.ControlUnit;
import com.pd.config.pdonlineconfig.pojo.TestParams;
import com.pd.config.pdonlineconfig.vies.NoScrollViewPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NetListener {
    ControlUnitReceiver receiver;
    TabLayout mTabLayout;
    Toolbar toolbar;
    NoScrollViewPage mViewPager;
    private String[] titles = new String[]{"详细信息", "基本参数", "内部参数", "PD采集"};
    private FragmentAdapter adapter;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private Button saveBtn;
    private Button logBtn;
    private Button dataBtn;
    private Button customBtn;
    private NetHandler handler;
    private ControlUnitBasicParamFragment runtimeConfig;
    private Button runTimeConfigBtn;
    private Button infoBtn;
    private ControlUnitBasicInfo  basicInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        initInternet();
        //初始化fragment
        initComponent();
        //对button添加事件
        initBtnClickListner();
        getBasicParamsInTheBeginning();
    }


    private void getBasicParamsInTheBeginning() {
      runtimeConfig.getBasicInfo();
      basicInfo.getBasicInfo();
    }

    private void initBtnClickListner() {
        saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(v -> {
            int item = mViewPager.getCurrentItem();

            //运行配置
            if (item == 1) {
                if (!runtimeConfig.sendData()) {
                    Toast.makeText(this, "格式不正确！", Toast.LENGTH_LONG).show();
                }
            }
        });
        logBtn = findViewById(R.id.log);
        logBtn.setOnClickListener(v -> {

        });
    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.home:
                    if (mViewPager.getCurrentItem() == 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        finish();
                    }
                    break;
            }
            return true;
        }
    };

    //初始化网络
    private void initInternet() {

        ControlUnitReceiver receiver = CacheData.receiver;
        handler = new NetHandler(this);
        receiver.setNetHandler(handler);
//        receiver.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        runTimeConfigBtn = findViewById(R.id.runTimeConfig);
        infoBtn = findViewById(R.id.info);
        runTimeConfigBtn.setOnClickListener(v -> mViewPager.setCurrentItem(1));
        infoBtn.setOnClickListener(v -> mViewPager.setCurrentItem(0));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTitles.add(titles[i]);
        }
        mFragments = new ArrayList<>();
//        ControlUnitBasicInfo info = ControlUnitBasicInfo.newInstance();
//        info.setActivity(this);
//        mFragments.add(info);
        basicInfo = ControlUnitBasicInfo.newInstance(new NetHandler(this));
        mFragments.add(basicInfo);
        runtimeConfig = ControlUnitBasicParamFragment.newInstance(handler);
        mFragments.add(runtimeConfig);
//        mFragments.add(ControUnitPDParamsFragment.newInstance());
        adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void handleMessage(Message msg) {
        int code = msg.what;
        switch (code) {
            case 0x01:
                //获取基本参数失败
                boolean fetchingData = runtimeConfig.isFetchingData();
                if (fetchingData) {
                    runtimeConfig.cancelRefresh();
                    Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
                }
//                runtimeConfig.setData("50");
                break;
            case 0x02:
//                设置运行参数成功
                Toast.makeText(this, "配置运行参数成功", Toast.LENGTH_SHORT).show();
                runtimeConfig.setSetting(false);
                runtimeConfig.cancelRefresh();
                break;
            case 0x03:
                boolean setting = runtimeConfig.isSetting();
                if (setting) {
                    //设置参数失败
                    Toast.makeText(this, "设置参数失败", Toast.LENGTH_SHORT).show();
                    runtimeConfig.cancelRefresh();
                    runtimeConfig.setSetting(false);
                }
                //判断是否设置运行参数成功
            case 0x04:
                Bundle data = msg.getData();
                TestParams testParams = (TestParams) data.getSerializable("basicTestParams");
                if(testParams!=null){
                    runtimeConfig.setData(testParams);
                    runtimeConfig.setFetchingData(false);
                    runtimeConfig.cancelRefresh();
                    Toast.makeText(this, "获取设备参数成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case 0x05:
                Bundle basicInfoPack = msg.getData();
                ControlUnit info= (ControlUnit) basicInfoPack.getSerializable("deviceBasicInfo");
                basicInfo.cancelSwiping();
                basicInfo.fetching=false;
                basicInfo.setData(info);
                Toast.makeText(this, "获取基本参数成功", Toast.LENGTH_SHORT).show();
                break;
            case 0x06:
                if(basicInfo.fetching){
                    Toast.makeText(this, "获取基本参数失败", Toast.LENGTH_SHORT).show();
                    basicInfo.cancelSwiping();
                    basicInfo.fetching=false;
                }
                break;
            case 0x0B:
                //清楚数据成功
                if(basicInfo.deletingData){
                    Toast.makeText(this, "清除数据成功", Toast.LENGTH_SHORT).show();
                    basicInfo.deletingData=false;
                    basicInfo.cancelSwiping();
                }
                break;
            case 0x0C:
                //清除日志成功
                if(basicInfo.deletingLog){
                    Toast.makeText(this, "清除日志成功", Toast.LENGTH_SHORT).show();
                    basicInfo.deletingLog=false;
                    basicInfo.cancelSwiping();
                }
                break;
            case 0x08:
                //判断清除日志是否成功
                if(basicInfo.deletingLog){
                    Toast.makeText(this, "清除日志失败，请重试！", Toast.LENGTH_SHORT).show();
                    basicInfo.cancelSwiping();
                    basicInfo.deletingLog=false;
                }
                break;
            case 0x09:
                if(basicInfo.deletingData){
                    Toast.makeText(this, "清除数据失败，请重试！", Toast.LENGTH_SHORT).show();
                    basicInfo.cancelSwiping();
                    basicInfo.deletingData=false;
                }
                //判断清除数据是否成功
                break;
            case 0x10:
                //设置设备编码成功
                boolean success = msg.getData().getBoolean("success");
                basicInfo.setting =false;
                if(success){
                    Toast.makeText(this, "设置设备编码成功", Toast.LENGTH_SHORT).show();
                    CacheData.currentUnit.setCodeOfDevice(basicInfo.getCurrentDeviceCode());
                    basicInfo.setDeviceCode(basicInfo.getCurrentDeviceCode());
                    basicInfo.getBasicInfo();
                }else {
                    Toast.makeText(this, "设置设备编码失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case 0x11:
                //检查是否设备编码设置成功
                if(basicInfo.setting){
                    Toast.makeText(this, "设置设备编码超时", Toast.LENGTH_SHORT).show();
                }
                break;
            case 0x0F:
                 if(runtimeConfig!=null){
                     String code1 = msg.getData().getString("code");
//                     runtimeConfig.setSNCode(code1);
                 }
                break;


        }
    }
}
