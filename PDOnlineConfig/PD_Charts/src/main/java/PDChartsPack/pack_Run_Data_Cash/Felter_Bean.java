package PDChartsPack.pack_Run_Data_Cash;

/**
 * Created by SONG on 2018/3/12 9:45.
 * The final explanation right belongs to author
 */

public class Felter_Bean {
    private byte m_Btype;
    private float m_fMax_Freq;
    private float m_fMin_Freq;
    private byte m_bSync = 0x00;

    public Felter_Bean(byte type, float max_freq, float min_fFreq,byte Sync_Type) {
        this.m_Btype = type;
        this.m_fMax_Freq = max_freq;
        this.m_fMin_Freq = min_fFreq;
        m_bSync = Sync_Type;
    }

    public byte getM_Btype() {
        return m_Btype;
    }

    public byte getM_bSync() {
        return m_bSync;
    }

    public float getM_fMax_Freq() {
        return m_fMax_Freq;
    }

    public float getM_fMin_Freq() {
        return m_fMin_Freq;
    }
}
