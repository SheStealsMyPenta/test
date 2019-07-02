package com.pdonlineport.Utils;


import android.content.Context;

import java.util.List;
import java.util.concurrent.locks.Lock;

import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.System_Config_Type;
import PDChartsPack.pack_compute.Compute_Information;


/***
 * 触发参考电平：0（0dB），560uV（5dB），1mV（10dB），1.8mV（15dB），3.2mV（20dB），5.6mV（25dB），10mV（30dB），18mV（35dB），32mV（40dB），56mV（45dB），100mV（50dB）
 * 触发参数时间常量：660us（2个相位单元），1ms（3个，默认值），3ms（9个），10ms（30个），30ms（90个）
 * 触发参考增益：1x，1.5x（默认值），2.0x，3.0x，4.0x，5.0x，10x
 * 脉冲开门时间：330us（1个相位单元，默认），660us（2个），1ms（3个）
 * 脉冲闭锁时间：1ms（3个相位单元），2ms（6个默认值），3ms（9个），5ms（15个），7ms（21个），10ms（30个），14ms（42个），18ms（56个），25ms（75个）
 */

/**
 * Created by SONG on 2018/3/28 9:00.
 * The final explanation right belongs to author
 */

public class Tof_AA_Compute {
    /**
     * 触发参考电平
     **/
    private static float B = 2.2f;
    /**
     * 触发参考增益
     **/
    private static float K = 1.5f;
    /**
     * 触发参考时间常量
     **/
    private static int T_LEN = 3;
    /**
     * 开锁时间
     */
    private static int UnLock = 1;
    /**
     * 闭锁时间
     */
    private static int Lock = 3;


    private static boolean USED = true;

    /**
     * 数组长度控制
     **/
    private final static int ARR_LEN = 600;

    /**
     * 数据长度控制
     **/
    private final static int DATA_LEN = 10;

    private static float[] Temp_B = new float[ARR_LEN];
    private static float[] Temp_A = new float[ARR_LEN];
    private static float[] Temp_C = new float[ARR_LEN];

    private static float Temp_Sum = 0;

    private static int m_nTail;
    private static int m_nTOF_B;
    private static int m_nTOF_K;
    private static int m_nTOF_TLEN;
    private static int m_nTOF_UNLOCK;
    private static int m_nTOF_LOCK;

    /**
     * 设置计算参数
     */
    public static void Set_Calc(Context context) {

        m_nTail = 0;
        m_nTOF_B = 3;
        m_nTOF_K = 1;
        m_nTOF_TLEN = 1;
        m_nTOF_UNLOCK = 0;

        USED = m_nTail == 0 ? true : false;
        B = Parse_B(m_nTOF_B);
        K = Parse_K(m_nTOF_K);
        T_LEN = Parse_TLen(m_nTOF_TLEN);
        UnLock = Parse_Unkock(m_nTOF_UNLOCK);
        Lock = Parse_Lock(m_nTOF_LOCK);
    }

