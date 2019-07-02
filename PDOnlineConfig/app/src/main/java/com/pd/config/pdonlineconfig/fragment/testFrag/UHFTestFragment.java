package com.pd.config.pdonlineconfig.fragment.testFrag;

import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_pd_charts.PrpsView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.pd.config.pdonlineconfig.R;
import com.pd.config.pdonlineconfig.pojo.CubicleBasicData;

import java.util.ArrayList;
import java.util.List;


public class UHFTestFragment extends Fragment {
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.title3)
    TextView title3;
    @BindView(R.id.prpsView)
    PrpsView prpsView;
    @BindView(R.id.data_1)
    TextView data_1_view;
    @BindView(R.id.data_2)
    TextView data_2_view;
    @BindView(R.id.data_3)
    TextView data_3_view;

    String title1Text;
    String title2Text;
    String title3Text;

    private LinearLayout linearLayout;

    public static UHFTestFragment newInstance(String title1, String title2, String title3) {
        Bundle bundle = new Bundle();
        UHFTestFragment fragment = new UHFTestFragment();
        fragment.title1Text = title1;
        fragment.title2Text = title2;
        fragment.title3Text = title3;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uhf_test_fragment, null);
        ButterKnife.bind(this, view);
        title1.setText(title1Text);
        title2.setText(title2Text);
        title3.setText(title3Text);

        return view;
    }

    public void setData(CubicleBasicData data) {
        data_1_view.setText(data.getData_1());
        data_2_view.setText(data.getData_2());
        data_3_view.setText(data.getData_3());
    }

    public void setPrpsData(byte[] data) {
        List<P_Data> temp = new ArrayList<P_Data>();
//        for (int i = 0; i < 10; i++) {
//            byte[] b_temp = new byte[60];
//            System.arraycopy(data, 1 + 60 * i, b_temp, 0, 60);
//            P_Data p_temp = new P_Data(b_temp);
//            temp.add(p_temp);
//            prpsView.SetData(temp);
//        }
        prpsView.SetData(data);

    }
}
