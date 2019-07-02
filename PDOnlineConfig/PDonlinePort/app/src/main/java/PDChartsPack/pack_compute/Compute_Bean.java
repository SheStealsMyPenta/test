package PDChartsPack.pack_compute;

/**
 * Created by SONG on 2018/1/8,12:39.
 * The final explanation right belongs to author
 */

public class Compute_Bean {

    private int m_fPWMCnt;
    private float m_fSyncFreq;
    private float m_fShowTreshold;
    private boolean m_bIstevused;

    public Compute_Bean(int PWMCnt, float SyncFreq, float ShowThreshold, byte isused) {
        m_fShowTreshold = ShowThreshold;
        m_fSyncFreq = SyncFreq;
        m_fPWMCnt = PWMCnt;
        m_bIstevused = (isused == 0) ? false : true;

    }

    public int getM_fPWMCnt() {
        return m_fPWMCnt;
    }

    public float getM_fSyncFreq() {
        return m_fSyncFreq;
    }

    public float getM_fShowTreshold() {
        return m_fShowTreshold;
    }

    public boolean isM_bIstevused() {
        return m_bIstevused;
    }
}
