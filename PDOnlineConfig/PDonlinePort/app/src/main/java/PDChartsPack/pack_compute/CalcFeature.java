/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PDChartsPack.pack_compute;

/**
 * @author hr018
 */
public class CalcFeature {
    /*
     * static variable for define data size
     */
    public static int MAX_AMP_COUNT = 60;
    public static int HALF_AMP_COUNT = MAX_AMP_COUNT / 2;
    public static int PERIOD_COUNT = 50;
    public static int MAX_DATA_VALUE = 80;
    public static float DATA_PROPORTION = 3.0f;

    public static float s_arExtVal[] = new float[MAX_AMP_COUNT / 2];
    public static float s_arExtData[] = new float[MAX_AMP_COUNT];
    public static float s_arExtTemp[] = new float[MAX_AMP_COUNT];

    /*
     * variables for calculate
     */
    private FeatureData theFeature;
    private float fLastPulse;
    private float fCurPulse;

    /*
     * 50 periods data
     */
    private int nCurPeriodCnt;
    private byte[] arPeriodData;

    private byte[] arMaxPh;
    private byte[] arMaxPeriod;
    private byte[] arDischargeCnt;

    /*
     * variables for calculate
     */
    private float fShowThred;
    private float fFrequency;

    /*
     * static methods for calculate
     */
    public static float calcK(float data[], int nWndNum) {
        int i, j, m;
        int nOffset;

        int nWindowNum, dx, ddx, offsetnum;
        float fKVal0, fKVal1, fMin, fMax;
        if (data.length <= 0) {
            return 0.0f;
        }

        nWindowNum = 5;
        dx = nWndNum;
        ddx = MAX_AMP_COUNT / nWndNum;
        offsetnum = ddx / nWindowNum;

        /* find the limit value of 'sum(min-window)/sum(max-window)' */
        fKVal1 = 0.0f;
        for (m = 0; m < nWindowNum; m++) {
            nOffset = offsetnum * m;
            for (i = 0; i < dx; i++) {
                s_arExtVal[i] = 0.0f;
                for (j = 0; j < ddx; j++) {
                    s_arExtVal[i] += data[(j + nOffset) % MAX_AMP_COUNT];
                }

                nOffset += ddx;
            }

            fMin = fMax = s_arExtVal[0];
            for (i = 1; i < dx; i++) {
                if (fMin > s_arExtVal[i]) {
                    fMin = s_arExtVal[i];
                }

                if (fMax < s_arExtVal[i]) {
                    fMax = s_arExtVal[i];
                }
            }

            if ((fMax > -0.000001) && (fMax < 0.000001)) {
                /* max value is 0.0f */
                return 0.0f;
            }

            fKVal0 = fMax - fMin;
            if (fKVal0 < 100.0f) {
                if ((fMin > -0.000001) && (fMin < 0.000001)) {
                    /* min value is 0.0f */
                    fKVal0 *= 0.01f;
                } else {
                    fKVal0 = 0.0f;
                }
            } else if (fKVal0 < 500.0f) {
                fKVal0 = (1.0f - fMin / fMax) * (fKVal0 / 500.0f);
            } else {
                fKVal0 = 1.0f - fMin / fMax;
            }

            if (fKVal0 > fKVal1) {
                fKVal1 = fKVal0;
            }
        }

        return fKVal1;
    }

    public static float calcMaxSum(float data[], int nMaxLen, int offset, int nLen) {
        int i, j, nDataLen;
        float fSum, fTmp;
        nDataLen = data.length - offset;
        if ((nMaxLen > nDataLen) || (nDataLen <= 0) || (nDataLen < nLen)) {
            return 0.0f;
        }

        fSum = fTmp = 0.0f;
        for (i = 0; i < nMaxLen; i++) {
            for (j = i; j < nLen; j++) {
                if (data[i + offset] < data[j + offset]) {
                    fTmp = data[i + offset];
                    data[i + offset] = data[j + offset];
                    data[j + offset] = fTmp;
                }
            }

            fSum += data[i + offset];
        }

        return fSum;
    }

