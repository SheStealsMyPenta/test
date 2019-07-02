package com.pd.config.pdonlineconfig.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fizzer.doraemon.passwordinputdialog.PassWordDialog;
import com.fizzer.doraemon.passwordinputdialog.impl.DialogCompleteListener;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.FragmentAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.fragment.configFrag.UHFConfigFragment;
import com.pd.config.pdonlineconfig.fragment.testFrag.UHFTestFragment;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.PdParamsUHF;
import com.pd.config.pdonlineconfig.utils.ConversionTool;
import com.pd.config.pdonlineconfig.vies.LoadingDialog;
import com.pd.config.pdonlineconfig.vies.NoScrollViewPage;

import java.util.ArrayList;
import java.util.List;

import static com.pd.config.pdonlineconfig.constants.CacheData.currentPage;

public class CubicleTestHFActivity extends AppCompatActivity implements NetListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private List<String> mTitles = new ArrayList<>();
    private FragmentAdapter adapterForFragment;
    @BindView(R.id.viewPager)
    NoScrollViewPage mViewPager;
    @BindView(R.id.currentType)
    Button currentType;
    private Activity currentActivity;
    @BindView(R.id.btn_1)
    Button btn_1;
    @BindView(R.id.btn_4)
    Button btn_4;
    @BindView(R.id.codeOfDevice)
    TextView codeOfDevice;
    @BindView(R.id.ipAddress)
    TextView ipAddress;
    @BindView(R.id.backToListLayout)
    LinearLayout backToListLaout;
    private InternetService service;
    private NetHandler netHandler;
    private String type;
    private UHFTestFragment testFragment;
    private UHFConfigFragment  uhfConfig;
    private boolean isFetchingData = false;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cubicle_test_activity);
        ButterKnife.bind(this);
        currentActivity = this;
        initHeader();
        initInternet();
        initFragment();
        initListener();

    }

    private void initHeader() {
        codeOfDevice.setText(CacheData.currentMonitorDevice.getCodeOfDevice());
        ipAddress.setText(CacheData.currentMonitorDevice.getPhysicAddress());
        currentType.setText("高频电流");
    }

    private PopupMenu.OnMenuItemClickListener onMenuItemClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
