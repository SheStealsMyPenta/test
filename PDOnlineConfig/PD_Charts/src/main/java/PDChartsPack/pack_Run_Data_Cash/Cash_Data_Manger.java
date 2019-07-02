package PDChartsPack.pack_Run_Data_Cash;



import PDChartsPack.pack_commen.Convert_Tools;
import PDChartsPack.pack_compute.CalcVaule;
import PDChartsPack.pack_compute.Compute_Bean;
import PDChartsPack.pack_compute.Compute_Information;
import PDChartsPack.pack_compute.FeatureData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONG on 2018/1/8,9:42.
 * The final explanation right belongs to author
 */

public class Cash_Data_Manger {
    /**
     * 普通私有变量
     **/
    private int m_nPeriodCnt = 0;
    private int m_nSecondsCnt = 0;
    private List<P_Data> m_listP_Data = new ArrayList<P_Data>();
    private Cash_Data cash_data;
    private Compute_Bean compute_bean;

    /**
     * TEV特征参数
     **/
    private float m_fTev_Period_Pwm_Cnt1 = 0;
    private float m_fTev_Qp = 0;
    private float getM_fTev_Period_Pwm_Cnt2 = 0;


    /**
     * 普通特征参数
     **/
    private float m_fNormalQp = 0;
    private float m_fNormalQm = 0;
    private float m_fNormalF1 = 0;
    private float m_fNormalF2 = 0;

    /**
     * 构造函数
     *
     * @param Data_Type 数据的类别
     */
    public Cash_Data_Manger(byte Data_Type) {

        m_listP_Data = new ArrayList<P_Data>();
        cash_data = new Cash_Data(Data_Type, 1);
        m_nPeriodCnt = 0;
    }

    public void Clear_All(byte Data_Type, int len) {
        m_listP_Data = new ArrayList<P_Data>();
        cash_data = new Cash_Data(Data_Type, 1);
        m_nPeriodCnt = 0;

    }

    public Cash_Data_Manger(byte Data_Type, int len) {
        m_listP_Data = new ArrayList<P_Data>();
        cash_data = new Cash_Data(Data_Type, len);
        m_nPeriodCnt = 0;
    }

    public void setsyncFreq(float freq) {
        cash_data.setM_fSyncFreq(freq);
    }

    public void setsynctype(byte type) {

        cash_data.setM_fSyncFreqtype(type);
    }

    public void savetesttime() {
        cash_data.savetestdate();
    }

    /**
     * 添加并计算数据
     *
     * @param data
     * @return
     */
    public FeatureData Append_P_Data(List<P_Data> data, Compute_Bean compute_bean, Felter_Bean Felter) {

        Compute_Information.Compute(data, compute_bean);
        m_listP_Data.addAll(data);
        m_nPeriodCnt++;
        if (m_nPeriodCnt >= 5) {
            FeatureData feature = new FeatureData();
            feature.setQp(Compute_Information.GetResult().getQp());
            feature.setQm(Compute_Information.GetResult().getQm());
            feature.setF1(Compute_Information.GetResult().getF1());
            feature.setF2(Compute_Information.GetResult().getF2());
            feature.setTevPeriodPulse(Compute_Information.GetResult().getTevPeriodPulse());
            feature.setTev2sPulse(Compute_Information.GetResult().getTev2sPulse());
            feature.setTevDischarge(Compute_Information.GetResult().getTevDischarge());
            /*储存数据*/
            Data_Bean bean = new Data_Bean(m_listP_Data, feature, compute_bean.getM_fPWMCnt(), compute_bean.getM_fSyncFreq());
            cash_data.Append_Data(bean, Felter);
            m_listP_Data = new ArrayList<P_Data>();
            m_nPeriodCnt = 0;
            return feature;
        }
        return null;
    }

