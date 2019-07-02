package PDChartsPack.pack_pd_charts;

import android.graphics.Canvas;
import android.graphics.Paint;

import PDChartsPack.pack_commen.Convert_Tools;


/**
 * Created by SONG on 2018/3/7 9:28.
 * The final explanation right belongs to author
 */

public final class AA_Asiy {

    public static String[] Asiy1 = {"2000", "200", "20", "2", "0"};


    public static void Drw_Asiy4BT(Canvas canvas, float Start_X, float Start_Y, float End_Y, float Tab, float TEXT_SIZE)
    {
        Paint paint_text = new Paint();
        paint_text.setColor(Color_set.TEXT_COLOR);
        paint_text.setAntiAlias(true);
        paint_text.setStrokeWidth(1);
        paint_text.setTextSize(TEXT_SIZE);
        float Per_Larage_Tab = (End_Y - Start_Y) / (4);
        float Per_Small_Tab = Per_Larage_Tab / 5.0f;
        for (int i = 0; i < 5; i++) {

            String lable = Convert_Tools.FormatVaule(Tab/4*(4-i)); //String.format("%.1f",Tab/4*(4-i));
            /**绘制长刻度**/
            canvas.drawLine(Start_X,
                    Start_Y + i * Per_Larage_Tab
                    , Start_X - 15.0f
                    , Start_Y + i * Per_Larage_Tab
                    , paint_text
            );
            /**绘制标签**/
            canvas.drawText(lable
                    , Start_X - 20.0f - getTextWidth(lable, TEXT_SIZE)
                    , Start_Y + i * Per_Larage_Tab + getTextHeight(lable, TEXT_SIZE) / 4.0f
                    , paint_text);

            /**绘制短刻度**/
            if (i != 5-1) {
                for (int j = 1; j < 5; j++) {
                    canvas.drawLine(Start_X,
                            Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
                            , Start_X - 7.5f
                            , Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
                            , paint_text
                    );
                }
            }

        }
    }


    /**
     * 绘制Y轴专用方法
     *
     * @param canvas  画布对象
     * @param Start_X
     * @param Start_Y
     * @param End_Y
     * @param IS_LEFT 画在左边还是右边
     */
    public static void Draw_Asiy(Canvas canvas, float Start_X, float Start_Y, float End_Y, int Y_CNT, boolean IS_LEFT, float Text_Size) {
        Paint paint_text = new Paint();
        paint_text.setColor(Color_set.TEXT_COLOR);
        paint_text.setAntiAlias(true);
        paint_text.setStrokeWidth(1);
        paint_text.setTextSize(Text_Size);
        float Per_Larage_Tab = (End_Y - Start_Y) / (Y_CNT - 1);
        float Per_Small_Tab = Per_Larage_Tab / 5.0f;

        for (int i = 0; i < Y_CNT; i++) {
            //5-Y_CNT+i
            /**判断往左绘制还是向右绘制**/
            if (!IS_LEFT) {
                /**绘制长刻度**/
                canvas.drawLine(Start_X,
                        Start_Y + i * Per_Larage_Tab
                        , Start_X - 15.0f
                        , Start_Y + i * Per_Larage_Tab
                        , paint_text
                );
                /**绘制标签**/
                canvas.drawText(Asiy1[5 - Y_CNT + i]
                        , Start_X - 20.0f - getTextWidth(Asiy1[5 - Y_CNT + i], Text_Size)
                        , Start_Y + i * Per_Larage_Tab + getTextHeight(Asiy1[5 - Y_CNT + i], Text_Size) / 4.0f
                        , paint_text);

                /**绘制短刻度**/
                if (i != Y_CNT - 1) {
                    for (int j = 1; j < 5; j++) {
                        canvas.drawLine(Start_X,
                                Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
                                , Start_X - 7.5f
                                , Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
                                , paint_text
                        );
                    }
                }

            } else {
                /**绘制长刻度**/
                canvas.drawLine(Start_X,
                        Start_Y + i * Per_Larage_Tab
                        , Start_X + 15.0f
                        , Start_Y + i * Per_Larage_Tab
                        , paint_text
                );
                /**绘制标签**/
                canvas.drawText(Asiy1[5 - Y_CNT + i]
                        , Start_X + 20.0f
                        , Start_Y + i * Per_Larage_Tab + getTextHeight(Asiy1[5 - Y_CNT + i], Text_Size) / 4.0f
                        , paint_text);

                /**绘制短刻度**/
                if (i != Y_CNT - 1) {
                    for (int j = 1; j < 5; j++) {
                        canvas.drawLine(Start_X,
                                Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
                                , Start_X + 7.5f
                                , Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
                                , paint_text
                        );
                    }
                }
            }


        }


    }
    public static void Draw_Asiy_1(Canvas canvas, float Start_X, float Start_Y, float End_Y, int Y_CNT, boolean IS_LEFT, float Text_Size) {
        Paint paint_text = new Paint();
        paint_text.setColor(Color_set.TEXT_COLOR);
        paint_text.setAntiAlias(true);
        paint_text.setStrokeWidth(1);
        paint_text.setTextSize(Text_Size);
        float Per_Larage_Tab = (End_Y - Start_Y) / (Y_CNT - 1);
        float Per_Small_Tab = Per_Larage_Tab / 5.0f;

        for (int i = 0; i < Y_CNT; i++) {
            //5-Y_CNT+i
            /**判断往左绘制还是向右绘制**/
            if (!IS_LEFT) {
                /**绘制长刻度**/
//                canvas.drawLine(Start_X,
//                        Start_Y + i * Per_Larage_Tab
//                        , Start_X - 15.0f
//                        , Start_Y + i * Per_Larage_Tab
//                        , paint_text
//                );
                /**绘制标签**/
                canvas.drawText(Asiy1[5 - Y_CNT + i]
                        , Start_X - Text_Size/2.5f - getTextWidth(Asiy1[5 - Y_CNT + i], Text_Size)
                        , Start_Y + i * Per_Larage_Tab + getTextHeight(Asiy1[5 - Y_CNT + i], Text_Size) / 4.0f
                        , paint_text);

//                /**绘制短刻度**/
//                if (i != Y_CNT - 1) {
//                    for (int j = 1; j < 5; j++) {
//                        canvas.drawLine(Start_X,
//                                Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
//                                , Start_X - 7.5f
//                                , Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
//                                , paint_text
//                        );
//                    }
//                }

            } else {
                /**绘制长刻度**/
//                canvas.drawLine(Start_X,
//                        Start_Y + i * Per_Larage_Tab
//                        , Start_X + 15.0f
//                        , Start_Y + i * Per_Larage_Tab
//                        , paint_text
//                );
                /**绘制标签**/
                canvas.drawText(Asiy1[5 - Y_CNT + i]
                        , Start_X +Text_Size/2.5f
                        , Start_Y + i * Per_Larage_Tab + getTextHeight(Asiy1[5 - Y_CNT + i], Text_Size) / 4.0f
                        , paint_text);

                /**绘制短刻度**/
//                if (i != Y_CNT - 1) {
//                    for (int j = 1; j < 5; j++) {
//                        canvas.drawLine(Start_X,
//                                Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
//                                , Start_X + 7.5f
//                                , Start_Y + i * Per_Larage_Tab + j * Per_Small_Tab
//                                , paint_text
//                        );
//                    }
//                }
            }


        }


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
        return fm.descent - fm.ascent;
    }
}