//                case R.id.action_config:
////                    if(CacheData.isValidate){
//                    Intent intent = new Intent(TestActivity.this, MainActivity.class);
//                    startActivity(intent);
////                    }else {
////                           showDialogToConfigActivity();
////                    }
//                    break;

                case R.id.uhf:
                    Intent intent = new Intent();
                    intent.setClass(CubicleTestHFActivity.this, CubicleTestActivity.class);
                    startActivity(intent);
                    break;
                case R.id.humAndTemp:

                    break;
                case R.id.ae:
                    Intent intentAe = new Intent();
                    intentAe.setClass(CubicleTestHFActivity.this, CubicleTestAEActicity.class);
                    startActivity(intentAe);
                    break;
                case R.id.tev:
                    Intent intentTev = new Intent();
                    intentTev.setClass(CubicleTestHFActivity.this, CubicleTestTEVActivity.class);
                    startActivity(intentTev);
                    break;

            }
            return true;
        }
    };

    private void initListener() {
        backToListLaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CubicleTestHFActivity.this, MonitorDeviceManageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("fromTest", true);
                startActivity(intent);
            }
        });
        currentType.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(currentActivity, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_cubicle_pd, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(onMenuItemClick);
            popupMenu.show();
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_4.getText().toString().equals("配置")) {
                    new PassWordDialog(currentActivity).setTitle("请输入密码").setSubTitle("提示").setMoney("内部参数谨慎修改").setCompleteListener(new DialogCompleteListener() {
                        @Override
                        public void dialogCompleteListener(String money, String pwd) {
                            if (pwd.equals("123456")) {
                                CacheData.isValidate = true;
                                currentPage = "config";
                                mViewPager.setCurrentItem(1);
                                mViewPager.setCurrentItem(1);
                                btn_4.setText("保存");
                            } else {
                                Toast.makeText(currentActivity, "密码错误请重试", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).show();

                } else {
                    uhfConfig.sendData();
                }

            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() == 1) {

                    mViewPager.setCurrentItem(0);
                }
                service.getDataByType(netHandler, "高频电流");
                loadingDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        backToListLaout.callOnClick();
    }

    private void initFragment() {
        List<Fragment> mFragments = new ArrayList<>();
        testFragment = UHFTestFragment.newInstance("放电峰值", "放电次数", "放电能量");
        uhfConfig = UHFConfigFragment.newInstance(netHandler,"开关柜","Hf");
        mFragments.add(testFragment);
        mFragments.add(uhfConfig);
        mTitles.add("超声");
        mTitles.add("超声配置");
        adapterForFragment = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapterForFragment);
        tabLayout.setupWithViewPager(mViewPager);
        if(currentPage.equals("test")){
            mViewPager.setCurrentItem(0);
        }else {
            mViewPager.setCurrentItem(1);
        }
    }

    private void initInternet() {
        netHandler = new NetHandler(this);
        CacheData.receiver.setNetHandler(netHandler);
        service = new InternetService();
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Command.GET_TEST_DATA:
                //柜内调试会显示一个数组
                byte data[] = (byte[]) msg.getData().get("data");
                byte[] lengthOfData = new byte[4];
                System.arraycopy(data, 12, lengthOfData, 0, lengthOfData.length);
                int length = ConversionTool.toint(lengthOfData);
                if (length == 13) {
                    //不包含prps,arr是数据类型
                    byte[] arr = {data[5], data[6]};
                    //给fragment设置
                }
                break;
            case Command.CHECK_PDPARAMS_OVERTIME:
//                判断获取uhf是否超时
                if (uhfConfig.fetching) {
                    //超时
                    uhfConfig.fetching = false;
                    uhfConfig.cancelSwipeLoading();
                    Toast.makeText(this, "获取高频电流参数超时", Toast.LENGTH_LONG).show();
                }
                break;
            case Command.CONFIG_FETCH_SUCCESS:
                if (mViewPager.getCurrentItem() == 1) {
                    Toast.makeText(this, "获取高频电流参数成功", Toast.LENGTH_LONG).show();
                }
                Bundle bundle = msg.getData();
                PdParamsUHF uhfPdParams = (PdParamsUHF) bundle.get("UHFParams");
                if (uhfPdParams != null) {
                    uhfConfig.setData(uhfPdParams);
                }
                CacheData.currentUHFParams = uhfPdParams;
                uhfConfig.fetching = false;
                uhfConfig.cancelSwipeLoading();
                CacheData.currentUHFParams = uhfPdParams;
                break;
            case Command.SET_RUNTIME_CONFIG_SUCCUSS:
                //检查UHF配置是否成功
                uhfConfig.setting = false;
                uhfConfig.cancelSwipeLoading();
//                    CacheData.currentUHFParams = uhfFragment.getData();
                Toast.makeText(this, "设置高频电流参数成功", Toast.LENGTH_LONG).show();
                break;
            case Command.CHECK_SETUHFPARAMS_OVERTIME:
                //设置运行时配置成功
                if (uhfConfig.setting) {
                    //超时
                    if (mViewPager.getCurrentItem() == 1) {
                        Toast.makeText(this, "设置高频电流参数超时", Toast.LENGTH_LONG).show();
                    }
                    uhfConfig.cancelSwipeLoading();
                    uhfConfig.setting = false;
                }
                break;
            case Command.CHECK_GET_CUBICLE_DATA_OVERTIME:
                if (isFetchingData) {
                    isFetchingData = false;
                    loadingDialog.close();
                    Toast.makeText(this, "获取失败", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}