    public static byte getMaxUnsignedByte(byte data1, byte data2) {
        int val1, val2, val;

        val1 = ((data1 < 0) ? ((int) data1 + 256) : data1);
        val2 = ((data2 < 0) ? ((int) data2 + 256) : data2);
        val = ((val1 > val2) ? val1 : val2);
        return (byte) (val & 0xFF);
    }

    public static void sortUnsignedByte(byte data[]) {
        int val1, val2, i, j, cnt;
        cnt = data.length;
        if (cnt <= 1) {
            return;
        }

        for (i = 0; i < cnt; i++) {
            for (j = i + 1; j < cnt; j++) {
                val1 = ((data[i] < 0) ? ((int) data[i] + 256) : data[i]);
                val2 = ((data[j] < 0) ? ((int) data[j] + 256) : data[j]);
                if (val2 > val1) {
                    data[j] = data[i];
                    data[i] = (byte) (val2 & 0xFF);
                }
            }
        }
    }

    /*
     * constructor
     */
    public CalcFeature() {
        int i, cnt;
        theFeature = new FeatureData();
        fLastPulse = 0.0f;
        fCurPulse = 0.0f;

        nCurPeriodCnt = 0;
        cnt = PERIOD_COUNT * MAX_AMP_COUNT;
        arPeriodData = new byte[cnt];
        for (i = 0; i < cnt; i++) {
            arPeriodData[i] = 0;
        }

        arMaxPh = new byte[MAX_AMP_COUNT];
        for (i = 0; i < MAX_AMP_COUNT; i++) {
            arMaxPh[i] = 0;
        }

        arMaxPeriod = new byte[PERIOD_COUNT];
        for (i = 0; i < PERIOD_COUNT; i++) {
            arMaxPeriod[i] = 0;
        }

        cnt = MAX_AMP_COUNT * MAX_DATA_VALUE;
        arDischargeCnt = new byte[cnt];
        for (i = 0; i < cnt; i++) {
            arDischargeCnt[i] = 0;
        }

        fShowThred = 0.0f;
        fFrequency = 50.0f;
    }

    /*
     * clearData
     * clear all data
     */
    public void clearData() {
        int i, cnt;

        theFeature.setQp(0.0f);
        theFeature.setQm(0.0f);
        theFeature.setF1(0.0f);
        theFeature.setF2(0.0f);

        theFeature.setTevPeriodPulse(0.0f);
        theFeature.setTev2sPulse(0.0f);
        theFeature.setTevDischarge(0.0f);

        fLastPulse = 0.0f;
        fCurPulse = 0.0f;

        /* period data */
        nCurPeriodCnt = 0;
        cnt = PERIOD_COUNT * MAX_AMP_COUNT;
        if (arPeriodData.length < cnt) {
            arPeriodData = new byte[cnt];
        }

        for (i = 0; i < cnt; i++) {
            arPeriodData[i] = 0;
        }

        /* max-ph */
        if (arMaxPh.length < MAX_AMP_COUNT) {
            arMaxPh = new byte[MAX_AMP_COUNT];
        }

        for (i = 0; i < MAX_AMP_COUNT; i++) {
            arMaxPh[i] = 0;
        }

        /* max period */
        if (arMaxPeriod.length < PERIOD_COUNT) {
            arMaxPeriod = new byte[PERIOD_COUNT];
        }

        for (i = 0; i < PERIOD_COUNT; i++) {
            arMaxPeriod[i] = 0;
        }

        /* discharge count */
        cnt = MAX_AMP_COUNT * MAX_DATA_VALUE;
        if (arDischargeCnt.length < cnt) {
            arDischargeCnt = new byte[cnt];
        }

        for (i = 0; i < cnt; i++) {
            arDischargeCnt[i] = 0;
        }
    }

