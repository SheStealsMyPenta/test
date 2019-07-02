package PDChartsPack.pack_pd_charts;

/**
 * Created by SONG on 2017/12/11 16:19.
 */

public class PrpsEvent {
    private int SHOW_TAB = 0;
    private int PHASE = 0;
    private int THRESHOLD = 0;

    /**
     * 构造函数
     * @param tab
     * @param phase
     * @param threshold
     */
    public PrpsEvent(int tab,int phase,int threshold)
    {
        SHOW_TAB = tab;
        PHASE = phase;
        THRESHOLD = threshold;
    }

    /**
     * 获取显示量程
     * @return
     */
    public int getSHOW_TAB() {
        return SHOW_TAB;
    }

    /**
     * 设置显示量程
     * @param SHOW_TAB
     */
    public void setSHOW_TAB(int SHOW_TAB) {
        this.SHOW_TAB = SHOW_TAB;
    }

    /**
     * 获取偏移相位
     * @return
     */
    public int getPHASE() {
        return PHASE;
    }

    /**
     * 设置偏移相位
     * @param PHASE
     */
    public void setPHASE(int PHASE) {
        this.PHASE = PHASE;
    }

    /**
     * 获取显示阈值
     * @return
     */
    public int getTHRESHOLD() {
        return THRESHOLD;
    }

    /**
     * 设置偏移阈值
     * @param THRESHOLD
     */
    public void setTHRESHOLD(int THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }
}
