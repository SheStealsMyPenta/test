package PDChartsPack.pack_pd_charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.System_Commen;
import PDChartsPack.pack_commen.System_Flag;
import PDChartsPack.pack_compute.Compute_Information;


/**
 * Created by SONG on 2017/11/6 11:53.
 */

public class PrpsView extends View {

    private boolean IS_AA_MODE = false;

    private boolean SHOW_TAB_CAN_TAUCHED = false;

    private int COLOR_1 = Color_set.ARROW_Color_1;
    private int COLOR_2 = Color_set.ARROW_Color_2;
    private int COLOR_3 = Color_set.ARROW_Color_3;


    private int SHOW_OFFSET = 0;

    private int PHASE = 0;
    private int THRESHOLD = 0;

    private int m_ntempPhase = 0;
    private int m_ntempShowTab = 0;
    private int m_ntempThreshold = 0;
    private float m_fViewAngle = 0.0f;


    private String Format = "dB";
    private PrpsListener listener = null;


    /**
     * 背景颜色
     */
    private int BKG_COLOR = Color_set.BKG_COLOR;
    /**
     * 栅格颜色
     */
    private int GRID_COLOR = Color_set.Grid_Color;
    /**
     * 字体颜色
     */
    private int TEXT_COLOR = Color_set.TEXT_COLOR;
    /**
     * 字体大小
     */
    private float TEXT_SIZE = 25;
    /**
     * 平面一颜色
     */
    private int COLOR_PLANE1 = Color_set.DATA_BKG_COLOR;
    /**
     * 平面二颜色
     */
    private int COLOR_PLANE2 = Color_set.DATA_BKG_COLOR;

    /**
     * 视图角度一
     */
    private float ViewAngle1 = 0.0f;
    /**
     * 视图角度二
     */
    private float ViewAngle2 = 30.0f;

    private List<P_Data> Data = null;

    /**
     * 显示范围
     */
    private int SHOW_TAB = 80;

    private int Start_Color = Color.GREEN;
    private int End_Color = Color.RED;

    /***
     * 色栏颜色表
     */
    private int Color_Tab = 80;
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
    /*画笔*/
    private Paint paintText;
    private Paint paintGrid;
    private Paint paintPlane1;
    private Paint paintPlane2;
    private float viewStartTop;
    private float viewStartLeft;
    private float viewHeight;
    private float viewWidth;
    private float plane1_x3;
    private float plane1_y3;
    private float plane1_x4;
    private float plane1_y4;
    private float plane2_x1;
    private float plane2_y1;
    private float plane2_x2;
    private float plane2_y2;
    private float plane1_x1;
    private float plane1_y1;

    private float touch_start_X;
    private float touch_start_Y;

    private float touch_end_X;
    private float touch_end_Y;
    private float plane1_x2;
    private float plane1_y2;
    private float mid_y;
    private float start_x;
    private float start_y;
    private float mid_x;
    private Color_Get color_bean = new Color_Get(Start_Color, End_Color, 80.0f);
    private boolean SHOW_QP = false;
    private float Qp_pre = 20.0f;
    private float QP_War = 40.0f;
    private float DATA_QP = 0;
    private float lable_size;
    private float end_y;


    public PrpsView(Context context) {
        super(context);
    }

    public PrpsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PrpsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(BKG_COLOR);

        Information_Init();