    /*
     * setCalcParam
     * set calculate parameters
     */
    public void setCalcParam(float fThreashold, float fFreq) {
        fShowThred = fThreashold;
        fFrequency = fFreq;
    }

    /*
     * getter
     */
    public FeatureData getFeature() {
        return theFeature;
    }

    /*
     * addPeriodData
     * add data for only one period
     */
    public void addPeriodData(byte arPeriod[], boolean bTevValid, int nCurTevPulse) {
        int i, cnt, pos;
        if (nCurPeriodCnt >= PERIOD_COUNT) {
            return;
        }

        /*
         * set period data
         * step 1: set valid data
         */
        cnt = arPeriod.length;
        if (cnt > MAX_AMP_COUNT) {
            cnt = MAX_AMP_COUNT;
        }

        pos = nCurPeriodCnt * MAX_AMP_COUNT;
        i = 0;
        for (; i < cnt; i++) {
            arPeriodData[pos + i] = arPeriod[i];
        }

        /* step 2: set 0 to invalid position */
        for (; i < MAX_AMP_COUNT; i++) {
            arPeriodData[pos + i] = 0;
        }

        /* step 3: increment period count and calculate features */
        nCurPeriodCnt++;
        if (nCurPeriodCnt >= PERIOD_COUNT) {
            nCurPeriodCnt = 0;
            doCalcFeature();
        }

        /*
         * set TEV parameters
         */
        if (bTevValid) {
            fLastPulse = fCurPulse;
            fCurPulse = (float) nCurTevPulse;
            doCalcTevFeature();
        }
    }

    /*
     * doCalcFeature
     * calculate normal features (exclude TEV-FEATUREs)
     */
    private void doCalcFeature() {
        int i, j, n, pos;
        byte byAmpVal;
        float fValue;

        /* clear buffer */
        pos = MAX_AMP_COUNT * MAX_DATA_VALUE;
        for (i = 0; i < pos; i++) {
            arDischargeCnt[i] = 0;
        }

        for (i = 0; i < PERIOD_COUNT; i++) {
            arMaxPeriod[i] = 0;
        }

        /* set data to buffer */
        for (i = 0; i < MAX_AMP_COUNT; i++) {
            arMaxPh[i] = 0;
            for (j = 0; j < PERIOD_COUNT; j++) {
                pos = MAX_AMP_COUNT * j + i;
                byAmpVal = arPeriodData[pos];
                arMaxPh[i] = getMaxUnsignedByte(arMaxPh[i], byAmpVal);
                arMaxPeriod[j] = getMaxUnsignedByte(arMaxPeriod[j], byAmpVal);

                n = ((byAmpVal < 0) ? ((int) byAmpVal + 256) : byAmpVal);
                fValue = (float) n / DATA_PROPORTION;
                n = (int) fValue;
                pos = MAX_DATA_VALUE * i + n;
                arDischargeCnt[pos]++;
            }
        }

        /* calculate Qp, Qm */
        byAmpVal = 0;
        j = 0;
        for (i = 0; i < PERIOD_COUNT; i++) {
            byAmpVal = getMaxUnsignedByte(byAmpVal, arMaxPeriod[i]);
            if (arMaxPeriod[i] != 0) {
                j++;
            }
        }

        n = ((byAmpVal < 0) ? ((int) byAmpVal + 256) : byAmpVal);
        fValue = (float) n / DATA_PROPORTION;
        theFeature.setQp(fValue);
        if (j == 0) {
            theFeature.setQm(0.0f);
        } else {
            sortUnsignedByte(arMaxPeriod);
            if (fFrequency >= 200.0f) {
                j = PERIOD_COUNT;
            } else {
                j = (int) (fFrequency / 4.0f + 1.0f);
            }

            pos = 0;
            fValue = 0.0f;
            for (i = 0; i < j; i++) {
                n = ((arMaxPeriod[i] < 0) ? ((int) arMaxPeriod[i] + 256) : arMaxPeriod[i]);
                if (n > 0) {
                    fValue += (float) n;
                    pos++;
                }
            }

            if (pos == 0) {
                fValue = 0.0f;
            } else {
                fValue = fValue / (float) pos / DATA_PROPORTION;
            }

            theFeature.setQm(fValue);
        }

        /* F1, F2 */
        doCalcF1F2();
    }

