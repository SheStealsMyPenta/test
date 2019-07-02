package PDChartsPack.pack_Run_Data_Cash;



import java.util.ArrayList;
import java.util.List;

import PDChartsPack.pack_commen.Convert_Tools;
import PDChartsPack.pack_compute.FeatureData;
import PDChartsPack.pack_pd_charts.TheOtherParameter;

/**
 * Created by SONG on 2018/1/12,14:42.
 * The final explanation right belongs to author
 */

public class Check_Out_Data {
    private byte m_nDataType;
    private int m_nPWMcnt;
    private byte SyncType;
    private float SyncFreq;
    private FeatureData featureData = new FeatureData();
    private List<P_Data> DATA = new ArrayList<P_Data>();

    public byte[] RC_WAVE_DATA = new byte[TheOtherParameter.PHASE_COUNT*50];

    public Check_Out_Data(byte datatype)
    {
        this.m_nDataType = datatype;
    }

    public void setDATA(byte[]  rc_data)
    {
        for(int i=0;i<50;i++)
        {
            byte[] b_temp = new byte[TheOtherParameter.PHASE_COUNT];
            System.arraycopy(rc_data,TheOtherParameter.PHASE_COUNT * i, b_temp, 0, TheOtherParameter.PHASE_COUNT);
            P_Data temp = new P_Data(b_temp);
            DATA.add(temp);
        }

    }

    /**
     * 设置特征值数据
     * @param rc_data
     */
    public void setFeatureData(byte[] rc_data)
    {
        if (rc_data.length<16)
        {
            featureData.setF1(0);
            featureData.setF2(0);
            featureData.setQm(0);
            featureData.setQp(0);
        }
        else
        {
            featureData.setQp(Convert_Tools.tofloat(Convert_Tools.Sub_Arry(rc_data,0,4)));
            featureData.setQm(Convert_Tools.tofloat(Convert_Tools.Sub_Arry(rc_data,4,4)));
            featureData.setF1(Convert_Tools.tofloat(Convert_Tools.Sub_Arry(rc_data,8,4)));
            featureData.setF2(Convert_Tools.tofloat(Convert_Tools.Sub_Arry(rc_data,12,4)));
        }
    }

    /**
     * 获取特征值数据
     * @return
     */
    public FeatureData getFeatureData()
    {
        return featureData;
    }

    public List<P_Data> getDATA()
    {
        return this.DATA;
    }


    public byte getM_nDataType() {
        return m_nDataType;
    }

    public void setM_nDataType(byte m_nDataType) {
        this.m_nDataType = m_nDataType;
    }

    public int getM_nPWMcnt() {
        return m_nPWMcnt;
    }

    public void setM_nPWMcnt(int m_nPWMcnt) {
        this.m_nPWMcnt = m_nPWMcnt;
    }

    public byte getSyncType() {
        return SyncType;
    }

    public void setSyncType(byte syncType) {
        SyncType = syncType;
    }

    public float getSyncFreq() {
        return SyncFreq;
    }

    public void setSyncFreq(float syncFreq) {
        SyncFreq = syncFreq;
    }
}
