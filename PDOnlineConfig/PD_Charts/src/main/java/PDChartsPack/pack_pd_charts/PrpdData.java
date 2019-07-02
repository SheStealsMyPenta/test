package PDChartsPack.pack_pd_charts;

import java.util.List;

/**
 * Created by SONG on 2017/11/2 14:57.
 */

public class PrpdData {
    private List<Integer> values;
    private List<Integer> count;

    /**
     * 一个相位内的值和统计次数
     * @param values
     * @param count
     */
    public PrpdData(List<Integer> values, List<Integer> count)
    {
        this.values = values;
        this.count = count;
    }

    /**
     * 设置值
     * @param values
     */
    public void setValues(List<Integer> values)
    {
        this.values = values;
    }

    /**
     * 设置次数
     * @param count
     */
    public  void setCount(List<Integer> count)
    {
        this.count = count;
    }

    /**
     * 获取值
     * @return
     */
    public List<Integer> getValues()
    {
        return values;
    }

    /**
     * 获取次数
     * @return
     */
    public List<Integer> getCount()
    {
        return count;
    }

}