    /**
     * Tof拖尾处理
     *
     * @param Rc_Data
     */
    public static void Tof_Calc(List<P_Data> Rc_Data) {
        /**赋值原始数据**/
        if (!USED) { //处理不处理数据
            return;
        }
        /*计算一个S的数据50和周期*60个相位*/
        if (Rc_Data.size() < DATA_LEN) {
            return;
        }
        for (int i = 0; i < DATA_LEN; i++) {
            for (int j = 0; j < 60; j++) {
                Temp_B[i * 60 + j] = (float) (((int) Rc_Data.get(i).getData()[j]) & 0xff) / 3.0f;
                Temp_A[i * 60 + j] = Compute_Information.dBuV_2_uV(((int) Rc_Data.get(i).getData()[j] & 0xff) / 3.0f);
            }
        }
        /**清空缓冲数组**/
        for (int i = 0; i < ARR_LEN; i++) {
            Temp_B[i] = 0;
        }

        /**平滑处理**/
        for (int i = 0; i <= ARR_LEN - T_LEN; i++) {
            Temp_Sum = 0;
            for (int j = i; j < i + T_LEN; j++) {
                Temp_Sum += Temp_A[j];
            }
            Temp_B[i + T_LEN - 1] = Temp_Sum / (float) T_LEN;

        }
        /**放大处理和增量处理**/
        for (int i = 0; i < ARR_LEN; i++) {
            Temp_B[i] *= K;
            Temp_B[i] += B; //B值转uV
        }
        /*清空参考时间以前未平滑的数据*/
        for (int i = 0; i < T_LEN - 1; i++) {
            Temp_A[i] = Temp_A[T_LEN - 1];
            Temp_B[i] = Temp_B[T_LEN - 1];
        }

        for (int i = 0; i < ARR_LEN; i++) {
            Temp_C[i] = Temp_A[i] >= Temp_B[i] ? Temp_A[i] : 0;
        }

        /**根据开门时间和闭锁时间处理**/
        for (int i = 0; i < ARR_LEN; ) {
            if (Temp_C[i] != 0) {
                /*找到峰值跳过开锁时间*/
                i += UnLock;
                /*末尾处理*/
                if (i >= ARR_LEN) {
                    break;//开锁时间内越界,直接跳出
                }

                if ((i + Lock) >= ARR_LEN) {
                    //闭锁时间内越界,清空剩余数据
                    for (int j = i; j < ARR_LEN; j++) {
                        Temp_C[j] = 0;
                    }
                    break;
                }

                /*清空闭锁时间内的值*/
                for (int j = i; j < i + Lock; j++) {
                    Temp_C[j] = 0;
                }
                /*跳过闭锁时间*/
                i += Lock;
            } else {
                i++;
            }
        }

        /**重新幅值**/
        for (int i = 0; i < ARR_LEN; i++) {
            if (Temp_C[i] == 0) {
                Rc_Data.get(i / 60).getData()[i % 60] = 0;
            }
        }

    }

    /**
     * B值解析
     *
     * @param select
     * @return
     */
    private static float Parse_B(int select) {
        switch (select) {
            case 0:
                return Compute_Information.dBuV_2_uV(0);
            case 1:
                return Compute_Information.dBuV_2_uV(5);
            case 2:
                return Compute_Information.dBuV_2_uV(10);
            case 3:
                return Compute_Information.dBuV_2_uV(15);
            case 4:
                return Compute_Information.dBuV_2_uV(20);
            case 5:
                return Compute_Information.dBuV_2_uV(25);
            case 6:
                return Compute_Information.dBuV_2_uV(30);
            case 7:
                return Compute_Information.dBuV_2_uV(35);
            case 8:
                return Compute_Information.dBuV_2_uV(40);
            case 9:
                return Compute_Information.dBuV_2_uV(45);
            case 10:
                return Compute_Information.dBuV_2_uV(50);
            default:
                return Compute_Information.dBuV_2_uV(0);
        }
    }

    /**
     * 解析参考时间
     *
     * @param select
     * @return
     */
    private static int Parse_TLen(int select) {
        switch (select) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 9;
            case 3:
                return 30;
            case 4:
                return 90;
            default:
                return 2;
        }
    }

    /**
     * 解析参考增益
     *
     * @param select
     * @return
     */
    private static float Parse_K(int select) {
        switch (select) {
            case 0:
                return 1.0f;
            case 1:
                return 1.5f;
            case 2:
                return 2.0f;
            case 3:
                return 3.0f;
            case 4:
                return 4.0f;
            case 5:
                return 5.0f;
            case 6:
                return 10.0f;
            default:
                return 1.0f;

        }
    }

    /**
     * 解析闭锁时间
     *
     * @param select
     * @return
     */
    private static int Parse_Lock(int select) {
        switch (select) {
            case 0:
                return 3;
            case 1:
                return 6;
            case 2:
                return 9;
            case 3:
                return 15;
            case 4:
                return 21;
            case 5:
                return 30;
            case 6:
                return 42;
            case 7:
                return 56;
            case 8:
                return 75;
            default:
                return 1;

        }
    }

    private static int Parse_Unkock(int select) {
        switch (select) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            default:
                return 1;
        }
    }

}
