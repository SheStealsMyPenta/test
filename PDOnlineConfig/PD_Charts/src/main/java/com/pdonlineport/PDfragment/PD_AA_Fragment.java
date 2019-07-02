package com.pdonlineport.PDfragment;

import PDChartsPack.pack_Run_Data_Cash.Cash_Data_Manger;
import PDChartsPack.pack_Run_Data_Cash.Felter_Bean;
import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.Convert_Tools;
import PDChartsPack.pack_commen.System_Commen;
import PDChartsPack.pack_commen.System_Flag;
import PDChartsPack.pack_compute.Compute_Bean;
import PDChartsPack.pack_compute.FeatureData;
import PDChartsPack.pack_pd_charts.*;
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

import java.util.ArrayList;
import java.util.List;

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
    private LinearLayout m_linePRPS;

    private PrpdView m_prpdView;
    private PrpsView m_prpsView;
    private TofView m_TofView;
    private TrendView m_TrendView;

    private ContentView m_CView1;
    private ContentView m_CView2;
    private ContentView m_CView3;
    private ContentView m_CView4;

    private AA_Datas m_DataAnlyzer = new AA_Datas();

    private final static int PRPS_ID = 0x00;
    private final static int PRPD_ID = 0x01;
    private final static int TOF_ID = 0X02;


    private int m_iCurrentViewID = PRPD_ID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_CurrentVeiw = inflater.inflate(R.layout.pd_aa_layout,container,false);
        InitView();

        SwitchView(PRPS_ID);
        return m_CurrentVeiw;
    }

    private FeatureData m_objfeature_data;
    private Cash_Data_Manger data_manger = new Cash_Data_Manger((byte) 0, 2);
    public void SetData(byte[] src)
    {

        if (src.length < 639) {
            return;
        }

        List<P_Data> temp = new ArrayList<P_Data>();
        for (int i = 0; i < 10; i++) {
            byte[] b_temp = new byte[60];
            System.arraycopy(src, 1 + 60 * i, b_temp, 0, 60);
            P_Data p_temp = new P_Data(b_temp);
            temp.add(p_temp);
        }



        /**数据拖尾处理**/
     //   Tof_AA_Compute.Tof_Calc(temp);

        m_objfeature_data = data_manger.Append_P_Data_AA(temp,
                Convert_Tools.Sub_Arry(src, 604, 16)
                , new Compute_Bean(0
                        , System_Flag.Current_Sync_Freq
                        , 20
                        , src[603]), new Felter_Bean((byte) 0xff
                        , System_Flag.Felter_Height(System_Flag.Ae_Height)
                        , System_Flag.Felter_Low(System_Flag.Ae_Low), System_Flag.Current_Sync_Type));

        if(m_objfeature_data!=null)
        {
            m_TrendView.Add_Data((int) m_objfeature_data.getQp());

            m_CView1.SetData(m_objfeature_data.getQp());
            m_CView2.SetData(m_objfeature_data.getQm());
            m_CView3.SetData(m_objfeature_data.getF1());
            m_CView4.SetData(m_objfeature_data.getF2());
        }

        m_prpsView.SetData(temp);
        m_prpdView.Compute_PRPD(temp);
        m_TofView.Compute_Tof(temp);


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
        m_prpsView.Clear_Prps();


    }

    private void InitView() {
        m_btnSwitchView = m_CurrentVeiw.findViewById(R.id.AA_btn_SwitchView);
        m_btnSwitchView.setOnClickListener(this);
		m_btnSwitchView.setVisibility(View.GONE);

        m_LinePRPD = m_CurrentVeiw.findViewById(R.id.AA_Line_PRPD);
        m_LineTof = m_CurrentVeiw.findViewById(R.id.AA_Line_TOF);
        m_linePRPS = m_CurrentVeiw.findViewById(R.id.AA_Line_PRPS);


        m_prpdView = m_CurrentVeiw.findViewById(R.id.AA_PRPDVIEW);
        m_TofView = m_CurrentVeiw.findViewById(R.id.AA_TOFVIEW);
        m_TrendView = m_CurrentVeiw.findViewById(R.id.AA_TrendView);
        m_prpsView = m_CurrentVeiw.findViewById(R.id.AA_PRPS);

        m_CView1 = m_CurrentVeiw.findViewById(R.id.AA_ContentView1);
        m_CView2 = m_CurrentVeiw.findViewById(R.id.AA_ContentView2);
        m_CView3 = m_CurrentVeiw.findViewById(R.id.AA_ContentView3);
        m_CView4 = m_CurrentVeiw.findViewById(R.id.AA_ContentView4);


        m_prpdView.setClearListener(this);
        m_prpdView.SetShowOffset(AA_OFFSET);
        m_prpdView.SetFormat(AA_FORMAT);
        m_prpdView.seteventlistenr(this);
        m_prpdView.SetClearUese(true);


        m_prpsView.seteventlistenr(this);
        m_prpsView.SetShowOffset(AA_OFFSET);
        m_prpsView.SetFormat(AA_FORMAT);


        m_TofView.setTouchEventListener(this);
        m_TofView.SetOffset(AA_OFFSET);
        m_TofView.SetClearCanUsed(true);
        m_TofView.setClearListener(this);


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



        SwitchView(m_iCurrentViewID);

    }
	
	public void ChangeView()
	{
        m_iCurrentViewID++;
        m_iCurrentViewID%=3;
        SwitchView(m_iCurrentViewID);
	}

    private void SwitchView(int  id) {
      m_linePRPS.setVisibility(View.GONE);
      m_LinePRPD.setVisibility(View.GONE);
      m_LineTof.setVisibility(View.GONE);

      switch (id)
      {
          case PRPD_ID:
              m_LinePRPD.setVisibility(View.VISIBLE);
              break;
          case PRPS_ID:
              m_linePRPS.setVisibility(View.VISIBLE);
              break;
          case TOF_ID:
              m_LineTof.setVisibility(View.VISIBLE);
              break;

      }
    }


    @Override
    public void onClick(View view) {
//        if(m_isShowPrpd)
//        {
//            m_isShowPrpd = false;
//        }
//        else
//        {
//            m_isShowPrpd = true;
//        }
//        SwitchView(m_isShowPrpd);

    }

    @Override
    public void Clear_Event() {
        m_TofView.Clear_Tof();
        m_TrendView.Other_Clear();
        m_prpdView.Clear_PRPD();
    }

    @Override
    public void PrpsTouchEvent(PrpsEvent event) {
        m_prpdView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
        m_prpsView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
        m_TofView.SetInformation(event.getSHOW_TAB(), event.getTHRESHOLD());
    }

    @Override
    public void TofEvent(int Show_Tab, int Threshold) {
        m_prpdView.set_information(Show_Tab, Threshold);
    }
}
