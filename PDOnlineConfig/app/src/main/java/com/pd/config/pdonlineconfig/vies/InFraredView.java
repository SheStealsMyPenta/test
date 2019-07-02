package com.pd.config.pdonlineconfig.vies;

import android.content.Context;
import android.graphics.*;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.pd.config.pdonlineconfig.fragment.testFrag.InfraredTestFragment;
import com.pd.config.pdonlineconfig.impls.InternetService;
import com.pd.config.pdonlineconfig.utils.ConversionTool;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InFraredView extends View {
    private int heightOfTheScreen;
    private int widthOfTheScreen;
    private Paint staticPaint; //静态不变的画笔
    private Paint dynamicPaint; //会变化的画笔
    private int widthOfTheFrame;
    private float viewLeft; //距离左边的坐标
    private float viewTop; //距离上边的坐标
    private float orignalY;
    private float orignalX;
    private float startPointX;
    private float startPointY;
    private float marginRight;
    private float marginBottom;
    private float marginLeft;
    private boolean isWrite = false;
    private float marginTop;
    private float eachStroke = 1.5f;
    private float picktureWidth = 360;
    private float picktureHeight = 288;
    private int[][] colorMatrix;
    private int xIndex;
    private int yIndex;
    private float xCoordinate;//画图的横坐标
    private float yCoordinate;
    private float xCoordinateFinger=360;
    private float yCoordinateFinger=288;
    private float xCoordinateMax;
    private float yCoordinateMax;
    private float xCoordinateMin;
    private float yCoordinateMin;


    private InternetService service;
    private InfraredTestFragment fragment;
    private float crossOffset = 12f;
    private int heightOFTheFrame;
    private List<Integer> listOfint = new ArrayList<>();
    private Bitmap cacheBitMap;
    private Canvas cacheCanvas;


    public InternetService getService() {
        return service;
    }


    public int[][] getColorMatrix() {
        return colorMatrix;
    }


    public InFraredView(Context context) {

        super(context);
    }

    public InFraredView(Context context, AttributeSet attrs) {
        super(context, attrs);

        cacheCanvas = new Canvas();

        viewSet();
        initPaints();
//        System.out.println(widthOfTheScreen);

    }

    public InFraredView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        cacheCanvas = new Canvas();

        viewSet();
        initPaints();
//        System.out.println(widthOfTheScreen);
//        cacheBitMap = Bitmap.createBitmap(widthOfTheScreen, heightOfTheScreen*288/360, Bitmap.Config.ARGB_8888);
//        cacheCanvas.setBitmap(cacheBitMap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightOfTheScreen = heightSize;
        widthOfTheScreen = widthSize;
        widthOfTheFrame = widthOfTheScreen;
        System.out.println(widthOfTheScreen);
        heightOFTheFrame = widthOfTheFrame * 288 / 360;
        System.out.println(heightOFTheFrame);
        cacheBitMap = Bitmap.createBitmap(widthOfTheFrame, heightOFTheFrame, Bitmap.Config.ARGB_8888);
        cacheCanvas.setBitmap(cacheBitMap);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (colorMatrix != null) {
//            Paint bmpPaint = new Paint();
            canvas.drawBitmap(cacheBitMap, 0, 0, null);
        }
//        drawCross(cacheCanvas);
        drawCross(canvas);


    }

    private void drawCross(Canvas canvas) {
        staticPaint.setStrokeWidth(2.0f);
        staticPaint.setColor(Color.BLUE);
        canvas.drawLine(xCoordinateFinger, yCoordinateFinger, xCoordinateFinger, yCoordinateFinger + crossOffset, staticPaint);
        canvas.drawLine(xCoordinateFinger, yCoordinateFinger, xCoordinateFinger, yCoordinateFinger - crossOffset, staticPaint);
        canvas.drawLine(xCoordinateFinger, yCoordinateFinger, xCoordinateFinger - crossOffset, yCoordinateFinger, staticPaint);
        canvas.drawLine(xCoordinateFinger, yCoordinateFinger, xCoordinateFinger + crossOffset, yCoordinateFinger, staticPaint);
        invalidate();
    }

    public void setColorMatrix(int[][] colorMatrix, short xCoordinateMax, short yCoordinateMax, short xCoordinateMin, short yCoordinateMin) {
        long start1 = System.currentTimeMillis();
        this.colorMatrix = colorMatrix;
        long end1 = System.currentTimeMillis();
        System.out.println("幅值花了" + (end1 - start1));
//        listOfint.clear();
        int eachStepX = 2;
        int eachStepY = 2;

        long start = System.currentTimeMillis();
        for (int i = 0; i < picktureHeight; i++) {
            for (int j = 0; j < picktureWidth; j++) {
                xCoordinate = orignalX + j * 2;
                yCoordinate = orignalY + i * 2;
//                if (colorMatrix[i][j] == Color.BLACK) {
//                    dynamicPaint.setColor(Color.YELLOW);
////                    System.out.println("横坐标："+i+"y:" +j);
//                }else {
                dynamicPaint.setColor(colorMatrix[i][j]);
//                }
                cacheCanvas.drawRect(xCoordinate, yCoordinate, xCoordinate + eachStepX, yCoordinate + eachStepY, dynamicPaint);
//                cacheCanvas.drawCircle(xCoordinate, yCoordinate, 1.5f, dynamicPaint);
//                cacheCanvas.draw(xCoordinate, yCoordinate, xCoordinate + 1, yCoordinate, dynamicPaint);
//                cacheCanvas.drawPoint(xCoordinate, yCoordinate, dynamicPaint);

//                if (!isWrite) {
//                    listOfint.add(colorMatrix[i][j]);
//                }

            }

        }

//        drawRect(cacheCanvas);
        long end = System.currentTimeMillis();
        System.out.println("画图的时间为:" + (end - start));
        drawCrossMax(cacheCanvas, xCoordinateMax, yCoordinateMax, Color.GREEN);
        drawCrossMax(cacheCanvas, xCoordinateMin, yCoordinateMin, Color.parseColor("#FFD700"));

        //writeToFile();
        invalidate();

    }

    private void drawCrossMax(Canvas cacheCanvas, short xCoordinateMax, short yCoordinateMax, int color) {
        float x = xCoordinateMax * 2;
        float y = yCoordinateMax * 2;
        staticPaint.setColor(color);
        cacheCanvas.drawLine(x, y, x, y + crossOffset, staticPaint);
        cacheCanvas.drawLine(x, y, x, y - crossOffset, staticPaint);
        cacheCanvas.drawLine(x, y, x - crossOffset, y, staticPaint);
        cacheCanvas.drawLine(x, y, x + crossOffset, y, staticPaint);
    }

    private void drawEachPointByColorMatrix(int[][] colorMatrix, Canvas canvas) {
        picktureWidth = colorMatrix[0].length;
        picktureHeight = colorMatrix.length;
        float eachStepX = (widthOfTheFrame / picktureWidth);
        float eachStepY = (heightOFTheFrame / picktureHeight);
        long start = System.currentTimeMillis();
        for (int i = 0; i < picktureHeight; i++) {
            for (int j = 0; j < picktureWidth; j++) {
                xCoordinate = orignalX + j * 2;
                yCoordinate = orignalY + i * 2;
//
//                short colorValue =
//                listOfShort.add(colorValue);

//                if (colorValue != 0) {
//                    printRGB(ConversionTool.ConvertRgb565(colorValue));
//                }
                dynamicPaint.setColor(colorMatrix[i][j]);
//                canvas.drawRect(xCoordinate, yCoordinate, xCoordinate + eachStepX, yCoordinate + eachStepY, dynamicPaint);
                canvas.drawPoint(xCoordinate, yCoordinate, dynamicPaint);
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("绘图耗时" + (end - start) + "毫秒");
    }

    private void writeToFile() {
        if (!isWrite) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/data/" + "color" + System.currentTimeMillis() + ".bin";
            Runnable a = () -> {
                File file = new File(path);
                FileOutputStream outputStream = null;
                BufferedOutputStream bufferOut = null;
//            DataOutputStream outputStream1 =new DataOutputStream(outputStream);

                try {
                    boolean file1 = file.createNewFile();
                    if (file1) {
                        outputStream = new FileOutputStream(file);
                        bufferOut = new BufferedOutputStream(outputStream);
                        for (Integer i : listOfint) {
                            byte[] bytes = ConversionTool.int2BytesArray(i);
                            bufferOut.write(bytes);
                        }
                        bufferOut.flush();
                        outputStream.flush();
                        System.out.println("写入文件成功");

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        System.out.println("关闭流成功");
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread thread = new Thread(a);
            thread.start();
            isWrite = true;
        }


    }

    public byte[] short2bits(short n) {
        byte[] temp = new byte[2];

        temp[0] = (byte) n;
        temp[1] = (byte) (n >> 8);
        return temp;
    }

    private void viewSet() {

        viewLeft = getLeft();
        viewTop = getTop();
        orignalY = marginTop;
        startPointX = viewLeft + (widthOfTheScreen / 20.0f) * 1.5f;
        startPointY = viewTop + (heightOfTheScreen / 20.0f);
        marginRight = (widthOfTheScreen / 20.0f) * 1.3f;
        marginLeft = (widthOfTheScreen / 20.0f) * 1.3f;
        marginTop = (heightOfTheScreen / 25.0f) * 4.5f;
        orignalX = marginLeft;
    }

    private void drawRect(Canvas canvas) {
//        short[][] infrared= new short[1024][56];
        //画出整体边框
        canvas.drawRect(marginLeft, marginTop, widthOfTheScreen - marginRight, heightOfTheScreen - marginBottom, staticPaint);

    }

    private void initPaints() {
        staticPaint = new Paint();
        staticPaint.setAntiAlias(true);
        staticPaint.setStrokeWidth(2.5f);
        staticPaint.setStyle(Paint.Style.STROKE);
        staticPaint.setColor(Color.BLACK);
        dynamicPaint = new Paint();
        dynamicPaint.setAntiAlias(true);
        dynamicPaint.setStrokeWidth(eachStroke);
        dynamicPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //相对图片框的x，y坐标
                float x = event.getX() - orignalX;
                float y = event.getY() - orignalY;
//                System.out.println(x);
//                System.out.println(y);
                if (event.getY() < heightOFTheFrame) {
                    xIndex = (int) ((x / widthOfTheFrame) * 360);//占了Y轴的多少份
                    yIndex = (int) ((y / heightOFTheFrame) * 288);
                    xCoordinateFinger = event.getX();
                    yCoordinateFinger = event.getY();
                    fragment.getCurrentTemp((short) xIndex, (short) yIndex);
//                    System.out.println(xIndex);
//                    System.out.println(yIndex);

                    invalidate();
                }

//                System.out.println(x);
//                System.out.println(y);
//                System.out.println(orignalX);
//                System.out.println(orignalY);
                break;
            case MotionEvent.ACTION_UP:
                float x1 = event.getX();
                float y1 = event.getY();
                xIndex = (int) (x1 / widthOfTheFrame);//占了Y轴的多少份
                yIndex = (int) y1 / heightOfTheScreen;
                if (service != null) {
//                    service.getTempValueByIndex(xIndex, yIndex);
                }
//                System.out.println(x1);
//                System.out.println(y1);
                break;
        }
        return super.onTouchEvent(event);
    }

    void printRGB(int color) {
        int r = (color & 0xff0000) >> 16;
        int g = (color & 0xff00) >> 8;
        int b = color & 0xff;
        System.out.println(r + ", " + g + ", " + b);
    }

    public void setFragment(InfraredTestFragment fragment) {
        this.fragment = fragment;
    }
}
