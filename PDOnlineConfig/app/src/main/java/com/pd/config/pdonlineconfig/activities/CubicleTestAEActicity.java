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
import com.pd.config.pdonlineconfig.fragment.configFrag.AAConfigFragment;
import com.pd.config.pdonlineconfig.fragment.testFrag.UHFTestFragment;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.PdParamsAA;
import com.pd.config.pdonlineconfig.utils.ConversionTool;
import com.pd.config.pdonlineconfig.vies.LoadingDialog;
import com.pd.config.pdonlineconfig.vies.NoScrollViewPage;

import java.util.ArrayList;
import java.util.List;

import static com.pd.config.pdonlineconfig.constants.CacheData.currentPage;

public class CubicleTestAEActicity extends AppCompatActivity implements NetListener {

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
    private AAConfigFragment aaConfig;
    private LoadingDialog loadingDialog;
    private boolean isFetchingData=false;
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
        currentType.setText("空气式超声");
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
                    intent.setClass(CubicleTestAEActicity.this, CubicleTestActivity.class);
                    startActivity(intent);
                    break;
                case R.id.humAndTemp:

                    break;
                case R.id.ae:

                    break;
                case R.id.hf:
                    Intent intentHf = new Intent();
                    intentHf.setClass(CubicleTestAEActicity.this, CubicleTestHFActivity.class);
                    startActivity(intentHf);
                    break;
                case R.id.tev:
                    Intent intentTev = new Intent();
                    intentTev.setClass(CubicleTestAEActicity.this, CubicleTestTEVActivity.class);
                    startActivity(intentTev);
                    break;

            }
            return true;
        }
    };

    private void initListener() {
        currentType.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(currentActivity, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_cubicle_pd, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(onMenuItemClick);
            popupMenu.show();
        });
        backToListLaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CubicleTestAEActicity.this, MonitorDeviceManageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("fromTest", true);
                startActivity(intent);
            }
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
                    CacheData.typeOfSend = "ae";
                    boolean success = aaConfig.sendData();
                    if (!success) {
                        Toast.makeText(currentActivity, "输入不合法！", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() == 1) {
                    mViewPager.setCurrentItem(0);
                    btn_4.setText("配置");
                }
                loadingDialog = new LoadingDialog(currentActivity, "数据获取中...");
                loadingDialog.show();
                service.getDataByType(netHandler, "超声");
                isFetchingData=true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        backToListLaout.callOnClick();
    }

    private void initFragment() {
        List<Fragment> mFragments = new ArrayList<>();
        testFragment = UHFTestFragment.newInstance("Vpp", "放电次数", "放电能量");
        aaConfig = AAConfigFragment.newInstance(netHandler, "开关柜", "ae");
        mFragments.add(testFragment);
        mFragments.add(aaConfig);
        mTitles.add("超声");
        mTitles.add("超声配置");
        adapterForFragment = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapterForFragment);
        tabLayout.setupWithViewPager(mViewPager);
        if (currentPage.equals("test")) {
            mViewPager.setCurrentItem(0);
        } else {
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
            //获取参数成功
            case 0x01:
                Toast.makeText(this, "获取AA参数成功", Toast.LENGTH_LONG).show();
                Bundle bundle = msg.getData();
                PdParamsAA aaPdParams = (PdParamsAA) bundle.get("aaParams");
                if (aaPdParams != null) {
                    aaConfig.setData(aaPdParams);
                }
                aaConfig.fetching = false;
                aaConfig.cancelSwipeLoading();
                break;
            case 0x06:
                if (aaConfig.fetching) {
                    //超时
                    aaConfig.fetching = false;
                    aaConfig.cancelSwipeLoading();
                    Toast.makeText(this, "获取UHf参数超时", Toast.LENGTH_LONG).show();
                    break;
                }
            case Command.SET_RUNTIME_CONFIG_SUCCUSS:
                //设置超声成功
                Toast.makeText(this, "设置超声参数成功", Toast.LENGTH_LONG).show();
                aaConfig.setting = false;
                aaConfig.cancelSwipeLoading();
                break;
            case Command.CHECK_SETUHFPARAMS_OVERTIME:
                if (aaConfig != null) {
                    if (aaConfig.setting) {
                        aaConfig.setting = false;
                        aaConfig.cancelSwipeLoading();
                        Toast.makeText(this, "设置空气式超声参数失败", Toast.LENGTH_LONG).show();
                    }
                }
                break;
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
