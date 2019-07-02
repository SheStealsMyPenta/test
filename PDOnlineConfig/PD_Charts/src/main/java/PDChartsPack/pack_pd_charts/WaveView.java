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
import PDChartsPack.pack_compute.Compute_Information;

import java.util.List;


/**
 * Created by SONG on 2018/4/23 17:16.
 * The final explanation right belongs to author
 */
public class WaveView extends View {

    private float viewStartLeft;
    private float viewStartTop;
    private float viewHeight;
    private float viewWidth;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;


    private float[] WAVE_DATA = new float[300];

    private int Asiy_Cnt = 2;

    private float MaxValue = 0.0f;

    private float TAB = 1.0f;


    private String Y_FORMAT = "mV";

    private String X_FORMAT = "ms";

    private String Y_LABLE = "幅值(mV)";

    private String X_LABLE = "时间(ms)";

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

    private boolean IS_AEmode = true;

    private float lable_size;

    /**
     * 周期数
     **/
    private int PERIOD_CNT = 5;
    /***档位选项*/
    private int TAB_OPTION = 3;

    private int COLOR_1 = Color_set.ARROW_Color_1;
    private int COLOR_2 = Color_set.ARROW_Color_2;
    private int COLOR_3 = Color_set.ARROW_Color_3;

    private static final float PAT = 3.141592627f;

