package com.pd.config.pdonlineconfig.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.fizzer.doraemon.passwordinputdialog.PassWordDialog;
import com.fizzer.doraemon.passwordinputdialog.impl.DialogCompleteListener;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.adapters.FragmentAdapter;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.fragment.configFrag.AAConfigFragment;
import com.pd.config.pdonlineconfig.fragment.configFrag.InfraredConfigFragment;
import com.pd.config.pdonlineconfig.fragment.configFrag.LightConfigFragment;
import com.pd.config.pdonlineconfig.fragment.configFrag.UHFConfigFragment;
import com.pd.config.pdonlineconfig.fragment.testFrag.InfraredTestFragment;
import com.pd.config.pdonlineconfig.impls.InfoCreateImpl;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.InfoCreator;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.ControlUnitReceiver;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.*;
import com.pd.config.pdonlineconfig.vies.LoadingDialog;
import com.pd.config.pdonlineconfig.vies.NoScrollViewPage;
import com.pdonlineport.PDfragment.PD_AA_Fragment;
import com.pdonlineport.PDfragment.PD_UHF_Fragment;
import com.pdonlineport.PDfragment.PD_Vedio_Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.pd.config.pdonlineconfig.constants.CacheData.*;

public class TestActivity extends AppCompatActivity implements NetListener {
    ControlUnitReceiver receiver;
    TabLayout mTabLayout;
    private String[] titles = new String[]{"AA", "UHF", "红外", "视屏流"};
    private Button startTestBtn;
    private FragmentAdapter adapter;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private Toolbar toolBar;
    private TestActivity currentActivity;
    private TextView title;
    private TextView ipAddress;
    private TextView codeOfDevice;
    private Button currentTypeView;
    private LinearLayout container;
    private LightConfigFragment lightConfigFragment;
    private InfraredConfigFragment infraredConfigFragment;
    private PD_AA_Fragment pdAaFragment;
    private PD_UHF_Fragment pdUhfFragment;
    PD_Vedio_Fragment videoFragment;
    NoScrollViewPage mViewPager;
    private DeviceInfo currentUnit;
    private String currentType;
    private String currentFragment;
    private Button chartBtn;
    private Button custom;
    private Button configBtn;
    private UHFConfigFragment uhfFragment;
    private AAConfigFragment aaFragment;
    private LinearLayout optionLayout;
    private InfraredTestFragment fragmentInfrad;
    private LoadingDialog loadingDialog;
    private long exitTime = 0;
    private boolean playing;
    private InternetService service;
    private NetHandler handler;
    private InfoCreator infoCreator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_layout);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        currentActivity = this;
        initInternet();
        //初始化fragment
        init();
        initComponent();
        initToolBar();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


//        View view= findViewById(R.id.viewPager);
//        view.destroyDrawingCache();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        View theView = uhfFragment.getTheView();
//        View view1 = uhfFragment.getView();
//        Bitmap bitmap = viewConversionBitmap(theView);
//
//
//        File file = new File(Environment.getExternalStorageDirectory().toURI());

