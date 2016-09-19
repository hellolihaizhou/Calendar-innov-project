package punchclock.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.lihaizhou.mycalendar.R;


/**
 *柱状图
 *
 */
public class HistogramView extends View {
    private Paint xLinePaint;// 坐标轴 轴线 画笔：
    private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
    private Paint titlePaint;// 绘制文本的画笔
    private Paint paint;// 矩形画笔 柱状图的样式信息
    private   int[] Progress;// 实现动画的值
    private Bitmap bitmap;
    // 坐标轴左侧的数标
    private   String[] ySteps;
    // 坐标轴底部的文本
    private  String[] texts;
    private  double max;//y轴最大值

    public void setDate(int[] progress, String[] texts, double max) {

        Progress = progress;
        this.texts = texts;
        this.max = max;
    }

    public HistogramView(Context context) {
        super(context);
        init();
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        xLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        paint = new Paint();

        // 给画笔设置颜色
        xLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);

        // 加载画图
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight() - dp2px(50);
        // 绘制底部的线条
        canvas.drawLine(dp2px(20), height + dp2px(3), width - dp2px(30), height
                + dp2px(3), xLinePaint);

        int leftHeight = height - dp2px(5);// 左侧外周的 需要划分的高度：

        int hPerHeight = leftHeight / 4;// 分成四部分

        hLinePaint.setTextAlign(Align.CENTER);
/*        // 设置四条虚线
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(dp2px(30), dp2px(10) + i * hPerHeight, width
                    - dp2px(30), dp2px(10) + i * hPerHeight, hLinePaint);
        }*/

        // 绘制 Y 周坐标
        titlePaint.setTextAlign(Align.RIGHT);
        titlePaint.setTextSize(sp2px(12));
        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);
 /*       // 设置左部的数字
        for (int i = 0; i < ySteps.length; i++) {
            canvas.drawText(ySteps[i], dp2px(25), dp2px(13) + i * hPerHeight,
                    titlePaint);
        }*/

        // 绘制 X 周 做坐标
        int xAxisLength = width - dp2px(30);
        int columCount = texts.length + 1;
        int step = xAxisLength / columCount;


        // 设置底部的数字
        for (int i = 0; i < columCount - 1; i++) {
            // text, baseX, baseY, textPaint
       /*   canvas.drawText(texts[i], dp2px(25) + step * (i + 1), height
                    + dp2px(20), titlePaint)*/
/*        	TextPaint textPaint = new TextPaint();
        	textPaint.setARGB(0xFF, 0xFF, 0, 0);
        	canvas.translate( dp2px(25) + step * (i + 1), height+ dp2px(20));
        	textPaint.setTextSize(20.0F);
        	 StaticLayout layout = new StaticLayout(texts[i],textPaint,240,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
        	 layout.draw(canvas);*/

            TextPaint textPaint = new TextPaint();
            textPaint.setARGB(0xFF, 0, 0, 0);
            textPaint.setTextSize(15);
            textPaint.setAntiAlias(true);
            StaticLayout layout = new StaticLayout(texts[i], textPaint, 100,
                    Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            canvas.save();
            canvas.translate( step * (i + 1), height+ dp2px(15));//从20，20开始画
            layout.draw(canvas);
            canvas.restore();//别忘了restore
        }

        // 绘制矩形
        if (Progress != null && Progress.length > 0) {

            for (int i = 0; i < Progress.length; i++) {// 循环遍历将7条柱状图形画出来

                int value = Progress[i];
                //Log.i("TAG", "value-->"+value);
                paint.setAntiAlias(true);// 抗锯齿效果
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(sp2px(8));// 字体大小
                paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
                Rect rect = new Rect();// 柱状图的形状
                rect.left =step * (i +1);
                rect.right = dp2px(15) + step * (i + 1);
                int rh = (int) (leftHeight - leftHeight * (value /max));
                rect.top = rh + dp2px(10);
                rect.bottom = height;

                canvas.drawBitmap(bitmap, null, rect, paint);

                // 是否显示柱状图上方的数字

                canvas.drawText(value + "%", dp2px(15) + step * (i + 1)
                        - dp2px(15), rh + dp2px(7), paint);


            }
        }


    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

    public void set() {
        // TODO Auto-generated method stub
        init();
    }



}