package com.pd.config.pdonlineconfig.fragment.configFrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.net.UDPSender;
import com.pd.config.pdonlineconfig.pojo.InfraredParams;

public class InfraredConfigFragment extends Fragment {
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rateValue)
    TextView rateValue;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.atoTempValue)
    TextView atoTempValue;

    @BindView(R.id.relateHumValue)
    TextView relateHumValue;
    @BindView(R.id.reflectTempValue)
    TextView reflectTempValue;


    //变量
    private NetHandler netHandler;
    private InternetService service;
    public boolean fetching;
    public boolean setting;

    public void cancelSwipeLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public InternetService getService() {
        return service;
    }

    public void setService(InternetService service) {
        this.service = service;
    }

    public NetHandler getNetHandler() {
        return netHandler;
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public static InfraredConfigFragment newInstance(NetHandler netHandler) {
        InfraredConfigFragment fragment = new InfraredConfigFragment();
        fragment.setNetHandler(netHandler);
        UDPSender udpSender = new UDPSender();
//        udpSender.setManager(new InternetManagerImpl());
        udpSender.setNetHandler(netHandler);
        fragment.setService(new InternetService());
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.infrared_config_fragment, null);
        ButterKnife.bind(this, view);
        bindListener();
        return view;
    }

    private void bindListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              fetchInfrared();
            }
        });
    }

    public void setData(InfraredParams infraredParams) {
        rateValue.setText(infraredParams.getRate() * 100 + "");
        distance.setText(infraredParams.getDistance() + "");
        atoTempValue.setText(infraredParams.getAtoTemp() + "");
        relateHumValue.setText(infraredParams.getRelateTemp() +"");
        reflectTempValue.setText(infraredParams.getReflectTemp() + "");
    }

    public boolean checkValue() {
        try {
            float rate = Float.parseFloat(rateValue.getText().toString());
            if (rate > 100 || rate < 0) return false;
            byte dist = Byte.parseByte(distance.getText().toString());
            if (dist > 100 || dist < 1) {
                return false;
            }
            float ato = Float.parseFloat(atoTempValue.getText().toString());
            byte relate = Byte.parseByte(relateHumValue.getText().toString());
            if (relate > 100 || relate < 1) return false;
            float reflect = Float.parseFloat(reflectTempValue.getText().toString());
            if (reflectTempValue.getText().equals("") || atoTempValue.getText().toString().equals("")) return false;

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public InfraredParams getData() {
        InfraredParams params = new InfraredParams();
        params.setRate(Float.parseFloat(rateValue.getText().toString()) / 100);
        params.setDistance(Byte.parseByte(distance.getText().toString()));
        params.setAtoTemp(Float.parseFloat(atoTempValue.getText().toString()));
        params.setRelateTemp(Byte.parseByte(relateHumValue.getText().toString()));
        params.setReflectTemp(Float.parseFloat(reflectTempValue.getText().toString()));
        return params;
    }

    public boolean sendData() {
        if (checkValue()) {
            setting = true;
            InfraredParams infraredParams = getData();
            service.setInfraredConfigFragment(infraredParams, netHandler);
            return true;
        } else {
            return false;
        }

    }


    public void fetchInfrared() {
        fetching = true;
        service.getInfraredParams(netHandler);

    }
}