//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    Bitmap resizeBitmap(int desHeight, int desWidth, Bitmap originBitmap) {

        int height = originBitmap.getHeight();
        int width = originBitmap.getWidth();
        Log.d("bitmap", "height = " + originBitmap.getHeight() + " width = "
                + originBitmap.getWidth());

        float scale_y = desHeight / height;
        float scale_x = desWidth / width;

        Matrix matrix = new Matrix();
        matrix.setScale(scale_x, scale_y);
        Bitmap desBitmap = Bitmap.createBitmap(originBitmap, 0, 0, width,//此处出现异常
                height, matrix, true);
        Log.d("bitmap", "height = " + desBitmap.getHeight() + " width = "
                + desBitmap.getWidth());
        return desBitmap;
    }

    public Bitmap viewConversionBitmap(View v) {

        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明  */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    private void init() {
        currentTypeView = findViewById(R.id.currentType);
        currentTypeView.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(currentActivity, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(onMenuItemClick);
            popupMenu.show();
        });
        codeOfDevice = findViewById(R.id.codeOfDevice);
        ipAddress = findViewById(R.id.ipAddress);
        Bundle extras = getIntent().getExtras();
        String activity = (String) extras.get("activity");
        currentUnit = currentMonitorDevice;
        if (activity != null) {
            if (activity.equals("fromList")) {

                currentType = CacheData.currentType;
                currentTypeView.setText(currentType);
//                ip = currentUnit.getIpAddress();
                codeOfDevice.setText(CacheData.currentMonitorDevice.getCodeOfDevice());
                ipAddress.setText(CacheData.currentMonitorDevice.getPhysicAddress());
            }
        } else {
            codeOfDevice.setText(CacheData.currentMonitorDevice.getCodeOfDevice());
            ipAddress.setText(CacheData.currentMonitorDevice.getPhysicAddress());
            currentType = CacheData.currentType;
            currentTypeView.setText(currentType);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolBar.getMenu().setGroupVisible(0, false);
        toolBar.getMenu().setGroupEnabled(0, true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        List<String> cells = currentUnit.getTypesOfCells();
        if (cells != null) {
            if (!cells.contains("AA")) {
                menu.findItem(R.id.AA).setVisible(false);
            }
            if (!cells.contains("UHF")) {
                menu.findItem(R.id.UHF).setVisible(false);
            }
//            if (!cells.contains("video")) {
//                menu.findItem(R.id.light).setVisible(false);
//            }
            if (!cells.contains("infrared")) {
                menu.findItem(R.id.infrared).setVisible(false);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!startTestBtn.getText().toString().equals("停止")) {
//            if (mViewPager.getCurrentItem() == 0) {
//                exit();
////                Intent intent = new Intent(TestActivity.this, First_ListOfDeviceActivity.class);
////                startActivity(intent);
////                Intent intent = new Intent();
////// 为Intent设置Action、Category属性
////                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
////                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
////                startActivity(intent);
//            } else {
//                mViewPager.setCurrentItem(0);
//                custom.setText("配置");
//
//                CacheData.currentPage = "test";
//            }
            Intent intent = new Intent(TestActivity.this, MonitorDeviceManageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("fromTest", true);
            startActivity(intent);
        } else {
            Toast.makeText(this, "正在采集，请先停止采集", Toast.LENGTH_LONG).show();
        }


    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent();
// 为Intent设置Action、Category属性
            intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
            intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CacheData.receiver.setNetHandler(new NetHandler(this));
    }

    //初始化网络
    private void initInternet() {
//        receiver = new ControlUnitReceiver(CacheData.ip, CacheData.receivePort);
//        receiver.setInfoCreator(new InfoCreateImpl());
//        receiver.setManager(new InternetManagerImpl());
//        Fragment curretFrag = adapter.getItem(mViewPager.getCurrentItem());
//        NetHandler netHandler = new NetHandler(this);
//        receiver.setNetHandler(netHandler);
        infoCreator = new InfoCreateImpl();
        handler = new NetHandler(this);
        receiver = CacheData.receiver;
        receiver.setNetHandler(handler);
        service = new InternetService();
//        receiver.start();

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

                case R.id.UHF:
                    if (!CacheData.currentType.equals(CacheData.UHF)) {
                        Intent uhfIntent = new Intent(TestActivity.this, TestActivity.class);
                        uhfIntent.putExtra("currentDevice", currentUnit);
                        uhfIntent.putExtra("currentType", CacheData.UHF);
                        CacheData.currentType = CacheData.UHF;
                        startActivity(uhfIntent);
                    } else {
//                        Toast.makeText(currentActivity, "当前已经是特高频界面，请勿重复切换", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.AA:
                    if (!CacheData.currentType.equals(CacheData.AA)) {
                        Intent aaIntent = new Intent(TestActivity.this, TestActivity.class);
                        aaIntent.putExtra("currentDevice", currentUnit);
                        aaIntent.putExtra("currentType", CacheData.AA);
                        CacheData.currentType = CacheData.AA;
                        startActivity(aaIntent);
                    } else {
//                        Toast.makeText(currentActivity, "当前已经是空气式超声界面，请勿重复切换", Toast.LENGTH_LONG).show();
                    }

                    break;
//                case R.id.light:
//                    if (!CacheData.currentType.equals(CacheData.Light)) {
//                        Intent lightIntent = new Intent(TestActivity.this, TestActivity.class);
//                        lightIntent.putExtra("currentDevice", currentUnit);
//                        lightIntent.putExtra("currentType", CacheData.Light);
//                        CacheData.currentType = CacheData.Light;
//                        startActivity(lightIntent);
//                    } else {
////                        Toast.makeText(currentActivity, "当前已经是视频图像界面，请勿重复切换", Toast.LENGTH_LONG).show();
//                    }
//
//                    break;
                case R.id.infrared:
                    if (!CacheData.currentType.equals(CacheData.infrared)) {
                        Intent infrared = new Intent(TestActivity.this, TestActivity.class);
                        infrared.putExtra("currentDevice", currentUnit);
                        infrared.putExtra("currentType", CacheData.infrared);
                        CacheData.currentType = CacheData.infrared;
                        startActivity(infrared);
                    } else {
//                        Toast.makeText(currentActivity, "当前已经是红外热像界面，请勿重复切换", Toast.LENGTH_LONG).show();
                    }

                    break;

            }
            return true;
        }
    };

    private void initToolBar() {


//        if (currentType.equals("UHF")) {
//            title.setText("特高频");
//        }else if(currentType.equals("AA")){
//            title.setText("超声波");
//        }
        List<String> listOfTypes = currentUnit.getTypesOfCells();
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar bar = getSupportActionBar();
//        bar.setDisplayHomeAsUpEnabled(true);
//        toolBar.setOnMenuItemClickListener(onMenuItemClick);
        toolBar.setTitle(CacheData.UHF);

//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() == 1) {
                    mViewPager.setCurrentItem(0);
                } else {
                    Intent intent = new Intent(TestActivity.this, First_ListOfDeviceActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private Bitmap viewConvertToBitMap(View v) {
        int vW = v.getWidth();
        int vH = v.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(vW, vH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        v.layout(0, 0, vW, vH);
        v.draw(canvas);
        return bitmap;
    }
    //初始化网络


    private void initComponent() {
        container = findViewById(R.id.container);
        optionLayout = findViewById(R.id.backToListLayout);
        optionLayout.setOnClickListener(v -> {
            Intent intent = new Intent(TestActivity.this, MonitorDeviceManageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("fromTest", true);
            startActivity(intent);
//            finish();
        });
        custom = findViewById(R.id.custom);
        custom.setOnClickListener(v -> {
            if (custom.getText().equals("保存")) {
                //保存配置.
                if (CacheData.currentType != null) {
                    switch (CacheData.currentType) {
                        case CacheData.UHF:
                            if (uhfFragment != null) {
                                boolean success = uhfFragment.sendData();
                                if (!success) {
                                    Toast.makeText(currentActivity, "输入不合法！", Toast.LENGTH_LONG).show();
                                }
                            }
                            break;
                        case CacheData.AA:
                            if (aaFragment != null) {
                                typeOfSend = "ae";
                                boolean success = aaFragment.sendData();
                                if (!success) {
                                    Toast.makeText(currentActivity, "输入不合法！", Toast.LENGTH_LONG).show();
                                }
                            }
                            break;
                        case CacheData.Light:
                            if (lightConfigFragment != null) {
                                lightConfigFragment.sendData();
                            }
                            break;
                        case CacheData.infrared:
                            if (infraredConfigFragment != null) {
                                boolean b = infraredConfigFragment.sendData();
                                if (!b) {
                                    Toast.makeText(currentActivity, "输入不合法！", Toast.LENGTH_LONG).show();
                                }
                            }
                            break;


                    }
                }

            } else if (custom.getText().equals("配置")) {
                if (CacheData.isValidate) {
                    currentPage = "config";
                    mViewPager.setCurrentItem(1);
                    disableBtn(chartBtn);
                    disableBtn(configBtn);
                    custom.setText("保存");
                } else {
                    new PassWordDialog(currentActivity).setTitle("请输入密码").setSubTitle("提示").setMoney("内部参数谨慎修改").setCompleteListener(new DialogCompleteListener() {
                        @Override
                        public void dialogCompleteListener(String money, String pwd) {
                            if (pwd.equals("123456")) {
                                CacheData.isValidate = true;
                                currentPage = "config";
                                mViewPager.setCurrentItem(1);
                                disableBtn(chartBtn);
                                disableBtn(configBtn);
                                custom.setText("保存");

                            } else {
                                Toast.makeText(currentActivity, "密码错误请重试", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).show();
                }

            }
        });

        chartBtn = findViewById(R.id.chartBtn);
        chartBtn.setOnClickListener(v -> {
            if (chartBtn.getText().equals("谱图")) {
                //如果btn的text为谱图则切换谱图
                if (CacheData.currentType.equals(CacheData.AA)) {
                    pdAaFragment.ChangeView();
                }
            } else {
                //拍照
                if (CacheData.currentType.equals(CacheData.Light)) {
                    takeAPhoto("light");
                } else {
                    takeAPhoto("infrared");
                }


            }
        });
        title = findViewById(R.id.title);
        configBtn = findViewById(R.id.config);
        configBtn.setOnClickListener(v -> {
            //點擊配置
            if (!configBtn.getText().equals("")) {
                currentPage = "config";
                mViewPager.setCurrentItem(1);
                startTestBtn.setEnabled(false);
                custom.setVisibility(View.VISIBLE);
                custom.setEnabled(true);
                custom.setText("保存");
            }

        });
        startTestBtn = findViewById(R.id.start);
        startTestBtn.setOnClickListener(v -> {


            if (startTestBtn.getText().equals("启动")) {
                boolean validate = true;
                if (!currentPage.equals("test")) {
                    //在配置界面
                    //验证当前缓存参数是否发生改变。如果发生改变并且没有保存则提示用户需要提前保存再进行配置。
                    validate = false;
                    if (CacheData.currentType.equals(CacheData.UHF)) {
                        validate = validateUHF();
                    } else {
                        validate = true;
                    }
                }
                if (validate) {
                    StartTestFunc();
                    mViewPager.setCurrentItem(0);
                    currentPage = "test";
                }
            } else {
                if (CacheData.currentType.equals(CacheData.Light)) {
                    videoFragment.StopVedio();
                }
                ControlUnitMessager messager = new ControlUnitMessager(ip, port, CommandTypes.STOPTEST);
                messager.setManager(new InternetManagerImpl());
                messager.start();
//                toolBar.setOnMenuItemClickListener(onMenuItemClick);
//                toolBar.getMenu().setGroupVisible(0, true);
                startTestBtn.setText("启动");
                optionLayout.setEnabled(true);
                currentTypeView.setEnabled(true);
                custom.setText("配置");
                custom.setEnabled(true);
                if (CacheData.currentType.equals(CacheData.AA)) {
                    chartBtn.setText("谱图");
                    chartBtn.setEnabled(true);
                }
            }

        });

        mTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTitles.add(titles[i]);
        }
        mFragments = new ArrayList<>();

        //初始化
        if (currentType.equals(CacheData.UHF)) {
            disableBtn(chartBtn);
            disableBtn(configBtn);
            pdUhfFragment = new PD_UHF_Fragment();
            mFragments.add(pdUhfFragment);
            uhfFragment = UHFConfigFragment.newInstance(new NetHandler(this), "环境", "uhf");
            mFragments.add(uhfFragment);
            if (!currentPage.equals("test")) {
                custom.setText("保存");
            }

        } else if (currentType.equals(CacheData.AA)) {
            pdAaFragment = new PD_AA_Fragment();
            mFragments.add(pdAaFragment);
            aaFragment = AAConfigFragment.newInstance(new NetHandler(this), "环境", "ae");
            mFragments.add(aaFragment);
            if (currentPage.equals("test")) {
                chartBtn.setEnabled(true);
                chartBtn.setText("谱图");
                custom.setText("配置");
                disableBtn(configBtn);
            } else {
                custom.setText("保存");
                //在配置界面
                disableBtn(chartBtn);
                disableBtn(configBtn);
            }
            getParamsInTheBeginningAA();
        } else if (currentType.equals(CacheData.Light)) {
            if (!currentPage.equals("test")) {
                custom.setText("保存");
            }
            chartBtn.setText("拍照");
            disableBtn(configBtn);
            videoFragment = new PD_Vedio_Fragment();
            mFragments.add(videoFragment);
            lightConfigFragment = LightConfigFragment.newInstance(new NetHandler(this));
            mFragments.add(lightConfigFragment);
        } else if (CacheData.currentType.equals(CacheData.infrared)) {
            if (!currentPage.equals("test")) {
                custom.setText("保存");
            }

            fragmentInfrad = InfraredTestFragment.newInstance(service, handler);
            mFragments.add(fragmentInfrad);
            infraredConfigFragment = InfraredConfigFragment.newInstance(new NetHandler(this));
            mFragments.add(infraredConfigFragment);
            infraredConfigFragment.setNetHandler(new NetHandler(this));
//            chartBtn.setText("拍照");
            disableBtn(configBtn);
            disableBtn(chartBtn);
        }

        currentFragment = "aa";
        adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Fragment item = adapter.getItem(i);
                if (item instanceof PD_AA_Fragment) {
                    currentFragment = "AA";
                } else {
                    currentFragment = "UHF";
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        if (currentPage.equals("test")) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(1);
        }
        if (CacheData.currentType.equals(CacheData.UHF)) {
            uhfFragment.fetchingUHF();
        } else if (CacheData.currentType.equals(CacheData.AA)) {
            aaFragment.fetchingAA();
        } else if (CacheData.currentType.equals(Light)) {
            lightConfigFragment.fetchingLight();
        } else {
            infraredConfigFragment.fetchInfrared();
        }

    }

    private void takeAPhoto(String type) {
        if (type.equals("light")) {
            if (videoFragment != null) {
                service.takeAPhoto(handler);
//                View view = videoFragment.getView();
//                Bitmap bitmap = viewConversionBitmap(view);
//                try {
//                    long l = System.currentTimeMillis();
//                    String path = Environment.getExternalStorageDirectory().getPath() + "/data/" + "report" + l + ".png";
//                    File file = new File(path);
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//                    fileOutputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        } else {
            if (fragmentInfrad != null) {
                View view = fragmentInfrad.getView();
                Bitmap bitmap = viewConversionBitmap(view);
                try {
                    long l = System.currentTimeMillis();
                    String path = Environment.getExternalStorageDirectory().getPath() + "/data/" + "report" + l + ".png";
                    File file = new File(path);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //验证特高频

    private boolean validateUHF() {
        boolean validate = false;
        PdParamsUHF data = uhfFragment.getData();
        if (data != null) {
            if (data.equals(CacheData.currentUHFParams)) {
                mViewPager.setCurrentItem(0);
                currentPage = "test";
                validate = true;
            } else {
                showConfirmDialog();
            }
            return validate;
        } else {
            return true;
        }
    }

    private void StartTestFunc() {
        ControlUnitMessager messager = new ControlUnitMessager(ip, port, CommandTypes.STARTTEST);
        messager.setManager(new InternetManagerImpl());
        messager.setTypeOfTest(CacheData.currentType);
        messager.start();
        startTestBtn.setText("停止");
        if (!CacheData.currentType.equals(CacheData.AA) && !CacheData.currentType.equals(CacheData.Light)) {
            disableBtn(chartBtn);
        } else {
            if (CacheData.currentType.equals(CacheData.AA)) {
                chartBtn.setVisibility(View.VISIBLE);
                chartBtn.setEnabled(true);
                chartBtn.setText("谱图");
            } else {
                chartBtn.setVisibility(View.VISIBLE);
                chartBtn.setEnabled(true);
                chartBtn.setText("拍照");
                loadingDialog = new LoadingDialog(this, "视频加载中...");
//显示Dialog
                loadingDialog.show();
                NetHandler handler = new NetHandler(this);
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        Message msg = Message.obtain();
                        msg.what = 0x20;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
//关闭Dialog
            }

        }

        disableBtn(configBtn);
        disableBtn(custom);

        optionLayout.setEnabled(false);
        currentTypeView.setEnabled(false);

    }

    private void showConfirmDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(TestActivity.this);
        normalDialog.setTitle("警告");
        normalDialog.setMessage("当前最新参数未保存。是否直接进入采集界面?");
        normalDialog.setPositiveButton("确定",
                (dialog, which) -> {
                    //...To-do
                    mViewPager.setCurrentItem(0);
                    currentPage = "test";
                    StartTestFunc();
                });
        normalDialog.setNegativeButton("关闭",
                (dialog, which) -> {
                });
        // 显示
        normalDialog.show();
    }

    private void showDialogToConfigActivity() {
        new PassWordDialog(currentActivity).setTitle("请输入密码").setSubTitle("提示").setMoney("内部参数谨慎修改").setCompleteListener(new DialogCompleteListener() {
            @Override
            public void dialogCompleteListener(String money, String pwd) {
                if (pwd.equals("123456")) {
                    CacheData.isValidate = true;
                    Intent intent = new Intent(TestActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(currentActivity, "密码错误请重试", Toast.LENGTH_LONG).show();
                }
            }
        }).show();
    }

    private void getParamsInTheBeginningAA() {
        NetHandler netHandler = new NetHandler(this);
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, CommandTypes.GET_AA_PARAMS);
        messager.setManager(new InternetManagerImpl());
        messager.start();
        if (!CacheData.currentPage.equals("test")) {
            aaFragment.fetching = true;
//            aaFragment.startSwiping();
        }
        //设置监听线程
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Message msg = Message.obtain();
                msg.what = 0x02;
                netHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void getParamsInTheBeginningUHF(String command) {
        NetHandler netHandler = new NetHandler(this);
        ControlUnitMessager messager = new ControlUnitMessager(CacheData.ip, CacheData.port, command);
        messager.setManager(new InternetManagerImpl());
        messager.start();
//        uhfFragment.fetching = true;
        if (!CacheData.currentPage.equals("test")) {

//            uhfFragment.startSwiping();
        }

//        //设置监听线程
//        new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                Message msg = Message.obtain();
//                msg.what = 0x02;
//                netHandler.sendMessage(msg);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    private void disableBtn(Button btn) {
        btn.setText("");
        btn.setEnabled(false);

    }

    @Override
    public void handleMessage(Message msg) {
        int code = msg.what;
        switch (code) {
            case Command.CHECK_GETBASICPARAMS_OVERTIME:
                switch (currentType) {
                    case CacheData.UHF: {
                        if (!CacheData.currentPage.equals("test")) {
                            Toast.makeText(this, "获取UHF参数成功", Toast.LENGTH_LONG).show();
                        }
                        Bundle bundle = msg.getData();
                        PdParamsUHF uhfPdParams = (PdParamsUHF) bundle.get("UHFParams");
                        if (uhfPdParams != null) {
                            uhfFragment.setData(uhfPdParams);
                        }
                        CacheData.currentUHFParams = uhfPdParams;
                        uhfFragment.fetching = false;
                        uhfFragment.cancelSwipeLoading();
                        CacheData.currentUHFParams = uhfPdParams;
                        break;
                    }
                    case CacheData.AA: {
                        Toast.makeText(this, "获取AA参数成功", Toast.LENGTH_LONG).show();
                        Bundle bundle = msg.getData();
                        PdParamsAA aaPdParams = (PdParamsAA) bundle.get("aaParams");
                        if (aaPdParams != null) {
                            aaFragment.setData(aaPdParams);
                        }
                        aaFragment.fetching = false;
                        aaFragment.cancelSwipeLoading();
                        break;
                    }
                    case CacheData.Light: {
                        Toast.makeText(this, "获取视屏流参数成功", Toast.LENGTH_LONG).show();
                        Bundle bundle = msg.getData();
                        LightParams light = (LightParams) bundle.get("LightParams");
                        if (light != null) {
                            lightConfigFragment.setData(light);
                        }
                        lightConfigFragment.fetching = false;
                        lightConfigFragment.cancelSwipeLoading();
                        break;

                    }
                    case infrared: {
                        Toast.makeText(this, "获取红外参数成功", Toast.LENGTH_LONG).show();
                        Bundle bundle = msg.getData();
                        InfraredParams params = (InfraredParams) bundle.get("infrared");
                        if (params != null) {
                            infraredConfigFragment.setData(params);
                        }
                        infraredConfigFragment.fetching = false;
                        infraredConfigFragment.cancelSwipeLoading();
                        break;
                    }

                }

                break;
            case Command.CHECK_PDPARAMS_OVERTIME: {
                //获取超时检测
                switch (currentType) {
                    case CacheData.UHF: {
                        if (uhfFragment != null) {
                            if (uhfFragment.fetching) {
                                uhfFragment.fetching = false;
                                uhfFragment.cancelSwipeLoading();
                                if (!CacheData.currentPage.equals("test")) {
                                    Toast.makeText(this, "获取UHF参数超时", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        break;
                    }
                    case CacheData.AA: {
                        if (aaFragment != null) {
                            if (aaFragment.fetching) {
                                aaFragment.fetching = false;
                                aaFragment.cancelSwipeLoading();
                                if (!CacheData.currentPage.equals("test")) {
                                    Toast.makeText(this, "获取超声参数超时", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                        break;

                    }
                    case CacheData.Light: {
                        if (lightConfigFragment != null) {
                            if (lightConfigFragment.fetching) {
                                lightConfigFragment.fetching = false;
                                lightConfigFragment.cancelSwipeLoading();
                                if (!CacheData.currentPage.equals("test")) {
                                    Toast.makeText(this, "获取视屏流参数超时", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                        break;
                    }
                    case CacheData.infrared: {
                        if (infraredConfigFragment != null) {
                            if (infraredConfigFragment.fetching) {
                                infraredConfigFragment.fetching = false;
                                infraredConfigFragment.cancelSwipeLoading();
                                if (!CacheData.currentPage.equals("test")) {
                                    Toast.makeText(this, "获取红外参数超时", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        break;
                    }
                }
                break;
            }
            case Command.SET_RUNTIME_CONFIG_SUCCUSS: {
                //设置运行参数成功
                if (CacheData.currentType.equals(CacheData.UHF)) {
                    uhfFragment.setting = false;
                    uhfFragment.cancelSwipeLoading();
                    PdParamsUHF data = uhfFragment.getData();
                    currentUHFParams = data;
//                    CacheData.currentUHFParams = uhfFragment.getData();
                    Toast.makeText(this, "设置特高频参数成功", Toast.LENGTH_LONG).show();
                } else if (CacheData.currentType.equals(CacheData.AA)) {
                    Toast.makeText(this, "设置超声参数成功", Toast.LENGTH_LONG).show();
                    aaFragment.setting = false;
                    aaFragment.cancelSwipeLoading();
                } else if (CacheData.currentType.equals(CacheData.Light)) {
                    Toast.makeText(this, "设置视屏流参数成功", Toast.LENGTH_LONG).show();
                    lightConfigFragment.setting = false;
                    lightConfigFragment.cancelSwipeLoading();
                }else {
                    Toast.makeText(this, "设置红外参数成功", Toast.LENGTH_LONG).show();
                   infraredConfigFragment.setting=false;
                   infraredConfigFragment.cancelSwipeLoading();
                }
                break;
            }
            case Command.CHECK_SETUHFPARAMS_OVERTIME: {
                if (CacheData.currentType.equals(CacheData.UHF)) {
                    if (uhfFragment != null) {
                        if (uhfFragment.setting) {
                            uhfFragment.setting = false;
                            uhfFragment.cancelSwipeLoading();
                            Toast.makeText(this, "设置特高频参数失败", Toast.LENGTH_LONG).show();
                        }
                    }

                } else if (CacheData.currentType.equals(CacheData.AA)) {
                    if (aaFragment != null) {
                        if (aaFragment.setting) {
                            aaFragment.setting = false;
                            aaFragment.cancelSwipeLoading();
                            Toast.makeText(this, "设置空气式超声参数失败", Toast.LENGTH_LONG).show();
                        }
                    }

                } else if (CacheData.currentType.equals(CacheData.Light)) {
                    if (lightConfigFragment != null) {
                        if (lightConfigFragment.setting) {
                            lightConfigFragment.setting = false;
                            lightConfigFragment.cancelSwipeLoading();
                            Toast.makeText(this, "设置视屏流超声参数失败", Toast.LENGTH_LONG).show();
                        }
                    }

                } else if (CacheData.currentType.equals(CacheData.infrared)) {
                    if (infraredConfigFragment != null) {
                        if (infraredConfigFragment.setting) {
                            infraredConfigFragment.setting = false;
                            infraredConfigFragment.cancelSwipeLoading();
                            Toast.makeText(this, "设置红外超时超声参数失败", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                //设置参数超时检测
                break;
            }

            case Command.STARTTEST:
                Bundle result = msg.getData();
                byte info = (byte) result.get("result");
                if (info == 0) {
                    if (startTestBtn.getText().toString().equals("停止")) {
                        Toast.makeText(this, "启动成功", Toast.LENGTH_LONG).show();
                        if (CacheData.currentType.equals(CacheData.UHF)) {
                            pdUhfFragment.ClearData();
                        } else if (CacheData.currentType.equals(CacheData.AA)) {
                            pdAaFragment.ClearData();
                        }
                    } else {
                        Toast.makeText(this, "停止成功", Toast.LENGTH_LONG).show();
//                        if (CacheData.currentType.equals(CacheData.UHF)) {
//                            pdUhfFragment.ClearData();
//                        } else if (CacheData.currentType.equals(CacheData.AA)) {
//                            pdAaFragment.ClearData();
//                        }
                    }

                } else if (info == 1) {
                    Toast.makeText(this, "启动失败", Toast.LENGTH_LONG).show();
                    //启动失败把状态改为原始状态
                    startTestBtn.setText("启动");
                    optionLayout.setEnabled(true);
                    currentTypeView.setEnabled(true);
                    custom.setText("配置");
                    custom.setEnabled(true);
                    if (CacheData.currentType.equals(CacheData.AA)) {
                        chartBtn.setText("谱图");
                        chartBtn.setEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "同步错误", Toast.LENGTH_LONG).show();
                }
                break;
            case 0x10:
                Bundle bundle = msg.getData();
                String url = bundle.getString("url");
                url = "test.mp4";
                if (!playing) {
                    String videoStr = "rtsp://" + CacheData.ip + ":8554/" + url;
                    Toast.makeText(this, videoStr, Toast.LENGTH_LONG).show();
                    videoFragment.SetUrlAndStart(videoStr);
                    playing = true;
                } else {
                    playing = false;
                    Toast.makeText(this, "视频停止成功", Toast.LENGTH_LONG).show();
                    videoFragment.StopVedio();
                }

                break;
            case 0x07:
                if (currentType.equals(CacheData.AA)) {
                    //  pdAaFragment.ClearData();
                    pdAaFragment.SetData(msg.getData().getByteArray("data"));
                } else if (currentType.equals(CacheData.UHF)) {
                    // pdUhfFragment.ClearData();
                    pdUhfFragment.SetData(msg.getData().getByteArray("data"));
                } else if (currentType.equals(infrared)) {
//                    byte[] raw =
                    Packages packages = (Packages) msg.getData().getSerializable("packages");
                    int[][] dimenArray = packages.getDataSet();
                    if (dimenArray != null) {
                        fragmentInfrad.setData(packages);
                        fragmentInfrad.setTemp(packages);
                    }

                }
                break;
            case 0x11:
                Toast.makeText(this, "停止成功", Toast.LENGTH_LONG).show();
                break;

            case 0x20:
                loadingDialog.close();
                break;
            case 0x02:
                if (lightConfigFragment != null) {
                    if (lightConfigFragment.fetching) {
                        Toast.makeText(this, "获取视频参数失败", Toast.LENGTH_LONG).show();
                        lightConfigFragment.fetching = false;
                        lightConfigFragment.cancelSwipeLoading();
                    }
                    break;
                }


        }
    }
}