    /**
     * 添加并保存数据AE数据
     *
     * @param rc_Data
     * @param bData
     * @param compute_bean
     * @return
     */
    public FeatureData Append_P_Data_AE(List<P_Data> rc_Data, byte[] bData, Compute_Bean compute_bean, Felter_Bean Felter) {
        /**一秒长度数据**/
        /*是个周期长度数据*/
        m_listP_Data.addAll(rc_Data);
        m_nPeriodCnt++;
        /**统计一秒钟的最大的QP**/
        float temp_qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 0, 4));
        if (temp_qp >= m_fNormalQp) {
            m_fNormalQp = temp_qp;
        }
        m_fNormalQm += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 4, 4));
        temp_qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 8, 4));
        m_fNormalF1 += temp_qp;
        m_fNormalF2 += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 12, 4));

        if (m_nPeriodCnt >= 5) {
            FeatureData feature = new FeatureData();
            /******解析特征数据****/
            feature.setQp(m_fNormalQp);
            feature.setQm(m_fNormalQm / 5.0f);
            feature.setF1(m_fNormalF1 / 5.0f);
            feature.setF2(m_fNormalF2 / 5.0f);
            Data_Bean bean = new Data_Bean(m_listP_Data
                    , feature, compute_bean.getM_fPWMCnt()
                    , compute_bean.getM_fSyncFreq());
            /**设置原始波形数据**/
            bean.setRC_WAVE_DATA(tempwave);

            cash_data.Append_Data(bean, Felter);
            m_listP_Data = new ArrayList<P_Data>();
            m_nPeriodCnt = 0;
            m_fNormalQp = 0;
            m_fNormalQm = 0;
            m_fNormalF1 = 0;
            m_fNormalF2 = 0;
            return feature;

        }
        return null;
    }

    private byte[] tempwave = new byte[300];

    /**
     * 添加波形数据
     *
     * @param RC_Data
     */
    public void Append_WaveData(List<P_Data> RC_Data) {
        if (m_nPeriodCnt == 4) {
            for (int i = 0; i < 5; i++) {
                System.arraycopy(RC_Data.get(RC_Data.size() - 5 + i).getData(), 0, tempwave, i * 60, 60);

            }
        }

    }

    /**
     * 特高频
     *
     * @param rc_Data
     * @param bData
     * @param compute_bean
     * @param Felter
     * @return
     */
    public FeatureData Append_P_Data_HFCT(List<P_Data> rc_Data, byte[] bData, Compute_Bean compute_bean, Felter_Bean Felter) {
        return Append_P_Data(rc_Data, bData, compute_bean, Felter);
    }

    /**
     * 空气式超声
     *
     * @param rc_Data
     * @param bData
     * @param compute_bean
     * @param Felter
     * @return
     */
    public FeatureData Append_P_Data_AA(List<P_Data> rc_Data, byte[] bData, Compute_Bean compute_bean, Felter_Bean Felter) {
        /**一秒长度数据**/
        /*是个周期长度数据*/
        m_listP_Data.addAll(rc_Data);
        m_nPeriodCnt++;
        /**统计一秒钟的最大的QP**/
        float temp_qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 0, 4));
        if (temp_qp >= m_fNormalQp) {
            m_fNormalQp = temp_qp;
        }
        m_fNormalQm += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 4, 4));
        temp_qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 8, 4));
        m_fNormalF1 += temp_qp;
        m_fNormalF2 += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 12, 4));

        if (m_nPeriodCnt >= 5) {
            FeatureData feature = new FeatureData();

            /******解析特征数据****/
            feature.setQp(m_fNormalQp);
            feature.setQm(m_fNormalQm / 5.0f);
            feature.setF1(m_fNormalF1 / 5.0f);
            feature.setF2(m_fNormalF2 / 5.0f);


            Data_Bean bean = new Data_Bean(m_listP_Data
                    , feature, compute_bean.getM_fPWMCnt()
                    , compute_bean.getM_fSyncFreq());


            /**设置原始波形数据**/
            bean.setRC_WAVE_DATA(tempwave);


            cash_data.Append_Data(bean, Felter);

            m_listP_Data = new ArrayList<P_Data>();
            m_nPeriodCnt = 0;
            m_fNormalQp = 0;
            m_fNormalQm = 0;
            m_fNormalF1 = 0;
            m_fNormalF2 = 0;
            return feature;

        }
        return null;
    }

    /**
     * 特高频
     *
     * @param rc_Data
     * @param bData
     * @param compute_bean
     * @param Felter
     * @return
     */
    public FeatureData Append_P_Data_UHF(List<P_Data> rc_Data, byte[] bData, Compute_Bean compute_bean, Felter_Bean Felter) {
        return Append_P_Data(rc_Data, bData, compute_bean, Felter);
    }

    /**
     * 添加并保存数据
     *
     * @param rc_Data
     * @param bData
     * @param compute_bean
     * @return
     */
    public FeatureData Append_P_Data(List<P_Data> rc_Data, byte[] bData, Compute_Bean compute_bean, Felter_Bean Felter) {
        /*是个周期长度数据*/
        m_listP_Data.addAll(rc_Data);
        m_nPeriodCnt++;
        /**统计一秒钟的最大的QP**/
        float temp_qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 0, 4));

        if (temp_qp >= m_fNormalQp) {
            m_fNormalQp = temp_qp;
        }
        m_fNormalQm += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 4, 4));
        temp_qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 8, 4));
        m_fNormalF1 += temp_qp;
        m_fNormalF2 += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 12, 4));

        if (m_nPeriodCnt >= 5) {
            FeatureData feature = new FeatureData();
            /******解析特征数据****/
            CalcVaule.Calculate(m_listP_Data, 0);
            feature.setQp(CalcVaule.Calc_Qp());
            feature.setQm(CalcVaule.Calc_Qm());
            feature.setF1(CalcVaule.getF_F1());
            feature.setF2(CalcVaule.getF_F2());
            Data_Bean bean = new Data_Bean(m_listP_Data
                    , feature, compute_bean.getM_fPWMCnt()
                    , compute_bean.getM_fSyncFreq());
            cash_data.Append_Data(bean, Felter);
            m_listP_Data = new ArrayList<P_Data>();
            m_nPeriodCnt = 0;
            m_fNormalQp = 0;
            m_fNormalQm = 0;
            m_fNormalF1 = 0;
            m_fNormalF2 = 0;
            return feature;

        }
        return null;
    }

    public FeatureData Append_P_Data_Tev(List<P_Data> rc_Data, byte[] bData, Compute_Bean compute_bean, Felter_Bean Felter, float Sync_Freq) {
        float Pc = 0.0f;
        float PS = 0.0f;
        float SI = 0.0f;
        m_listP_Data.addAll(rc_Data);
        m_nPeriodCnt++;
        /**周期脉冲数**/
        m_fTev_Period_Pwm_Cnt1 += Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 4, 4));
        /**Qp**/
        if (Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 0, 4)) >= m_fTev_Qp) {
            m_fTev_Qp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(bData, 0, 4));
        }
        if (m_nPeriodCnt >= 5) {
            FeatureData feature = new FeatureData();
            /******解析特征数据****/
            feature.setTev2sPulse(m_fTev_Period_Pwm_Cnt1);

            /**QP**/
            feature.setQp(m_fTev_Qp);
            /**周期脉冲数**/

            Pc = m_fTev_Period_Pwm_Cnt1 / Sync_Freq;
            feature.setQm(Pc);

            /**一秒秒脉冲数**/
            PS = m_fTev_Period_Pwm_Cnt1;

            feature.setF1(PS);

            /**放电严重度**/
            SI = (Pc * Compute_Information.dBmV_2_mV(m_fTev_Qp));
            feature.setF2(SI > 999999.0f ? 999999.0f : SI);
            Data_Bean bean = new Data_Bean(m_listP_Data, feature, compute_bean.getM_fPWMCnt(), compute_bean.getM_fSyncFreq());
            cash_data.Append_Data(bean, Felter);
            m_listP_Data = new ArrayList<P_Data>();
            m_nPeriodCnt = 0;
            m_fTev_Qp = 0;
            m_fTev_Period_Pwm_Cnt1 = 0;
            return feature;
        }

        return null;
    }


    public Cash_Data getCash_data() {
        return cash_data;
    }

}
