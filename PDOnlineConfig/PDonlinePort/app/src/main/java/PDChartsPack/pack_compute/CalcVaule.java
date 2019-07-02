package PDChartsPack.pack_compute;


import java.util.List;

import PDChartsPack.pack_Run_Data_Cash.P_Data;

/**
 *
 */
public class CalcVaule {

    private final static int NUM_CYCLE_OF_SECOND = 50;
    private final static int NUM_PHASE_OF_CYCLE = 60;
    private final static int NUM_DIGTAL_OFAMP = 240;


    //50个周期每个周期中的最大值
    private static short maxCycle[] = new short[NUM_CYCLE_OF_SECOND];
    //50个周期中最大幅值
    private static short maxAmp = 0;
    //50个周期中最小幅值
    private static short minAmp = 0;
    //60个相位单元最大值
    private static short maxPhase[] = new short[NUM_PHASE_OF_CYCLE];
    //有效幅值：大于0的幅值
    //每个相位单元的有效幅值的累加值
    private static int accPhaseAmp[] = new int[NUM_PHASE_OF_CYCLE];
    //每个相位单元的有效幅值的次数
    private static int accPhaseCnt[] = new int[NUM_PHASE_OF_CYCLE];
    //每个相位单元的有效幅值平均值
    private static int avgPhaseAmp[] = new int[NUM_PHASE_OF_CYCLE];
    //所有有效幅值的累加值
    private static long accTotalCycleAmp = 0;
    //所有有效幅值的个数
    private static int cntValidAmp = 0;
    //放电次数（大于数据阈值）
    private static int cntDis = 0;

    private static int uTmp[] = new int[NUM_PHASE_OF_CYCLE];

    private static byte bardata[] = new byte[NUM_CYCLE_OF_SECOND*NUM_PHASE_OF_CYCLE];

    public   static float getF_F1() {
        return f_F1;
    }

    public static float getF_F2() {
        return f_F2;
    }

    private static float f_F1,f_F2;

    private  static void do_CycleData(byte[] rc_data, int Threshold) {
        /**数值清空**/
        for (int i = 0; i < NUM_PHASE_OF_CYCLE; i++) {
            if(i<NUM_CYCLE_OF_SECOND) {
                maxCycle[i] = 0;
            }
            maxPhase[i] = 0;
            accPhaseAmp[i] = 0;
            accPhaseCnt[i] = 0;
            avgPhaseAmp[i] = 0;
        }
        maxAmp = 0;
        minAmp = 0;
        cntDis = 0;
        accTotalCycleAmp = 0;
        cntValidAmp = 0;
        int offset;
        short val;
        for (int i = 0; i < NUM_CYCLE_OF_SECOND; i++) {

            for (int j = 0; j < NUM_PHASE_OF_CYCLE; j++) {

                offset = i * NUM_PHASE_OF_CYCLE + j;
                val = (short) (rc_data[offset] & 0xff);

                if (val > 0) {
                    if (val > maxCycle[i]) maxCycle[i] = val;
                    if (val > maxAmp) maxAmp = val;
                    if (val < minAmp) minAmp = val;
                    if (val > maxPhase[j]) maxPhase[j] = val;

                    if (val > Threshold) {
                        accPhaseAmp[j] += val;
                        accPhaseCnt[j]++;

                        accTotalCycleAmp += val;
                        cntValidAmp++;

                        cntDis++;
                    }
                }
            }
        }

        for (int i = 0; i < NUM_CYCLE_OF_SECOND; i++) {
            if (0 != accPhaseCnt[i]) {
                avgPhaseAmp[i] = (int) (accPhaseAmp[i] / (float) accPhaseCnt[i]);
            }
        }

    }

    /**
     * 计算Qp
     *
     * @return
     */
    public  static float Calc_Qp() {
        return maxAmp / 3.0f;
    }

    /***
     * 计算Qm
     * @return
     */
    private static float Calc_Qm1() {
        float Qm = 0;
        int N = 0;
        for (int i = 0; i < NUM_CYCLE_OF_SECOND; i++) {
            if (maxCycle[i] != 0) {
                Qm += maxCycle[i];
                N++;
            }
        }

        if (0 == N) return 0;
        else return (Qm / (float) N);
    }

    /***
     * 计算Qm
     */
    public  static float Calc_Qm() {
        return Calc_Qm1() / 3.0f;
    }

    /**
     * 获取中心线位置
     *
     * @return
     */
    private static int cal_CenterLine() {
        int i;
        int uMax = accPhaseAmp[0];
        int iPos = 0;

        for (i = 1; i < NUM_PHASE_OF_CYCLE; i++) {
            if (accPhaseAmp[i] > uMax) {
                uMax = accPhaseAmp[i];
                iPos = i;
            }
        }

        iPos += NUM_PHASE_OF_CYCLE / 3;
        iPos %= NUM_PHASE_OF_CYCLE;

        return iPos;
    }

