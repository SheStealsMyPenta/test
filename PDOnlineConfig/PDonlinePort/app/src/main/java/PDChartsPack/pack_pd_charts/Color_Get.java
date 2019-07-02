package PDChartsPack.pack_pd_charts;

import android.graphics.Color;

/**
 * Created by SONG on 2017/12/28 16:45.
 * The final explanation right belongs to author
 */

public class Color_Get {

    private float m_nPerColorR;
    private float m_nPerColorG;
    private float m_nPerColorB;


    private int m_nStartColor;
    private int m_nEndColor;
    private float m_fPermonst;
    private int color1_r;
    private int color1_g;
    private int color1_b;

    private int[] m_bArrColor;

    private int[] Color_Bar = {
            Color.rgb(208, 208, 208),
            Color.rgb(192, 192, 192),
            Color.rgb(160, 160, 255),
            Color.rgb(128, 128, 255),
            Color.rgb(96, 0, 255),
            Color.rgb(176, 0, 255),
            Color.rgb(176, 0, 176),
            Color.rgb(239, 0, 0),
            Color.rgb(255, 16, 0),
            Color.rgb(255, 32, 0),
            Color.rgb(255, 64, 0),
            Color.rgb(255, 128, 0),
            Color.rgb(255, 160, 0),
            Color.rgb(255, 192, 0),
            Color.rgb(255, 224, 0),
            Color.rgb(255, 255, 0)
    };


    public Color_Get(int Start_Color, int End_Color, float per_number) {
        m_fPermonst = per_number;
        m_bArrColor = new int[(int) per_number];
        compute_Color();
    }

    private void compute_Color() {
        for (int i = 0; i < 15; i++) {
            if (i == 14) {
                i++;
                i--;
            }
            compute(Color_Bar[i], Color_Bar[i + 1], m_fPermonst / 15.0f);
            for (int j = 0; j <= ((int) m_fPermonst / 15); j++) {
                m_bArrColor[i * (int) m_fPermonst / 15 + j] = get(j);
            }

        }

    }

    private void compute(int Start_Color, int End_Color, float cnt) {
        int temp_color1 = Start_Color & 0xffffff;
        color1_r = temp_color1 & 0xff0000;
        color1_g = temp_color1 & 0x00ff00;
        color1_b = temp_color1 & 0x0000ff;

        int temp_color2 = End_Color & 0xffffff;
        int color2_R = temp_color2 & 0xff0000;
        int color2_G = temp_color2 & 0x00ff00;
        int color2_B = temp_color2 & 0x0000ff;

        m_nPerColorR = (color2_R - color1_r) / cnt;
        m_nPerColorG = (color2_G - color1_g) / cnt;
        m_nPerColorB = (color2_B - color1_b) / cnt;

    }


    private int get(int postion) {
        int temp_color;
        int temp_color_R;
        int temp_color_G;
        int temp_color_B;
        temp_color_R = ((int) (m_nPerColorR * (float) (postion)) & 0xff0000) + color1_r;
        temp_color_G = ((int) (m_nPerColorG * (float) (postion)) & 0x00ff00) + color1_g;
        temp_color_B = ((int) (m_nPerColorB * (float) (postion)) & 0x0000ff) + color1_b;
        temp_color = ((temp_color_G | temp_color_B | temp_color_R)) | 0xff000000;
        return temp_color;
    }

    /***
     * 获取颜色
     * @param postion 颜色位置
     * @return
     */

    public int getcolor(int postion) {
        if (postion >= m_bArrColor.length) {
            return m_bArrColor[m_bArrColor.length - 1];
        }
        return m_bArrColor[postion];
    }

}
