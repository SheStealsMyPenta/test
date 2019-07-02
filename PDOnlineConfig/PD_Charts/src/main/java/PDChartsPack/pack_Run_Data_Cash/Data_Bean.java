package PDChartsPack.pack_Run_Data_Cash;


import java.util.ArrayList;
import java.util.List;

import PDChartsPack.pack_compute.FeatureData;


/**
 * Created by SONG on 2017/12/12 9:43.
 */

/**
 * HR1300S
 * 存储50个周期*60个相位的原始数据
 * 一秒钟内的所有数据
 */
public class Data_Bean {

    private List<P_Data> Source_Data = new ArrayList<P_Data>();
    private FeatureData feature_data;
    private int m_nPwmCnt;
    private float Sync_Freq;
    /**波形原始数据**/
    private byte[] RC_WAVE_DATA = new byte[300];


    public byte[] getRC_WAVE_DATA() {
        return RC_WAVE_DATA;
    }

    public void setRC_WAVE_DATA(byte[] rc_wave_data) {
      System.arraycopy(rc_wave_data,0,RC_WAVE_DATA,0,300);
    }

    public float getSync_Freq() {
        return Sync_Freq;
    }

    public void setSync_Freq(float sync_Freq) {
        Sync_Freq = sync_Freq;
    }

    /**
     * 构造函数
     *
     * @param Data 50个周期*60个相位的数据
     */
    public Data_Bean(List<P_Data> Data, FeatureData feature, int PwmCnt, float Freq) {
        Source_Data = Data;
        feature_data = feature;
        m_nPwmCnt = PwmCnt;
        Sync_Freq = Freq;
    }



    public int getPwmCnt()
    {
        return  m_nPwmCnt;
    }

    /**
     * 获取源数据
     *
     * @return
     */
    public List<P_Data> getSource_Data() {
        return Source_Data;
    }

    /**
     * 设置源数据
     *
     * @param source_Data
     */
    public void setSource_Data(List<P_Data> source_Data) {
        Source_Data = source_Data;
    }


    public FeatureData getFeature_data() {
        return feature_data;
    }

    public void setFeature_data(FeatureData feature_data) {
        this.feature_data = feature_data;
    }

    public  P_Data Get(int pos)
    {
        return Source_Data.get(pos);
    }
}
