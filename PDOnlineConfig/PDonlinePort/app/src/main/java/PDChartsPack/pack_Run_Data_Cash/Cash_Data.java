package PDChartsPack.pack_Run_Data_Cash;



import PDChartsPack.pack_commen.Convert_Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by SONG on 2017/12/12 10:41.
 */

public class Cash_Data {
    private List<Data_Bean> Cash_D = new ArrayList<Data_Bean>();
    public int Time_Length = 1;
    /**
     * 数据长度为120s
     */
    private int PWM_Cnt;
    private int Data_Len = 120;
    private byte Data_Type;
    private float m_fSyncFreq = 50.0f;
    private byte m_fSyncFreqtype = 0x00;
    private byte[] m_bArrTestTime = new byte[8];
    private static final String pattern = "yyyyMMdd_HHmmss";//时间格式
    private SimpleDateFormat dateFormat;
    private String SDate = null;
    public  Felter_Bean m_objFelter;



    public String gettesttime() {
        return SDate.substring(0, 8);
    }

    public byte[] getM_bArrTestTime() {
        return this.m_bArrTestTime;
    }

    /**
     * 设置时间间隔长度
     * 默认长度为1
     *
     * @param length
     */
    public void setTimeLength(int length) {
        this.Time_Length = length;
    }

    public void savetestdate() {
        Date l_data = new Date();
        long l_lnms = l_data.getTime();

        l_lnms -= (getDatasize() * 1000);
        l_data.setTime(l_lnms);


        dateFormat = new SimpleDateFormat(pattern);
        SDate = dateFormat.format(l_data);

        short year = (short) Integer.parseInt(SDate.substring(0, 4));
        System.arraycopy(Convert_Tools.short2bits(year), 0, m_bArrTestTime, 0, 2);
        m_bArrTestTime[2] = (byte) Integer.parseInt(SDate.substring(4, 6));
        m_bArrTestTime[3] = (byte) Integer.parseInt(SDate.substring(6, 8));
        m_bArrTestTime[4] = (byte) Integer.parseInt(SDate.substring(9, 11));
        m_bArrTestTime[5] = (byte) Integer.parseInt(SDate.substring(11, 13));
        m_bArrTestTime[6] = (byte) Integer.parseInt(SDate.substring(13, 15));
        m_bArrTestTime[7] = (byte) 0x00;

    }

    public float getM_fSyncFreq() {
        return m_fSyncFreq;
    }

    public void setM_fSyncFreq(float m_fSyncFreq) {
        this.m_fSyncFreq = m_fSyncFreq;
    }

    public byte getM_fSyncFreqtype() {
        return m_fSyncFreqtype;
    }

    public void setM_fSyncFreqtype(byte m_fSyncFreqtype) {
        this.m_fSyncFreqtype = m_fSyncFreqtype;
    }

    public int getPWM_Cnt() {

        return PWM_Cnt;
    }

    public void setPWM_Cnt(int PWM_Cnt) {
        this.PWM_Cnt = PWM_Cnt;
    }


    /*数据类型*/
    public Cash_Data(byte Data_Type, int Time_Len) {

        this.Data_Type = Data_Type;
        this.Time_Length = Time_Len;

    }



    /*数据类型*/
    public Cash_Data() {

        this.Data_Type = Data_Type;

    }



    /**
     * 末尾追加数据
     */
    public void Append_Data(Data_Bean data,Felter_Bean felter_bean) {

        m_objFelter = felter_bean;
        m_fSyncFreq = data.getSync_Freq();
        /*front*/
        if (Cash_D.size() >= Data_Len) {
            Cash_D.remove(0);
        }
        /*rear*/
        Cash_D.add(data);
    }

    /**
     * 获取缓存数据中的某一项
     *
     * @param location 数据下标
     * @return 有数据则返回，无数据则返回null
     */
    public Data_Bean getitem(int location) {
        if (location <= (Cash_D.size() - 1)) {
            return Cash_D.get(location);
        }
        return null;
    }


    /**
     * 获取数据总长度
     *
     * @return
     */
    public int getDatasize() {
        return Cash_D.size();
    }


    public byte gettype() {
        return Data_Type;
    }

}
