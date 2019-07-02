package com.pd.config.pdonlineconfig.activities;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.interfaces.NetListener;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.ControlUnit;
import com.pd.config.pdonlineconfig.utils.ConversionTool;

public class SystemInfoActivity extends AppCompatActivity implements NetListener {
    @BindView(R.id.swipeLayoutInfo)
    SwipeRefreshLayout mUpdater;
    @BindView(R.id.clearDataBtn)
    Button mClearDataBtn;
    @BindView(R.id.clearLogBtn)
    Button mClearLogBtn;
    @BindView(R.id.codeOfDevice)
    TextView mCodeOfDevice;
    @BindView(R.id.hardwarePatch)
    TextView mHardwarePatch;
    @BindView(R.id.softwarePatch)
    TextView mSoftwarePatch;
    @BindView(R.id.storageRemain)
    TextView mStorageRemain;
    @BindView(R.id.numberOfUnit)
    TextView mNumberOfCells;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private InternetService mInternetService;
    private NetHandler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_info);
        ButterKnife.bind(this);
        initInternet();
        initListener();
        mUpdater.setRefreshing(true);
        mInternetService.getSystemInfo(mHandler);
    }

    private void initInternet() {
        mHandler = new NetHandler(this);
        CacheData.receiver.setNetHandler(mHandler);
        mInternetService = new InternetService();
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

    private void initListener() {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mUpdater.setOnRefreshListener(() -> mInternetService.getSystemInfo(mHandler));
        mClearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdater.setRefreshing(true);
                mInternetService.deleteData(mHandler);
            }
        });
        mClearLogBtn.setOnClickListener(v -> {
            mUpdater.setRefreshing(true);
            mInternetService.deleteLog(mHandler);
        });
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Command
                    .CHECK_GETBASICPARAMS_OVERTIME:
                if (mUpdater.isRefreshing()) {
                    Toast.makeText(this, "获取系统参数超时", Toast.LENGTH_LONG).show();
                    mUpdater.setRefreshing(false);
                }
                break;
            case Command.GET_BASIC_TESTPARAMS_SUCCESS:
                mUpdater.setRefreshing(false);
                Bundle basicInfoPack = msg.getData();

                ControlUnit info = (ControlUnit) basicInfoPack.getSerializable("deviceBasicInfo");
                if (info != null) {
                    setData(info);
                }
                break;
            case Command.DELETE_DATA:
                if (mUpdater.isRefreshing()) {
                    Toast.makeText(this, "删除数据成功！", Toast.LENGTH_LONG).show();
                    mUpdater.setRefreshing(false);
                }
                break;
            case Command.CHECK_DELETE_DATA_OVERTIME:
                if (mUpdater.isRefreshing()) {
                    Toast.makeText(this, "删除数据超时", Toast.LENGTH_LONG).show();
                    mUpdater.setRefreshing(false);
                }
                break;
            case Command.DELETE_LOG:
                if (mUpdater.isRefreshing()) {
                    Toast.makeText(this, "删除日志成功！", Toast.LENGTH_LONG).show();
                    mUpdater.setRefreshing(false);
                }
                break;
            case Command.CHECK_DELETE_LOG_OVERTIME:
                if (mUpdater.isRefreshing()) {
                    Toast.makeText(this, "删除日志超时", Toast.LENGTH_LONG).show();
                    mUpdater.setRefreshing(false);
                }
                break;


        }

    }

    private void setData(ControlUnit basicInfo) {
        mCodeOfDevice.setText(CacheData.currentUnit.getCodeOfDevice() + "");
        byte[] softwaareArr = basicInfo.getSoftwarePatch();
        String softwareStr = String.format("%d.%d.%d", softwaareArr[0], softwaareArr[1], ConversionTool.toshort(new byte[]{softwaareArr[2], softwaareArr[3]}));
        int hardware = basicInfo.getHardwarePathch();
        mHardwarePatch.setText(intToHex(hardware));
        mSoftwarePatch.setText(softwareStr);
        mStorageRemain.setText(basicInfo.getStorageRemain() + "");
        mNumberOfCells.setText(CacheData.currentUnit.getNumberOfCells() + "个");
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
}