        Paint_Init();
        Draw_Coordinate(canvas);
        Draw_Data(canvas);
        Draw_Title(canvas);
        Draw_TriCyle(canvas);
        Draw_Draw_touch_information(canvas);
        Draw_QP(canvas);
        Draw_Y_Lable(canvas);
    }

    private void Draw_Y_Lable(Canvas canvas) {
        float offset = 0;
        String msg = "幅值(" + Format + ")";
        if (IS_AA_MODE) {
            offset = getTextWidth("0000", TEXT_SIZE) + TEXT_SIZE / 3.0f;
        } else {
            offset = getTextWidth("000", TEXT_SIZE) + TEXT_SIZE / 2.0f;
        }
        if (ViewAngle2 > 90.0f) {
            Information_Drawer.Draw_Right_Center(canvas, start_x + offset, start_y, (mid_y - start_y), msg, TEXT_SIZE);
        } else {

            Information_Drawer.Draw_left_Center(canvas, mid_x - offset, start_y, (mid_y - start_y), msg, TEXT_SIZE);
        }

    }

    /**
     *
     */
    private void Information_Init() {
        viewStartTop = super.getTop();
        viewStartLeft = super.getLeft();
        viewHeight = super.getHeight();
        viewWidth = super.getWidth();


        TEXT_SIZE = viewHeight / 20.0f;
        lable_size = viewHeight / 16.0f;


        start_x = viewStartLeft + (viewWidth / 9.0f) * 6.0f;
        start_y = viewStartTop + (viewHeight / 10.0f) * 1.5f;

        mid_x = viewStartLeft + (viewWidth / 9.0f) * 3.0f;
        mid_y = viewStartTop + (viewHeight / 10.0f) * 6.0f;

        // float End_y = viewStartTop + (viewHeight / 10.0f) * 8.0f;
        end_y = mid_y + (viewWidth / 6.0f) - lable_size * 2.1f;


        /*定点操作*/
        /*第一平面*/

        plane1_x1 = mid_x;
        plane1_y1 = start_y + (float) Math.tan(Math.toRadians(ViewAngle1)) * (start_x - mid_x);

        plane1_x2 = start_x;
        plane1_y2 = start_y;

        plane1_x3 = start_x;
        plane1_y3 = mid_y - (float) Math.tan(Math.toRadians(ViewAngle1)) * (start_x - mid_x);

        plane1_x4 = mid_x;
        plane1_y4 = mid_y;




        /*第二平面*/
        plane2_x1 = mid_x + (end_y - mid_y) * (1.0f / (float) Math.tan(Math.toRadians(ViewAngle2)));
        plane2_y1 = end_y;

        plane2_x2 = start_x + (end_y - mid_y) * (1.0f / (float) Math.tan(Math.toRadians(ViewAngle2)));
        plane2_y2 = plane2_y1 - (float) Math.tan(Math.toRadians(ViewAngle1)) * (start_x - mid_x);
    }

    public void setIS_AA_MODE(boolean isaa) {
        IS_AA_MODE = isaa;
        invalidate();
    }


    private void Draw_QP(Canvas canvas) {
        if (!SHOW_QP) {
            return;
        }
        Paint paint_qp = new Paint();
        paint_qp.setStrokeWidth(lable_size / 3);


        paint_qp.setColor(Color_set.FORMAT_COLOR);
        paint_qp.setTextSize(lable_size);
        float x1 = (viewStartLeft + viewWidth) - lable_size * 1.5f - getTextWidth("-80.0", lable_size * 1.5f) * 0.8f;
        float y1 = viewStartTop + lable_size * 2.0f;
        canvas.drawText(Format, x1, y1, paint_qp);

        paint_qp.setTextSize(lable_size * 2);


        if (DATA_QP >= QP_War) {
            paint_qp.setColor(System_Flag.War_Color);
        } else if (DATA_QP >= Qp_pre) {
            paint_qp.setColor(System_Flag.PRE_COLOR);
        } else {
            paint_qp.setColor(System_Flag.Normal_Color);
        }
        String str_qp = String.format("%03.1f", DATA_QP + SHOW_OFFSET);
        float x = x1 - getTextWidth(str_qp, lable_size * 1.5f) * 1.5f;
        float y = viewStartTop + lable_size * 2;
        canvas.drawText(str_qp, x, y, paint_qp);


    }

    /**
     * 设置QP
     *
     * @param Qp
     */
    public void Set_Qp(float Qp) {
        DATA_QP = Qp;
        invalidate();
    }

    /**
     * 设置QP显示
     *
     * @param show_Qp
     * @param PRE
     * @param War
     */
    public void Set_Qp_Information(boolean show_Qp, float PRE, float War) {
        SHOW_QP = show_Qp;
        QP_War = War;
        Qp_pre = PRE;

    }


    /**
     * 绘制图表标题
     *
     * @param canvas
     */
    private void Draw_Title(Canvas canvas) {

        Information_Drawer.Draw_Title(canvas, viewStartLeft, viewStartTop, viewWidth, viewHeight, "PRPS", lable_size);

    }

    /**
     * 绘制显示阈值三角形
     *
     * @param canvas
     */
    private void Draw_TriCyle(Canvas canvas) {
        float tri_start_x, tri_start_y;
        Path tri_path = new Path();
        if (ViewAngle2 < 90) {

            tri_start_x = mid_x - 1;
            tri_start_y = mid_y - (mid_y - start_y) / (float) SHOW_TAB * (float) THRESHOLD;
            tri_path.moveTo(tri_start_x, tri_start_y);
            tri_path.lineTo(tri_start_x - lable_size, tri_start_y - lable_size / 2);
            tri_path.lineTo(tri_start_x - lable_size, tri_start_y + lable_size / 2);
            tri_path.close();

        } else {
            tri_start_x = start_x + 1;
            tri_start_y = mid_y - (mid_y - start_y) / (float) SHOW_TAB * (float) THRESHOLD;
            tri_path.moveTo(tri_start_x, tri_start_y);
            tri_path.lineTo(tri_start_x + lable_size, tri_start_y - lable_size / 2);
            tri_path.lineTo(tri_start_x + lable_size, tri_start_y + lable_size / 2);
            tri_path.close();

        }
        Paint tri_paint = new Paint();
        tri_paint.setStyle(Paint.Style.FILL);
        tri_paint.setColor(Color.argb(0x80, 0xff, 0x00, 0x00));
        canvas.drawPath(tri_path, tri_paint);

    }

    /**
     * 绘制触摸信息提示
     *
     * @param canvas
     */
    private void Draw_Draw_touch_information(Canvas canvas) {

        Paint paint_Text = new Paint();
        paint_Text.setColor(Color_set.INFORMATION_COLOR);
        paint_Text.setTextSize(lable_size);
        paint_Text.setAntiAlias(true);
        /*一、旋转角度*/
        String text1 = String.format("旋转(%d°)", (int) (90 - ViewAngle2));
        canvas.drawText(text1, viewStartLeft + viewWidth / 2.0f - getTextWidth(text1, lable_size) / 2.0f
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text
        );
        /*************＞＞＞************/
        paint_Text.setColor(COLOR_1);
        canvas.drawText("＞", viewStartLeft + viewWidth / 2.0f + getTextWidth(text1, lable_size) / 2.0f + TEXT_SIZE * 1
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text);
        paint_Text.setColor(COLOR_2);
        canvas.drawText("＞", viewStartLeft + viewWidth / 2.0f + getTextWidth(text1, lable_size) / 2.0f + TEXT_SIZE * 2
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text);
        paint_Text.setColor(COLOR_3);
        canvas.drawText("＞", viewStartLeft + viewWidth / 2.0f + getTextWidth(text1, lable_size) / 2.0f + TEXT_SIZE * 3
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text);
        /*************＜＜＜************/
        paint_Text.setColor(COLOR_1);
        canvas.drawText("＜", viewStartLeft + viewWidth / 2.0f - getTextWidth(text1, lable_size) / 2.0f - TEXT_SIZE * 2
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text);
        paint_Text.setColor(COLOR_2);
        canvas.drawText("＜", viewStartLeft + viewWidth / 2.0f - getTextWidth(text1, lable_size) / 2.0f - TEXT_SIZE * 3
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text);
        paint_Text.setColor(COLOR_3);
        canvas.drawText("＜", viewStartLeft + viewWidth / 2.0f - getTextWidth(text1, lable_size) / 2.0f - TEXT_SIZE * 4
                , viewStartTop + getTextHeight(text1, lable_size)
                , paint_Text);


        /*二、相位*/
        String text2 = String.format("相位(%d°)", PHASE);
        canvas.drawText(text2, viewStartLeft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        /*************＞＞＞************/

        paint_Text.setColor(COLOR_1);
        canvas.drawText("＞", viewStartLeft + viewWidth / 2.0f + getTextWidth(text2, lable_size) / 2.0f + TEXT_SIZE * 1
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );

        paint_Text.setColor(COLOR_2);
        canvas.drawText("＞", viewStartLeft + viewWidth / 2.0f + getTextWidth(text2, lable_size) / 2.0f + TEXT_SIZE * 2
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        paint_Text.setColor(COLOR_3);
        canvas.drawText("＞", viewStartLeft + viewWidth / 2.0f + getTextWidth(text2, lable_size) / 2.0f + TEXT_SIZE * 3
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        /*************＜＜＜************/
        paint_Text.setColor(COLOR_1);
        canvas.drawText("＜", viewStartLeft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f - TEXT_SIZE * 2
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        paint_Text.setColor(COLOR_2);
        canvas.drawText("＜", viewStartLeft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f - TEXT_SIZE * 3
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        paint_Text.setColor(COLOR_3);
        canvas.drawText("＜", viewStartLeft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f - TEXT_SIZE * 4
                , viewStartTop + viewHeight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        /*三、显示范围（量程）*/
        paint_Text.setColor(Color_set.INFORMATION_COLOR);
        String text_tab = null;
        String text3 = null;
        String text4 = null;
        /*四、显示阈值*/
        if (IS_AA_MODE) {
            text3 = String.format("阈值(%.1fmV)", Compute_Information.AE_dB_2_mV(THRESHOLD) >= System_Commen.AE_MAX_VIEW ? System_Commen.AE_MAX_VIEW : Compute_Information.AE_dB_2_mV(THRESHOLD));
        } else {
            text3 = String.format("阈值(%d%s)", THRESHOLD + SHOW_OFFSET, Format);
        }
        if (IS_AA_MODE) {
            switch (SHOW_TAB) {
                case 80:
                    text_tab = "2000";
                    break;
                case 60:
                    text_tab = "200";
                    break;
                case 40:
                    text_tab = "20";
                    break;
                case 20:
                    text_tab = "2";
                    break;
            }
            text4 = "量程(" + 0 + "~" + text_tab + " mV" + ")";
        } else {
            text4 = String.format("量程(%d~%d%s)", SHOW_OFFSET, SHOW_TAB + SHOW_OFFSET, Format);
        }


        Information_Drawer.Draw_Left_Tips(canvas, viewStartLeft, viewStartTop, viewHeight, text3, lable_size);


        Information_Drawer.Draw_Right_Tips(canvas, viewStartLeft + viewWidth, viewStartTop, viewHeight, text4, lable_size);


    }

    /**
     * 绘制数据
     *
     * @param canvas
     */
    private void Draw_Data(Canvas canvas) {

        Paint PaintData = new Paint();
        PaintData.setStrokeWidth(1);
        PaintData.setAntiAlias(true);
        if (Data == null) {
            return;
        }

        float Data_Perx = (plane2_x2 - plane2_x1) / ((float) TheOtherParameter.PHASE_COUNT + 1.0f);
        float Data_head_perx = (plane2_x1 - plane1_x4) / 51.0f;

        float Data_Pery = (plane2_y1 - plane1_y4) / 52.0f;
        float Data_start_y = (51 - (Data.size() > 50 ? 50 : Data.size())) * Data_Pery + plane1_y4;
        float Data_start_x = (51 - (Data.size() > 50 ? 50 : Data.size())) * Data_head_perx + plane1_x4;
        float Data_per_y = (plane1_y4 - plane1_y3) / (float) TheOtherParameter.PHASE_COUNT;

        float Data_per_tab = (plane1_y4 - plane1_y1) / SHOW_TAB;
        int Phase_Offst_Temp = (int) (PHASE / 6.0f);
        int offset = 0;
        if (Phase_Offst_Temp > 0) {
            /*左移*/
            offset = TheOtherParameter.PHASE_COUNT - Phase_Offst_Temp;

        } else {
            /*右移*/
            offset = -Phase_Offst_Temp;

        }

        for (int i = 0; i < (Data.size() > 50 ? 50 : Data.size()); i++) {

            byte[] temp = Data.get(i).getData();

            /*offset-60*/
            for (int j = offset; j < TheOtherParameter.PHASE_COUNT; j++) {

                int values_temp = (int) (temp[j]) & 0xff;
                float Temp = values_temp / 3.0f;

                if (Temp > THRESHOLD) {
                    PaintData.setColor(color_bean.getcolor(values_temp / 3));
                    PaintData.setStrokeWidth(1.5f);
                    canvas.drawLine(Data_start_x + Data_head_perx * (i + 1) + Data_Perx * ((j - offset) + 1)
                            , Data_start_y + (i + 1) * Data_Pery - Data_per_y * ((j - offset) + 1)
                            , Data_start_x + Data_head_perx * (i + 1) + Data_Perx * ((j - offset) + 1)
                            , Data_start_y + (i + 1) * Data_Pery - Data_per_y * ((j - offset) + 1) - Data_per_tab * (Temp >= SHOW_TAB ? SHOW_TAB : Temp)
                            , PaintData
                    );
                }
            }
            /*0-offset*/
            for (int j = 0; j < offset; j++) {

                int values_temp = (int) (temp[j]) & 0xff;
                float Temp = values_temp / 3.0f;

                if (Temp > THRESHOLD) {
                    PaintData.setColor(color_bean.getcolor(values_temp / 3));
                    PaintData.setStrokeWidth(1.5f);
                    canvas.drawLine(Data_start_x + Data_head_perx * (i + 1) + Data_Perx * ((TheOtherParameter.PHASE_COUNT - offset + j) + 1)
                            , Data_start_y + (i + 1) * Data_Pery - Data_per_y * ((TheOtherParameter.PHASE_COUNT - offset + j) + 1)
                            , Data_start_x + Data_head_perx * (i + 1) + Data_Perx * ((TheOtherParameter.PHASE_COUNT - offset + j) + 1)
                            , Data_start_y + (i + 1) * Data_Pery - Data_per_y * ((TheOtherParameter.PHASE_COUNT - offset + j) + 1) - Data_per_tab * (Temp >= SHOW_TAB ? SHOW_TAB : Temp)
                            , PaintData
                    );
                }
            }


        }


    }

    /**
     * 获取颜色
     *
     * @param data
     * @return
     */
    private int getDataColor(int data) {
        int temp = data / (Color_Tab / 16);
        if (temp <= 15) {
            return Color_Bar[temp];
        }
        return Color_Bar[15];


    }

    /**
     * 绘制接触式超声专用的Y轴
     *
     * @param canvas
     */
    private void Draw_Y_AA(Canvas canvas) {

        /**根据不同的档位绘制坐标轴**/
        switch (SHOW_TAB) {
            case 80: //2000
                AA_Asiy.Draw_Asiy_1(canvas, mid_x, start_y, mid_y, 5, false, TEXT_SIZE);
                break;
            case 60://200
                AA_Asiy.Draw_Asiy_1(canvas, mid_x, start_y, mid_y, 4, false, TEXT_SIZE);
                break;
            case 40://20
                AA_Asiy.Draw_Asiy_1(canvas, mid_x, start_y, mid_y, 3, false, TEXT_SIZE);
                break;
            case 20://2
                AA_Asiy.Draw_Asiy_1(canvas, mid_x, start_y, mid_y, 2, false, TEXT_SIZE);
                break;
        }

    }

    private void Draw_Y_AA_1(Canvas canvas) {

        /**根据不同的档位绘制坐标轴**/
        switch (SHOW_TAB) {
            case 80: //2000
                AA_Asiy.Draw_Asiy_1(canvas, start_x, start_y, mid_y, 5, true, TEXT_SIZE);
                break;
            case 60://200
                AA_Asiy.Draw_Asiy_1(canvas, start_x, start_y, mid_y, 4, true, TEXT_SIZE);
                break;
            case 40://20
                AA_Asiy.Draw_Asiy_1(canvas, start_x, start_y, mid_y, 3, true, TEXT_SIZE);
                break;
            case 20://2
                AA_Asiy.Draw_Asiy_1(canvas, start_x, start_y, mid_y, 2, true, TEXT_SIZE);
                break;
        }
    }

    /**
     * 坐标轴
     */
    private void Draw_Coordinate(Canvas canvas) {



        /*竖直栅格步进偏移量*/
        float Plane1_ver_perx = (start_x - mid_x) / 4.0f;
        float Plane1_ver_pery = (plane1_y1 - plane1_y2) / 4.0f;

        /*水平栅格步进偏移量*/
        float Plane1_hor_pery = (plane1_y4 - plane1_y1) / 4.0f;

        /*竖直栅格步进偏移量*/
        float Plane2_ver_perx = (plane2_x2 - plane2_x1) / 4.0f;
        float Plane2_ver_pery = (plane1_y4 - plane1_y3) / 4.0f;

        /*水平栅格步进偏移量*/
        float Plane2_hor_per_x = (plane2_x1 - plane1_x4) / 5.0f;
        float Plane2_hor_per_y = (plane2_y1 - plane1_y4) / 5.0f;

        /*绘制第一平面*/
        Path Plane1_Path = new Path();
        Plane1_Path.moveTo(plane1_x1, plane1_y1);
        Plane1_Path.lineTo(plane1_x2, plane1_y2);
        Plane1_Path.lineTo(plane1_x3, plane1_y3);
        Plane1_Path.lineTo(plane1_x4, plane1_y4);
        Plane1_Path.close();
        canvas.drawPath(Plane1_Path, paintPlane1);

        /*第一平面栅格*/
        for (int i = 0; i < 3; i++) {
            /*水平*/
            canvas.drawLine(plane1_x1
                    , plane1_y1 + (i + 1) * Plane1_hor_pery
                    , plane1_x2
                    , plane1_y2 + Plane1_hor_pery * (i + 1)
                    , paintGrid
            );
            /*竖直*/
            canvas.drawLine(
                    plane1_x1 + (i + 1) * Plane1_ver_perx
                    , plane1_y1 - (i + 1) * Plane1_ver_pery
                    , plane1_x1 + (i + 1) * Plane1_ver_perx
                    , plane1_y4 - (i + 1) * Plane1_ver_pery
                    , paintGrid
            );
        }
        int Plane_hor_per_tab = SHOW_TAB / 4;
        if (ViewAngle2 <= 90.0f) {

            /**AA模式**/
            if (IS_AA_MODE) {
                Draw_Y_AA(canvas);
            } else {
                for (int i = 0; i < 5; i++) {
                    if (i == 4) {
                        canvas.drawText(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET)
                                , plane1_x1 - getTextWidth(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET), TEXT_SIZE) - TEXT_SIZE / 2.0f
                                , plane1_y1 + (i) * Plane1_hor_pery + getTextHeight(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET), TEXT_SIZE) / 3.0f
                                , paintText
                        );
                    } else {
                        canvas.drawText(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET)
                                , plane1_x1 - getTextWidth(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET), TEXT_SIZE) - TEXT_SIZE / 2.0f
                                , plane1_y1 + (i) * Plane1_hor_pery + getTextHeight(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET), TEXT_SIZE) / 3.0f
                                , paintText
                        );
                    }
                }
            }
        } else {

            /*AA模式*/
            if (IS_AA_MODE) {
                Draw_Y_AA_1(canvas);
            } else {
                for (int i = 0; i < 5; i++) {
                    if (i == 4) {
                        canvas.drawText(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET)
                                , plane1_x2 + TEXT_SIZE / 2.0f
                                , plane1_y2 + (i) * Plane1_hor_pery + getTextHeight(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET), TEXT_SIZE) / 3.0f
                                , paintText
                        );
                    } else {
                        canvas.drawText(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET)
                                , plane1_x2 + TEXT_SIZE / 2.0f
                                , plane1_y2 + i * Plane1_hor_pery + getTextHeight(String.format("%d", SHOW_TAB - (i * Plane_hor_per_tab) + SHOW_OFFSET), TEXT_SIZE) / 3.0f
                                , paintText
                        );
                    }
                }
            }
        }
        paintPlane1.setColor(TEXT_COLOR);
        paintPlane1.setStyle(Paint.Style.STROKE);
        canvas.drawPath(Plane1_Path, paintPlane1);

        /*绘制第二平面*/
        Path Plane2_Path = new Path();
        Plane2_Path.moveTo(plane1_x4, plane1_y4);
        Plane2_Path.lineTo(plane1_x3, plane1_y3);
        Plane2_Path.lineTo(plane2_x2, plane2_y2);
        Plane2_Path.lineTo(plane2_x1, plane2_y1);
        Plane2_Path.close();
        canvas.drawPath(Plane2_Path, paintPlane2);
        /*竖直栅格*/
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(
                    plane1_x4 + (i + 1) * Plane2_ver_perx
                    , plane1_y4 - (i + 1) * Plane2_ver_pery
                    , plane2_x1 + (i + 1) * Plane2_ver_perx
                    , plane2_y1 - (i + 1) * Plane2_ver_pery
                    , paintGrid


            );

        }
        /*水平栅格*/
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(
                    plane1_x4 + (i + 1) * Plane2_hor_per_x
                    , plane1_y4 + (i + 1) * Plane2_hor_per_y
                    , plane1_x3 + (i + 1) * Plane2_hor_per_x
                    , plane1_y3 + (i + 1) * Plane2_hor_per_y
                    , paintGrid
            );

        }
        /*第一坐标轴*/
        if (ViewAngle2 <= 90.0f) {
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
//                    canvas.drawText(String.format("%d", i * 10)
//                            , plane1_x4 + i * Plane2_hor_per_x - getTextWidth(String.format("%d", i * 10), TEXT_SIZE)
//                            , plane1_y4 + (i) * Plane2_hor_per_y + getTextHeight(String.format("%d", 80 - i * 10), TEXT_SIZE)
//                            , paintText
//                    );
                } else {
                    canvas.drawText(String.format("%d", i * 10)
                            , plane1_x4 + i * Plane2_hor_per_x - getTextWidth(String.format("%d", i * 10), TEXT_SIZE) * 1.5f
                            , plane1_y4 + (i) * Plane2_hor_per_y + getTextHeight(String.format("%d", 80 - i * 10), TEXT_SIZE) / 2.0f
                            , paintText
                    );

                }
            }


            canvas.drawText("周期", plane1_x4 + 3 * Plane2_hor_per_x - getTextWidth("周期", TEXT_SIZE) * 3.0f
                    , plane1_y4 + (2) * Plane2_hor_per_y + getTextHeight("周期", TEXT_SIZE),
                    paintText);


        } else {
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
//                    canvas.drawText(String.format("%d", i * 10)
//                            , plane1_x3 + i * Plane2_hor_per_x + getTextWidth(String.format("%d", i * 10), TEXT_SIZE)
//                            , plane1_y3 + (i) * Plane2_hor_per_y + getTextHeight(String.format("%d", 80 - i * 10), TEXT_SIZE)
//                            , paintText
//                    );
                } else {
                    canvas.drawText(String.format("%d", i * 10)
                            , plane1_x3 + i * Plane2_hor_per_x + getTextWidth(String.format("%d", i * 10), TEXT_SIZE) * 1.0f
                            , plane1_y3 + (i) * Plane2_hor_per_y + getTextHeight(String.format("%d", 80 - i * 10), TEXT_SIZE) / 2.0f
                            , paintText
                    );

                }
            }


            canvas.drawText("周期", plane1_x3 + 3 * Plane2_hor_per_x + getTextWidth("周期", TEXT_SIZE) * 2.0f
                    , plane1_y3 + (2) * Plane2_hor_per_y + getTextHeight("周期", TEXT_SIZE),
                    paintText);


        }

        /*第二最标轴*/
        for (int i = 0; i < 5; i++) {

            if (i == 4) {
                canvas.rotate(-ViewAngle1, plane2_x1 + (i) * Plane2_ver_perx - getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 1.2f
                        , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE));
                canvas.drawText(String.format("%d", i * 90)
                        , plane2_x1 + (i) * Plane2_ver_perx - getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 1.2f
                        , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE)
                        , paintText
                );
                canvas.rotate(ViewAngle1, plane2_x1 + (i) * Plane2_ver_perx - getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 1.2f
                        , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE));

            } else {
                if (i == 0) {
                    canvas.rotate(-ViewAngle1, plane2_x1 + (i) * Plane2_ver_perx + getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 2.0f
                            , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE));
                    canvas.drawText(String.format("%d", i * 90)
                            , plane2_x1 + (i) * Plane2_ver_perx + getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 2.0f
                            , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE)
                            , paintText
                    );
                    canvas.rotate(ViewAngle1, plane2_x1 + (i) * Plane2_ver_perx + getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 2.0f
                            , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE));
                } else {
                    canvas.rotate(-ViewAngle1, plane2_x1 + (i) * Plane2_ver_perx - getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 2.0f
                            , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE));
                    canvas.drawText(String.format("%d", i * 90)
                            , plane2_x1 + (i) * Plane2_ver_perx - getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 2.0f
                            , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE)
                            , paintText
                    );
                    canvas.rotate(ViewAngle1, plane2_x1 + (i) * Plane2_ver_perx - getTextWidth(String.format("%d", i * 90), TEXT_SIZE) / 2.0f
                            , plane2_y1 - (i) * Plane2_ver_pery + getTextHeight(String.format("%d", i * 90), TEXT_SIZE));
                }
            }

        }

        paintPlane2.setColor(TEXT_COLOR);
        paintPlane2.setStyle(Paint.Style.STROKE);
        canvas.drawPath(Plane2_Path, paintPlane2);


