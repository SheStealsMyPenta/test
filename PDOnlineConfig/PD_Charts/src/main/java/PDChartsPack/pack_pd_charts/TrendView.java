package PDChartsPack.pack_pd_charts;

import android.content.Context;
import android.graphics.Canvas;
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
 * Created by SONG on 2017/11/10 14:00.
 */

public class TrendView extends View {


    private int Other_Clear_Cnt = 0;


    private boolean IS_AAmode = false;
    private int Show_Offset = 0;
    private String FORMAT = "dB";
    /**
     * 背景颜色
     */
    private int BKG_COLOR = Color_set.BKG_COLOR;
    /**
     * 显示底色
     */
    private int DATA_BACK_COLOR = Color_set.DATA_BKG_COLOR;
    /**
     * 栅格颜色
     */
    private int GRID_COLOR = Color_set.Grid_Color;
    /**
     * 字体颜色
     */
    private int TEXT_COLOR = Color_set.TEXT_COLOR;
    /**
     * 数据颜色
     */
    private int DATA_COLOR = Color_set.Trend_DATA_COLOR;
    /**
     * 显示模式掩板颜色
     */
    private int DISPLAY_MASK_COLOR = Color_set.Trend_MASK_COLOR;

    /**
     * 触摸选择模式掩板颜色
     */
    private int TOUCH_MASK_COLOR =  Color_set.Trend_MASK_COLOR;

    private int BORDEN_COLOR = Color_set.Trend_DATA_BORDEN_COLOR;

    /**
     * 字体大小
     */
    private float TEXT_SIZE = 25;
    /**
     * 统计时长
     */
    private int CNT_TIME = 1;

    /**
     * 统计开始时间
     */
    private int Start_Time = 0;

    /**
     * 统计结束时间
     */
    private int End_Time = 1;

    /**
     * 显示的数据
     */
    private List<Integer> DATA = null;

    /**
     * 滑动监听器
     */
    private TrendListener listener = null;
    /**
     * 滑动事件对应的统计结果
     */
    private TrendEvent event = null;

    private boolean USE_TOUCH = false;


    private Paint paint_data_bkg;
    private Paint paint_text;
    private Paint paint_grid;
    private Paint paint_mask;
    private Paint paint_data;
    private Paint paint_borden;

    private float viewStartTop;
    private float viewStartLeft;
    private float viewHeight;
    private float viewWidth;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;


    private int dataSize = 0;
    private int SHOW_TAB = 80;
    private float touchStart_x;
    private float touchStart_y;
    private float touchEnd_x;
    private float touchEnd_y;
    private float per_y;
    private float per_x;
    private float touch_2_startx = 0;
    private float touch_2_starty = 0;
    private int test_time = 1;
    private float start_len;
    private float start_len_x;
    private float start_len_y;
    private float end_len;
    private int AUTO_CLEAR_TIME = 10;
    private boolean Auto_Clear_Data = false;

    public TrendView(Context context) {
        super(context);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(BKG_COLOR);

        Information_Init();

        Paint_Init();
        Draw_bkg(canvas);
        Draw_Data(canvas);
        Draw_Y_Lable(canvas);

    }
    private void Draw_Y_Lable(Canvas canvas) {
        float offset = 0;
        String msg = "幅值(" + FORMAT + ")";
        if (IS_AAmode) {
            offset = getTextWidth("0000", TEXT_SIZE) + TEXT_SIZE / 3.0f + 15.0f;
        } else {
            offset = getTextWidth("000", TEXT_SIZE) + TEXT_SIZE / 2.0f + 15.0f;
        }


        Information_Drawer.Draw_left_Center(canvas, start_x - offset, start_y, (end_y - start_y), msg, TEXT_SIZE);

    }

