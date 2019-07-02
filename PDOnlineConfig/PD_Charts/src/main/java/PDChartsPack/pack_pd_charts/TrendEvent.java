package PDChartsPack.pack_pd_charts;

/**
 * Created by SONG on 2017/11/13 10:14.
 */

public class TrendEvent {
    /*趋势图触摸事件事件内容*/

    private int StartTime;
    private int EndTime;

    /**
     * 构造方法
     * @param StartTime 统计开始时间
     * @param EndTime 统计结束时间
     */
    public  TrendEvent(int StartTime,int EndTime)
    {
        this.StartTime = StartTime;
        this.EndTime = EndTime;
    }

    /**
     * 获取统计开始时间
     * @return 统计开始时间
     */
    public int getStartTime()
    {
        return StartTime;

    }

    /**
     * 获取统计结束时间
     * @return 统计结束时间
     */
    public int  getEndTime()
    {
        return EndTime;
    }

    /**
     * 获取统计时长
     * @return 统计时长
     */
    public  int getTimeLength()
    {
        return EndTime - StartTime;
    }
}
