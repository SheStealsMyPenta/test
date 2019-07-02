package com.pd.config.pdonlineconfig.fragment.configFrag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.constants.CacheData;
import com.pd.config.pdonlineconfig.constants.Command;
import com.pd.config.pdonlineconfig.constants.CommandTypes;
import com.pd.config.pdonlineconfig.impls.InternetManagerImpl;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.net.ControlUnitMessager;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.net.UDPSender;
import com.pd.config.pdonlineconfig.pojo.LightParams;

public class LightConfigFragment extends Fragment {

    private SeekBar brightnessBar;
    private SeekBar constract;
    private TextView brightnessValue;
    private TextView constractValue;
    private Spinner resolution;
    private NetHandler netHandler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UDPSender udpSender;
    private InternetService service;
    public boolean setting = false;
    public boolean fetching = false;

    public NetHandler getNetHandler() {
        return netHandler;
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public void setUdpSender(UDPSender udpSender) {
        this.udpSender = udpSender;
    }

    public InternetService getService() {
        return service;
    }

    public void setService(InternetService service) {
        this.service = service;
    }

    public static LightConfigFragment newInstance(NetHandler netHandler) {
        LightConfigFragment fragment = new LightConfigFragment();
        fragment.setNetHandler(netHandler);
        UDPSender udpSender = new UDPSender();
//        udpSender.setManager(new InternetManagerImpl());
        udpSender.setNetHandler(netHandler);
        fragment.setUdpSender(udpSender);
        fragment.setService(new InternetService());
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.light_config, null);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        brightnessBar = view.findViewById(R.id.brightness);
        constract = view.findViewById(R.id.contrast);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        brightnessValue = view.findViewById(R.id.brightnessValue);
        constractValue = view.findViewById(R.id.constract);
        resolution = view.findViewById(R.id.resolution);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessValue.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        constract.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                constractValue.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> fetchingLight());
    }

    public void fetchingLight() {
        fetching = true;
        udpSender.setMessager(new ControlUnitMessager(CacheData.ip,CacheData.port)).
                setCommandType(CommandTypes.GET_LIGHT_PARAMS).
                setManager(new InternetManagerImpl()).
                setNotifyNumber(Command.CHECK_LIGHT_OVERTIME).send();
    }

    public LightParams getData() {
        LightParams params = new LightParams();
        params.setResolution((byte) resolution.getSelectedItemPosition());
        params.setConstract((byte)Integer.parseInt(constractValue.getText().toString().split("%")[0]));
        params.setLightness((byte)Integer.parseInt(brightnessValue.getText().toString().split("%")[0]));
        return params;
    }

    public void sendData() {
        LightParams data = getData();
        swipeRefreshLayout.setRefreshing(true);
        setting=true;
        udpSender.setMessager(new ControlUnitMessager(CacheData.ip,CacheData.port)).setLightParams(data).
                setNotifyNumber(Command.CHECK_SETLIGNTPARAMS_OVERTIME).
                setManager(new InternetManagerImpl()).
                send();
    }

    public void setData(LightParams light) {
      resolution.setSelection(light.getResolution());
      brightnessBar.setProgress(light.getLightness());
      constract.setProgress(light.getConstract());
    }
    public void cancelSwipeLoading() {

        swipeRefreshLayout.setRefreshing(false);

    }
}