    private Paint painttext;
    private Paint paintdatabkg;
    private Paint paintborden;
    private Paint paintgrid;
    private Paint paintdata;
    private Paint paint_bkg_sin;
    private float touch_start_x;
    private float touch_start_y;
    private int percnttemp;
    private int taboptiontemp;
    private float touch_end_x;
    private float touch_end_y;


    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(BKG_COLOR);
        Information_Init();
        init(canvas);
        Draw_Y_Lable(canvas);
    }

    private void Draw_Y_Lable(Canvas canvas) {
        float offset = 0;
        String msg = "幅值(mV)";

        offset = getTextWidth("0000", TEXT_SIZE) + TEXT_SIZE / 3.0f + 15.0f;


        Information_Drawer.Draw_left_Center(canvas, start_x - offset, start_y, (end_y - start_y), msg, TEXT_SIZE);

    }

    /**
     * 绘制背景sin曲线
     */
    private void Draw_Sin_Wave(Canvas canvas) {
        float sin_start_x = start_x + 1.0f;
        float sin_start_y = (end_y - start_y) / 2.0f + start_y;
        float sin_end_x = end_x - 1.0f;
        float per_x = (sin_end_x - sin_start_x) / 1000.0f;
        float pro_x = sin_start_x;
        float pro_y = sin_start_y;
        float temp_height = (end_y - start_y) / 2.0f - 2.0f;
        float temp_y;
        for (int i = 0; i < 1000; i++) {
            temp_y = -(float) Math.sin((PERIOD_CNT * 2) * PAT / 1000 * i) * temp_height + sin_start_y;
            canvas.drawLine(pro_x, pro_y
                    , i * per_x + sin_start_x
                    , temp_y
                    , paint_bkg_sin
            );
            pro_x = i * per_x + sin_start_x;
            pro_y = temp_y;
        }

    }

    /**
     * 信息初始化
     */
    private void Information_Init() {
        BordenInit();

    }

    /**
     * 初始化
     */
    private void init(Canvas canvas) {
        Paint_Init();
        Draw_Bkg(canvas);
        Draw_Information(canvas);
        Draw_Data(canvas);
    }

    private void Draw_Information(Canvas canvas) {

        Paint paint_Text = new Paint();
        paint_Text.setColor(Color_set.INFORMATION_COLOR);
        paint_Text.setTextSize(lable_size);
        paint_Text.setAntiAlias(true);

        String text4 = String.format("量程(0~%d%s)", (int) TAB, "mV");

        Information_Drawer.Draw_Right_Tips(canvas, viewStartLeft + viewWidth, viewStartTop, viewHeight, text4, lable_size);


        String text2 = String.format("时长%d(ms)", PERIOD_CNT * 20);
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

    }

    /**
     * 数据绘制
     *
     * @param canvas
     */
    private void Draw_Data(Canvas canvas) {
        if (MaxValue == 0.0f) {
            return;
        }

        float Per_Y = (end_y - start_y - 2.0f) / TAB;
        float per_X = (end_x - start_x - 2.0f) / (PERIOD_CNT * 60.0f);
        float temp = 0;

        Path dataPath = new Path();
        temp = WAVE_DATA[0];
        if (WAVE_DATA[0] > TAB) {
            temp = TAB;
        }

        if (WAVE_DATA[0] < 0) {
            temp = 0;
        }
        dataPath.moveTo(start_x + 1, end_y - temp * Per_Y);

        for (int i = 1; i < PERIOD_CNT * 60; i++) {
            temp = WAVE_DATA[i];
            if (WAVE_DATA[i] > TAB) {
                temp= TAB;
            }

            if (WAVE_DATA[i] < 0) {
                temp = 0;
            }
            dataPath.lineTo((start_x + 1) + (i * per_X), end_y - temp * Per_Y);
        }

        canvas.drawPath(dataPath, paintdata);


    }


    /**
     * 背景绘制
     *
     * @param canvas
     */
    private void Draw_Bkg(Canvas canvas) {
        /**绘制背景**/
        /*背景*/
        Path pathbkg = new Path();
        pathbkg.moveTo(start_x, start_y);
        pathbkg.lineTo(end_x, start_y);
        pathbkg.lineTo(end_x, end_y);
        pathbkg.lineTo(start_x, end_y);
        pathbkg.close();
        canvas.drawPath(pathbkg, paintdatabkg);
        canvas.drawPath(pathbkg, paintborden);

        /**绘制标题**/
        Draw_Title(canvas);

        /**绘制X轴**/
        draw_AxiX(canvas);

        /**绘制Y轴**/
        draw_AxiY(canvas);
        /**绘制正弦背景**/
        Draw_Sin_Wave(canvas);
        /**绘制栅格**/
        float tab_y = (end_y - start_y) / 4.0f;
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(start_x, start_y + (i + 1) * tab_y, end_x, start_y + (i + 1) * tab_y, paintgrid);

        }

        Paint paint_Text = new Paint();
        paint_Text.setColor(Color_set.INFORMATION_COLOR);
        paint_Text.setTextSize(lable_size);
        paint_Text.setAntiAlias(true);


        String text3 = Y_LABLE;


    }

    /**
     * 绘制图表标题
     *
     * @param canvas
     */
    private void Draw_Title(Canvas canvas) {

        Information_Drawer.Draw_Title(canvas, viewStartLeft, viewStartTop, viewWidth, viewHeight, "Curve", lable_size);

    }

    private void draw_AxiY(Canvas canvas) {

        switch (TAB_OPTION) {
            case 0:
                TAB = 2.0f;
                break;
            case 1:
                TAB = 5.0f;
                break;
            case 2:
                TAB = 10.0f;
                break;
            case 3:
                TAB = 20.0f;
                break;
            case 4:
                TAB = 50.0f;
                break;

            case 5:
                TAB = 100.0f;
                break;

            case 6:
                TAB = 200.0f;
                break;

            case 7:
                TAB = 500.0f;
                break;

            case 8:
                TAB = 1000.0f;
                break;
            case 9:
                TAB = 2000.0f;
                break;

            default:
                TAB = 2000.0f;
                break;

        }

        AA_Asiy.Drw_Asiy4BT(canvas, start_x, start_y, end_y, TAB, TEXT_SIZE);

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
                canvas.drawText(String.format("%d", i * (PERIOD_CNT * 2))
                        , start_x + i * per_X - getTextWidth(String.format("%d", i * (PERIOD_CNT * 2)), TEXT_SIZE) / 2.0f
                        , end_y + getTextHeight(String.format("%d", i * (PERIOD_CNT * 2)), TEXT_SIZE) * 1.5f
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
                canvas.drawText(String.format("%d", i * (PERIOD_CNT * 2))
                        , start_x + i * per_X - getTextWidth(String.format("%d", i * (PERIOD_CNT * 2)), TEXT_SIZE) / 2.0f
                        , end_y + getTextHeight(String.format("%d", i * (PERIOD_CNT * 2)), TEXT_SIZE) * 1.5f
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
     * 边界初始化
     */
    private void BordenInit() {
        /*控件边界*/
        viewStartLeft = super.getLeft();
        viewStartTop = super.getTop();
        viewHeight = super.getHeight();
        viewWidth = super.getWidth();
        /*图表边界*/
        TEXT_SIZE = viewHeight / 20.0f;
        lable_size = viewHeight / 16.0f;


        start_x = viewStartLeft + (viewWidth / 10.0f) * 1.5f + lable_size;
        start_y = viewStartTop + (viewHeight / 10.0f) * 1.0f + lable_size;
        end_x = viewStartLeft + (viewWidth / 10.0f) * 9.0f - lable_size;
        end_y = viewStartTop + (viewHeight / 10.0f) * 8.5f - lable_size;
    }

    /**
     * 画笔对象初始化
     */
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

        /*数据颜色*/
        paintdata = new Paint();
        paintdata.setColor(Color_set.TOF_DATA_COLOR);
        paintdata.setStyle(Paint.Style.STROKE);
        paintdata.setStrokeWidth(2.0f);
        paintdata.setAntiAlias(true);

        paint_bkg_sin = new Paint();
        paint_bkg_sin.setColor(GRID_COLOR);
        paint_bkg_sin.setStrokeWidth(1.5f);
        paint_bkg_sin.setAntiAlias(true);

    }


    /**
     * 清空视图
     */
    public void ClearView() {
        for (int i = 0; i < 300; i++) {
            WAVE_DATA[i] = 0.0f;
        }

        MaxValue = 0.0f;
        invalidate();

    }


    /**
     * 设置模拟测试
     */
    public void SetDemo() {


    }

    /**
     * 设置数据并更新视图
     *
     * @param rcdata
     */

    public void setData(List<P_Data> rcdata) {

        if (rcdata.size() < 10) {
            return;
        }

        /**复制并转化**/
        int temp = rcdata.size() - 5;
        for (int i = 0; i < 5; i++) {
            byte[] temp_arr = rcdata.get(temp + i).getData();
            for (int j = 0; j < 60; j++) {
                float val = ((int) temp_arr[j] & 0xff) / 3.0f;

                if (IS_AEmode) {
                    WAVE_DATA[i * 60 + j] = Compute_Information.AE_dB_2_mV(val);
                } else {

                    WAVE_DATA[i * 60 + j] = Compute_Information.AA_dB_2_mV(val);
                }

            }
        }

        /**计算最大值**/
        MaxValue = 0;
        for (int i = 0; i < 300; i++) {
            if (WAVE_DATA[i] > MaxValue) {
                MaxValue = WAVE_DATA[i];
            }
        }

        invalidate();
    }


    /**
     * 重载数据加载函数
     *
     * @param RCdata
     */
    public void setdata(byte[] RCdata) {
        if (RCdata.length < 300) {
            return;
        }
        /**复制并转化**/
        for (int i = 0; i < 300; i++) {
            float val = ((int) RCdata[i] & 0xff) / 3.0f;
            if (IS_AEmode) {
                WAVE_DATA[i] = Compute_Information.AE_dB_2_mV(val);
            } else {

                WAVE_DATA[i] = Compute_Information.AA_dB_2_mV(val);
            }

        }
        /**计算最大值**/
        MaxValue = 0;
        for (int i = 0; i < 300; i++) {
            if (WAVE_DATA[i] > MaxValue) {
                MaxValue = WAVE_DATA[i];
            }
        }

        invalidate();

    }


    /**
     * 设置工作模式
     *
     * @param isae
     */
    public void setWorkMod(boolean isae) {
        this.IS_AEmode = isae;
    }

    /**
     * 获取图上数据
     *
     * @return
     */
    public float[] getWAVE_DATA() {

        return WAVE_DATA;
    }

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
     * 设置回放数据
     *
     * @param wavedata
     * @param tab
     */
    public void setReviewData(float[] wavedata, float tab) {
        this.WAVE_DATA = wavedata;

        /**计算最大值**/
        MaxValue = 0;
        for (int i = 0; i < 300; i++) {
            if (WAVE_DATA[i] > MaxValue) {
                MaxValue = WAVE_DATA[i];
            }
        }

        invalidate();


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
                /**手指落下**/
                touch_start_x = event.getX();
                touch_start_y = event.getY();

                percnttemp = PERIOD_CNT;
                taboptiontemp = TAB_OPTION;
                break;
            case MotionEvent.ACTION_MOVE:
                /**手指滑动**/
                touch_end_x = event.getX();
                touch_end_y = event.getY();
                /*判断是否超出视图区域*/
                if ((touch_end_x >= (viewStartLeft + viewWidth)) || (touch_end_x <= viewStartLeft)) {
                    /*左右边界超限*/
                    return true;
                }
                if ((touch_end_y <= viewStartTop) || (touch_end_y >= (viewStartTop + viewHeight))) {
                    return true;
                }
                if (Math.abs(touch_end_x - touch_start_x) >= Math.abs(touch_end_y - touch_start_y)) {
                    /*上下屏幕*/
                    Touched_Top_Bottom_Screen();
                } else {
                    /*左右屏幕*/
                    Touched_Left_Right_Screen();

                }

                break;
            case MotionEvent.ACTION_UP:
                /**手指抬起**/


                break;

        }


        return true;
    }

    /****
     * 左右屏幕事件
     */
    private void Touched_Left_Right_Screen() {

        if (touch_start_x >= (viewStartLeft + viewWidth / 2.0f)) {

            float temp_per = viewHeight / 4.0f;
            TAB_OPTION = (int) (taboptiontemp - (int) ((touch_end_y - touch_start_y) / temp_per));
            TAB_OPTION = TAB_OPTION >= 9 ? 9 : TAB_OPTION;
            TAB_OPTION = TAB_OPTION <= 0 ? 0 : TAB_OPTION;
            invalidate();
            /****不考虑，单次滑动有效*****/
        } else {

        }
    }

    /***
     * 上下屏幕事件
     */
    private void Touched_Top_Bottom_Screen() {
        if (Math.abs(touch_end_x - touch_start_x) < 5.0f) {
            return;
        }
        if (touch_start_y >= (viewStartTop + viewHeight / 2.0f)) {
            /*下半屏幕*/
            /*相位调节*/
            float temp_per = viewWidth / 6.0f;
            PERIOD_CNT = (int) (percnttemp + (int) ((touch_end_x - touch_start_x) / temp_per));
            /*判断是否超出范围*/
            PERIOD_CNT = PERIOD_CNT <= 1 ? 1 : PERIOD_CNT;
            PERIOD_CNT = PERIOD_CNT >= 5 ? 5 : PERIOD_CNT;
            invalidate();
        } else {
            /*上半屏幕不使用*/

        }

    }

    /**
     * Created by SONG on 2018/2/1 9:14.
     * The final explanation right belongs to author
     */

    public static class Wave_View extends View {
        /*************边界定义*************/
        /*View*/
        private float viewStartTop;
        private float viewStartLeft;
        private float viewHeight;
        private float viewWidth;
        private float viewEndRight;
        private float viewEndBottom;
        /*Charts*/
        private float chart_start_x;
        private float chart_start_y;
        private float chart_end_x;
        private float chart_end_y;


        /**************颜色定义****************/
        private int BKG_COLOR = Color.LTGRAY;
        private int CHART_BKG_COLOR = Color.argb(0x30, 0x20, 0x80, 0x35);
        private int CHART_BORDEN_COLOR = Color.BLACK;
        private int CHART_LABLE_TEXT_COLOR = Color.BLACK;
        private int CHART_BKG_WAVE = Color.WHITE;
        private int CHART_DATA_COLOR = Color.RED;
        private int CHART_GRAD_COLOR = Color.argb(0x30, 0x00, 0x00, 0x05);


        /*****************字体大小***********************/
        private float LABLE_TEXT_SIZE = 25.0f;

        /********************画笔定义********************/
        private Paint paint_chart_bkg;
        private Paint paint_chart_borden;
        private Paint paint_lable;
        private Paint paint_bkg_sin;
        private Paint paint_grad;

        /********************路径定义**********************/
        private Path borden_path;

        /***********************私有全局定义***************************/
        private float SHOW_TIME_LEN = 120.0f;
        private float PAI = 3.14165f;
        private Paint paint_data;


        /**
         * 程序构造
         *
         * @param context
         */
        public Wave_View(Context context) {
            super(context);
            init();
        }

        /**
         * 视图创建
         *
         * @param context
         * @param attrs
         */
        public Wave_View(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public Wave_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            super.setBackgroundColor(BKG_COLOR);
            init();
            Draw_Chart(canvas);
            Draw_Data(canvas);
        }

        /**
         * 绘制数据
         * @param canvas
         */
        private void Draw_Data(Canvas canvas)
        {
            float sin_start_x = chart_start_x + 1.0f;
            float sin_start_y = (chart_end_y - chart_start_y) / 2.0f + chart_start_y;
            float sin_end_x = chart_end_x - 1.0f;
            float per_x = (sin_end_x - sin_start_x) / 1000.0f;
            float pro_x = sin_start_x;
            float pro_y = sin_start_y;
            float temp_height = (chart_end_y - chart_start_y) / 4.0f - 2.0f;
            float temp_y;
            for (int i = 0; i < 1000; i++) {
                temp_y = -(float) Math.sin((8.0f) * PAI / 1000 * i) * temp_height + sin_start_y;
                canvas.drawLine(pro_x, pro_y
                        , i * per_x + sin_start_x
                        , temp_y
                        , paint_data
                );
                pro_x = i * per_x + sin_start_x;
                pro_y = temp_y;
            }

        }

        /**
         * 绘制图表
         */
        private void Draw_Chart(Canvas canvas) {

            /*绘制背景*/
            Draw_BKG(canvas);

            /*绘制XY*/
            Draw_XY(canvas);

             /*坐标轴*/
            Draw_Grad(canvas);

            /*正弦曲线*/
            Draw_Sin_Wave(canvas);




        }

        /**
         * 绘制XY
         *
         * @param canvas
         */
        private void Draw_XY(Canvas canvas) {
            float per_x = (chart_end_x - chart_start_x) / 20.0f;
            float per_y = (chart_end_y - chart_start_y) / 10.0f;
            float len;

            /*************刻度***************/
            for (int i = 0; i < 11; i++) {
                if ((i == 0) || (i == 5) || (i == 10)) {
                    if (i==5)
                    {
                        canvas.drawText(
                                String.format("%d(mV)", 0)
                                , chart_start_x - 25.0f - getTextWidth(String.format("%d(mV)", 0), LABLE_TEXT_SIZE)
                                , chart_start_y + i * per_y + getTextHeight(String.format("%d(mV)", 0), LABLE_TEXT_SIZE) / 3.0f
                                , paint_lable
                        );
                    }
                    else {
                        canvas.drawText(
                                String.format("%d", 5000 - i * 1000)
                                , chart_start_x - 25.0f - getTextWidth(String.format("%d", 5000 - i * 1000), LABLE_TEXT_SIZE)
                                , chart_start_y + i * per_y + getTextHeight(String.format("%d", 5000 - i * 1000), LABLE_TEXT_SIZE) / 3.0f
                                , paint_lable
                        );
                    }
                    len = 20.0f;
                } else {
                    len = 7.5f;
                }
                canvas.drawLine(
                        chart_start_x
                        , chart_start_y + i * per_y
                        , chart_start_x - len
                        , chart_start_y + i * per_y
                        , paint_lable
                );

            }

            /***************标签*************/
            for (int i = 0; i < 21; i++) {
                if ((i == 0) || (i == 5) || (i == 10) || (i == 15) || (i == 20)) {
                    canvas.drawText(
                            String.format("%d", i * 6)
                            , chart_start_x + i * per_x - getTextWidth(String.format("%d", i * 6), LABLE_TEXT_SIZE) / 2.0f
                            , chart_end_y + 20.0f + getTextHeight(String.format("%d", i * 6), LABLE_TEXT_SIZE)
                            , paint_lable
                    );
                    len = 20.0f;
                } else {
                    len = 7.5f;
                }
                canvas.drawLine(
                        chart_start_x + i * per_x
                        , chart_end_y
                        , chart_start_x + i * per_x
                        , chart_end_y + len
                        , paint_lable
                );

            }

            canvas.drawText("时间(ms)"
                    , chart_start_x + (chart_end_x - chart_start_x) / 2.0f -getTextWidth("时间(ms)", LABLE_TEXT_SIZE) / 2.0f
                    , chart_end_y + 90.0f
                    , paint_lable
            );

        }

        /**
         * 绘制栅格
         *
         * @param canvas
         */
        private void Draw_Grad(Canvas canvas) {
            float per_x = (chart_end_x - chart_start_x) / 20.0f;
            float per_y = (chart_end_y - chart_start_y) / 10.0f;
            /*X*/
            for (int i = 1; i < 20; i++) {
                canvas.drawLine(
                        chart_start_x + per_x * i
                        , chart_start_y + 1.0f
                        , chart_start_x + per_x * i
                        , chart_end_y - 1.0f
                        , paint_grad
                );
            }

            /*Y*/
            for (int i = 1; i < 10; i++) {
                canvas.drawLine(
                        chart_start_x + 1.0f
                        , chart_start_y + per_y * i
                        , chart_end_x - 1.0f
                        , chart_start_y + per_y * i
                        , paint_grad
                );
            }
        }

        /**
         * 绘制背景
         *
         * @param canvas
         */
        private void Draw_BKG(Canvas canvas) {
            /*背景*/
            canvas.drawPath(borden_path, paint_chart_bkg);
            /*边界*/
            canvas.drawPath(borden_path, paint_chart_borden);

        }

        /**
         * 绘制背景sin曲线
         */
        private void Draw_Sin_Wave(Canvas canvas) {
            float sin_start_x = chart_start_x + 1.0f;
            float sin_start_y = (chart_end_y - chart_start_y) / 2.0f + chart_start_y;
            float sin_end_x = chart_end_x - 1.0f;
            float per_x = (sin_end_x - sin_start_x) / 1000.0f;
            float pro_x = sin_start_x;
            float pro_y = sin_start_y;
            float temp_height = (chart_end_y - chart_start_y) / 2.0f - 2.0f;
            float temp_y;
            for (int i = 0; i < 1000; i++) {
                temp_y = -(float) Math.sin((SHOW_TIME_LEN / 10.0f) * PAI / 1000 * i) * temp_height + sin_start_y;
                canvas.drawLine(pro_x, pro_y
                        , i * per_x + sin_start_x
                        , temp_y
                        , paint_bkg_sin
                );
                pro_x = i * per_x + sin_start_x;
                pro_y = temp_y;
            }

        }

        /**
         * 初始化操作
         */
        private void init() {

            /************边界定位*/
            viewStartTop = super.getTop();
            viewStartLeft = super.getLeft();
            viewHeight = super.getHeight();
            viewWidth = super.getWidth();
            viewEndRight = super.getRight();
            viewEndBottom = super.getBottom();

            /***************图像定位*/
            chart_start_x = viewStartLeft + (viewWidth / 10.0f) * 1.5f;
            chart_start_y = viewStartTop + (viewHeight / 10.0f) * 1.2f;
            chart_end_x = viewStartLeft + (viewWidth / 10.0f) * 9.5f;
            chart_end_y = viewStartTop + (viewHeight / 10.0f) * 8.0f;


            /*************画笔初始化*/
            paint_chart_bkg = new Paint();
            paint_chart_bkg.setColor(CHART_BKG_COLOR);
            paint_chart_bkg.setStyle(Paint.Style.FILL);


            paint_chart_borden = new Paint();
            paint_chart_borden.setColor(CHART_BORDEN_COLOR);
            paint_chart_borden.setStyle(Paint.Style.STROKE);

            paint_lable = new Paint();
            paint_lable.setColor(CHART_LABLE_TEXT_COLOR);
            paint_lable.setTextSize(LABLE_TEXT_SIZE);
            paint_lable.setAntiAlias(true);


            paint_bkg_sin = new Paint();
            paint_bkg_sin.setColor(CHART_BKG_WAVE);
            paint_bkg_sin.setStrokeWidth(1.5f);
            paint_bkg_sin.setAntiAlias(true);


            paint_grad = new Paint();
            paint_grad.setColor(CHART_GRAD_COLOR);
            paint_bkg_sin.setAntiAlias(true);

            paint_data = new Paint();
            paint_data.setColor(CHART_DATA_COLOR);
            paint_data.setStrokeWidth(3);
            paint_data.setAntiAlias(true);

            /***************路径初始化*/
            borden_path = new Path();
            borden_path.moveTo(chart_start_x, chart_start_y);
            borden_path.lineTo(chart_end_x, chart_start_y);
            borden_path.lineTo(chart_end_x, chart_end_y);
            borden_path.lineTo(chart_start_x, chart_end_y);
            borden_path.close();


        }

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

    }
}