//        if (ViewAngle2 <= 90) {
        /*色块*/
        Paint paint_bar = new Paint();
        float start_bar_x = (viewStartLeft + viewWidth) - lable_size * 2.0f - lable_size * 1.3f;
        float start_bar_y = end_y - (mid_y - start_y);
        float end_bar_x = (viewStartLeft + viewWidth) - lable_size * 2.0f - lable_size * 0.5f;
        float end_bar_y = end_y;
        float per_bar = (end_bar_y - start_bar_y) / 80.0f;
        for (int i = 0; i < 80; i++) {
            paint_bar.setColor(color_bean.getcolor(79 - i));
            canvas.drawRect(start_bar_x,
                    start_bar_y + i * per_bar,
                    end_bar_x,
                    start_bar_y + (i + 1) * per_bar,
                    paint_bar
            );
        }
        draw_squre(start_bar_x, start_bar_y, end_bar_x - start_bar_x, end_bar_y - start_bar_y, paintText, canvas);

        String text_top;
        String text_bouttom;
        if (IS_AA_MODE) {
            text_top = "2000";
            text_bouttom = "0";
        } else {
            text_top = String.format("%d", 80 + SHOW_OFFSET);
            text_bouttom = String.format("%d", 0 + SHOW_OFFSET);
        }

        float top_start_x = start_bar_x - (getTextWidth(text_top, TEXT_SIZE) - (end_bar_x - start_bar_x)) / 2.0f;
        float top_start_y = start_bar_y - getTextHeight(text_top, TEXT_SIZE) / 2.0f;
        canvas.drawText(text_top, top_start_x, top_start_y, paintText);


        float Bouttom_start_x = start_bar_x - (getTextWidth(text_bouttom, TEXT_SIZE) - (end_bar_x - start_bar_x)) / 2.0f;
        float Bouttom_start_y = end_bar_y + getTextHeight(text_bouttom, TEXT_SIZE);
        canvas.drawText(text_bouttom, Bouttom_start_x, Bouttom_start_y, paintText);

    }


    /**
     * 画笔初始化
     */
    private void Paint_Init() {
        /*文字画笔*/
        paintText = new Paint();
        paintText.setColor(TEXT_COLOR);
        paintText.setTextSize(TEXT_SIZE);
        paintText.setStrokeWidth(1);
        paintText.setAntiAlias(true);
        /*栅格画笔*/
        paintGrid = new Paint();
        paintGrid.setStrokeWidth(1);
        paintGrid.setAntiAlias(true);
        paintGrid.setColor(GRID_COLOR);
        /*平面一画笔*/
        paintPlane1 = new Paint();
        paintPlane1.setColor(COLOR_PLANE1);
        paintPlane1.setAntiAlias(true);
        /*平面二画笔*/
        paintPlane2 = new Paint();
        paintPlane2.setColor(COLOR_PLANE2);
        paintPlane2.setAntiAlias(true);
    }
    /*页面绘制方法*/

    /*私有计算绘制方法*/

    /**
     * 画矩形
     *
     * @param x
     * @param y
     * @param whith
     * @param hight
     * @param paint
     * @param canvas
     */
    private void draw_squre(float x, float y, float whith, float hight, Paint paint, Canvas canvas) {
        /*top*/
        canvas.drawLine(x, y, x + whith, y, paint);
        /*bottom*/
        canvas.drawLine(x, y + hight, x + whith, y + hight, paint);
        /*left*/
        canvas.drawLine(x, y, x, y + hight, paint);
        /*right*/
        canvas.drawLine(x + whith, y, x + whith, y + hight, paint);
    }
    /*页面计算方法*/

    /**
     * 计算字体的占用宽度
     *
     * @param text     字符串
     * @param textsize 字体大小
     * @return
     */
    private float getTextWidth(String text, float textsize) {
        if (text == null || text.length() <= 0) {
            return 0;
        }
        Paint paint = new Paint();
        paint.setTextSize(textsize);
        return paint.measureText(text);
    }

    /**
     * 计算文本的占用高度
     *
     * @param text     显示的字符串
     * @param textsize 显示的字体大小
     * @return
     */
    private float getTextHeight(String text, float textsize) {
        Paint paint = new Paint();
        paint.setTextSize(textsize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /*设置方法*/

    /**
     * 设置背景颜色
     *
     * @param color RGB颜色
     */
    public void SetBackgroundColor(int color) {
        BKG_COLOR = color;
    }

    /**
     * 设置显示范围
     *
     * @param range 1->0to20;2->0to40;3->0to60;4->0to80
     */
    public void SetRange(int range) {
        switch (range) {
            case 1:
                SHOW_TAB = 20;
                break;
            case 2:
                SHOW_TAB = 40;
                break;
            case 3:
                SHOW_TAB = 60;
                break;
            case 4:
                SHOW_TAB = 80;
                break;
            default:
                SHOW_TAB = 80;
                break;
        }
        invalidate();
    }

    /**
     * 设置字体颜色
     *
     * @param color RGB颜色
     */
    public void SetTextColor(int color) {
        TEXT_COLOR = color;
    }

    /**
     * 设置字体的大小
     *
     * @param size 字体大小值
     */
    public void SetTextSize(int size) {
        TEXT_SIZE = size;
    }

    /**
     * 设置平面一的颜色
     *
     * @param color RGB颜色
     *              PS:最好设置点透明度，要好看一点
     */
    public void SetPlane1BackgroundColor(int color) {
        COLOR_PLANE1 = color;
    }

    /**
     * 设置平面二的颜色
     *
     * @param color RGB颜色
     *              PS:最好设置点透明度，要好看一点
     */
    public void SetPlane2BackgroundColor(int color) {
        COLOR_PLANE2 = color;
    }

    /**
     * 设置平面一的偏移角度
     *
     * @param angle 角度
     */
    public void SetViewAngle1(float angle) {
        ViewAngle1 = angle;
    }

    /**
     * 设置平面二的偏移角度
     *
     * @param angle 角度
     */
    public void SetViewAngle2(float angle) {
        ViewAngle2 = angle;
    }

    /**
     * 颜色柱子的颜色设置
     *
     * @param color List的RGB颜色
     *              ps：list的大小为16个
     */
    public void SetBarColors(List<Integer> color) {
        if (color.size() < 16) {
            return;
        }
        for (int i = 0; i < 16; i++) {
            Color_Bar[i] = color.get(i);
        }
    }

    /**
     * 设置显示单位
     *
     * @param format 字符串类型的显示单位
     */
    public void SetFormat(String format) {
        Format = format;
        invalidate();
    }

    /**
     * 数据设置方法
     *
     * @param rc_data 当前所有周期的数据
     */
    public void SetData(List<P_Data> rc_data) {
        if (Data == null) {
            Data = new ArrayList<P_Data>();
            Data.addAll(rc_data);
            invalidate();
            return;
        }

        Data.addAll(rc_data);

        if (Data.size() > 50) {
            int temp = Data.size() - 50;
            for (int i = 0; i < temp; i++) {
                Data.remove(0);
            }
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //捕获到单指按下，或者说捕获到第一个指头按下
                touch_start_X = event.getX();
                touch_start_Y = event.getY();

                /*****记录开始滑动以前的值******/
                m_ntempPhase = PHASE;
                m_ntempThreshold = THRESHOLD;
                m_ntempShowTab = SHOW_TAB;
                m_fViewAngle = ViewAngle2;

                SHOW_TAB_CAN_TAUCHED = true;
                break;


            case MotionEvent.ACTION_MOVE:
                //捕获到最后一个指头离开
                touch_end_X = event.getX();
                touch_end_Y = event.getY();

                /*判断是否超出视图区域*/
                if ((touch_end_X >= (viewStartLeft + viewWidth)) || (touch_end_X <= viewStartLeft)) {
                    /*左右边界超限*/

                    return true;
                }
                if ((touch_end_Y <= viewStartTop) || (touch_end_Y >= (viewStartTop + viewHeight))) {
                    /*上下边界超限*/
                    SHOW_TAB_Touch_Proc();
                    return true;
                }


                if (Math.abs(touch_end_X - touch_start_X) >= Math.abs(touch_end_Y - touch_start_Y)) {
                    /*上下屏幕*/
                    Touched_Top_Bottom_Screen();

                } else {
                    /*左右屏幕*/
                    Touched_Left_Right_Screen();

                }
                break;
            /*手指抬起*/
            case MotionEvent.ACTION_UP:
                //捕获到最后一个指头离开
                touch_end_X = event.getX();
                touch_end_Y = event.getY();
                /*上下*/

                /*判断是否超出视图区域*/
                if ((touch_end_X >= (viewStartLeft + viewWidth)) || (touch_end_X <= viewStartLeft)) {
                    /*左右边界超限*/
                    SHOW_TAB_CAN_TAUCHED = false;
                    return true;
                }
                if ((touch_end_Y <= viewStartTop) || (touch_end_Y >= (viewStartTop + viewHeight))) {
                    /*上下边界超限*/
                    SHOW_TAB_CAN_TAUCHED = false;
                    return true;
                }

                /*******显示量程调节*******/
                SHOW_TAB_Touch_Proc();

                break;

        }
        return true;
    }

    private void SHOW_TAB_Touch_Proc() {
        if (!SHOW_TAB_CAN_TAUCHED) {
            return;
        }
        if ((Math.abs(touch_end_X - touch_start_X) <= Math.abs(touch_end_Y - touch_start_Y))) {
            /*左屏幕*/
            if (touch_start_X >= (viewStartLeft + viewWidth / 2.0f)) {
                if ((touch_end_Y - touch_start_Y) >= 10.0f) {
                    /*下滑*/
                    SHOW_TAB -= 20;
                    SHOW_TAB = SHOW_TAB <= 20 ? 20 : SHOW_TAB;
                    THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
                    Touch_Notifier();
                } else if ((touch_end_Y - touch_start_Y) <= -10.0f) {
                    /*上滑动*/
                    SHOW_TAB += 20;
                    SHOW_TAB = SHOW_TAB >= 80 ? 80 : SHOW_TAB;
                    THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
                    Touch_Notifier();
                }
            }
        }
        SHOW_TAB_CAN_TAUCHED = false;
    }

    private void Touched_Left_Right_Screen() {
        if (touch_start_X >= (viewStartLeft + viewWidth / 2.0f)) {
//            float temp_per = viewHeight/4.0f;
//            SHOW_TAB = (int) (m_ntempShowTab - (int) ((touch_end_Y - touch_start_Y) / temp_per)*20);
//            SHOW_TAB = SHOW_TAB >= 80 ? 80 : SHOW_TAB;
//            SHOW_TAB = SHOW_TAB <= 20 ? 20 : SHOW_TAB;
//            THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
//            Touch_Notifier();
        } else {
            /*右*/
            /*显示阈值*/
            float temp_per = viewHeight / 6.0f;
            THRESHOLD = (int) (m_ntempThreshold - (int) ((touch_end_Y - touch_start_Y) / temp_per));
            /*判断是否超出显示量程*/
            THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
            THRESHOLD = THRESHOLD <= 0 ? 0 : THRESHOLD;
            Touch_Notifier();
        }
        /*左*/
        /*不使用滑动*/


    }

    private void Touched_Top_Bottom_Screen() {
        if (touch_start_Y >= (viewStartTop + viewHeight / 2.0f)) {
            /*下*/
            /*相位偏移*/
            float temp_per = viewWidth / 6.0f;
            PHASE = (int) (m_ntempPhase + (int) ((touch_end_X - touch_start_X) / temp_per) * 6);
            /*判断是否超出范围*/
            PHASE = PHASE <= -180 ? -180 : PHASE;
            PHASE = PHASE >= 180 ? 180 : PHASE;
            Touch_Notifier();

        } else {
            /*上*/
            /*旋转角度*/
            float temp_per = viewWidth / 121.0f;
            ViewAngle2 = m_fViewAngle - (touch_end_X - touch_start_X) / temp_per;

            ViewAngle2 = ViewAngle2 >= 30.0f ? ViewAngle2 : 30.0f;
            ViewAngle2 = ViewAngle2 <= 150.0f ? ViewAngle2 : 150.0f;
            invalidate();
        }


    }

    /**
     * 设置触摸事件监听器
     *
     * @param listener
     */
    public void seteventlistenr(PrpsListener listener) {
        this.listener = listener;
    }

    /**
     * 触摸事件通知器
     */
    private void Touch_Notifier() {
        if (listener != null) {
            listener.PrpsTouchEvent(new PrpsEvent(SHOW_TAB, PHASE, THRESHOLD));
        }
        invalidate();
    }

    public void show_demo() {

        List<P_Data> temp = new ArrayList<P_Data>();
        for (int i = 0; i < 50; i++) {

            byte[] data = new byte[TheOtherParameter.PHASE_COUNT];
            for (int j = 0; j < 60; j++) {
                data[j] = (byte) 240;
            }
            temp.add(new P_Data(data));
        }
        SetData(temp);
    }


    /**
     * 设置图像信息
     *
     * @param phase     偏移相位
     * @param tab       量程
     * @param threshold 显示阈值
     */
    public void set_information(int phase, int tab, int threshold) {
        this.PHASE = phase;
        this.SHOW_TAB = tab;
        this.THRESHOLD = threshold;
        invalidate();
    }


    public void Clear_Prps() {

        Data = null;
        invalidate();
    }

    public void SetShowOffset(int offset) {
        SHOW_OFFSET = offset;
    }

    public void setthreshold(int threshold) {
        this.THRESHOLD = threshold;
        invalidate();
    }

    public void set_information(int tab, int threshold) {
        this.SHOW_TAB = tab;
        this.THRESHOLD = threshold;
        invalidate();
    }

    public void SetData(byte[] m_prpsDataS) {
        if (Data == null) {
            Data = new ArrayList<P_Data>();
        }
        if(m_prpsDataS.length<3000)
        {
            return;
        }

        Data.clear();

        for(int i=0;i<50;i++)
        {
            byte[] b_temp = new byte[60];
            System.arraycopy(m_prpsDataS,60 * i, b_temp, 0, 60);
            P_Data temp = new P_Data(b_temp);
            Data.add(temp);
        }

        invalidate();



    }
}
