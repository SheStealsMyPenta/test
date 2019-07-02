package PDChartsPack.pack_compute;


import java.util.List;

import PDChartsPack.pack_Run_Data_Cash.Check_Out_Data;
import PDChartsPack.pack_Run_Data_Cash.Data_Bean;
import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.System_Commen;

/**
 * Created by SONG on 2018/1/5,17:17.
 * The final explanation right belongs to author
 */

public final class Compute_Information {

    private static CalcFeature m_objCalc = new CalcFeature();


    public static void Compute(List<P_Data> data, Compute_Bean bean) {
        m_objCalc.setCalcParam(bean.getM_fShowTreshold(), bean.getM_fSyncFreq());
        for (int i = 0; i < data.size(); i++) {
            m_objCalc.addPeriodData(data.get(i).getData(), bean.isM_bIstevused(), bean.getM_fPWMCnt());
        }
    }

    /**
     * 计算一个周期的特征数据,测试回放时使用
     *
     * @param bean
     * @return
     */
    public static FeatureData Compute(Data_Bean bean, float ShowThreshold) {
        m_objCalc.setCalcParam(ShowThreshold, bean.getSync_Freq());
        for (int i = 0; i < bean.getSource_Data().size(); i++) {
            m_objCalc.addPeriodData(bean.getSource_Data().get(i).getData(), true, bean.getPwmCnt());
        }

        return m_objCalc.getFeature();
    }

    /**
     * 计算检出数据一个周期的特征数据,数据回放使用
     *
     * @param Data
     * @param ShowThreshold
     * @return
     */
    public static FeatureData Compute(Check_Out_Data Data, float ShowThreshold) {
        m_objCalc.setCalcParam(ShowThreshold, Data.getSyncFreq());
        for (int i = 0; i < Data.getDATA().size(); i++) {
            m_objCalc.addPeriodData(Data.getDATA().get(i).getData(), true, Data.getM_nPWMcnt());
        }

        return m_objCalc.getFeature();
    }

    /**
     *
     */
    public static void ClearData() {

        m_objCalc.clearData();
    }

    /**
     * @return
     */
    public static FeatureData GetResult() {

        return m_objCalc.getFeature();


    }

    public static float dBmV_2_mV(float fVal) {

        return (float) (Math.pow(10.0f, fVal / 20.0f));
    }

    public static float dBuV_2_uV(float fVal) {

        return (float) (Math.pow(10.0f, fVal / 20.0f));
    }


    public static float AA_dB_2_mV(float fVal)
    {
        //0dB对应的dBuV值
        float BaseValue = System_Commen.AA_OFFSET;
        //放大倍数
        float K = 100;
        //偏移值
        float B = 0;
        return (float) (K * (0.001f * Math.pow(10.0f, (BaseValue + fVal) / 20.0f)) + B);
    }
    /**
     * fVal为dB值 范围为0-80dB
     *
     * @param fVal
     * @return
     */
    public static float AE_dB_2_mV(float fVal) {
        //0dB对应的dBuV值
        float BaseValue = 7;
        //放大倍数
        float K = 100;
        //偏移值
        float B = 0;
        return (float) (K * (0.001f * Math.pow(10.0f, (BaseValue + fVal) / 20.0f)) + B);
    }

    public static float mV_2_dB(float fVal) {
        //0dB对应的dBuV值
        float BaseValue = 7;
        //放大倍数
        float K = 100;
        //偏移值
        float B = 0;
        fVal = (float) (20 * Math.log10((fVal - B) / K * 1000) - BaseValue);

        return fVal;
    }

    /**
     * 计算档位
     *
     * @param val1
     * @param val2
     * @return
     */
    public static float AA_Range(float val1, float val2) {
        float temp0 = val1 >= val2 ? val1 : val2;
        if (temp0 > 200.0f) {
            return 2000.0f;
        } else if (temp0 > 20.0f) {
            return 200.0f;
        } else if (temp0 > 2.0f) {
            return 20.0f;
        } else {
            return 2.0f;
        }


    }
}
