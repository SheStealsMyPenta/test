package com.pdonlineport.Utils;

import PDChartsPack.pack_commen.Convert_Tools;

/**
 * Created by SONG on 2019/5/2 11:47.
 * The final explanation right belongs to author
 */
public class UHF_Datas {

    private int m_iDataFlag;

    private short m_DataType;

    private byte[] m_DataVersion = new byte[2];

    private int m_iDataLen;

    private int m_Bound;


    private float m_fQp;


    private byte m_AmpMax;

    private byte m_AmpMin;

    private byte m_WindowCnt;

    private byte m_PrpsPeriodCnt;

    private byte m_AmpValue;

    private byte[] m_PrpsDataS = new byte[50 * 60];

    private short[] m_PrpdDatas = new short[60 * 80];

    public float getM_fQp() {
        return m_fQp;
    }

    public byte[] getM_PrpsDataS() {
        return m_PrpsDataS;
    }

    public short[] getM_PrpdDatas() {
        return m_PrpdDatas;
    }

    public boolean ParseData(byte[] src) {

        if (src.length <= 0) {
            return false;
        }

        int temp = 0;

        m_iDataFlag = Convert_Tools.toint(Convert_Tools.Sub_Arry(src, 0, 4));

        m_DataType = Convert_Tools.toshort(Convert_Tools.Sub_Arry(src, 4, 2));

        System.arraycopy(src, 6, m_DataVersion, 0, 2);

        m_iDataLen = Convert_Tools.toint(Convert_Tools.Sub_Arry(src, 8, 4));

        m_Bound = src[12];

        m_fQp = Convert_Tools.tofloat(Convert_Tools.Sub_Arry(src, 13, 4));

        m_AmpMax = src[17];

        m_AmpMin = src[18];

        m_WindowCnt = src[19];

        m_AmpValue = src[20];

        m_PrpsPeriodCnt = src[21];

        System.arraycopy(src, 22, m_PrpsDataS, 0, 50 * 60);

        /**PRPD**/
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 80; i++) {
                temp = 50 * 60 + 22 + (i * 80 + j) * 2;
                m_PrpdDatas[i * 80 + j] = Convert_Tools.toshort(Convert_Tools.Sub_Arry(src, temp, 2));
            }
        }

        return true;
    }


}