    /**
     * 信息初始化
     */
    private void Information_Init() {


        /*视图边界*/
        viewStartTop = super.getTop();
        viewStartLeft = super.getLeft();
        viewHeight = super.getHeight();
        viewWidth = super.getWidth();

        TEXT_SIZE = viewHeight/10.0f;
        /*图表定点*/
        start_x = viewStartLeft +(viewWidth/10.0f)*1.0f + TEXT_SIZE;
        start_y = viewStartTop + (viewHeight/10.0f);
        end_x = viewStartLeft +(viewWidth/10.0f)*9.5f;
        end_y = viewStartTop + (viewHeight/20.0f)*14.0f;


    }
    public void setAutoClear(int time, boolean Used) {
        Auto_Clear_Data = Used;
        AUTO_CLEAR_TIME = time;
    }
    /**
     * 绘制数据
     */
    private void Draw_Data(Canvas canvas) {
        if (DATA == null) {
            return;
        }
        per_y = (end_y - start_y) / SHOW_TAB;
        per_x = (end_x - start_x) / 120.0f;
        dataSize = (DATA.size() >= 120 ? 120 : DATA.size());
        Path path_temp = new Path();
        paint_text.setColor(DATA_COLOR);
        for (int i = 0; i < dataSize; i++) {
            int temp = DATA.get(i) >= SHOW_TAB ? SHOW_TAB : DATA.get(i);
            float data_y = end_y - temp * per_y;

            path_temp.moveTo(start_x + i * per_x
                    , data_y);
            path_temp.lineTo(start_x + (i + 1) * per_x, data_y);
            path_temp.lineTo(start_x + (i + 1) * per_x, end_y);
            path_temp.lineTo(start_x + i * per_x, end_y);
            path_temp.close();
            canvas.drawPath(path_temp, paint_data);
            canvas.drawPath(path_temp, paint_borden);

        }
        Path path_Data = new Path();

        /*使用触摸模式的时候 绘制选中区域，使用显示模式的时候选中所有数据*/
        if (USE_TOUCH) {
            paint_mask.setColor(TOUCH_MASK_COLOR);
            path_Data.moveTo(start_x + Start_Time * per_x, start_y);
            path_Data.lineTo(start_x + End_Time * per_x, start_y);
            path_Data.lineTo(start_x + End_Time * per_x, end_y);
            path_Data.lineTo(start_x + Start_Time * per_x, end_y);
            path_Data.close();
        } else {
            paint_mask.setColor(TOUCH_MASK_COLOR);
            path_Data.moveTo(start_x + Start_Time * per_x, start_y);
            path_Data.lineTo(start_x + End_Time * per_x, start_y);
            path_Data.lineTo(start_x + End_Time * per_x, end_y);
            path_Data.lineTo(start_x + Start_Time * per_x, end_y);
            path_Data.close();
        }
        canvas.drawPath(path_Data, paint_mask);
        canvas.drawPath(path_Data, paint_borden);
    }

