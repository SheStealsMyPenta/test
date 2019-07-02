package com.pd.config.pdonlineconfig.fragment.testFrag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.net.NetHandler;
import com.pd.config.pdonlineconfig.pojo.Packages;
import com.pd.config.pdonlineconfig.vies.InFraredView;

public class InfraredTestFragment extends Fragment {
    private InternetService service;
    private NetHandler netHandler;
    @BindView(R.id.infraredView)
    InFraredView inFraredView;
    //    @BindView(R.id.xCoordinate)
//    TextView xCoordinate;
//    @BindView(R.id.yCoordinate)
//    TextView yCoordinate;
    @BindView(R.id.maxValue)
    TextView maxValueText;
    @BindView(R.id.minValue)
    TextView minValueText;
    @BindView(R.id.coordinateValue)
    TextView currentValueText;
    @BindView(R.id.avgValueText)
    TextView avgValueText;

    public static InfraredTestFragment newInstance(InternetService service, NetHandler netHandler) {

        InfraredTestFragment fragment = new InfraredTestFragment();
        fragment.service = service;
        fragment.netHandler = netHandler;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.infrared_test_fragment, null);
        ButterKnife.bind(this, view);
//        xCoordinate = view.findViewById(R.id.xCoordinate);
//        yCoordinate = view.findViewById(R.id.yCoordinate);
//        inFraredView = view.findViewById(R.id.infraredView);
        inFraredView.setFragment(this);
        inFraredView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        if (inFraredView.isHardwareAccelerated()) {
            System.out.println("是硬件加速");
        } else {
            System.out.println("不是硬件加速");
        }
        return view;
    }

    public void setData(Packages data) {
        inFraredView.setColorMatrix(data.getDataSet(), data.getMaxTempX(), data.getMaxTempY(), data.getMinTempX(), data.getMinTempY());
    }

    public void setTemp(Packages packages) {
        currentValueText.setTextColor(Color.BLUE);
        minValueText.setTextColor(Color.parseColor("#FFD700"));
        maxValueText.setTextColor(Color.GREEN);
        maxValueText.setText(packages.getMaxTemp() + " ℃");
        minValueText.setText(packages.getMinTemp() + " ℃");
        currentValueText.setText(packages.getCurrentTemp() + " ℃");
        avgValueText.setText(packages.getAvgTemp() + " ℃");
    }

    public void getCurrentTemp(short xIndex, short yIndex) {
//        xCoordinate.setText(xIndex + "");
//        yCoordinate.setText(yIndex + "");
        service.getTempValueByIndex(xIndex, yIndex, netHandler);
    }
}
