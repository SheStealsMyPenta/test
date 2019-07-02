package com.pdonlineport.Utils;

import PDChartsPack.pack_commen.Convert_Tools;
import PDChartsPack.pack_pd_charts.ContentView;

/**
 * Created by SONG on 2019/4/29 15:13.
 * The final explanation right belongs to author
 */
public class AA_Datas {
    /**
     * 数据标志
     */
    private int m_iDataFlag;
    /**
     * 数据类型
     */
    private short m_iDataType;
    /**
     * 数据版本
     */
    private byte[] m_DataVersion = new byte[2];
    /**
     * 数据长度
     */
    private int m_iDataLen;

    /**
     * 峰值
     */
    private float m_fVpp;
    /**
     * 均值
     */
    private float m_fRMS;
    /**
     * F1
     */
    private float m_fF1;

    /**
     * F2
     */
    private float m_fF2;

    /**
     * 幅值上限
     */
    private byte m_AmpMax;
    /**
     * 幅值下限
     */

    private byte m_AmpMin;
    /**
     * 相位窗数
     */

    private byte m_WindowCnt;

    /**
     * 量化幅值
     */
    private byte m_AmpVaule;


    /**
     * prpd
     */
    private short[] m_PrpdDatas = new short[60 * 80];


    /**
     * TOF点数
     **/
    private short m_sTofPointsNum;

    /**
     * TOF图谱
     **/
    private byte[] m_TofDatas = new byte[80 * 200];

    public float getM_fVpp() {
        return m_fVpp;
    }

    public float getM_fRMS() {
        return m_fRMS;
    }

    public float getM_fF1() {
        return m_fF1;
    }

    public float getM_fF2() {
        return m_fF2;
    }

    public short[] getM_PrpdDatas() {
        return m_PrpdDatas;
    }

    public byte[] getM_TofDatas() {
        return m_TofDatas;
    }

    public boolean ParseData(byte[] src) {
        if (src.length <= 0) {
            return false;
        }

        int temp = 0;

        m_iDataFlag = Convert_Tools.toint(Convert_Tools.Sub_Arry(src, 0, 4));

        m_iDataType = Convert_Tools.toshort(Convert_Tools.Sub_Arry(src, 4, 2));

        System.arraycopy(src, 6, m_DataVersion, 0, 2);

        m_iDataLen = Convert_Tools.toint(Convert_Tools.Sub_Arry(src, 8, 4));

        m_fVpp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(src, 12, 4));

        m_fRMS = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(src, 16, 4));

        m_fF1 = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(src, 20, 4));

        m_fF2 = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(src, 24, 4));


        m_AmpMax = src[28];

        m_AmpMin = src[29];

        m_WindowCnt = src[30];

        m_AmpVaule = src[31];

        /**PRPD**/
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 80; i++) {
                temp = 32 + (i * 80 + j) * 2;
                m_PrpdDatas[i * 80 + j] = Convert_Tools.toshort(Convert_Tools.Sub_Arry(src, temp, 2));
            }
        }

        /**TOF点数**/

        temp = 32 + (60 * 80) * 2;

        m_sTofPointsNum = Convert_Tools.toshort(Convert_Tools.Sub_Arry(src, temp, 2));

        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 80; j++) {
                m_TofDatas[i * 80 + j] = 0;
            }
        }

        int offset = temp + 2;

        int X, Y;

        for (int i = 0; i < m_sTofPointsNum; i++) {
            if ((offset + i * 2 + 1) >= src.length) {
                return false;
            }

            X = src[offset + i];
            Y = src[offset + i * 2 + 1];

            if(X>=200||Y>=80)
            {
                continue;
            }

            m_TofDatas[X*200+Y] = 1;

        }


        return true;
    }

}