    /*
     * doCalcTevFeature
     * calculate TEV features
     */
    private void doCalcTevFeature() {
        float fValue;

        fValue = fCurPulse / fFrequency;
        theFeature.setTevPeriodPulse(fValue);
        theFeature.setTev2sPulse(fLastPulse + fCurPulse);

        fValue = fValue * theFeature.getQp();
        theFeature.setTevDischarge(fValue);
    }

    /*
     * doCalcF1F2
     * calcuate F1 and F2
     */
    private void doCalcF1F2() {
        int i, j, n;
        int nMin, nMax, nCL;
        float fVal, fExtK, fExtA, fExtB;

        theFeature.setF1(0.0f);
        theFeature.setF2(0.0f);
        nCL = 0;
        
        /* step 1: find max and min */
        n = ((arMaxPh[0] < 0) ? ((int) arMaxPh[0] + 256) : arMaxPh[0]);
        nMin = nMax = n;
        for (i = 1; i < MAX_AMP_COUNT; i++) {
            n = ((arMaxPh[i] < 0) ? ((int) arMaxPh[i] + 256) : arMaxPh[i]);
            if (nMin > n) {
                nMin = n;
            }

            if (nMax < n) {
                nMax = n;
            }
        }

        n = nMax - nMin;
        fVal = (float) n / DATA_PROPORTION;
        if (fVal <= 3.0f) {
            return;
        }

        /* step 2: calculate the sum of discharge (greater than base) */
        n = (int) fShowThred;
        for (i = 0; i < MAX_AMP_COUNT; i++) {
            s_arExtData[i] = 0.0f;
            for (j = n; j < MAX_DATA_VALUE; j++) {
                nMin = MAX_DATA_VALUE * i + j;
                if (arDischargeCnt[nMin] > 0) {
                    fVal = (float) arDischargeCnt[nMin] * (float) j;
                    s_arExtData[i] += fVal;
                }
            }
        }

        /* caluclate the center line and proportion */
        fVal = s_arExtData[0];
        j = 0;
        for (i = 1; i < MAX_AMP_COUNT; i++) {
            if (fVal < s_arExtData[i]) {
                fVal = s_arExtData[i];
                j = i;
            }
        }

        j = (j + MAX_AMP_COUNT / 3) % MAX_AMP_COUNT;
        nCL = j;
        fExtK = calcK(s_arExtData, 4);

        /* step 3: calculate average values of data that before and after center line */
        j = 0;
        for (i = nCL; i < MAX_AMP_COUNT; i++) {
            s_arExtTemp[j++] = s_arExtData[i];
        }

        for (i = 0; i < nCL; i++) {
            s_arExtTemp[j++] = s_arExtData[i];
        }

        fExtA = calcMaxSum(s_arExtTemp, 4, 0, HALF_AMP_COUNT);
        fExtB = calcMaxSum(s_arExtTemp, 4, HALF_AMP_COUNT, HALF_AMP_COUNT);

        /* step 4: caluclate F1, F2 (F1 = fabs(A - B) / (A + B); F2 = 1 - F1) */
        fVal = fExtA + fExtB;
        if ((fVal < -0.000001) || (fVal > 0.000001)) {
            fExtA = 100.0f * Math.abs(fExtA - fExtB) / (fExtA + fExtB);
            fExtB = 100.0f - fExtA;
        }

        /* step 5: calibration */
        fVal = fExtK * fExtA;
        theFeature.setF1(fVal);

        fVal = fExtK * fExtB;
        theFeature.setF2(fVal);
    }
}