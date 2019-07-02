package PDChartsPack.pack_pd_charts;

/**
 * Created by SONG on 2017/11/8 11:07.
 */

public class PrpsData {
    private byte[] PeriodData = new byte[60];

    /**
     *构造函数
     * @param Data 一个周期里面的所有幅值数据
     */
    public PrpsData(byte[] Data)
    {
        int temp = Data.length>=60?60:Data.length;
        System.arraycopy(Data,0,PeriodData,0,temp);
    }
    /**
     * 设置方法
     * @param Data 一个周期里面的所有幅值数据
     */
    public void SetPeriodData( byte[] Data)
    {
        PeriodData = Data;
    }
    /**
     * 获取数据
     * @return 一个周期里面的所有幅值数据
     */
    public  byte[] GetPeriodData()
    {
        return PeriodData;
    }





}
