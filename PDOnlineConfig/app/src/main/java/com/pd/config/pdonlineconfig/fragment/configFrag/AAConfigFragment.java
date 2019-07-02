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
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.net.UDPSender;
import com.pd.config.pdonlineconfig.pojo.PdParamsAA;

public class AAConfigFragment extends Fragment {
    public boolean setting = false;
    private EditText aaSignalView;
    private EditText aaKValue;
    private EditText aaBValue;
    private EditText dischargeValue;
    private NetHandler netHandler;
    private LinearLayout dischargeLayout;
    private LinearLayout chartLayout;
    private EditText chartValue;
    public boolean fetching = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UDPSender udpSender;
    private int ceilValue = 72;
    private int floorValue = -7;
    private boolean isEnv = true;
    private String type1;
    private InternetService service;

    public InternetService getService() {
        return service;
    }

    public void setService(InternetService service) {
        this.service = service;
    }

    public int getCeilValue() {
        return ceilValue;
    }

    public void setCeilValue(int ceilValue) {
        this.ceilValue = ceilValue;
    }

    public int getFloorValue() {
        return floorValue;
    }

    public void setFloorValue(int floorValue) {
        this.floorValue = floorValue;
    }

    public void setUdpSender(UDPSender udpSender) {
        this.udpSender = udpSender;
    }

    public NetHandler getNetHandler() {
        return netHandler;
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public static AAConfigFragment newInstance(NetHandler netHandler, String type, String type1) {
        AAConfigFragment configFragment = new AAConfigFragment();
        if (type.equals("环境")) {
            configFragment.isEnv = true;
        } else {
            configFragment.isEnv = false;
        }


        configFragment.setNetHandler(netHandler);
        configFragment.type1 = type1;
        UDPSender udpSender = new UDPSender();
        udpSender.setNetHandler(netHandler);
        configFragment.setUdpSender(udpSender);
        configFragment.setService(new InternetService());
        return configFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aa_config, null);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        aaSignalView = view.findViewById(R.id.aaSignal);
        aaKValue = view.findViewById(R.id.aaKValue);
        aaBValue = view.findViewById(R.id.aaBValue);
        dischargeValue = view.findViewById(R.id.dischargeValue);
        dischargeLayout = view.findViewById(R.id.dischargeLayout);
        chartLayout = view.findViewById(R.id.chartLayout);
        chartValue = view.findViewById(R.id.chartValue);
        if (isEnv) {
            dischargeLayout.setVisibility(View.INVISIBLE);
            chartLayout.setVisibility(View.INVISIBLE);

        } else {
            dischargeLayout.setVisibility(View.VISIBLE);
            chartLayout.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> fetchingAA());
    }

    public void fetchingAA() {
        fetching = true;
        if (type1.equals("ae")) {
            CacheData.typeOfSend = "ae";
        } else {
            CacheData.typeOfSend = "tec";
        }

        udpSender.setMessager(new ControlUnitMessager(CacheData.ip, CacheData.port)).
                setManager(new InternetManagerImpl()).
                setCommandType(CommandTypes.GET_AA_PARAMS).
                setNotifyNumber(Command.CHECK_PDPARAMS_OVERTIME).
                send();
    }

    private PdParamsAA getValues() {
        PdParamsAA pdParamsAA = new PdParamsAA();
        if (ceilValue == 72) {
            pdParamsAA.setAaSignalValue((byte) (Integer.parseInt(aaSignalView.getText().toString()) + 7));
            pdParamsAA.setAaKValue(Float.parseFloat(aaKValue.getText().toString()));
            pdParamsAA.setAaBValue(Float.parseFloat(aaBValue.getText().toString()));
            pdParamsAA.setDischargeValue((byte) (Integer.parseInt(dischargeValue.getText().toString()) + 7));
            pdParamsAA.setChartValue((byte) (Integer.parseInt(chartValue.getText().toString()) + 7));
        } else if (ceilValue == 80) {
            pdParamsAA.setAaSignalValue((byte) (Integer.parseInt(aaSignalView.getText().toString())));
            pdParamsAA.setAaKValue(Float.parseFloat(aaKValue.getText().toString()));
            pdParamsAA.setAaBValue(Float.parseFloat(aaBValue.getText().toString()));
            pdParamsAA.setDischargeValue((byte) (Integer.parseInt(dischargeValue.getText().toString())));
            pdParamsAA.setDischargeValue((byte) Integer.parseInt(chartValue.getText().toString()));
        }

        return pdParamsAA;
    }

    private boolean checkValue() {
        if (aaSignalView.getText().toString().equals("")) return false;
        if (aaBValue.getText().toString().equals("")) return false;
        if (aaKValue.getText().toString().equals("")) return false;
        Integer aaS;
        Integer discharge;
        Integer chart;
        try {
            aaS = Integer.parseInt(aaSignalView.getText().toString());
            if (aaS < floorValue || aaS > ceilValue) return false;
            if (!isEnv) {


                discharge = Integer.parseInt(dischargeValue.getText().toString());
                chart = Integer.parseInt(chartValue.getText().toString());

                if (discharge < floorValue || discharge > ceilValue) return false;
                if (chart < floorValue || chart > ceilValue) return false;
            } else {
                dischargeValue.setText("0");
                chartValue.setText("0");
            }
        } catch (Exception e) {
            return false;
        }


        return true;
    }

    public void setData(PdParamsAA params) {
        if (ceilValue == 72) {
            aaSignalView.setText((params.getAaSignalValue() - 7) + "");

            aaKValue.setText((int) params.getAaKValue() + "");

            aaBValue.setText((int) params.getAaBValue() + "");

            dischargeValue.setText((params.getDischargeValue() - 7) + "");
        } else if (ceilValue == 80) {
            aaSignalView.setText((params.getAaSignalValue()) + "");

            aaKValue.setText((int) params.getAaKValue() + "");

            aaBValue.setText((int) params.getAaBValue() + "");

            dischargeValue.setText((params.getDischargeValue()) + "");
        }


    }

    public PdParamsAA getData() {
        //返回所有的值
        boolean b = checkValue();
        if (b) {
            return getValues();
        }

        return null;
    }

    public boolean sendData() {
        setting = true;
        PdParamsAA data = getData();

        if (data != null) {
            if (type1.equals("ae")) {
                CacheData.typeOfSend = "ae";
            } else {
                CacheData.typeOfSend = "tev";
            }

            swipeRefreshLayout.setRefreshing(true);
            udpSender.
                    setMessager(new ControlUnitMessager(CacheData.ip, CacheData.port)).
                    setManager(new InternetManagerImpl()).
                    setAAParams(data).
                    setNotifyNumber(Command.CHECK_SETAAPARAMS_OVERTIME).send();
            return true;
        } else {
            return false;
        }

    }

    public void cancelSwipeLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void startSwiping() {
        swipeRefreshLayout.setRefreshing(true);
    }
}
