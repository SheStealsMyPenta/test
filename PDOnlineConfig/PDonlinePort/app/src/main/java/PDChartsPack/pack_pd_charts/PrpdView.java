//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
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

import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.System_Commen;
import PDChartsPack.pack_compute.Compute_Information;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * Created by SONG on 2017/11/1 14:52,13:59.
 * The final explanation right belongs to author
 */

public class PrpdView extends View {


    private int MAX_TAB = 80;

    private int AA_MAX_TEB = 80;

    private boolean CLEAR_CANUSED = false;


    private boolean SHOW_TAB_CAN_TAUCHED = false;
    private int COLOR_1 = Color_set.ARROW_Color_1;
    private int COLOR_2 = Color_set.ARROW_Color_2;
    private int COLOR_3 = Color_set.ARROW_Color_3;

    private boolean IS_AAmode = false;

    private List<P_Data> m_listHistryData = new ArrayList<P_Data>();

    private int AUTO_CLEAR_TIME = 10;
    private boolean Auto_Clear_Data = false;


    private int SHOW_OFFSET = 0;
    private boolean m_bISbtnFouce = false;

    //    private int BKG_COLOR = Color.LTGRAY;
    private int BKG_COLOR = Color_set.BKG_COLOR;
    private int DATA_BKG_COLOR = Color_set.DATA_BKG_COLOR;

    private int GRID_COLOR = Color_set.PRPD_GRID_COLOR;

    private int TEXT_COLOR = Color_set.TEXT_COLOR;

    private float TEXT_SIZE = 25;

    private int PHASE = 0;
    private int SHOW_TAB = 80;
    private int THRESHOLD = 0;


    private int m_ntempPhase = 0;
    private int m_ntempShowTab = 0;
    private int m_ntempThreshold = 0;


    private int PRPD_VIEW_DATA[][] = new int[TheOtherParameter.PHASE_COUNT][80];

    private List<PrpdData> DATA = null;

    private float viewStarttop;
    private float viewStartleft;
    private float viewHight;
    private float viewWidth;

    private int Start_Color = Color.GREEN;
    private int End_Color = Color.RED;

    private String Format = "dB";
    private float touch_start_X;
    private float touch_start_Y;

    private float touch_end_X;
    private float touch_end_Y;
    private PrpsListener listenr;

    private Color_Get Color_bean = new Color_Get(Start_Color, End_Color, 100.0f);

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
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;
    private float btn_start_x;
    private float btn_start_y;
    private float btn_end_x;
    private float btn_end_y;
    private Clear_Listener clear_listener;
    private float lable_size;

    public PrpdView(Context context) {
        super(context);
        Remove_PRPD_Data();
    }

