package PDChartsPack.pack_pd_charts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by SONG on 2018/5/9 13:31.
 * The final explanation right belongs to author
 */
public class Information_Drawer {

    private static Paint paint = new Paint();

    public static void Draw_Left_Tips(Canvas canvas, float viewstart_x, float viewstart_y, float height, String Msg, float Text_size) {
        String msg1 = Msg.substring(Msg.lastIndexOf("("));


        float Text_Height = getTextHeight(Msg, Text_size);
        float Text_Width = getTextWidth(Msg, Text_size);

        /**字体坐标**/
        float Text_Startx = viewstart_x + Text_Height / 2.0f;
        float Text_Starty = viewstart_y + (height - Text_Width) / 2.0f;

        /**上箭头一**/
        float TOP1X = Text_Startx;
        float TOP1Y = Text_Starty - Text_Height * 1.5f;

        /**上箭头二**/
        float TOP2X = Text_Startx;
        float TOP2Y = TOP1Y - Text_Height * 0.7f;

        /**上箭头三**/
        float TOP3X = Text_Startx;
        float TOP3Y = TOP2Y - Text_Height * 0.7f;


        /**下箭头一**/
        float BOTTOM1X = Text_Startx;
        float BOTTOM1Y = Text_Starty + Text_Width + Text_Height * 0.5f;

        /**下箭头二**/
        float BOTTOM2X = Text_Startx;
        float BOTTOM2Y = BOTTOM1Y + Text_Height * 0.7f;

        /**下箭头三**/
        float BOTTOM3X = Text_Startx;
        float BOTTOM3Y = BOTTOM2Y + Text_Height * 0.7f;

        float C1X = Text_Startx - (getTextHeight("阈",Text_size)-getTextWidth("阈",Text_size))/2.0f;
        float C1Y = Text_Starty + getTextWidth("阈",Text_size)/2.0f;

        float C2X = Text_Startx -  (getTextHeight("值",Text_size)-getTextWidth("值",Text_size))/2.0f;
        float C2Y = C1Y + getTextWidth("值",Text_size);


        float S1X = Text_Startx + Text_Height/2.0f + (getTextHeight("值",Text_size)-getTextWidth("值",Text_size))/2.0f;
        float S1Y = C2Y + getTextWidth(msg1,Text_size) + getTextWidth("值",Text_size)/2.0f;

        /**画笔初始化**/
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(Color_set.INFORMATION_COLOR);
        paint.setTextSize(Text_size);

        canvas.drawText("阈",C1X,C1Y,paint);
        canvas.drawText("值",C2X,C2Y,paint);



        /**绘制字体**/
        canvas.rotate(-90, S1X, S1Y);
        canvas.drawText(msg1, S1X, S1Y, paint);
        canvas.rotate(90, S1X, S1Y);


        /**绘制箭头**/
        paint.setColor(Color_set.ARROW_Color_1);
        /***＜**/
        canvas.rotate(90, TOP1X, TOP1Y);
        canvas.drawText("＜", TOP1X, TOP1Y, paint);
        canvas.rotate(-90, TOP1X, TOP1Y);

        /***＞**/
        canvas.rotate(90, BOTTOM1X, BOTTOM1Y);
        canvas.drawText("＞", BOTTOM1X, BOTTOM1Y, paint);
        canvas.rotate(-90, BOTTOM1X, BOTTOM1Y);


        paint.setColor(Color_set.ARROW_Color_2);
        /***＜**/
        canvas.rotate(90, TOP2X, TOP2Y);
        canvas.drawText("＜", TOP2X, TOP2Y, paint);
        canvas.rotate(-90, TOP2X, TOP2Y);

        /***＞**/
        canvas.rotate(90, BOTTOM2X, BOTTOM2Y);
        canvas.drawText("＞", BOTTOM2X, BOTTOM2Y, paint);
        canvas.rotate(-90, BOTTOM2X, BOTTOM2Y);


        paint.setColor(Color_set.ARROW_Color_3);

        /***＜**/
        canvas.rotate(90, TOP3X, TOP3Y);
        canvas.drawText("＜", TOP3X, TOP3Y, paint);
        canvas.rotate(-90, TOP3X, TOP3Y);

        /***＞**/
        canvas.rotate(90, BOTTOM3X, BOTTOM3Y);
        canvas.drawText("＞", BOTTOM3X, BOTTOM3Y, paint);
        canvas.rotate(-90, BOTTOM3X, BOTTOM3Y);

    }

