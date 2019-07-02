package PDChartsPack.pack_pd_charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import PDChartsPack.pack_commen.Convert_Tools;
import PDChartsPack.pack_commen.System_Flag;


/**
 * Created by SONG on 2017/11/14 13:59.
 */

public class ContentView extends View {

    private float Show_Offset = 0.0f;
    /**
     * 背景颜色
     */
    private int BKG_COLOR = Color_set.BKG_COLOR;
    /**
     * 字体颜色
     */
    private int NAME_COLOR = Color_set.TEXT_COLOR;
    /**
     * 标签颜色
     */
    private int LABLE_COLOR = Color_set.TEXT_COLOR;

    /**
     * 抬头数据
     */
    private int HEAD_SIZE;

    /**
     * 显示范围
     */
    private float SHOW_RANE = 80.0f;

    /**
     * 显示格式
     */
    private String Format = "%.1f";

    /**
     * 预警颜色
     */
    private int PRE_COLOR = System_Flag.PRE_COLOR;
    /**
     * 报警颜色
     */
    private int WAR_COLOR = System_Flag.War_Color;
    /**
     * 正常颜色
     */
    private int NORMAL_COLOR = System_Flag.Normal_Color;

    /**
     * 预警值
     */
    private int PRE_VALUE = 20;

    /**
     * 报警值
     */
    private int WAR_VALUE = 40;

    /**
     * 名称
     */
    private String NAME = "Qp";
    /**
     * 数据
     */


    private int DATA_BKG =Color_set.Content_DATA_BKG_COLOR;
    /**
     * 数据颜色
     */
    private int DATA_COLOR = Color.GREEN;

    private float DATA = -20000.0f;
    private float viewStartLeft;
    private float viewStartTop;
    private float viewHeight;
    private float viewWidth;
    private float start_x;
    private float start_y;
    private float end_x;
    private float end_y;
    private int text_size;
    private int lable_size;
    private Paint paint_name;
    private Paint paint_lable;
    private Paint paint_bkg;
    private Paint paint_borden;
    private Paint paint_head;
    private Paint paint_data;

    private boolean IS_AA_MODE = false;


    public ContentView(Context context) {
        super(context);
    }