    public PrpdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Remove_PRPD_Data();
    }

    public PrpdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Remove_PRPD_Data();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(BKG_COLOR);


        Information_Init();

        Draw_BKG(canvas);
        Draw_X(canvas);
        /**是否使用的是AA模式**/
        if (IS_AAmode) {
            Draw_Y_AA(canvas);
        } else {
            Draw_y(canvas);
        }

        if (CLEAR_CANUSED) {
            Draw_Remove_Button(canvas);
        }
        Draw_Data(canvas);
        Draw_Title(canvas);
        Draw_TriCyle(canvas);
        Draw_Information(canvas);
        Draw_Y_Lable(canvas);


    }

    private void Draw_Y_Lable(Canvas canvas) {
        float offset = 0;
        String msg = "幅值(" + Format + ")";
        if (IS_AAmode) {
            offset = getTextWidth("0000", TEXT_SIZE) + TEXT_SIZE / 3.0f + 15.0f;
        } else {
            offset = getTextWidth("000", TEXT_SIZE) + TEXT_SIZE / 2.0f + 15.0f;
        }


        Information_Drawer.Draw_left_Center(canvas, start_x - offset, start_y, (end_y - start_y), msg, TEXT_SIZE);

    }

    private void Information_Init() {
        viewStarttop = super.getTop();
        viewStartleft = super.getLeft();
        viewHight = super.getHeight();
        viewWidth = super.getWidth();

        TEXT_SIZE = viewHight / 30.0f;
        lable_size = viewHight / 20.0f;

        start_x = viewStartleft + (viewWidth / 10.0f) * 1.5f + lable_size;
        start_y = viewStarttop + (viewHight / 10.0f) * 1.0f + lable_size;
        end_x = viewStartleft + (viewWidth / 10.0f) * 9.0f - lable_size;
        end_y = viewStarttop + (viewHight / 10.0f) * 8.5f - lable_size;


    }

    public void SetClearUese(boolean isused) {
        CLEAR_CANUSED = isused;
        invalidate();
    }

    /**
     * 绘制图表标题
     *
     * @param canvas
     */
    private void Draw_Title(Canvas canvas) {

        Information_Drawer.Draw_Title(canvas, viewStartleft, viewStarttop, viewWidth, viewHight, "PRPD", lable_size);
    }

    /**
     * 绘制PRPD清除键
     *
     * @param canvas
     */
    private void Draw_Remove_Button(Canvas canvas) {

        /*按键定位*/
        btn_start_x = start_x + (end_x - start_x) / 2.0f - lable_size * 2;
        btn_start_y = viewStarttop + lable_size / 5;
        btn_end_x = start_x + (end_x - start_x) / 2.0f + lable_size * 2;
        btn_end_y = start_y - lable_size / 5;

        Paint btn_paint = new Paint();
        Path btn_path = new Path();
        btn_path.moveTo(btn_start_x, btn_start_y);
        btn_path.lineTo(btn_end_x, btn_start_y);
        btn_path.lineTo(btn_end_x, btn_end_y);
        btn_path.lineTo(btn_start_x, btn_end_y);
        btn_path.close();

        btn_paint.setStyle(Paint.Style.FILL);
        /*按键获取焦点*/
        if (m_bISbtnFouce) {
            btn_paint.setColor(Color.argb(0x80, 0x00, 0xff, 0x00));
            canvas.drawPath(btn_path, btn_paint);

        } else {
            btn_paint.setColor(Color.argb(0xff, 0x7a, 0x8b, 0x8b));
            canvas.drawPath(btn_path, btn_paint);

        }

        btn_paint.setStyle(Paint.Style.STROKE);
        if (m_bISbtnFouce) {
            btn_paint.setColor(Color.argb(0xff, 0x00, 0x00, 0x00));
            canvas.drawPath(btn_path, btn_paint);

        } else {
            btn_paint.setColor(Color.argb(0xff, 0xff, 0xff, 0xff));
            canvas.drawPath(btn_path, btn_paint);

        }
        btn_paint.setTextSize(lable_size);
        btn_paint.setStrokeWidth(1);
        btn_paint.setAntiAlias(true);
        btn_paint.setStyle(Paint.Style.FILL);
        btn_paint.setColor(Color.WHITE);
        canvas.drawText("清除"
                , btn_start_x + (btn_end_x - btn_start_x) / 2.0f - getTextWidth("清除", lable_size) / 2.0f
                , btn_start_y + (btn_end_y - btn_start_y) / 2.0f + getTextHeight("清除", lable_size) / 3.0f
                , btn_paint
        );


    }

    /**
     * 绘制显示阈值三角形
     *
     * @param canvas
     */
    private void Draw_TriCyle(Canvas canvas) {
        float tri_start_x, tri_start_y;
        tri_start_x = start_x - 1;
        tri_start_y = end_y - (end_y - start_y) / (float) SHOW_TAB * (float) THRESHOLD;

        Path tri_path = new Path();
        tri_path.moveTo(tri_start_x, tri_start_y);
        tri_path.lineTo(tri_start_x - TEXT_SIZE, tri_start_y - TEXT_SIZE / 2.0F);
        tri_path.lineTo(tri_start_x - TEXT_SIZE, tri_start_y + TEXT_SIZE / 2.0F);
        tri_path.close();
        Paint tri_paint = new Paint();
        tri_paint.setStyle(Paint.Style.FILL);
        tri_paint.setColor(Color.argb(0x80, 0xff, 0x00, 0x00));
        canvas.drawPath(tri_path, tri_paint);

    }

    /**
     * 绘制触摸事件提示
     *
     * @param canvas
     */
    private void Draw_Information(Canvas canvas) {


        Paint paint_Text = new Paint();
        paint_Text.setColor(Color_set.INFORMATION_COLOR);
        paint_Text.setTextSize(lable_size);
        paint_Text.setAntiAlias(true);

        /*二、相位*/
        String text2 = String.format("相位(%d°)", PHASE);
        canvas.drawText(text2, viewStartleft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        /*************＞＞＞************/

        paint_Text.setColor(COLOR_1);
        canvas.drawText("＞", viewStartleft + viewWidth / 2.0f + getTextWidth(text2, lable_size) / 2.0f + TEXT_SIZE * 1
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );

        paint_Text.setColor(COLOR_2);
        canvas.drawText("＞", viewStartleft + viewWidth / 2.0f + getTextWidth(text2, lable_size) / 2.0f + TEXT_SIZE * 2
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        paint_Text.setColor(COLOR_3);
        canvas.drawText("＞", viewStartleft + viewWidth / 2.0f + getTextWidth(text2, lable_size) / 2.0f + TEXT_SIZE * 3
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        /*************＜＜＜************/
        paint_Text.setColor(COLOR_1);
        canvas.drawText("＜", viewStartleft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f - TEXT_SIZE * 2
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        paint_Text.setColor(COLOR_2);
        canvas.drawText("＜", viewStartleft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f - TEXT_SIZE * 3
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        paint_Text.setColor(COLOR_3);
        canvas.drawText("＜", viewStartleft + viewWidth / 2.0f - getTextWidth(text2, lable_size) / 2.0f - TEXT_SIZE * 4
                , viewStarttop + viewHight - getTextHeight(text2, lable_size) / 2.0f
                , paint_Text
        );
        /*三、显示范围（量程）*/
        paint_Text.setColor(Color_set.INFORMATION_COLOR);
        String text_tab = null;
        String text3 = null;
        String text4 = null;
        /*四、显示阈值*/
        if (IS_AAmode) {
            text3 = String.format("阈值(%.1fmV)", Compute_Information.AE_dB_2_mV(THRESHOLD) >= System_Commen.AE_MAX_VIEW ? System_Commen.AE_MAX_VIEW : Compute_Information.AE_dB_2_mV(THRESHOLD));
        } else {
            text3 = String.format("阈值(%d%s)", THRESHOLD + SHOW_OFFSET, Format);
        }
        if (IS_AAmode) {
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

        Information_Drawer.Draw_Left_Tips(canvas, viewStartleft, viewStarttop, viewHight, text3, lable_size);


        Information_Drawer.Draw_Right_Tips(canvas, viewStartleft + viewWidth, viewStarttop, viewHight, text4, lable_size);


    }

    private void Draw_BKG(Canvas canvas) {


        /*文字画笔*/
        Paint paint_text = new Paint();
        paint_text.setColor(TEXT_COLOR);
        /*背景画笔*/
        Paint paint_data_bkg = new Paint();
        paint_data_bkg.setColor(DATA_BKG_COLOR);
        /*栅格线条画笔*/
        Paint paint_grid = new Paint();

        paint_grid.setColor(GRID_COLOR);
        paint_grid.setAntiAlias(true);
        paint_grid.setStrokeWidth(2);
        paint_grid.setStyle(Paint.Style.STROKE);
        canvas.drawRect(start_x + 1, start_y + 1, end_x - 1, end_y - 1, paint_data_bkg);
        /*背景栅格线*/
        float tab_x = (end_x - start_x) / 4.0f;
        float tab_y = (end_y - start_y) / 4.0f;
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(start_x + (i + 1) * tab_x, start_y, start_x + (i + 1) * tab_x, end_y, paint_grid);
            canvas.drawLine(start_x, start_y + (i + 1) * tab_y, end_x, start_y + (i + 1) * tab_y, paint_grid);

        }
        Path mPath = new Path();
        mPath.moveTo(start_x + 1, start_y + (end_y - start_y) / 2.0f);
        mPath.rQuadTo((end_x - start_x) / 4.0f, -(end_y - start_y) + 4, (end_x - start_x) / 2.0f, 0);
        mPath.rQuadTo((end_x - start_x) / 4.0f, (end_y - start_y) - 4, (end_x - start_x) / 2.0f - 4, 0);
        mPath.lineTo(start_x + 1, start_y + (end_y - start_y) / 2.0f);
        canvas.drawPath(mPath, paint_grid);
        draw_squre(start_x, start_y, end_x - start_x, end_y - start_y, paint_text, canvas);


        /*************************颜色表格****************************************/
        paint_text.setTextSize(TEXT_SIZE);
        float bar_start_x = (viewStartleft + viewWidth) - lable_size * 2.0f - lable_size * 1.3f;
        float bar_end_x = (viewStartleft + viewWidth) - lable_size * 2.0f - lable_size * 0.5f;

        float bar_start_y = start_y + (end_y - start_y) / 10;

        float bar_end_y = end_y - (end_y - start_y) / 10;

        float per_y = (bar_end_y - bar_start_y) / 100.0f;
        Paint paint_bar = new Paint();
        for (int i = 0; i < 100; i++) {
            paint_bar.setColor(Color_bean.getcolor(99 - i));
            canvas.drawRect(
                    bar_start_x,
                    bar_start_y + per_y * i,
                    bar_end_x - 1,
                    bar_start_y + per_y * (i + 1),
                    paint_bar
            );

        }
        draw_squre(bar_start_x, bar_start_y, bar_end_x - bar_start_x, bar_end_y - bar_start_y, paint_text, canvas);
        String text_top;
        String text_bouttom;
        if (IS_AAmode) {
            text_top = "2000";
            text_bouttom = "0";
        } else {
            text_top = "100";
            text_bouttom = "0";
        }

        float top_start_x = bar_start_x - (getTextWidth(text_top, TEXT_SIZE) - (bar_end_x - bar_start_x)) / 2.0f;
        float top_start_y = bar_start_y - getTextHeight(text_top, TEXT_SIZE) / 2.0f;
        canvas.drawText(text_top, top_start_x, top_start_y, paint_text);


        float Bouttom_start_x = bar_start_x - (getTextWidth(text_bouttom, TEXT_SIZE) - (bar_end_x - bar_start_x)) / 2.0f;
        float Bouttom_start_y = bar_end_y + getTextHeight(text_bouttom, TEXT_SIZE);
        canvas.drawText(text_bouttom, Bouttom_start_x, Bouttom_start_y, paint_text);


    }

    private void Draw_X(Canvas canvas) {

        float per_x = (end_x - start_x) / 20;
        /*文字画笔*/
        Paint paint_text = new Paint();
        paint_text.setColor(TEXT_COLOR);
        paint_text.setAntiAlias(true);

        paint_text.setTextSize(TEXT_SIZE);

        int per_value = 18;
        for (int i = 0; i < 21; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(start_x + per_x * i, end_y, start_x + per_x * i, end_y + 15.0f, paint_text);
                canvas.drawText(String.format("%d", per_value * i),
                        start_x + per_x * i - getTextWidth(String.format("%d", per_value * i), TEXT_SIZE) / 2.0f
                        , end_y + getTextHeight(String.format("%d", per_value * i), TEXT_SIZE) * 1.5f
                        , paint_text);
            } else {
                canvas.drawLine(start_x + per_x * i, end_y, start_x + per_x * i, end_y + 7.5f, paint_text);
            }
        }


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
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 5, false, TEXT_SIZE);
                break;
            case 60://200
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 4, false, TEXT_SIZE);
                break;
            case 40://20
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 3, false, TEXT_SIZE);
                break;
            case 20://2
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 2, false, TEXT_SIZE);
                break;
        }

    }

    /**
     * 绘制Y轴
     *
     * @param canvas
     */
    private void Draw_y(Canvas canvas) {
        /*文字画笔*/
        Paint paint_text = new Paint();
        paint_text.setColor(TEXT_COLOR);
        paint_text.setAntiAlias(true);
        paint_text.setStrokeWidth(1);
        paint_text.setTextSize(TEXT_SIZE);


        float per_y = (end_y - start_y) / 20.0f;
        int PER_TAB = SHOW_TAB / 20;
        for (int i = 0; i < 21; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(start_x - 15.0f, start_y + i * per_y, start_x, start_y + i * per_y, paint_text);
                canvas.drawText(String.format("%d", (20 - i) * PER_TAB + SHOW_OFFSET),
                        start_x - 20.0f - getTextWidth(String.format("%d", (20 - i) * PER_TAB + SHOW_OFFSET), TEXT_SIZE),
                        start_y + i * per_y + getTextHeight(String.format("%d", (20 - i) * PER_TAB + SHOW_OFFSET), TEXT_SIZE) / 4.0f
                        , paint_text);
            } else {
                canvas.drawLine(start_x - 7.05f, start_y + i * per_y, start_x, start_y + i * per_y, paint_text);
            }
        }

    }

    public void SetShowOffset(int offset) {
        SHOW_OFFSET = offset;
    }

    /**
     * 设置显示范围
     *
     * @param range
     */
    public void SetShowRange(int range) {
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
     * 绘制数据
     *
     * @param canvas
     */
    public void Draw_Data(Canvas canvas) {

        float per_y = (end_y - start_y - 2) / (float) SHOW_TAB;
        float per_x = (end_x - start_x - 2) / (float)TheOtherParameter.PHASE_COUNT;
        int Phase_Offst_Temp = PHASE / 6;
        int offset = 0;
        Paint paint_data = new Paint();

        if (Phase_Offst_Temp > 0) {
            /*左移*/
            offset =TheOtherParameter.PHASE_COUNT - Phase_Offst_Temp;

        } else {
            /*右移*/
            offset = -Phase_Offst_Temp;

        }

        /*Offset-60*/
        for (int i = offset; i <TheOtherParameter.PHASE_COUNT; i++) {

            for (int j = THRESHOLD == SHOW_TAB ? SHOW_TAB - 1 : THRESHOLD; j < SHOW_TAB; j++) {
                int vaule_temp = PRPD_VIEW_DATA[i][j];
                if (vaule_temp > 0) {
                    paint_data.setColor(Color_bean.getcolor(vaule_temp));
                    canvas.drawRect(
                            start_x + per_x * (i - offset) + 1
                            , end_y - per_y * j - 1
                            , start_x + per_x * ((i - offset) + 1) + 1
                            , end_y - per_y * j - per_y - 1
                            , paint_data
                    );
                }
            }
        }
        /*0-offset*/
        for (int i = 0; i < offset; i++) {
            for (int j = THRESHOLD == SHOW_TAB ? SHOW_TAB - 1 : THRESHOLD; j < SHOW_TAB; j++) {

                int vaule_temp = PRPD_VIEW_DATA[i][j];
                if (vaule_temp > 0) {
                    paint_data.setColor(Color_bean.getcolor(vaule_temp));
                    canvas.drawRect(
                            start_x + per_x * (TheOtherParameter.PHASE_COUNT - offset + i) + 1
                            , end_y - per_y * j - 1
                            , start_x + per_x * ((TheOtherParameter.PHASE_COUNT - offset + i) + 1) + 1
                            , end_y - per_y * j - per_y - 1
                            , paint_data
                    );
                }
            }

        }


    }

    /**
     * 判断获取的颜色
     *
     * @param count
     * @return
     */
    private int getcolor(int count) {
        if (count < 2) return Color_Bar[0];
        else if (count < 5) return Color_Bar[1];
        else if (count < 10) return Color_Bar[2];
        else if (count < 20) return Color_Bar[3];
        else if (count < 40) return Color_Bar[4];
        else if (count < 80) return Color_Bar[5];
        else if (count < 150) return Color_Bar[6];
        else if (count < 200) return Color_Bar[7];
        else if (count < 300) return Color_Bar[8];
        else if (count < 400) return Color_Bar[9];
        else if (count < 500) return Color_Bar[10];
        else if (count < 600) return Color_Bar[11];
        else if (count < 700) return Color_Bar[12];
        else if (count < 800) return Color_Bar[13];
        else if (count < 900) return Color_Bar[14];
        else return Color_Bar[15];
    }

    /*设置方法*/

    /**
     * 设置背景颜色
     *
     * @param color RGB颜色
     */
    public void SetBackGroundColor(int color) {
        BKG_COLOR = color;
    }

    /**
     * 设置数据
     *
     * @param data 所有相位单元的统计数据
     */
    public void SetData(List<PrpdData> data) {
        DATA = data;
        invalidate();
    }

    /**
     * 设置显示单位
     *
     * @param format
     */
    public void SetFormat(String format) {
        this.Format = format;
        invalidate();
    }



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

    /**
     * 统计多个个周期内的数据
     *
     * @param Data
     */
    public void Compute_PRPD(List<P_Data> Data) {
        /*添加所有的数据*/
        m_listHistryData.addAll(Data);
        /*60个相位*/
        int temp;
        byte[] data = new byte[TheOtherParameter.PHASE_COUNT];
        for (int j = 0; j < Data.size(); j++) {
            System.arraycopy(Data.get(j).getData(), 0, data, 0,TheOtherParameter.PHASE_COUNT);
            for (int i = 0; i <TheOtherParameter.PHASE_COUNT; i++) {
                temp = (int) data[i] & 0xff;
                temp = (int) (temp / 3.0f);
                if ((temp < 80) && (temp > 0)) {
                    PRPD_VIEW_DATA[i][temp]++;
                }
            }
        }
        /*********使用自动清除功能*********/
        if (Auto_Clear_Data) {
            /*大于清除时间*/
            int temp_l = (m_listHistryData.size() - AUTO_CLEAR_TIME * 50);
            if ((m_listHistryData.size() / 50) >= AUTO_CLEAR_TIME) {
                for (int k = 0; k < temp_l; k++) {
                    System.arraycopy(m_listHistryData.get(0).getData(), 0, data, 0,TheOtherParameter.PHASE_COUNT);
                    m_listHistryData.remove(0);
                    for (int l = 0; l <TheOtherParameter.PHASE_COUNT; l++) {
                        temp = (int) data[l] & 0xff;
                        temp = (int) (temp / 3.0f);
                        if (temp < 80) {
                            PRPD_VIEW_DATA[l][temp]--;
                        }
                    }
                }
            }
        }
        invalidate();
    }

    /**
     * 获取图上显示的标准数据
     *
     * @return
     */
    public short[][] Get_Stanrd_Data() {
        short[][] temp = new short[60][240];
        int temp_value = 0;
        for (P_Data Data_Temp : m_listHistryData) {

            for (int i = 0; i < 60; i++) {
                temp_value = ((int) Data_Temp.getData()[i] & 0xff);
                if (temp_value >= 240) {
                    temp_value = 240;
                }
                if (temp_value > 0) {
                    temp[i][temp_value]++;
                }
            }

        }
        return temp;
    }


    /**
     * 清除PRPD视图数据
     */
    public void Clear_PRPD() {

        m_listHistryData.clear();
        /*清除数据*/
        Remove_PRPD_Data();
        /*更新视图*/

        invalidate();

    }


    public void setAutoClear(int time, boolean Used) {
        m_listHistryData.clear();
        Auto_Clear_Data = Used;
        AUTO_CLEAR_TIME = time;
    }

    private void Remove_PRPD_Data() {
        for (int i = 0; i <TheOtherParameter.PHASE_COUNT; i++) {
            for (int j = 0; j < 80; j++) {
                PRPD_VIEW_DATA[i][j] = 0;
            }
        }
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
                SHOW_TAB_CAN_TAUCHED = true;

                if ((touch_start_X > btn_start_x) &&
                        (touch_start_X < btn_end_x) &&
                        (touch_start_Y > btn_start_y) &&
                        (touch_start_Y < btn_end_y)
                        ) {
                    m_bISbtnFouce = true;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                touch_end_X = event.getX();
                touch_end_Y = event.getY();
                /*判断是否超出视图区域*/
                if ((touch_end_X >= (viewStartleft + viewWidth)) || (touch_end_X <= viewStartleft)) {
                    /*左右边界超限*/
                    return true;
                }
                if ((touch_end_Y <= viewStarttop) || (touch_end_Y >= (viewStarttop + viewHight))) {
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

                /*判断是否超出视图区域*/
                if ((touch_end_X >= (viewStartleft + viewWidth)) || (touch_end_X <= viewStartleft)) {
                    /*左右边界超限*/
                    SHOW_TAB_CAN_TAUCHED = false;
                    return true;
                }
                if ((touch_end_Y <= viewStarttop) || (touch_end_Y >= (viewStarttop + viewHight))) {
                    /*上下边界超限*/
                    SHOW_TAB_CAN_TAUCHED = false;
                    return true;
                }
                /*prpd按钮*/
                if ((touch_end_X > btn_start_x) &&
                        (touch_end_X < btn_end_x) &&
                        (touch_end_Y > btn_start_y) &&
                        (touch_end_Y < btn_end_y)
                        ) {
                    m_bISbtnFouce = false;
                    if (clear_listener != null) {
                        clear_listener.Clear_Event();
                    }
                    if (CLEAR_CANUSED) {
                        Clear_PRPD();
                    }


                } else {
                    m_bISbtnFouce = false;
                    invalidate();

                }
                /*******显示量程调节*******/
                SHOW_TAB_Touch_Proc();
        }
        return true;
    }

    private void SHOW_TAB_Touch_Proc() {
        if (!SHOW_TAB_CAN_TAUCHED) {
            return;
        }
        if ((Math.abs(touch_end_X - touch_start_X) <= Math.abs(touch_end_Y - touch_start_Y))) {
            /*右屏幕*/
            if (touch_start_X >= (viewStartleft + viewWidth / 2.0f)) {
                if ((touch_end_Y - touch_start_Y) >= 10.0f) {
                    /*下滑*/
                    SHOW_TAB -= 20;
                    SHOW_TAB = SHOW_TAB <= 20 ? 20 : SHOW_TAB;
                    THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
                    Touch_Notifier();
                } else if ((touch_end_Y - touch_start_Y) <= -10.0f) {
                    /*上滑动*/
                    SHOW_TAB += 20;
                    SHOW_TAB = SHOW_TAB >= MAX_TAB ? MAX_TAB : SHOW_TAB;
                    THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
                    Touch_Notifier();
                }
            }
        }
        SHOW_TAB_CAN_TAUCHED = false;
    }

    /**
     * 左右屏幕事件
     */
    private void Touched_Left_Right_Screen() {
        if (touch_start_X >= (viewStartleft + viewWidth / 2.0f)) {
            /*右半屏幕*/
            /****不考虑，单次滑动有效*****/
        } else {
            /*左半屏幕*/

            /*显示阈值调节*/
            float temp_per = viewHight / 6.0f;
            THRESHOLD = (int) (m_ntempThreshold - (int) ((touch_end_Y - touch_start_Y) / temp_per));
            /*判断是否超出显示量程*/
            THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
            THRESHOLD = THRESHOLD <= 0 ? 0 : THRESHOLD;
            Touch_Notifier();
        }


    }

    /**
     * 上下屏幕事件
     */
    private void Touched_Top_Bottom_Screen() {
        if (Math.abs(touch_end_X - touch_start_X) < 5.0f) {
            return;
        }
        if (touch_start_Y >= (viewStarttop + viewHight / 2.0f)) {
            /*下半屏幕*/
            /*相位调节*/
            float temp_per = viewWidth / 6.0f;
            PHASE = (int) (m_ntempPhase + (int) ((touch_end_X - touch_start_X) / temp_per) * 6);
            /*判断是否超出范围*/
            PHASE = PHASE <= -180 ? -180 : PHASE;
            PHASE = PHASE >= 180 ? 180 : PHASE;
            Touch_Notifier();
        } else {
            /*上半屏幕不使用*/

        }

    }

    /**
     * 清除Prpd
     */
    private void clean_Prpd() {
        for (int i = 0; i <TheOtherParameter.PHASE_COUNT; i++) {
            for (int j = 0; j < 80; j++) {
                PRPD_VIEW_DATA[i][j] = 0;

            }
        }
        invalidate();
    }

    /**
     * 触摸事件通知
     */
    private void Touch_Notifier() {

        if (listenr != null) {
            listenr.PrpsTouchEvent(new PrpsEvent(SHOW_TAB, PHASE, THRESHOLD));
        }
        invalidate();

    }

    /***
     * 设置监听器
     */
    public void seteventlistenr(PrpsListener listenr) {
        this.listenr = listenr;
    }

    /**
     * 填充示例数据
     */
    public void set_demo() {

        rodomdata();
        invalidate();

    }

    /**
     * 随机数据生成
     *
     * @return
     */
    private void rodomdata() {


        for (int i = 0; i <TheOtherParameter.PHASE_COUNT; i++) {
            for (int j = 0; j < 40; j++) {
                PRPD_VIEW_DATA[i][j] = (int) (new Random().nextInt(1000) + 1);

            }
        }
    }

    public void Set_Review_Data(int[][] data) {
        PRPD_VIEW_DATA = data;
        invalidate();
    }

    public void SetData(short[] Data)
    {
        for(int i = 0;i<60;i++)
        {

            for(int j = 0;j<80;j++)
            {
                PRPD_VIEW_DATA[i][j] = Data[i*80+j];
            }
        }
        invalidate();
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
        if (IS_AAmode) {
            MAX_TAB = AA_MAX_TEB;
        }
        if (SHOW_TAB >= MAX_TAB) {
            SHOW_TAB = MAX_TAB;
        }

        invalidate();
    }

    public void set_information(int tab, int threshold) {
        this.SHOW_TAB = tab;
        this.THRESHOLD = threshold;
        if (IS_AAmode) {
            MAX_TAB = AA_MAX_TEB;
        }
        if (SHOW_TAB >= MAX_TAB) {
            SHOW_TAB = MAX_TAB;
        }
        invalidate();
    }

    public void setIS_AAmode(boolean is_aAmode) {
        IS_AAmode = is_aAmode;
        if (is_aAmode) {
            MAX_TAB = AA_MAX_TEB;
        }
        invalidate();
    }


    public void setthreshold(int threshold) {
        this.THRESHOLD = threshold;
        invalidate();
    }

    /**
     * @param listener
     */
    public void setClearListener(Clear_Listener listener) {
        clear_listener = listener;
    }
}