    public static void Draw_Right_Tips(Canvas canvas, float viewend_x, float viewstart_y, float height, String Msg, float Text_size) {
        String msg1 = Msg.substring(Msg.lastIndexOf("("));

        float Text_Height = getTextHeight(Msg, Text_size);
        float Text_Width = getTextWidth(Msg, Text_size);

        /**字体坐标**/
        float Text_Startx = viewend_x - Text_Height;
        float Text_Starty = viewstart_y + (height - Text_Width) / 2.0f;

        /**上箭头一**/
        float TOP1X = Text_Startx;
        float TOP1Y = Text_Starty - Text_Height * 1.5f;

        /**上箭头二**/
        float TOP2X = Text_Startx;
        float TOP2Y = TOP1Y - Text_Height * 0.7f;

        /**上箭头三**/
        float TOP3X = Text_Startx;
        float TOP3Y = TOP2Y - Text_Height * 0.7f;


        /**下箭头一**/
        float BOTTOM1X = Text_Startx;
        float BOTTOM1Y = Text_Starty + Text_Width + Text_Height * 0.5f;

        /**下箭头二**/
        float BOTTOM2X = Text_Startx;
        float BOTTOM2Y = BOTTOM1Y + Text_Height * 0.7f;

        /**下箭头三**/
        float BOTTOM3X = Text_Startx;
        float BOTTOM3Y = BOTTOM2Y + Text_Height * 0.7f;


        float C1X = Text_Startx - (getTextHeight("量",Text_size)-getTextWidth("量",Text_size))/2.0f;
        float C1Y = Text_Starty + getTextWidth("量",Text_size)/2.0f;

        float C2X = Text_Startx -  (getTextHeight("程",Text_size)-getTextWidth("程",Text_size))/2.0f;
        float C2Y = C1Y + getTextWidth("程",Text_size);


        float S1X = Text_Startx + Text_Height/2.0f  + (getTextHeight("量",Text_size)-getTextWidth("量",Text_size))/2.0f;
        float S1Y = C2Y + getTextWidth(msg1,Text_size) + getTextWidth("程",Text_size)/2.0f;




        /**画笔初始化**/
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(Color_set.INFORMATION_COLOR);
        paint.setTextSize(Text_size);

        canvas.drawText("量",C1X,C1Y,paint);
        canvas.drawText("程",C2X,C2Y,paint);

        /**绘制字体**/
        canvas.rotate(-90, S1X, S1Y);
        canvas.drawText(msg1, S1X, S1Y, paint);
        canvas.rotate(90, S1X, S1Y);


        /**绘制箭头**/
        paint.setColor(Color_set.ARROW_Color_1);
        /***＜**/
        canvas.rotate(90, TOP1X, TOP1Y);
        canvas.drawText("＜", TOP1X, TOP1Y, paint);
        canvas.rotate(-90, TOP1X, TOP1Y);

        /***＞**/
        canvas.rotate(90, BOTTOM1X, BOTTOM1Y);
        canvas.drawText("＞", BOTTOM1X, BOTTOM1Y, paint);
        canvas.rotate(-90, BOTTOM1X, BOTTOM1Y);


        paint.setColor(Color_set.ARROW_Color_2);
        /***＜**/
        canvas.rotate(90, TOP2X, TOP2Y);
        canvas.drawText("＜", TOP2X, TOP2Y, paint);
        canvas.rotate(-90, TOP2X, TOP2Y);

        /***＞**/
        canvas.rotate(90, BOTTOM2X, BOTTOM2Y);
        canvas.drawText("＞", BOTTOM2X, BOTTOM2Y, paint);
        canvas.rotate(-90, BOTTOM2X, BOTTOM2Y);


        paint.setColor(Color_set.ARROW_Color_3);

        /***＜**/
        canvas.rotate(90, TOP3X, TOP3Y);
        canvas.drawText("＜", TOP3X, TOP3Y, paint);
        canvas.rotate(-90, TOP3X, TOP3Y);

        /***＞**/
        canvas.rotate(90, BOTTOM3X, BOTTOM3Y);
        canvas.drawText("＞", BOTTOM3X, BOTTOM3Y, paint);
        canvas.rotate(-90, BOTTOM3X, BOTTOM3Y);

    }


