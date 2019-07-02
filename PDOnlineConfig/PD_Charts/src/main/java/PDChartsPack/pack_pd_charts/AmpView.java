/*

                      __------__
                    /~          ~\
                   |    //^\\//^\|
                 /~~\  ||  o| |o|:~\
                | |6   ||___|_|_||:|
                 \__.  /      o  \/'
                  |   (       O   )
         /~~~~\    `\  \         /
        | |~~\ |     )  ~------~`\
       /' |  | |   /     ____ /~~~)\
      (_/'   | | |     /'    |    ( |
             | | |     \    /   __)/ \
             \  \ \      \/    /' \   `\
               \  \|\        /   | |\___|
                 \ |  \____/     | |
                 /^~>  \        _/ <
                |  |         \       \
                |  | \        \        \
                -^-\  \       |        )
                     `\_______/^\______/
 */
package PDChartsPack.pack_pd_charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by SONG on 2017/10/30 13:03.
 */
public class AmpView extends View {

    /**
     * 预警
     */
    private int PER_VALUE = 20;
    private int PER_COLOR = Color.YELLOW;
    /**
     * 报警
     */
    private int WAR_VALUE = 60;
    private int WAR_COLOR = Color.RED;

    /**
     * 显示范围
     */
    private int SHOWRANGE = 4;

    /**
     * 数据显示颜色
     */
    private int DATA_COLUMN_COLOR = Color.rgb(0, 0xff, 0);
    /**
     * View 的背景颜色
     */
    private int BKG_COLOR = Color.LTGRAY;
    /**
     * 标签颜色
     */
    private int LABLE_COLOR = Color.BLACK;
    /**
     * 标签字体大小
     */
    private int LABLE_SIZE = 40;

    /**
     * 字体颜色
     */
    private int TEXT_COLOR = Color.BLACK;

    /**
     * 字体大小
     */
    private int TEXT_SIZE = 20;

    /**
     * 抬头文字颜色
     */
    private int HEAD_TEXT_Color = Color.BLUE;

    /**
     * 抬头文字打下
     */
    private int HEAD_TEXT_SIZE = 40;


    /**
     * 控件的高度
     */
    private int View_Hight;
    /**
     * 控件的宽度
     */
    private int View_Weith;
    /**
     * 子视图个数
     */
    private float Child_View_Cnt = 4.1f;

    /**
     * 是否显示HEAD
     */
    private boolean HEAD_CAN_SHOW = false;
    /**
     * 数据
     */
    private int Qp = -1;

    private int Qm = -1;

    private int F1 = -1;

    private int F2 = -1;

    public AmpView(Context context) {
        super(context);
    }

    public AmpView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AmpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 默认构造函数
     *
     * @param context 上下文对象
     */


    /**
     * 绘制方法
     *
     * @param canvas 画布对象
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*设置背景颜色*/
        super.setBackgroundColor(BKG_COLOR);

