package PDChartsPack.pack_pd_charts;

import PDChartsPack.pack_Run_Data_Cash.P_Data;
import PDChartsPack.pack_commen.System_Commen;
import PDChartsPack.pack_compute.Compute_Information;
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
import java.util.Random;
/**
 * Created by SONG on 2017/11/9 15:31.
 */

public class TofView extends View {

    private boolean CLEAR_CAN_USED = false;


    private int COLOR_1 = Color_set.ARROW_Color_1;
    private int COLOR_2 = Color_set.ARROW_Color_2;
    private int COLOR_3 = Color_set.ARROW_Color_3;


    private boolean SHOW_TAB_CAN_TAUCHED = false;
    private int TOF_DATA_COLOR = Color_set.TOF_DATA_COLOR;

    private boolean IS_AAmode = false;
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
     * 数据背景
     */
    private int DATA_BACK = Color_set.DATA_BKG_COLOR;
    private String Format = "dB";
    private String Y_LABLE = "幅值(dB)";
    private String X_LABLE = "时间(ms)";

    private Paint painttext;
    private Paint paintdatabkg;
    private Paint paintborden;
    private Paint paintgrid;
    private float viewStartLeft;
    private float viewStartTop;
    private float viewHeight;
    private float viewWidth;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;
    private List<Integer> DATA = null;
    private Paint paintdata;


    /**
     * 显示范围
     */
    private int SHOW_TAB = 80;
    private int THRESHOLD = 0;
    private int m_ntempShowTab = 0;
    private int m_ntempThreshold = 0;
    private int SHOW_OFFSET = 0;
    private float touch_start_X;
    private float touch_start_Y;

    private float touch_end_X;
    private float touch_end_Y;

    private int[][] RESULT_DATA = new int[200][80];
    private int TOF_THRESHOLD = 10;

    /**
     * 新数据起始位置
     */
    private int Last_Start = 0;
    private byte[] m_objTempData = new byte[TheOtherParameter.PHASE_COUNT];
    private int Data_Period_Offset = 5;
    private boolean getfirst = false;

    private int Temp_Point_X = 0;
    private int Temp_Point_Y = 0;
    private int m_nTofdelta = 0;

    private int m_nTofTimeLen = 0;
    private int m_nTofClearTimeLen = 0;
    private int m_nMaxTime = 600; //Time*3 实际为200ms
    private int m_nMinTime = 0;

    private int AUTO_CLEAR_TIME = 10;
    private boolean Auto_Clear_Data = false;
    private List<P_Data> m_listHistryData = new ArrayList<P_Data>();

    private Tof_Listener listener1;

    private float btn_start_x;
    private float btn_start_y;
    private float btn_end_x;
    private float btn_end_y;
    private boolean m_bISbtnFouce = false;

    private Clear_Listener clear_listener;
    private float lable_size;

    private int PERIOD_CNT = 10;
    private int percnttemp;

    public TofView(Context context) {
        super(context);

        for(int i=0;i<200;i++)
        {
            for(int j=0;j<80;j++)
            {
                RESULT_DATA[i][j] = 0;
            }
        }
    }

