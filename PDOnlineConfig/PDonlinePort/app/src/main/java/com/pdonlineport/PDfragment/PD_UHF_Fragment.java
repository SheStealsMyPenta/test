package com.pdonlineport.PDfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pdonlineport.R;
import com.pdonlineport.Utils.UHF_Datas;

import PDChartsPack.pack_pd_charts.PrpdView;
import PDChartsPack.pack_pd_charts.PrpsEvent;
import PDChartsPack.pack_pd_charts.PrpsListener;
import PDChartsPack.pack_pd_charts.PrpsView;
import PDChartsPack.pack_pd_charts.TrendView;

/**
 * Created by SONG on 2019/5/2 15:07.
 * The final explanation right belongs to author
 */
public class PD_UHF_Fragment extends Fragment implements PrpsListener {
    private final static String UHF_FORMAT = "dBm";
    private final static int UHF_OFFSET = -80;

    private View m_CurrentView;

    private Button m_btn;
    private PrpdView m_PrpdView;
    private PrpsView m_PrpsView;

    private TrendView m_TrendView;

    private UHF_Datas m_UHFAnalyzer = new UHF_Datas();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_CurrentView = inflater.inflate(R.layout.pd_uhf_layout, container, false);

        InitInfo();

        return m_CurrentView;
    }


    public void SetData(byte[] src) {
        if (m_UHFAnalyzer.ParseData(src)) {
            m_PrpdView.SetData(m_UHFAnalyzer.getM_PrpdDatas());
            m_PrpsView.SetData(m_UHFAnalyzer.getM_PrpsDataS());
            m_PrpsView.Set_Qp(m_UHFAnalyzer.getM_fQp());
            m_TrendView.Add_Data((int) m_UHFAnalyzer.getM_fQp());
        }
    }

    public void ClearData()
    {
        m_PrpdView.Clear_PRPD();
        m_PrpsView.Clear_Prps();
        m_TrendView.Clear_Trend();
        m_PrpsView.Set_Qp(0);


    }

    private void InitInfo() {
        m_PrpdView = m_CurrentView.findViewById(R.id.UHF_PRPDVIEW);
        m_PrpdView.SetShowOffset(UHF_OFFSET);
        m_PrpdView.SetFormat(UHF_FORMAT);
        m_PrpdView.seteventlistenr(this);

        m_PrpsView = m_CurrentView.findViewById(R.id.UHF_PRPSView);
        m_PrpsView.SetFormat(UHF_FORMAT);
        m_PrpsView.SetShowOffset(UHF_OFFSET);
        m_PrpsView.Set_Qp_Information(true, 20, 40);
        m_PrpsView.seteventlistenr(this);

    }

    @Override
    public void PrpsTouchEvent(PrpsEvent event) {
        m_PrpdView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
        m_PrpsView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
    }
}