        Draw_BKG(canvas);
    }
    /*绘制*/

    /**
     * 绘制背景
     */
    private void Draw_BKG(Canvas canvas) {
        View_Hight = this.getHeight();
        View_Weith = this.getWidth();


        Drew_QP(canvas);
        Draw_QM(canvas);
        Draw_F1(canvas);
        Draw_F2(canvas);


    }

    /**
     * 绘制F2
     */

    private void Draw_F2(Canvas canvas) {
        int hight = (int) (View_Hight / Child_View_Cnt + 0.5);
        int weith = View_Weith;
        int start_top = this.getTop() + hight * 3;
        int start_left = this.getLeft();
        /*计算标签居中以后的位置*/
        int lable_start_top = start_top + hight / 5 + (int) getTextHeight("F2", LABLE_SIZE) / 2;
        int lable_start_left = start_left + (weith / 5 - (int) getTextWidth("F2", LABLE_SIZE)) / 2;
        Paint lable_paint = new Paint();
        lable_paint.setColor(LABLE_COLOR);
        lable_paint.setStrokeWidth(2);
        lable_paint.setAntiAlias(true);
        lable_paint.setTextSize(LABLE_SIZE);
        /*显示标签*/
        canvas.drawText("F2", lable_start_left, lable_start_top + (int) getTextHeight("F2", LABLE_SIZE) / 2, lable_paint);

        /*计算中心控件的坐标值*/
        int view_start_top = start_top + hight / 5 + (int) getTextHeight("F2", LABLE_SIZE) / 2;
        int view_start_left = start_left + weith / 5;
        int view_Len = (int) ((weith / 5) * 3.5f + 0.5f);
        int view_hight = (int) ((getTextHeight("F2", LABLE_SIZE) / 5) * 3.0f + 0.5f);
        Paint View_paint = new Paint();
        View_paint.setColor(TEXT_COLOR);
        View_paint.setStrokeWidth(1);
        View_paint.setAntiAlias(true);
        Paint view_paint1 = new Paint();
        view_paint1.setColor(Color.WHITE);
        /*画框*/
        draw_squre(view_start_left, view_start_top, view_Len, view_hight, View_paint, canvas);
        canvas.drawRect(view_start_left + 1, view_start_top + 1, view_start_left + view_Len - 2, view_start_top + view_hight - 2, view_paint1);
        /*画线*/
        int per = view_Len / 4;
        int Y_base = view_start_top + view_hight;
        int len1 = (int) ((getTextHeight("Qp", LABLE_SIZE) / 5) * 2 + 0.5f);
        int len2 = (int) ((getTextHeight("Qp", LABLE_SIZE) / 5) * 1 + 0.5f);
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len1, View_paint);
                canvas.drawText(Integer.toString(i * 25), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 25), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * 25), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            } else {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len2, View_paint);
                canvas.drawText(Integer.toString(i * 25), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 25), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(i * 25), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            }
        }
        canvas.drawLine(view_start_left + view_Len, Y_base, view_start_left + view_Len, Y_base + len1, View_paint);
        canvas.drawText(Integer.toString(4 * 25), view_start_left + 4 * per - (int) ((getTextWidth(Integer.toString(4 * 25), TEXT_SIZE) / 2)),
                Y_base + len1 + (getTextHeight(Integer.toString(4 * 25), TEXT_SIZE) / 2) + 5,
                View_paint
        );
        Paint Head_Paint = new Paint();
        Head_Paint.setColor(HEAD_TEXT_Color);
        Head_Paint.setTextSize(HEAD_TEXT_SIZE);
        Head_Paint.setStrokeWidth(2);
        /*填充数据*/
        if (F2 != -1) {
            Paint Data_Paint = new Paint();
            Data_Paint.setColor(DATA_COLUMN_COLOR);
            canvas.drawRect(view_start_left + 1, view_start_top + 4, view_start_left + (int) ((view_Len / 100.1f) * F2), view_start_top + view_hight - 4, Data_Paint);
            canvas.drawText(String.format("%d%%", F2),
                    view_start_left + 2 * per - (int) ((getTextWidth(String.format("%d%%", F2), HEAD_TEXT_SIZE) / 2)),
                    Y_base - (int) (getTextHeight(String.format("%d%%", F2), HEAD_TEXT_SIZE) / 1.5f),
                    Head_Paint);

        }
    }

    /**
     * 绘制F1
     */

    private void Draw_F1(Canvas canvas) {
        int hight = (int) (View_Hight / Child_View_Cnt + 0.5);
        int weith = View_Weith;
        int start_top = this.getTop() + hight * 2;
        int start_left = this.getLeft();
        /*计算标签居中以后的位置*/
        int lable_start_top = start_top + hight / 5 + (int) getTextHeight("F1", LABLE_SIZE) / 2;
        int lable_start_left = start_left + (weith / 5 - (int) getTextWidth("F1", LABLE_SIZE)) / 2;
        Paint lable_paint = new Paint();
        lable_paint.setColor(LABLE_COLOR);
        lable_paint.setStrokeWidth(2);
        lable_paint.setAntiAlias(true);
        lable_paint.setTextSize(LABLE_SIZE);
        /*显示标签*/
        canvas.drawText("F1", lable_start_left, lable_start_top + (int) getTextHeight("F1", LABLE_SIZE) / 2, lable_paint);

        /*计算中心控件的坐标值*/
        int view_start_top = start_top + hight / 5 + (int) getTextHeight("F1", LABLE_SIZE) / 2;
        int view_start_left = start_left + weith / 5;
        int view_Len = (int) ((weith / 5) * 3.5f);
        int view_hight = ((int) getTextHeight("F1", LABLE_SIZE) / 5) * 3;
        Paint View_paint = new Paint();
        View_paint.setColor(TEXT_COLOR);
        View_paint.setStrokeWidth(1);
        View_paint.setAntiAlias(true);
        Paint view_paint1 = new Paint();
        view_paint1.setColor(Color.WHITE);
        draw_squre(view_start_left, view_start_top, view_Len, view_hight, View_paint, canvas);
        canvas.drawRect(view_start_left + 1, view_start_top + 1, view_start_left + view_Len - 2, view_start_top + view_hight - 2, view_paint1);
        int per = view_Len / 4;
        int Y_base = view_start_top + view_hight;
        int len1 = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 2;
        int len2 = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 1;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len1, View_paint);
                canvas.drawText(Integer.toString(i * 25), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 25), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * 25), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            } else {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len2, View_paint);
                canvas.drawText(Integer.toString(i * 25), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 25), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * 25), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            }
        }
        canvas.drawLine(view_start_left + view_Len, Y_base, view_start_left + view_Len, Y_base + len1, View_paint);
        canvas.drawText(Integer.toString(4 * 25), view_start_left + 4 * per - (int) ((getTextWidth(Integer.toString(4 * 25), TEXT_SIZE) / 2)),
                Y_base + len1 + (getTextHeight(Integer.toString(4 * 25), TEXT_SIZE) / 2) + 5,
                View_paint
        );
        Paint Head_Paint = new Paint();
        Head_Paint.setColor(HEAD_TEXT_Color);
        Head_Paint.setTextSize(HEAD_TEXT_SIZE);
        Head_Paint.setStrokeWidth(2);
        if (F1 != -1) {
            Paint Data_Paint = new Paint();
            Data_Paint.setColor(DATA_COLUMN_COLOR);
            canvas.drawRect(view_start_left + 1, view_start_top + 4, view_start_left + (int) ((view_Len / 100.1f) * F1), view_start_top + view_hight - 4, Data_Paint);
            canvas.drawText(String.format("%d%%", F1),
                    view_start_left + 2 * per - (int) ((getTextWidth(String.format("%d%%", F1), HEAD_TEXT_SIZE) / 2)),
                    Y_base - (int) (getTextHeight(String.format("%d%%", F1), HEAD_TEXT_SIZE) / 1.5f),
                    Head_Paint);
        }
    }

    /**
     * 绘制QM
     */
    private void Draw_QM(Canvas canvas) {
        int hight = (int) (View_Hight / Child_View_Cnt + 0.5);
        int weith = View_Weith;
        int start_top = this.getTop() + hight * 1;
        int start_left = this.getLeft();
        /*计算标签居中以后的位置*/
        int lable_start_top = start_top + hight / 5 + (int) getTextHeight("Qm", LABLE_SIZE) / 2;
        int lable_start_left = start_left + (weith / 5 - (int) getTextWidth("Qm", LABLE_SIZE)) / 2;
        Paint lable_paint = new Paint();
        lable_paint.setColor(LABLE_COLOR);
        lable_paint.setStrokeWidth(2);
        lable_paint.setAntiAlias(true);
        lable_paint.setTextSize(LABLE_SIZE);
        /*显示标签*/
        canvas.drawText("Qm", lable_start_left, lable_start_top + (int) getTextHeight("Qm", LABLE_SIZE) / 2, lable_paint);

        /*计算中心控件的坐标值*/
        int view_start_top = start_top + hight / 5 + (int) getTextHeight("Qm", LABLE_SIZE) / 2;
        int view_start_left = start_left + weith / 5;
        int view_Len = (int) ((weith / 5) * 3.5f);
        int view_hight = ((int) getTextHeight("Qm", LABLE_SIZE) / 5) * 3;
        Paint View_paint = new Paint();
        View_paint.setColor(TEXT_COLOR);
        View_paint.setStrokeWidth(1);
        View_paint.setAntiAlias(true);
        Paint view_paint1 = new Paint();
        view_paint1.setColor(Color.WHITE);
        draw_squre(view_start_left, view_start_top, view_Len, view_hight, View_paint, canvas);
        canvas.drawRect(view_start_left + 1, view_start_top + 1, view_start_left + view_Len - 2, view_start_top + view_hight - 2, view_paint1);
        int per = view_Len / 4;
        int Y_base = view_start_top + view_hight;
        int len1 = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 2;
        int len2 = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 1;
        int per_values = 20;
        switch (SHOWRANGE) {
            case 1:
                per_values = 5;
                break;
            case 2:
                per_values = 10;
                break;
            case 3:
                per_values = 15;
                break;
            case 4:
                per_values = 20;
                break;
            default:
                per_values = 20;
                break;
        }
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len1, View_paint);
                canvas.drawText(Integer.toString(i * per_values), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 20), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * per_values), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            } else {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len2, View_paint);
                canvas.drawText(Integer.toString(i * per_values), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 20), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * per_values), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            }
        }
        canvas.drawLine(view_start_left + view_Len, Y_base, view_start_left + view_Len, Y_base + len1, View_paint);
        canvas.drawText(Integer.toString(4 * per_values), view_start_left + 4 * per - (int) ((getTextWidth(Integer.toString(4 * per_values), TEXT_SIZE) / 2)),
                Y_base + len1 + (getTextHeight(Integer.toString(4 * per_values), TEXT_SIZE) / 2) + 5,
                View_paint
        );
        Paint Head_Paint = new Paint();
        Head_Paint.setColor(HEAD_TEXT_Color);
        Head_Paint.setTextSize(HEAD_TEXT_SIZE);
        Head_Paint.setStrokeWidth(2);
        if (Qm != -1) {
            Paint Data_Paint = new Paint();
            Data_Paint.setColor(DATA_COLUMN_COLOR);
            if (Qm >= WAR_VALUE) {
                Head_Paint.setColor(WAR_COLOR);
            } else if (Qm >= PER_VALUE) {
                Head_Paint.setColor(PER_COLOR);
            } else {
                Head_Paint.setColor(HEAD_TEXT_Color);
            }
            canvas.drawRect(view_start_left + 1, view_start_top + 4,
                    view_start_left + (int) ((view_Len / (float) (4 * per_values + 0.1f)) * Qm)
                            >= view_start_left + view_Len - 2
                            ? view_start_left + view_Len - 2
                            : view_start_left + (int) ((view_Len / (float) (4 * per_values + 0.1f)) * Qm)
                    , view_start_top + view_hight - 4, Data_Paint);
            canvas.drawText(String.format("%ddB", Qm),
                    view_start_left + 2 * per - (int) ((getTextWidth(String.format("%ddB", Qm), HEAD_TEXT_SIZE) / 2)),
                    Y_base - (int) (getTextHeight(String.format("%ddB", Qm), HEAD_TEXT_SIZE) / 1.5f),
                    Head_Paint);
        }
    }

    /**
     * 绘制QP
     */
    private void Drew_QP(Canvas canvas) {
        int hight = (int) (View_Hight / Child_View_Cnt + 0.5);
        int weith = View_Weith;
        int start_top = this.getTop() + hight * 0;
        int start_left = this.getLeft();
        /*计算标签居中以后的位置*/
        int lable_start_top = start_top + hight / 5 + (int) getTextHeight("Qp", LABLE_SIZE) / 2;
        int lable_start_left = start_left + (weith / 5 - (int) getTextWidth("Qp", LABLE_SIZE)) / 2;
        Paint lable_paint = new Paint();
        lable_paint.setColor(LABLE_COLOR);
        lable_paint.setStrokeWidth(1);
        lable_paint.setAntiAlias(true);
        lable_paint.setTextSize(LABLE_SIZE);
        /*显示标签*/
        canvas.drawText("Qp", lable_start_left, lable_start_top + (int) getTextHeight("Qp", LABLE_SIZE) / 2, lable_paint);

        /*计算中心控件的坐标值*/
        int view_start_top = start_top + hight / 5 + (int) getTextHeight("Qp", LABLE_SIZE) / 2;
        int view_start_left = start_left + weith / 5;
        int view_Len = (int) ((weith / 5) * 3.5f);
        int view_hight = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 3;
        Paint View_paint = new Paint();
        View_paint.setColor(TEXT_COLOR);
        View_paint.setStrokeWidth(1);
        View_paint.setAntiAlias(true);
        Paint view_paint1 = new Paint();
        view_paint1.setColor(Color.WHITE);
        draw_squre(view_start_left, view_start_top, view_Len, view_hight, View_paint, canvas);
        canvas.drawRect(view_start_left + 1, view_start_top + 1, view_start_left + view_Len - 2, view_start_top + view_hight - 2, view_paint1);


        int per = view_Len / 4;
        int Y_base = view_start_top + view_hight;
        int len1 = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 2;
        int len2 = ((int) getTextHeight("Qp", LABLE_SIZE) / 5) * 1;
        int per_values = 20;
        switch (SHOWRANGE) {
            case 1:
                per_values = 5;
                break;
            case 2:
                per_values = 10;
                break;
            case 3:
                per_values = 15;
                break;
            case 4:
                per_values = 20;
                break;
            default:
                per_values = 20;
                break;
        }
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len1, View_paint);
                canvas.drawText(Integer.toString(i * per_values), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 20), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * per_values), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            } else {
                canvas.drawLine(view_start_left + i * per, Y_base, view_start_left + i * per, Y_base + len2, View_paint);
                canvas.drawText(Integer.toString(i * per_values), view_start_left + i * per - (int) ((getTextWidth(Integer.toString(i * 20), TEXT_SIZE) / 2)),
                        Y_base + len1 + (getTextHeight(Integer.toString(4 * per_values), TEXT_SIZE) / 2) + 5,
                        View_paint
                );
            }
        }
        canvas.drawLine(view_start_left + view_Len, Y_base, view_start_left + view_Len, Y_base + len1, View_paint);
        canvas.drawText(Integer.toString(4 * per_values), view_start_left + 4 * per - (int) ((getTextWidth(Integer.toString(4 * per_values), TEXT_SIZE) / 2)),
                Y_base + len1 + (getTextHeight(Integer.toString(4 * per_values), TEXT_SIZE) / 2) + 5,
                View_paint
        );
        Paint Head_Paint = new Paint();
        Head_Paint.setColor(HEAD_TEXT_Color);
        Head_Paint.setTextSize(HEAD_TEXT_SIZE);
        Head_Paint.setStrokeWidth(2);
        if (Qp != -1) {
            Paint Data_Paint = new Paint();
            Data_Paint.setColor(DATA_COLUMN_COLOR);
            if (Qp >= WAR_VALUE) {
                Head_Paint.setColor(WAR_COLOR);
            } else if (Qp >= PER_VALUE) {
                Head_Paint.setColor(PER_COLOR);
            } else {
                Head_Paint.setColor(HEAD_TEXT_Color);
            }
            canvas.drawRect(view_start_left + 1, view_start_top + 4,
                    view_start_left + (int) ((view_Len / (float) (4 * per_values + 0.1f)) * Qp)
                            >= view_start_left + view_Len - 2
                            ? view_start_left + view_Len - 2
                            : view_start_left + (int) ((view_Len / (float) (4 * per_values + 0.1f)) * Qp)
                    , view_start_top + view_hight - 4, Data_Paint);
            canvas.drawText(String.format("%ddB", Qp),
                    view_start_left + 2 * per - (int) ((getTextWidth(String.format("%ddB", Qp), HEAD_TEXT_SIZE) / 2)),
                    Y_base - (int) (getTextHeight(String.format("%ddB", Qp), HEAD_TEXT_SIZE) / 1.5f),
                    Head_Paint);
        }
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

    /*设置*/

    /**
     * 设置背景颜色
     *
     * @param color 颜色
     */
    public void setBKG_COLOR(int color) {
        BKG_COLOR = color;

    }

    /**
     * 设置标签颜色
     *
     * @param color 颜色
     */
    public void SetLableColor(int color) {
        LABLE_COLOR = color;
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
     * 设置标签大小
     *
     * @param size
     */
    public void SetLableSize(int size) {

        TEXT_SIZE = size;
    }

    /**
     * 设置字体大小
     */
    public void SetTextSize(int size) {
        TEXT_SIZE = size;
    }

    public void SetHeadColor(int color) {

        HEAD_TEXT_Color = color;
    }

    /**
     * 设置抬头颜色
     *
     * @param size
     */
    public void SetHeadSize(int size) {
        HEAD_TEXT_SIZE = size;
    }

    public void SetDataColumnColor(int color) {
        DATA_COLUMN_COLOR = color;

    }

    /**
     * 设置预警
     *
     * @param values 预警值
     * @param color  预警颜色
     */
    public void SetPre(int values, int color) {
        PER_COLOR = color;
        PER_VALUE = values;

    }

    /**
     * 设置报警
     *
     * @param values 报警值
     * @param color  报警颜色
     */

    public void SetWar(int values, int color) {
        WAR_COLOR = color;
        WAR_VALUE = values;
    }

    /**
     * 数据设置
     *
     * @param Qp 数据1
     * @param Qm 数据2
     * @param F1 数据3
     * @param F2 数据4
     */
    public void SetData(int Qp, int Qm, int F1, int F2) {
        this.Qp = Qp;
        this.Qm = Qm;
        this.F1 = F1;
        this.F2 = F2;
        /*从绘界面*/
        invalidate();
    }

    /**
     * 清空显示
     */
    public void ClearData() {
        this.Qp = -1;
        this.Qm = -1;
        this.F1 = -1;
        this.F2 = -1;

        /*从绘界面*/
        invalidate();

    }

    /**
     * 设置显示范围
     *
     * @param Range 0-0 ->1;0-40  ->2;0-60  ->3;0-80 ->4
     */
    public void SetShowRange(int Range) {

        SHOWRANGE = Range;
        invalidate();
    }


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
    private void draw_squre(int x, int y, int whith, int hight, Paint paint, Canvas canvas) {
        /*top*/
        canvas.drawLine(x, y, x + whith, y, paint);
        /*bottom*/
        canvas.drawLine(x, y + hight, x + whith, y + hight, paint);
        /*left*/
        canvas.drawLine(x, y, x, y + hight, paint);
        /*right*/
        canvas.drawLine(x + whith, y, x + whith, y + hight, paint);

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