    public TofView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        for(int i=0;i<200;i++)
        {
            for(int j=0;j<80;j++)
            {
                RESULT_DATA[i][j] = 0;
            }
        }
    }

    public TofView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        for(int i=0;i<200;i++)
        {
            for(int j=0;j<80;j++)
            {
                RESULT_DATA[i][j] = 0;
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(BKG_COLOR);
        Information_Init();


        Paint_Init();
        Draw_Bkg(canvas);
        if (CLEAR_CAN_USED) {
            Draw_Remove_Button(canvas);
        }
        Draw_Title(canvas);
        Draw_Data(canvas);
        Draw_Information(canvas);
        Draw_TriCyle(canvas);
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
        /*控件边界*/
        viewStartLeft = super.getLeft();
        viewStartTop = super.getTop();

        viewHeight = super.getHeight();
        viewWidth = super.getWidth();

        TEXT_SIZE = viewHeight / 30.0f;
        lable_size = viewHeight / 20.0f;
        /*图表边界*/
        start_x = viewStartLeft + (viewWidth / 10.0f) * 1.5f + lable_size;
        start_y = viewStartTop + (viewHeight / 10.0f) * 1.0f + lable_size;
        end_x = viewStartLeft + (viewWidth / 10.0f) * 9.0f - lable_size;
        end_y = viewStartTop + (viewHeight / 10.0f) * 8.5f - lable_size;

    }

    public void SetClearCanUsed(boolean isused) {
        CLEAR_CAN_USED = isused;
        invalidate();
    }

    private void Draw_Remove_Button(Canvas canvas) {

        /*按键定位*/
        btn_start_x = start_x + (end_x - start_x) / 2.0f - lable_size*2;
        btn_start_y = viewStartTop + lable_size/5;
        btn_end_x = start_x + (end_x - start_x) / 2.0f +  lable_size*2;
        btn_end_y = start_y - lable_size/5;

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
     * 设置回放数据
     *
     * @param data
     */
    public void set_Review_Data(int[][] data) {
        RESULT_DATA = data;
        invalidate();
    }

    /**
     * 绘制图表标题
     *
     * @param canvas
     */
    private void Draw_Title(Canvas canvas) {
        Information_Drawer.Draw_Title(canvas,viewStartLeft,viewStartTop,viewWidth,viewHeight,"TOF",lable_size);

    }


    /**
     * @param canvas
     */
    private void Draw_Data(Canvas canvas) {
        if (RESULT_DATA == null) {
            return;
        }
        float per_x = (end_x - start_x) / (PERIOD_CNT*20+2);
        float per_y = (end_y - start_y) / (SHOW_TAB + 2.0f);
        for (int i = 0; i < PERIOD_CNT*20; i++) {
            for (int j = 0; j < 80; j++) {
                if (RESULT_DATA[i][j] == 1) {
                    if (j > THRESHOLD && j < SHOW_TAB) {
                        canvas.drawCircle((i + 1) * per_x + start_x
                                , end_y - j * per_y, 3, paintdata);
                    }
                }

            }

        }
    }

    /**
     * 绘制图表背景
     *
     * @param canvas
     */
    private void Draw_Bkg(Canvas canvas) {

        /*背景*/
        Path pathbkg = new Path();
        pathbkg.moveTo(start_x, start_y);
        pathbkg.lineTo(end_x, start_y);
        pathbkg.lineTo(end_x, end_y);
        pathbkg.lineTo(start_x, end_y);
        pathbkg.close();
        canvas.drawPath(pathbkg, paintdatabkg);
        canvas.drawPath(pathbkg, paintborden);


        /**绘制X轴**/
        draw_AxiX(canvas);
        /**绘制Y轴**/
        if (IS_AAmode) {
            Draw_AsiY_AA(canvas);
        } else {
            draw_AsiY(canvas);
        }

        float tab_y = (end_y - start_y) / 4.0f;
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(start_x, start_y + (i + 1) * tab_y, end_x, start_y + (i + 1) * tab_y, paintgrid);

        }


//        /*X轴*/
//        canvas.drawText(X_LABLE, start_x + (end_x - start_x) / 2.0f - getTextWidth(X_LABLE, TEXT_SIZE) / 2.0f,
//                end_y + TEXT_SIZE*4.0f,
//                painttext);


    }

    /**
     * 设置视图使用AA模式
     *
     * @param USE_AAmode
     */
    public void Set_AAmode(boolean USE_AAmode) {
        IS_AAmode = USE_AAmode;
        invalidate();
    }


    /**
     * 绘制AA模式下的Y轴
     *
     * @param canvas
     */
    private void Draw_AsiY_AA(Canvas canvas) {
        switch (SHOW_TAB) {
            case 80:
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 5, false, TEXT_SIZE);
                break;
            case 60:
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 4, false, TEXT_SIZE);
                break;
            case 40:
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 3, false, TEXT_SIZE);
                break;
            case 20:
                AA_Asiy.Draw_Asiy(canvas, start_x, start_y, end_y, 2, false, TEXT_SIZE);
                break;
        }

    }

    public void SetOffset(int offset) {
        SHOW_OFFSET = offset;
        invalidate();
    }

    /**
     * 绘制X轴的刻度
     *
     * @param canvas
     */
    private void draw_AxiX(Canvas canvas) {
        /*水平坐标*/
        float per_X = (end_x - start_x) / 10.0f;
        float per_x = per_X / 5.0f;
        for (int i = 0; i < 11; i++) {
            if (i == 0 || i == 10) {
                canvas.drawLine(start_x + i * per_X
                        , end_y
                        , start_x + i * per_X
                        , end_y + 15.0f
                        , painttext
                );
                canvas.drawText(String.format("%d", i * (PERIOD_CNT*2))
                        , start_x + i * per_X - getTextWidth(String.format("%d", i * PERIOD_CNT*2), TEXT_SIZE) / 2.0f
                        , end_y  + getTextHeight(String.format("%d", i * PERIOD_CNT*2), TEXT_SIZE)* 1.5f
                        , painttext);

                if (i != 10) {
                    for (int j = 0; j < 4; j++) {
                        canvas.drawLine(start_x + i * per_X + (j + 1) * per_x
                                , end_y
                                , start_x + i * per_X + (j + 1) * per_x
                                , end_y + 7.5f
                                , painttext
                        );
                    }
                }

            } else {
                canvas.drawLine(start_x + i * per_X
                        , start_y
                        , start_x + i * per_X
                        , end_y
                        , paintgrid
                );
                canvas.drawLine(start_x + i * per_X
                        , end_y
                        , start_x + i * per_X
                        , end_y + 15.0f
                        , painttext
                );
                canvas.drawText(String.format("%d", i * PERIOD_CNT*2)
                        , start_x + i * per_X - getTextWidth(String.format("%d", i * PERIOD_CNT*2), TEXT_SIZE) / 2.0f
                        , end_y  + getTextHeight(String.format("%d", i * PERIOD_CNT*2), TEXT_SIZE) * 1.5f
                        , painttext);

                for (int j = 0; j < 4; j++) {
                    canvas.drawLine(start_x + i * per_X + (j + 1) * per_x
                            , end_y
                            , start_x + i * per_X + (j + 1) * per_x
                            , end_y + 7.5f
                            , painttext
                    );
                }

            }
        }


    }


    /**
     * h绘制Y轴
     *
     * @param canvas
     */
    private void draw_AsiY(Canvas canvas) {
        /*竖直坐标*/
        float per_Y = (end_y - start_y) / 4.0f;
        float per_y = per_Y / 5.0f;
        int tab = SHOW_TAB / 4;
        for (int i = 0; i < 5; i++) {
            if (i == 0 || i == 4) {
                canvas.drawLine(start_x, start_y + i * per_Y,
                        start_x - 15.0f, start_y + i * per_Y, painttext);
                canvas.drawText(String.format("%d", (4 - i) * tab + SHOW_OFFSET)
                        , start_x - 20.0f - getTextWidth(String.format("%d", (4 - i) * tab + SHOW_OFFSET), TEXT_SIZE)
                        , start_y + i * per_Y + getTextHeight(String.format("%d", (4 - i) * tab + SHOW_OFFSET), TEXT_SIZE) / 4.0f
                        , painttext
                );
                if (i != 4) {
                    for (int j = 0; j < 4; j++) {
                        canvas.drawLine(start_x
                                , start_y + i * per_Y + (j + 1) * per_y
                                , start_x - 7.5f
                                , start_y + i * per_Y + (j + 1) * per_y
                                , painttext);
                    }
                }

            } else {
                canvas.drawLine(start_x, start_y + i * per_Y,
                        end_x, start_y + i * per_Y, paintgrid);
                canvas.drawLine(start_x, start_y + i * per_Y,
                        start_x - 15.0f, start_y + i * per_Y, painttext);
                canvas.drawText(String.format("%d", (4 - i) * tab + SHOW_OFFSET)
                        , start_x - 20.0f - getTextWidth(String.format("%d", (4 - i) * tab + SHOW_OFFSET), TEXT_SIZE)
                        , start_y + i * per_Y + getTextHeight(String.format("%d", (4 - i) * tab + SHOW_OFFSET), TEXT_SIZE) / 4.0f
                        , painttext);
                for (int j = 0; j < 4; j++) {
                    canvas.drawLine(start_x
                            , start_y + i * per_Y + (j + 1) * per_y
                            , start_x - 7.5f
                            , start_y + i * per_Y + (j + 1) * per_y
                            , painttext);
                }
            }
        }
    }

    private void Paint_Init() {
        /*文字画笔*/
        painttext = new Paint();
        painttext.setColor(TEXT_COLOR);
        painttext.setTextSize(TEXT_SIZE);
        painttext.setAntiAlias(true);
        painttext.setStrokeWidth(1);
        /*图表背景*/
        paintdatabkg = new Paint();
        paintdatabkg.setAntiAlias(true);
        paintdatabkg.setColor(DATA_BACK);

        /*边界*/
        paintborden = new Paint();
        paintborden.setColor(TEXT_COLOR);
        paintborden.setAntiAlias(true);
        paintborden.setStyle(Paint.Style.STROKE);

        /*栅格线条*/
        paintgrid = new Paint();
        paintgrid.setColor(GRID_COLOR);
        paintgrid.setStrokeWidth(1);
        paintgrid.setAntiAlias(true);

        paintdata = new Paint();
        paintdata.setColor(TOF_DATA_COLOR);
        paintdata.setAntiAlias(true);
        X_LABLE = "时间(ms)";
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
     * 设置数据
     *
     * @param data 所有的数据
     */
    public void SetData(List<Integer> data) {
        DATA = data;
        invalidate();
    }

    /**
     * 设置显示格式
     *
     * @param format 显示格式
     */
    public void SetFormat(String format) {
        Format = format;
        invalidate();
    }

    /**
     * 设置显示信息
     *
     * @param tab       显示量程
     * @param threshold 显示阈值
     */

    public void SetShowInformation(int tab, int threshold) {
        SHOW_TAB = tab;
        THRESHOLD = threshold;
        invalidate();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //捕获到单指按下，或者说捕获到第一个指头按下
                touch_start_X = event.getX();
                touch_start_Y = event.getY();

                /*****记录开始滑动以前的值******/
                m_ntempThreshold = THRESHOLD;
                m_ntempShowTab = SHOW_TAB;
                percnttemp = PERIOD_CNT;
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
                    if (CLEAR_CAN_USED) {
                        Clear_Tof();
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

    private void Touched_Top_Bottom_Screen() {
        if (Math.abs(touch_end_X - touch_start_X) < 5.0f) {
            return;
        }
        if (touch_start_Y >= (viewStartTop + viewHeight / 2.0f)) {
            /*下半屏幕*/
            /*相位调节*/
            float temp_per = viewWidth / 6.0f;
            PERIOD_CNT = (int) (percnttemp + (int) ((touch_end_X - touch_start_X) / temp_per));
            /*判断是否超出范围*/
            PERIOD_CNT = PERIOD_CNT <= 1 ? 1 : PERIOD_CNT;
            PERIOD_CNT = PERIOD_CNT >= 10 ? 10 : PERIOD_CNT;
            invalidate();
        } else {
            /*上半屏幕不使用*/

        }
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

    /**
     * 左右屏幕事件
     */
    private void Touched_Left_Right_Screen() {
        if (touch_start_X >= (viewStartLeft + viewWidth / 2.0f)) {
            /*右半屏幕*/

        } else {
            /*左半屏幕*/
            /****不考虑，单次滑动有效*****/
            /*显示阈值调节*/
            float temp_per = viewHeight / 6.0f;
            THRESHOLD = (int) (m_ntempThreshold - (int) ((touch_end_Y - touch_start_Y) / temp_per));
            /*判断是否超出显示量程*/
            THRESHOLD = THRESHOLD >= SHOW_TAB ? SHOW_TAB : THRESHOLD;
            THRESHOLD = THRESHOLD <= 0 ? 0 : THRESHOLD;
            Touch_Notifier();
        }
    }

    /**
     * 触摸事件通知器
     */
    private void Touch_Notifier() {

        if (listener1 != null) {
            listener1.TofEvent(SHOW_TAB, THRESHOLD);
        }
        invalidate();
    }

    /**
     * 设置谱图信息
     */
    public void SetInformation(int tab, int threshold) {
        this.SHOW_TAB = tab;
        this.THRESHOLD = threshold;
        invalidate();
    }

    /**
     * 设置监听器
     */
    public void setTouchEventListener(Tof_Listener listener) {
        listener1 = listener;
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
            text4 = "量程(" + SHOW_OFFSET + "~" + text_tab + " mV" + ")";
        } else {
            text4 = String.format("量程(%d~%d%s)", SHOW_OFFSET, SHOW_TAB + SHOW_OFFSET, Format);
        }

        Information_Drawer.Draw_Left_Tips(canvas,viewStartLeft,viewStartTop,viewHeight,text3,lable_size);


        Information_Drawer.Draw_Right_Tips(canvas,viewStartLeft + viewWidth,viewStartTop,viewHeight,text4,lable_size);
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
        tri_path.lineTo(tri_start_x - TEXT_SIZE, tri_start_y - TEXT_SIZE/2.0F);
        tri_path.lineTo(tri_start_x - TEXT_SIZE, tri_start_y + TEXT_SIZE/2.0F);
        tri_path.close();
        Paint tri_paint = new Paint();
        tri_paint.setStyle(Paint.Style.FILL);
        tri_paint.setColor(Color.argb(0x80, 0xff, 0x00, 0x00));
        canvas.drawPath(tri_path, tri_paint);

    }

    /**
     * 计算Tof
     *
     * @param Rc_Data
     */
    public void Compute_Tof(List<P_Data> Rc_Data) {
        /***数据为空则创建数据对象**/
        if (RESULT_DATA == null) {
            RESULT_DATA = new int[200][80];
        }
        m_nTofTimeLen = 0;
        /*添加所有的数据*/
        m_listHistryData.addAll(Rc_Data);

        int temp = 0;
        float f_temp = 0;

        /*********使用自动清除功能*********/
        if (Auto_Clear_Data) {
            if ((m_listHistryData.size()) > AUTO_CLEAR_TIME * 50) {
                m_nTofClearTimeLen = 0;
                int temp_l = (m_listHistryData.size() - AUTO_CLEAR_TIME * 50);
                for (int k = 0; k < temp_l; k++) {
                    m_objTempData = m_listHistryData.get(0).getData();
                    for (int j = 0; j < m_objTempData.length; j++) {
                        temp = (int) m_objTempData[j] & 0xff;
                        temp /= 3;
                        if (temp > m_nTofdelta) {
                            if ((m_nTofClearTimeLen >= m_nMinTime) && (m_nTofClearTimeLen <= m_nMaxTime)) {
                                f_temp = (m_nTofClearTimeLen / 3.0f + 0.5f) >= 199 ? 199 : (int) (m_nTofClearTimeLen / 3.0f + 0.5f);
                                RESULT_DATA[(int) f_temp][temp] = 0;
                            }
                            m_nTofClearTimeLen = 0;

                        } else {
                            m_nTofClearTimeLen++;
                        }
                    }
                    m_listHistryData.remove(0);
                }
            }
        }
        /**统计**/
        for (int i = 0; i < Rc_Data.size(); i++) {
            m_objTempData = Rc_Data.get(i).getData();
            for (int j = 0; j < m_objTempData.length; j++) {
                temp = ((int) m_objTempData[j]) & 0xff;
                temp /= 3;
                if (temp > m_nTofdelta) {
                    if ((m_nTofTimeLen >= m_nMinTime) && (m_nTofTimeLen <= m_nMaxTime)) {
                        f_temp = (m_nTofTimeLen / 3.0f + 0.5f) >= 199 ? 199 : (int) (m_nTofTimeLen / 3.0f + 0.5f);
                        RESULT_DATA[(int) f_temp][temp] = 1;
                    }
                    m_nTofTimeLen = 0;

                } else {
                    m_nTofTimeLen++;
                }
            }
        }
        invalidate();
    }

    public List<P_Data> Get_Tof_Data() {
        return m_listHistryData;
    }

    public void setAutoClear(int time, boolean Used) {
        m_listHistryData.clear();
        Auto_Clear_Data = Used;
        AUTO_CLEAR_TIME = time;
    }

    public void SetData(byte[] src)
    {
        for(int i = 0;i<200;i++)
        {
            for(int j=0;j<80;j++)
            {
                RESULT_DATA[i][j] = src[i*80+j];
            }
        }
        invalidate();

    }

    /**
     * 清除Tof
     */
    public void Clear_Tof() {
        if (RESULT_DATA != null) {
            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < 80; j++) {
                    RESULT_DATA[i][j] = 0;
                }
            }
        }
        m_nTofTimeLen = 0;
        m_listHistryData.clear();
        invalidate();

    }

    public void setTOF_THRESHOLD(int threshold) {
        this.TOF_THRESHOLD = threshold;

    }

    public void Set_Demo() {
        List<P_Data> demo_data = new ArrayList<P_Data>();
        for (int i = 0; i < 50; i++) {
            byte[] temp = new byte[TheOtherParameter.PHASE_COUNT];
            for (int j = 0; j <TheOtherParameter.PHASE_COUNT; j++) {
                if (j == 10 || j == 50) {
                    temp[j] = (byte) (new Random().nextInt(50) + 10);
                } else {
                    temp[j] = 0;
                }
            }
            demo_data.add(new P_Data(temp));
        }
        Compute_Tof(demo_data);
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
