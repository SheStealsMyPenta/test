package com.pdonlineport.PDfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pdonlineport.R;
import com.pdonlineport.Utils.AA_Datas;

import PDChartsPack.pack_commen.System_Commen;
import PDChartsPack.pack_pd_charts.Clear_Listener;
import PDChartsPack.pack_pd_charts.ContentView;
import PDChartsPack.pack_pd_charts.PrpdView;
import PDChartsPack.pack_pd_charts.PrpsEvent;
import PDChartsPack.pack_pd_charts.PrpsListener;
import PDChartsPack.pack_pd_charts.TofView;
import PDChartsPack.pack_pd_charts.Tof_Listener;
import PDChartsPack.pack_pd_charts.TrendView;

/**
 * Created by SONG on 2019/4/29 10:29.
 * The final explanation right belongs to author
 */
public class PD_AA_Fragment extends Fragment implements View.OnClickListener, Clear_Listener, PrpsListener, Tof_Listener {


    private final static String AA_FORMAT = "dBuV";
    private final static int AA_OFFSET = -7;

    private View m_CurrentVeiw;
    private Button m_btnSwitchView;
    private LinearLayout m_LinePRPD;
    private LinearLayout m_LineTof;

    private PrpdView m_prpdView;
    private TofView m_TofView;
    private TrendView m_TrendView;

    private ContentView m_CView1;
    private ContentView m_CView2;
    private ContentView m_CView3;
    private ContentView m_CView4;

    private AA_Datas m_DataAnlyzer = new AA_Datas();

    private boolean m_isShowPrpd = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_CurrentVeiw = inflater.inflate(R.layout.pd_aa_layout,container,false);
        InitView();

        return m_CurrentVeiw;
    }


    public void SetData(byte[] src)
    {

        if(m_DataAnlyzer.ParseData(src))
        {
            m_CView1.SetData(m_DataAnlyzer.getM_fVpp());
            m_CView2.SetData(m_DataAnlyzer.getM_fRMS());
            m_CView3.SetData(m_DataAnlyzer.getM_fF1());
            m_CView4.SetData(m_DataAnlyzer.getM_fF2());

            m_TofView.SetData(m_DataAnlyzer.getM_TofDatas());

            m_prpdView.SetData(m_DataAnlyzer.getM_PrpdDatas());

            m_TrendView.Add_Data((int) m_DataAnlyzer.getM_fVpp());

        }


    }

    public void ClearData()
    {
        m_CView1.SetData(0);
        m_CView2.SetData(0);
        m_CView3.SetData(0);
        m_CView4.SetData(0);

        m_TofView.Clear_Tof();
        m_prpdView.Clear_PRPD();
        m_TrendView.Clear_Trend();


    }

    private void InitView() {
        m_btnSwitchView = m_CurrentVeiw.findViewById(R.id.AA_btn_SwitchView);
        m_btnSwitchView.setOnClickListener(this);

        m_LinePRPD = m_CurrentVeiw.findViewById(R.id.AA_Line_PRPD);
        m_LineTof = m_CurrentVeiw.findViewById(R.id.AA_Line_TOF);


        m_prpdView = m_CurrentVeiw.findViewById(R.id.AA_PRPDVIEW);
        m_TofView = m_CurrentVeiw.findViewById(R.id.AA_TOFVIEW);
        m_TrendView = m_CurrentVeiw.findViewById(R.id.AA_TrendView);

        m_CView1 = m_CurrentVeiw.findViewById(R.id.AA_ContentView1);
        m_CView2 = m_CurrentVeiw.findViewById(R.id.AA_ContentView2);
        m_CView3 = m_CurrentVeiw.findViewById(R.id.AA_ContentView3);
        m_CView4 = m_CurrentVeiw.findViewById(R.id.AA_ContentView4);


        m_prpdView.setClearListener(this);
        m_prpdView.SetShowOffset(AA_OFFSET);
        m_prpdView.SetFormat(AA_FORMAT);
        m_prpdView.seteventlistenr(this);

        m_TofView.setTouchEventListener(this);
        m_TofView.SetOffset(AA_OFFSET);


        m_CView1.SetFormat("%.2f" + AA_FORMAT);
        m_CView1.SetShowOffset(AA_OFFSET);
        m_CView1.SetName("Vpp");

        m_CView2.SetFormat("%.2f" + AA_FORMAT);
        m_CView2.SetShowOffset(AA_OFFSET);
        m_CView2.SetName("Rms");

        m_CView3.SetFormat("%.2f" + AA_FORMAT);
        m_CView3.SetShowOffset(AA_OFFSET);
        m_CView3.SetName("F1");

        m_CView4.SetFormat("%.2f" + AA_FORMAT);
        m_CView4.SetShowOffset(AA_OFFSET);
        m_CView4.SetName("F4");

        m_TrendView.SetShowOffset(System_Commen.AA_OFFSET);
        m_TrendView.SetFormat(AA_FORMAT);


        m_isShowPrpd  = true;
        SwitchView(m_isShowPrpd);

    }

    private void SwitchView(boolean m_isShowPrpd) {
        if(m_isShowPrpd)
        {
            m_LineTof.setVisibility(View.GONE);
            m_LinePRPD.setVisibility(View.VISIBLE);
        }
        else
        {
            m_LineTof.setVisibility(View.VISIBLE);
            m_LinePRPD.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        if(m_isShowPrpd)
        {
            m_isShowPrpd = false;
        }
        else
        {
            m_isShowPrpd = true;
        }
        SwitchView(m_isShowPrpd);

    }

    @Override
    public void Clear_Event() {
        m_TofView.Clear_Tof();
        m_TrendView.Other_Clear();
    }

    @Override
    public void PrpsTouchEvent(PrpsEvent event) {
        m_prpdView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
        m_TofView.SetInformation(event.getSHOW_TAB(), event.getTHRESHOLD());
    }

    @Override
    public void TofEvent(int Show_Tab, int Threshold) {
        m_prpdView.set_information(Show_Tab, Threshold);
    }
}
