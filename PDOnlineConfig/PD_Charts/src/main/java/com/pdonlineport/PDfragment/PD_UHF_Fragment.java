package com.pdonlineport.PDfragment;

import PDChartsPack.pack_Run_Data_Cash.Cash_Data_Manger;
import PDChartsPack.pack_Run_Data_Cash.Felter_Bean;
import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.Convert_Tools;
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
import com.pdonlineport.R;
import com.pdonlineport.Utils.UHF_Datas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONG on 2019/5/2 15:07.
 * The final explanation right belongs to author
 */
public class PD_UHF_Fragment extends Fragment implements PrpsListener, Clear_Listener {
    private final static String UHF_FORMAT = "dBm";
    private final static int UHF_OFFSET = -80;

    private View m_CurrentView;

    private Button m_btn;
    private PrpdView m_PrpdView;
    private PrpsView m_PrpsView;

    private TrendView m_TrendView;

    private UHF_Datas m_UHFAnalyzer = new UHF_Datas();

    private Cash_Data_Manger data_manger = new Cash_Data_Manger((byte) 4, 1);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_CurrentView = inflater.inflate(R.layout.pd_uhf_layout, container, false);

        InitInfo();

        return m_CurrentView;
    }

    private FeatureData m_objfeature_data;

    public void SetData(byte[] src) {
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

        m_objfeature_data = data_manger.Append_P_Data_UHF(temp,
                Convert_Tools.Sub_Arry(src, 604, 16)
                , new Compute_Bean(0
                        , System_Flag.Current_Sync_Freq
                        , 20
                        , src[603]), new Felter_Bean(System_Flag.HUF_Current_Felter_Type, 0, 0, System_Flag.Current_Sync_Type));
        if (m_objfeature_data != null) {
            m_PrpsView.Set_Qp(m_objfeature_data.getQp());
            m_TrendView.Add_Data((int) m_objfeature_data.getQp());
        }

        m_PrpdView.Compute_PRPD(temp);
        m_PrpsView.SetData(temp);


    }

    public void ClearData() {
        m_PrpdView.Clear_PRPD();
        m_PrpsView.Clear_Prps();
        m_TrendView.Clear_Trend();
        m_PrpsView.Set_Qp(0);
        if (data_manger != null) {
            data_manger.Clear_All((byte) 0, 1);
        }


    }

    private void InitInfo() {
        m_PrpdView = m_CurrentView.findViewById(R.id.UHF_PRPDVIEW);
        m_PrpdView.SetShowOffset(UHF_OFFSET);
        m_PrpdView.SetFormat(UHF_FORMAT);
        m_PrpdView.seteventlistenr(this);
        m_PrpdView.setAutoClear(10, true);
        m_PrpdView.setClearListener(this);
        m_PrpdView.SetClearUese(true);

        m_PrpsView = m_CurrentView.findViewById(R.id.UHF_PRPSView);
        m_PrpsView.SetFormat(UHF_FORMAT);
        m_PrpsView.SetShowOffset(UHF_OFFSET);
        m_PrpsView.Set_Qp_Information(true, 20, 40);
        m_PrpsView.seteventlistenr(this);

        m_TrendView = m_CurrentView.findViewById(R.id.UHF_TrendView);
        m_TrendView.SetShowOffset(UHF_OFFSET);
        m_TrendView.SetFormat(UHF_FORMAT);
        m_TrendView.setAutoClear(10,true);

    }

    @Override
    public void PrpsTouchEvent(PrpsEvent event) {
        m_PrpdView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
        m_PrpsView.set_information(event.getPHASE(), event.getSHOW_TAB(), event.getTHRESHOLD());
    }

    @Override
    public void Clear_Event() {
        m_TrendView.Other_Clear();
    }
}