    /**
     * 计算F1,F2
     *
     * @return
     */
    private static boolean cal_F12() {
        int HALF_PHASE_NUM = NUM_PHASE_OF_CYCLE >> 1;

        float A, B, K;
        int i, j;
        int CL = 0;

        f_F1 = 0;
        f_F2 = 0;

        //步骤1
        //若(最大-最小) < 3dB时则F1和F2为0,此时数据基本上是基底噪声
        if ((maxAmp - minAmp) <= 9) return false;

        //步骤2
        //计算60个相位单元的幅值大于基底噪声的累加量accPhaseAmp

        //步骤3
        //计算中心线位置
        CL = cal_CenterLine();

        //步骤4
        //计算修正系数K
        K = cal_K(4);
        if (0 == K) return false;

        //步骤5
        //计算中心线两边各4个最大相位单元放电峰值的平均值
        j = 0;
        for (i = CL; i < NUM_PHASE_OF_CYCLE; i++) uTmp[j++] = accPhaseAmp[i];
        for (i = 0; i < CL; i++) uTmp[j++] = accPhaseAmp[i];
        //计算中心线2边各4个最大相位单位能量值
        A = (float) get_MaxSum(uTmp, 0, 4, HALF_PHASE_NUM);
        B = (float) get_MaxSum(uTmp, HALF_PHASE_NUM, 4, HALF_PHASE_NUM);

        //步骤6
        //计算F1、F2：
        //F1=fabs(A-B)/(A+B)
        //F2=1-F1
        if ((A + B) != 0) {
            A = 100 * (float) fabs(A - B) / (A + B);
            B = 100 - A;
        }

        //步骤7
        //修正F1、F2：
        //F1 = K*F1；
        //F2 = K*F2；
        f_F1 = K * A;
        f_F2 = K * B;

        return true;
    }

    private static float fabs(float v) {
        if (v > 0) return v;
        else return -v;
    }

    private static long get_MaxSum(int[] data, int offset, int max_len, int data_len) {
        if (data == null) return 0;
        if (max_len > data_len) return 0;

        long sum = 0;
        int tmp = 0;
        int i, j;

        for (i = 0; i < max_len; i++) {
            for (j = i; j < data_len; j++) {
                if (data[i+offset] < data[j+offset]) {
                    tmp = data[i+offset];
                    data[i+offset] = data[j+offset];
                    data[j+offset] = tmp;
                }
            }
            sum += data[i+offset];
        }

        return sum;
    }

    private static float cal_K(int iHeapNum) {
        float Val[] = new float[10];

        if (iHeapNum > 10) iHeapNum = 10;

        int iWndNum = 5;
        int iWndLen = NUM_PHASE_OF_CYCLE / iHeapNum;
        int iStep = iWndLen / iWndNum;

        int i, j, m;
        int iStart = 0;
        float Max, MaxVal, MinVal;
        float K = 0, K0;
        int MaxPos, MinPos;

        //相位单元分为多窗口 找最小窗口和/最大窗口和的极值

        Max = 0;

        for (m = 0; m < iWndNum; m++) {
            //求堆值
            iStart = m * iStep;
            for (i = 0; i < iHeapNum; i++) {
                Val[i] = 0;
                for (j = 0; j < iWndLen; j++) {
                    Val[i] += accPhaseAmp[iStart % NUM_PHASE_OF_CYCLE];
                    iStart++;
                }
            }

            //找最大最小堆
            MaxVal = MinVal = Val[0];
            MaxPos = MinPos = 0;
            for (i = 1; i < iHeapNum; i++) {
                if (Val[i] > MaxVal) {
                    MaxVal = Val[i];
                    MaxPos = i;
                }
                if (Val[i] < MinVal) {
                    MinVal = Val[i];
                    MinPos = i;
                }
            }

            //最大值为0 没有数据 K=0 返回
            if (MaxVal == 0) return 0;

            K0 = 1 - MinVal / MaxVal;

            if (K0 > K) {
                K = K0;
            }
        }
        return K;
    }

    /**
     * 计算
     * @param rc_Data
     * @param T
 */
    public static void Calculate(List<P_Data> rc_Data, int T) {
        for(int i=0;i<50;i++)
        {
            System.arraycopy(rc_Data.get(i).getData(),0,bardata,i*60,60);
        }
        do_CycleData(bardata, T);
        cal_F12();

    }

}