    /**
     * 绘制图表背景
     */
    private void Draw_bkg(Canvas canvas) {



        Path path_Data_bkg = new Path();
        path_Data_bkg.moveTo(start_x, start_y);
        path_Data_bkg.lineTo(end_x, start_y);
        path_Data_bkg.lineTo(end_x, end_y);
        path_Data_bkg.lineTo(start_x, end_y);
        path_Data_bkg.close();
        canvas.drawPath(path_Data_bkg, paint_data_bkg);
        canvas.drawPath(path_Data_bkg, paint_text);
        paint_text.setStyle(Paint.Style.FILL);
        float tab_y = (end_y - start_y) / 4.0f;
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(start_x, start_y + (i + 1) * tab_y, end_x, start_y + (i + 1) * tab_y, paint_grid);

        }
        float per_X = (end_x - start_x) / 12.0f;
        float per_x = per_X / 5.0f;
        for (int i = 0; i < 13; i++) {
            if (i == 12) {
                canvas.drawLine(start_x + i * per_X,
                        end_y,
                        start_x + i * per_X,
                        end_y + 15,
                        paint_text
                );

            } else {
                canvas.drawLine(start_x + i * per_X,
                        end_y,
                        start_x + i * per_X,
                        end_y + 15,
                        paint_text
                );
                for (int j = 0; j < 4; j++) {
                    canvas.drawLine(start_x + i * per_X + (j + 1) * per_x,
                            end_y,
                            start_x + i * per_X + (j + 1) * per_x,
                            end_y + 7.5f,
                            paint_text
                    );
                }
            }
            canvas.drawLine(start_x + i * per_X
                    , start_y
                    , start_x + i * per_X
                    , end_y
                    , paint_grid
            );

            canvas.drawText(String.format("%ds", i * 10),
                    start_x + i * per_X - getTextWidth(String.format("%ds", i * 10), TEXT_SIZE) / 2.5f,
                    end_y + getTextHeight(String.format("%ds", i * 10), TEXT_SIZE) / 2.0f + 20.0f
                    , paint_text
            );
        }
        /**绘制Y轴**/
        if (IS_AAmode)
        {
            AA_Asiy.Draw_Asiy(canvas,start_x,start_y,end_y,5,false,TEXT_SIZE);
        }
        else{
            Draw_AxiY(canvas);
        }



    }

    /**
     * 设置使用AA模式
     * @param USE_AA_MODE
     */
    public void setIS_AAmode(boolean USE_AA_MODE)
    {
        IS_AAmode = USE_AA_MODE;
        invalidate();
    }

    /**
     * 绘制Y轴
     *
     * @param canvas
     */
    private void Draw_AxiY(Canvas canvas) {
        float per_Y = (end_y - start_y) / 4.0f;
        float per_y = per_Y / 5.0f;
        int per_tab = SHOW_TAB / 4;
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(start_x - 15.0f,
                    start_y + i * per_Y
                    , start_x
                    , start_y + i * per_Y
                    , paint_text
            );
            canvas.drawLine(start_x,
                    start_y + i * per_Y
                    , end_x
                    , start_y + i * per_Y
                    , paint_grid
            );
            canvas.drawText(
                    String.format("%d", (4 - i) * per_tab + Show_Offset)
                    , start_x - getTextWidth(String.format("%d", (4 - i) * per_tab + Show_Offset), TEXT_SIZE) - 20.0f
                    , start_y + i * per_Y + getTextHeight(String.format("%d", (4 - i) * per_tab + Show_Offset), TEXT_SIZE) / 3.0f
                    , paint_text

            );

            if (i != 4) {
                for (int j = 0; j < 4; j++) {
                    canvas.drawLine(start_x - 7.5f,
                            start_y + i * per_Y + (j + 1) * per_y
                            , start_x
                            , start_y + i * per_Y + (j + 1) * per_y
                            , paint_text
                    );

                }

            }

        }

    }


    public void SetFormat(String format) {
        FORMAT = format;
        invalidate();
    }

    /**
     * 画笔初始化
     */
    private void Paint_Init() {
        /*数据第景颜色*/
        paint_data_bkg = new Paint();
        paint_data_bkg.setColor(DATA_BACK_COLOR);
        paint_data_bkg.setAntiAlias(true);

        /*文字*/
        paint_text = new Paint();
        paint_text.setColor(TEXT_COLOR);
        paint_text.setTextSize(TEXT_SIZE);
        paint_text.setAntiAlias(true);
        paint_text.setStrokeWidth(1);
        paint_text.setStyle(Paint.Style.STROKE);

        /*栅格颜色*/
        paint_grid = new Paint();
        paint_grid.setColor(GRID_COLOR);
        paint_grid.setStrokeWidth(1);
        paint_grid.setAntiAlias(true);

        /*数据颜色*/
        paint_data = new Paint();
        paint_data.setColor(DATA_COLOR);

        /*边界*/
        paint_borden = new Paint();
        paint_borden.setColor(BORDEN_COLOR);
        paint_borden.setAntiAlias(true);
        paint_borden.setStrokeWidth(1);
        paint_borden.setStyle(Paint.Style.STROKE);


        /*掩板颜色*/
        paint_mask = new Paint();
        paint_mask.setColor(DISPLAY_MASK_COLOR);
        paint_mask.setAntiAlias(true);
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
     * 设置背景颜色
     *
     * @param color RGB颜色
     */
    public void SetBackgroundColor(int color) {
    /*设置背景颜色*/
        BKG_COLOR = color;
    }

    /**
     * 设置图表显示颜色
     *
     * @param color RGB颜色
     */
    public void SetChartBackgroundColor(int color) {
        DATA_BACK_COLOR = color;
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void SetTextColor(int color) {
        TEXT_COLOR = color;
    }

    /**
     * 设置柱子颜色
     *
     * @param color 带透明度的RGB颜色
     */
    public void SetCoulmnColor(int color) {
        DATA_COLOR = color;
    }

    /**
     * 设置字体大小
     *
     * @param size 大小
     */
    public void SetTextSize(int size) {
        TEXT_SIZE = size;
    }

    /**
     * 设置显示的最大值和单位标签的显示
     *
     * @param Range_max 显示数据的最大值
     */
    public void SetDataRange(int Range_max) {
        SHOW_TAB = Range_max;

        invalidate();
    }

    /**
     * 设置数据
     *
     * @param data List<Integer>数据
     */
    public void SetData(List<Integer> data) {
        DATA = data;
        USE_TOUCH = false;
        invalidate();
    }

    public void Clear_Trend() {
        DATA = null;
        Start_Time = 0;
        End_Time = 1;
        Other_Clear_Cnt = 0;
        invalidate();
    }

    public void Add_Data(int data) {
        if (DATA == null) {
            DATA = new ArrayList<Integer>();
            DATA.add(data);
        }
        else
        {
            DATA.add(data);
        }
        if (DATA.size() > 120) {
            int temp = DATA.size() - 120;
            for (int i = 0; i < temp; i++) {
                DATA.remove(i);
            }
        }

        if (Auto_Clear_Data)
        {
           // CNT_TIME = AUTO_CLEAR_TIME;
            Other_Clear_Cnt++;
            if (Other_Clear_Cnt>=AUTO_CLEAR_TIME)
            {
                Other_Clear_Cnt = AUTO_CLEAR_TIME;
            }
                Start_Time = (DATA.size() - Other_Clear_Cnt)<0?0:(DATA.size() - Other_Clear_Cnt);
                End_Time = DATA.size()<1?1:DATA.size();
        }
        else
        {
            if (!USE_TOUCH) {
                Start_Time = 0;
                End_Time = DATA.size();
            }
            Other_Clear_Cnt++;
            if(DATA.size()>=120)
            {
                Other_Clear_Cnt--;
            }
            Start_Time = (DATA.size() - Other_Clear_Cnt)<0?0:(DATA.size() - Other_Clear_Cnt);
            End_Time = DATA.size()<1?1:DATA.size();
        }


        invalidate();

    }

    /**
     * 设置触摸模式
     *
     * @param is_touch false为单纯的显示模式，true为触摸选中模式
     */
    public void Use_Touch_Mode(boolean is_touch) {
        USE_TOUCH = is_touch;
        if (is_touch&&Auto_Clear_Data)
        {
            CNT_TIME = AUTO_CLEAR_TIME;
        }
        invalidate();
    }

    /**
     * 设置统计时长
     *
     * @param time S
     */
    private void SetStatisticalTime(int time) {
        CNT_TIME = time;
        if (USE_TOUCH) {
            End_Time = Start_Time + CNT_TIME;
            if (End_Time > dataSize) {
                End_Time = dataSize;
            }
            if (listener!=null) {
                listener.onTrendMove(new TrendEvent(Start_Time, End_Time));
            }
            invalidate();
        }


    }

    /**
     * 设置滑动监听器
     *
     * @param listener 监听器
     */
    public void SetTrendOnTouchLisenter(TrendListener listener) {
        this.listener = listener;
    }

    /**
     * 触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!USE_TOUCH)
        {
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            /*一个点按下*/
            case MotionEvent.ACTION_DOWN:
                touchStart_x = event.getX(0);
                touchStart_y = event.getY(0);
                touch_2_startx = 0;
                touch_2_starty = 0;
                break;

                /*第二个点按下*/
            case MotionEvent.ACTION_POINTER_DOWN:
                touch_2_startx = event.getX(1);
                touch_2_starty = event.getY(1);
                start_len_x = event.getX(0) - event.getX(1);
                start_len_y = event.getY(0) - event.getY(1);
                start_len = (float) Math.sqrt(start_len_x * start_len_x + start_len_y * start_len_y);

                break;
            /*移动*/
            case MotionEvent.ACTION_MOVE:

                /*拖动事件*/
                if (touch_2_startx == 0) {
                    touchEnd_x = event.getX(0);
                    touchEnd_y = event.getY(0);
                    if (Math.abs(touchEnd_x - touchStart_x) >= Math.abs(touchEnd_y - touchStart_y)) {
                    /*左右*/
                        if (Math.abs(touchEnd_x - touchStart_x) >= per_x) {
                        /*有效移动*/
                            Start_Time += (int) ((touchEnd_x - touchStart_x) / per_x);
                            if ((Start_Time >= 0) && (Start_Time <= dataSize - CNT_TIME)) {
                            /*有效范围*/
                                End_Time = Start_Time + (dataSize >= CNT_TIME ? CNT_TIME : dataSize);
                                if ((listener != null) && USE_TOUCH) {
                                    listener.onTrendMove(new TrendEvent(Start_Time, End_Time));
                                }
                                invalidate();
                            } else {
                            /*无效范围处理*/
                                if ((Start_Time >= dataSize - CNT_TIME)) {
                                    Start_Time = dataSize - CNT_TIME;
                                }
                                if (Start_Time < 0) {
                                    Start_Time = 0;
                                }
                                End_Time = Start_Time + (dataSize >= CNT_TIME ? CNT_TIME : dataSize);
                                if ((listener != null) && USE_TOUCH) {
                                    listener.onTrendMove(new TrendEvent(Start_Time, End_Time));
                                }
                                invalidate();
                            }
                        }
                    } else {
                    /*上下*/
                    }
                    touchStart_x = start_x + Start_Time * per_x;
                    touchStart_y = touchEnd_y;
                } else/*放大事件*/ {
                    start_len_x = event.getX(0) - event.getX(1);
                    start_len_y = event.getY(0) - event.getY(1);
                    end_len = (float) Math.sqrt(start_len_x * start_len_x + start_len_y * start_len_y);
                    float len_add = end_len - start_len;
                    if (Math.abs(len_add) >= per_x) {
                        int temp_cnt = (int) (Math.abs(len_add) / per_x);
                        /*放大*/
                        if (len_add > 0) {
                            test_time = test_time >= dataSize ? dataSize : (temp_cnt + test_time);

                        } else/*缩小*/ {
                            test_time = test_time <= 1 ? 1 : (test_time - temp_cnt);

                        }
                        test_time = test_time <= 1 ? 1 : test_time;
                        SetStatisticalTime(test_time);
                        start_len = end_len;

                    }

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                touch_2_startx = 0;
                touch_2_starty = 0;
                break;

            default:
                break;
        }

        return true;
    }

    public void set_demo() {
        List<Integer> testdata = new ArrayList<Integer>();
        for (int i = 0; i < 120; i++) {
            testdata.add((int) (new Random().nextInt(60) + 1));
        }
        SetData(testdata);
    }

    public void SetShowOffset(int offset) {
        Show_Offset = offset;
    }

    public void SetStartEndTime(int start,int end)
    {
        Start_Time = start;
        End_Time = end;
        if ((listener != null) && USE_TOUCH) {
            listener.onTrendMove(new TrendEvent(Start_Time, End_Time));
        }
        invalidate();
    }

    public void Other_Clear()
    {
        if (DATA != null) {
            Start_Time = DATA.size() - 1;
            End_Time = DATA.size();
        } else
        {
            Start_Time  = 0;
            End_Time = 1;
        }
        Other_Clear_Cnt = 0;
    invalidate();
    }
}
