package PDChartsPack.pack_Run_Data_Cash;

/**
 * Created by SONG on 2017/12/12 10:17.
 */

import PDChartsPack.pack_pd_charts.TheOtherParameter;


/**
 * HR1300s一个周期内有60个相位的数据
 */
public class P_Data {
    /**
     * 一个周期内的原始数据
     */
    byte[] Data = new byte[TheOtherParameter.PHASE_COUNT];

    /**
     * 构造函数
     * @param data 一个周周期内的数据
     */
    public P_Data(byte[] data) {
        int temp = data.length>=TheOtherParameter.PHASE_COUNT?TheOtherParameter.PHASE_COUNT:data.length;
       System.arraycopy(data,0,Data,0,temp);
    }

    /**
     * 获取一个周期内的数据
     * @return
     */
    public byte[] getData() {
        return Data;
    }

    /**
     * 设置一个周期内的数据
     * @param data
     */
    public void setData(byte[] data) {
        int temp = data.length>=TheOtherParameter.PHASE_COUNT?TheOtherParameter.PHASE_COUNT:data.length;
        System.arraycopy(data,0,Data,0,temp);
    }
}
