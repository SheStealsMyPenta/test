package com.pd.config.pdonlineconfig.fragment.configFrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.net.UDPSender;
import com.pd.config.pdonlineconfig.pojo.PdParamsUHF;

public class UHFConfigFragment extends Fragment {
    public boolean fetching = false;
    public boolean setting = false;
    public boolean isEnv = true;
    private NetHandler netHandler;
    private Spinner syncType;
    private Spinner filterFrequency;
    private EditText kValue;
    private EditText bValue;
    private EditText frequency;
    private EditText dischargeValue;
    private EditText threholdValue;
    private EditText chartValue;
    private LinearLayout dischargeLayout;
    private LinearLayout chartLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private String type1;

    public NetHandler getNetHandler() {
        return netHandler;
    }

    private UDPSender udpSender;

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public void setUdpSender(UDPSender udpSender) {
        this.udpSender = udpSender;
    }

    //type是分开环境和开关柜 type1是分开uhf和Hf
    public static UHFConfigFragment newInstance(NetHandler netHandler, String type, String type1) {

        UHFConfigFragment configFragment = new UHFConfigFragment();
        if (type.equals("环境")) {
            configFragment.isEnv = true;
        } else {
            configFragment.isEnv = false;
        }
        configFragment.setNetHandler(netHandler);
        UDPSender udpSender = new UDPSender();
        udpSender.setNetHandler(netHandler);
        configFragment.type1 = type1;
        configFragment.setUdpSender(udpSender);
        return configFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uhf_config, null);
        initComponents(view);
        this.view = view;
        return view;
    }

    private void initComponents(View view) {

        dischargeLayout = view.findViewById(R.id.dischargeLayout);

        chartLayout = view.findViewById(R.id.chartLayout);

        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        threholdValue = view.findViewById(R.id.threholdValue);

        kValue = view.findViewById(R.id.kValue);

        bValue = view.findViewById(R.id.bValue);

        dischargeValue = view.findViewById(R.id.dischargeValue);

        chartValue = view.findViewById(R.id.chartValue);
        //环境类则隐藏
        if (isEnv) {
            dischargeLayout.setVisibility(View.INVISIBLE);
            chartLayout.setVisibility(View.INVISIBLE);

        } else {
            dischargeLayout.setVisibility(View.VISIBLE);
            chartLayout.setVisibility(View.VISIBLE);
        }

        filterFrequency = view.findViewById(R.id.filterFrequency);

        swipeRefreshLayout.setOnRefreshListener(() -> fetchingUHF());


    }

    public void fetchingUHF() {
        fetching = true;

        if (udpSender != null) {
            if (type1.equals("uhf")) {
               CacheData.typeOfSend = "uhf";
            }else if(type1.equals("Hf")) {
                CacheData.typeOfSend="Hf";
            }
            udpSender.
                    setMessager(new ControlUnitMessager(CacheData.ip, CacheData.port)).
                    setManager(new InternetManagerImpl()).
                    setCommandType(CommandTypes.GET_UHF_PARAMS).
                    setNetHandler(netHandler).
                    setNotifyNumber(Command.CHECK_PDPARAMS_OVERTIME).
                    send();
        }
    }

    private PdParamsUHF getValues() {
        PdParamsUHF pdParamsUHF = new PdParamsUHF();
        pdParamsUHF.setThresholdValue((byte) (Integer.parseInt(threholdValue.getText().toString()) + 80));
        pdParamsUHF.setkValue(Float.parseFloat(kValue.getText().toString()));
        pdParamsUHF.setBValue(Float.parseFloat(bValue.getText().toString()));
        pdParamsUHF.setDischargeValue((byte) (Integer.parseInt(dischargeValue.getText().toString()) + 80));
        pdParamsUHF.setChartValue((byte) (Integer.parseInt(dischargeValue.getText().toString()) + 80));
        String filterFre = (String) filterFrequency.getSelectedItem();
        if (filterFre.equals("全频段")) {
            pdParamsUHF.setFilterFrequency((byte) 0);
        } else if (filterFre.equals("低频段")) {
            pdParamsUHF.setFilterFrequency((byte) 1);
        } else {
            pdParamsUHF.setFilterFrequency((byte) 2);
        }
        return pdParamsUHF;
    }

    private boolean checkValue() {
        if (kValue.getText().toString().equals("")) return false;
        if (threholdValue.getText().toString().equals("")) return false;
        try {
            int threhold = Integer.parseInt(threholdValue.getText().toString());


            if (threhold > -1 || threhold < -80) return false;
            //如果是开关柜则检查放电阈值是否为空值
            if (!isEnv) {
                int chart = Integer.parseInt(chartValue.getText().toString());
                int discharge = Integer.parseInt(dischargeValue.getText().toString());
                if (discharge > -1 || discharge < -80) return false;
                if (chart > -1 || chart < -80) return false;
            } else {
                dischargeValue.setText("0");
                chartValue.setText("0");
            }

        } catch (Exception e) {
            return false;
        }

        if (bValue.getText().toString().equals("")) return false;


        return true;
    }

    public void setData(PdParamsUHF params) {
        byte frequency = params.getFilterFrequency();
        filterFrequency.setSelection(frequency);
        threholdValue.setText((params.getThresholdValue() - 80) + "");
        kValue.setText(String.valueOf(params.getkValue()));
        bValue.setText(String.valueOf(params.getBValue()));
        dischargeValue.setText((params.getDischargeValue() - 80) + "");
        //给uhf设置参数
    }

    public PdParamsUHF getData() {
        //返回所有的值
        boolean b = checkValue();
        if (b) {
            return getValues();
        }
        return null;
    }

    public void cancelSwipeLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public boolean sendData() {
        setting = true;
        PdParamsUHF paramsUHF = getData();
        //添加当前设备序号

        if (paramsUHF != null) {
            paramsUHF.setSort((byte) CacheData.sort);
            swipeRefreshLayout.setRefreshing(true);
            if (udpSender != null) {
                udpSender.setMessager(new ControlUnitMessager(CacheData.ip, CacheData.port)).
                        setManager(new InternetManagerImpl()).
                        setNotifyNumber(Command.CHECK_SETUHFPARAMS_OVERTIME).
                        setUHFParams(paramsUHF).send();
            }
            return true;
        } else {
            return false;
        }
    }

    public void startSwiping() {
        swipeRefreshLayout.setRefreshing(true);
    }

    public View getTheView() {
        return view;
    }
}