    public static void Draw_Title(Canvas canvas, float start_x, float start_y, float width, float height, String msgm, float text_size)
    {
        Paint title_paint = new Paint();
        Path title_path = new Path();

        title_paint.setTextSize(text_size);

        float title_start_X = start_x + width / 16.0f;
        float titile_start_Y = start_y + height / 20.0f;
        float title_end_x = title_start_X + getTextWidth("AAAAA",text_size);
        float title_end_y = start_y + height / 10.0f * 1.2f;


        title_path.moveTo(title_start_X, titile_start_Y);
        title_path.lineTo(title_end_x, titile_start_Y);
        title_path.lineTo(title_end_x, title_end_y);
        title_path.lineTo(title_start_X, title_end_y);
        title_path.close();

        title_paint.setStyle(Paint.Style.FILL);
        title_paint.setColor(Color.argb(0x80, 0xae, 0xae, 0xae));
        canvas.drawPath(title_path, title_paint);
        title_paint.setStyle(Paint.Style.STROKE);
        title_paint.setColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
        canvas.drawPath(title_path, title_paint);
        title_paint.setStyle(Paint.Style.FILL);
        canvas.drawText(msgm
                , title_start_X + (title_end_x - title_start_X) / 2.0f - getTextWidth(msgm, text_size) / 2.0f
                , titile_start_Y + (title_end_y - titile_start_Y) / 2.0f + getTextHeight(msgm, text_size) / 3.0f
                , title_paint);


    }

    /**绘制左侧居中信息**/
    public static void Draw_left_Center(Canvas canvas, float star_x, float start_y, float height, String msg, float text_size)
    {

        /**画笔初始化**/
        paint.setTextSize(text_size);
        paint.setColor(Color_set.TEXT_COLOR);

        float text_startx = star_x - getTextHeight(msg,text_size)/2.0f;
        float text_start_y = start_y + (height - getTextWidth(msg,text_size))/2.0f + getTextWidth(msg,text_size);

        canvas.rotate(-90,text_startx,text_start_y);
        canvas.drawText(msg,text_startx,text_start_y,paint);
        canvas.rotate(90,text_startx,text_start_y);

    }


    /***
     * 绘制右侧垂直居中信息
     * @param canvas
     * @param star_x
     * @param start_y
     * @param height
     * @param msg
     * @param text_size
     */
    public static void  Draw_Right_Center(Canvas canvas, float star_x, float start_y, float height, String msg, float text_size)
    {

        /**画笔初始化**/
        paint.setTextSize(text_size);
        paint.setColor(Color_set.TEXT_COLOR);

        float text_startx = star_x + getTextHeight(msg,text_size);
        float text_start_y = start_y + (height - getTextWidth(msg,text_size))/2.0f+ getTextWidth(msg,text_size);

        canvas.rotate(-90,text_startx,text_start_y);
        canvas.drawText(msg,text_startx,text_start_y,paint);
        canvas.rotate(90,text_startx,text_start_y);

    }


    /**
     * 计算字体的占用宽度
     *
     * @param text     字符串
     * @param textsize 字体大小
     * @return
     */
    private static float getTextWidth(String text, float textsize) {
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
    private static float getTextHeight(String text, float textsize) {
        Paint paint = new Paint();
        paint.setTextSize(textsize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }




}