    public ContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(BKG_COLOR);
        init();
        Draw_bkg(canvas);
        Draw_Data(canvas);

    }

    /**
     * 设置AA模式
     *
     * @param is_aa
     */
    public void Set_AA_MODE(boolean is_aa) {
        IS_AA_MODE = is_aa;
        invalidate();
    }


    /**
     * 清空
     */
    public void Clear_Content() {
        DATA = -20000.0f;
        invalidate();
    }

    /**
     * 绘制数据
     *
     * @param canvas
     */
    private void Draw_Data(Canvas canvas) {
        if (DATA == -20000.0f) {
            return;
        }
        float tab = (end_x - start_x) / SHOW_RANE;
        float data_x = start_x + DATA * tab;


        if ((DATA + Show_Offset) >= PRE_VALUE) {
            paint_head.setColor(PRE_COLOR);

        }
        if ((DATA + Show_Offset) >= WAR_VALUE) {
            paint_head.setColor(WAR_COLOR);
        }
        if ((DATA + Show_Offset) < PRE_VALUE) {
            paint_head.setColor(NORMAL_COLOR);
        }
        canvas.drawText(String.format(Format, DATA + Show_Offset)
                , start_x + (end_x - start_x) / 2.0f - getTextWidth(String.format(Format, DATA), HEAD_SIZE) / 2.0f
                , start_y - getTextHeight(String.format(Format, DATA), HEAD_SIZE) / 3.0f
                , paint_head
        );
        if (data_x >= end_x - 1) {
            data_x = end_x - 1;
        }
        if ((DATA + Show_Offset) >= PRE_VALUE) {
            canvas.drawRect(
                    start_x + 1
                    , start_y + 2
                    , data_x
                    , end_y - 3
                    , paint_head
            );
        } else {
            canvas.drawRect(
                    start_x + 1
                    , start_y + 2
                    , data_x
                    , end_y - 3
                    , paint_data);
        }
    }

    /**
     * 图表背景绘制
     *
     * @param canvas
     */
    private void Draw_bkg(Canvas canvas) {
        Path path_bkg = new Path();
        path_bkg.moveTo(start_x, start_y);
        path_bkg.lineTo(end_x, start_y);
        path_bkg.lineTo(end_x, end_y);
        path_bkg.lineTo(start_x, end_y);
        path_bkg.close();
        canvas.drawPath(path_bkg, paint_bkg);
        canvas.drawPath(path_bkg, paint_borden);
        canvas.drawText(NAME, viewStartLeft + (start_x - viewStartLeft) / 2.0f - getTextWidth(NAME, text_size) / 2.0f,
                start_y + getTextHeight(NAME, text_size) / 1.5f,
                paint_name
        );

        float per_x = (end_x - start_x) / 4.0f;

        float len = (end_y - start_y) * 0.7f;
        float len1 = (end_y - start_y) * 0.35f;
        String text;
        if (IS_AA_MODE) {
            float tab = SHOW_RANE / 4.0f;
            for (int i = 0; i < 5; i++) {
                if (i == 0 || i == 4) {
                    canvas.drawLine(
                            start_x + i * per_x,
                            end_y,
                            start_x + i * per_x,
                            end_y + len
                            , paint_lable
                    );
                    text = Convert_Tools.FormatVaule( i * tab + Show_Offset);
                    canvas.drawText(text
                            , start_x + i * per_x - getTextWidth(text, lable_size) / 2.0f
                            , end_y + len + getTextHeight(text, lable_size)
                            , paint_lable
                    );

                } else {
                    canvas.drawLine(
                            start_x + i * per_x,
                            end_y,
                            start_x + i * per_x,
                            end_y + len1
                            , paint_lable
                    );
                    text = Convert_Tools.FormatVaule( i * tab + Show_Offset);
                    canvas.drawText(text
                            , start_x + i * per_x - getTextWidth(text, lable_size) / 2.0f
                            , end_y + len + getTextHeight(text, lable_size)
                            , paint_lable
                    );

                }
            }
        } else {
            int tab = (int) SHOW_RANE / 4;
            for (int i = 0; i < 5; i++) {
                if (i == 0 || i == 4) {
                    canvas.drawLine(
                            start_x + i * per_x,
                            end_y,
                            start_x + i * per_x,
                            end_y + len
                            , paint_lable
                    );
                    canvas.drawText(String.format("%d", (int) (i * tab + Show_Offset))
                            , start_x + i * per_x - getTextWidth(String.format("%d", (int) (i * tab + Show_Offset)), lable_size) / 2.0f
                            , end_y + len + getTextHeight(String.format("%d", (int) (i * tab + Show_Offset)), lable_size)
                            , paint_lable
                    );

                } else {
                    canvas.drawLine(
                            start_x + i * per_x,
                            end_y,
                            start_x + i * per_x,
                            end_y + len1
                            , paint_lable
                    );
                    canvas.drawText(String.format("%d", (int) (i * tab + Show_Offset))
                            , start_x + i * per_x - getTextWidth(String.format("%d", (int) (i * tab + Show_Offset)), lable_size) / 2.0f
                            , end_y + len + getTextHeight(String.format("%d", (int) (i * tab + Show_Offset)), lable_size)
                            , paint_lable
                    );

                }
            }
        }

    }

    /**
     * 初始化
     */
    private void init() {
        /*边界*/
        viewStartLeft = super.getLeft();
        viewStartTop = super.getTop();
        viewHeight = super.getHeight();
        viewWidth = super.getWidth();

        start_x = viewStartLeft + (viewWidth / 10.0f) * 2.0f;
        start_y = viewStartTop + (viewHeight / 6.0f) * 2.5f;
        end_x = viewStartLeft + (viewWidth / 10.0f) * 9.0f;
        end_y = viewStartTop + (viewHeight / 6.0f) * 3.8f;

        text_size = (int) (viewHeight / 5.0f);
        lable_size = (int) (viewHeight / 7.0f);
        HEAD_SIZE = (int) (viewHeight / 5.5f);


        /*抬头*/
        paint_head = new Paint();
        paint_head.setTextSize(HEAD_SIZE);
        paint_head.setAntiAlias(true);
        paint_head.setStrokeWidth(1);

        /*字体*/
        paint_name = new Paint();
        paint_name.setColor(NAME_COLOR);
        paint_name.setAntiAlias(true);
        paint_name.setStrokeWidth(1);
        paint_name.setTextSize(text_size);

        /*标签*/
        paint_lable = new Paint();
        paint_lable.setColor(LABLE_COLOR);
        paint_lable.setStrokeWidth(1);
        paint_lable.setAntiAlias(true);
        paint_lable.setTextSize(lable_size);

        /*背景*/
        paint_bkg = new Paint();
        paint_bkg.setColor(DATA_BKG);
        paint_bkg.setAntiAlias(true);

        /*数据*/

        paint_data = new Paint();
        paint_data.setColor(DATA_COLOR);
        paint_data.setAntiAlias(true);

        /*边界*/
        paint_borden = new Paint();
        paint_borden.setColor(NAME_COLOR);
        paint_borden.setAntiAlias(true);
        paint_borden.setStyle(Paint.Style.STROKE);

    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void SetData(float data) {
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
    }

    /**
     * 设置预警
     *
     * @param vlaue 预警值
     */
    public void SetEarlyWarning(int vlaue) {
        PRE_VALUE = vlaue;

    }

    /**
     * 设置报警
     *
     * @param vlaue 报警值
     */
    public void SetAlarm(int vlaue) {
        WAR_VALUE = vlaue;

    }

    /**
     * 设置显示范围
     *
     * @param range 范围
     */
    public void SetShowRange(float range) {
        SHOW_RANE = range;
        invalidate();
    }

    /**
     * 设置显示名称
     *
     * @param name 显示名称
     */
    public void SetName(String name) {
        NAME = name;
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

    public void SetShowOffset(float offset) {
        Show_Offset = offset;
    }
}
