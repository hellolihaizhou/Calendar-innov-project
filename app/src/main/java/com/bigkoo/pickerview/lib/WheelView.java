package com.bigkoo.pickerview.lib;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;


import com.bigkoo.pickerview.adapter.WheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.lihaizhou.mycalendar.R;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 3d���ֿؼ�
 */
public class WheelView extends View {

    public enum ACTION {
        // ���������(������ͷ)����ק�¼�
        CLICK, FLING, DAGGLE
    }
    Context context;

    Handler handler;
    private GestureDetector gestureDetector;
    OnItemSelectedListener onItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    Paint paintOuterText;
    Paint paintCenterText;
    Paint paintIndicator;

    WheelAdapter adapter;

    private String label;//���ӵ�λ
    int textSize;//ѡ������ִ�С
    boolean customTextSize;//�Զ������ִ�С��Ϊtrue������ʹsetTextSize������Ч��ֻ��ͨ��xml�޸�
    int maxTextWidth;
    int maxTextHeight;
    float itemHeight;//ÿ�и߶�

    int textColorOut;
    int textColorCenter;
    int dividerColor;

    // ��Ŀ��౶��
    static final float lineSpacingMultiplier = 1.4F;
    boolean isLoop;

    // ��һ����Y����ֵ
    float firstLineY;
    //�ڶ�����Y����
    float secondLineY;
    //�м�Y����
    float centerY;

    //�����ܸ߶�yֵ
    int totalScrollY;
    //��ʼ��Ĭ��ѡ�еڼ���
    int initPosition;
    //ѡ�е�Item�ǵڼ���
    private int selectedItem;
    int preCurrentIndex;
    //����ƫ��ֵ,���ڼ�¼�����˶��ٸ�item
    int change;

    // ��ʾ������Ŀ
    int itemsVisible = 11;

    int measuredHeight;
    int measuredWidth;

    // ��Բ�ܳ�
    int halfCircumference;
    // �뾶
    int radius;

    private int mOffset = 0;
    private float previousY = 0;
    long startTime = 0;

    // �޸����ֵ���Ըı们���ٶ�
    private static final int VELOCITYFLING = 5;
    int widthMeasureSpec;

    private int mGravity = Gravity.CENTER;
    private int drawCenterContentStart = 0;//�м�ѡ�����ֿ�ʼ����λ��
    private int drawOutContentStart = 0;//���м����ֿ�ʼ����λ��
    private static final float SCALECONTENT = 0.8F;//���м��������ô˿��Ƹ߶ȣ�ѹ���γ�3d���
    private static final float CENTERCONTENTOFFSET = 6;//�м��������־�����Ҫ��ƫ��ֵ
    private static final String GETPICKERVIEWTEXT = "getPickerViewText";//����ķ�����

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textColorOut = getResources().getColor(R.color.pickerview_wheelview_textcolor_out);
        textColorCenter = getResources().getColor(R.color.pickerview_wheelview_textcolor_center);
        dividerColor = getResources().getColor(R.color.pickerview_wheelview_textcolor_divider);
        //���customTextSizeʹ�ã�customTextSizeΪtrue�Żᷢ��Ч��
        textSize = getResources().getDimensionPixelSize(R.dimen.pickerview_textsize);
        customTextSize = getResources().getBoolean(R.bool.pickerview_customTextSize);
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.wheelview,0,0);
            mGravity = a.getInt(R.styleable.wheelview_gravity, Gravity.CENTER);
            textColorOut = a.getColor(R.styleable.wheelview_textColorOut, textColorOut);
            textColorCenter = a.getColor(R.styleable.wheelview_textColorCenter,textColorCenter);
            dividerColor = a.getColor(R.styleable.wheelview_dividerColor,dividerColor);
            textSize = a.getDimensionPixelOffset(R.styleable.wheelview_textSize,textSize);
        }
        initLoopView(context);
    }

    private void initLoopView(Context context) {
        this.context = context;
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        gestureDetector.setIsLongpressEnabled(false);

        isLoop = true;

        totalScrollY = 0;
        initPosition = -1;

        initPaints();

    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(textColorOut);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(textColorCenter);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(1.1F);
        paintCenterText.setTypeface(Typeface.MONOSPACE);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint();
        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void remeasure() {
        if (adapter == null) {
            return;
        }

        measureTextWidthHeight();

        //���Text�ĸ߶ȳ˼�౶���õ� �ɼ�����ʵ�ʵ��ܸ߶ȣ���Բ���ܳ�
        halfCircumference = (int) (itemHeight * (itemsVisible - 1)) ;
        //����Բ���ܳ�����PI�õ�ֱ�������ֱ�������ؼ����ܸ߶�
        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        //����뾶
        radius = (int) (halfCircumference / Math.PI);
        //�ؼ���ȣ�����֧��weight
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        //�����������ߺͿؼ��м���Yλ��
        firstLineY = (measuredHeight - itemHeight) / 2.0F;
        secondLineY = (measuredHeight + itemHeight) / 2.0F;
        centerY = (measuredHeight + maxTextHeight) / 2.0F - CENTERCONTENTOFFSET;
        //��ʼ����ʾ��item��position�������Ƿ�loop
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (adapter.getItemsCount() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    /**
     * �������len��Text�Ŀ�߶�
     */
    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < adapter.getItemsCount(); i++) {
            String s1 = getContentText(adapter.getItem(i));
            paintCenterText.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect); // ����
            int textHeight = rect.height();
            if (textHeight > maxTextHeight) {
                maxTextHeight = textHeight;
            }
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action== ACTION.FLING||action== ACTION.DAGGLE) {
            mOffset = (int) ((totalScrollY%itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        //ֹͣ��ʱ��λ����ƫ�ƣ�����ȫ��������ȷֹͣ���м�λ�õģ����������λ��Ų���м�ȥ
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();

        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, VELOCITYFLING, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture!=null&&!mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * �����Ƿ�ѭ������
     * @param cyclic
     */
    public final void setCyclic(boolean cyclic) {
        isLoop = cyclic;
    }

    public final void setTextSize(float size) {
        if (size > 0.0F&&!customTextSize) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setCurrentItem(int currentItem) {
        this.initPosition = currentItem;
        totalScrollY = 0;//�ع鶥������Ȼ����setCurrentItem�Ļ�λ�û�ƫ�Ƶģ��ͻ���ʾ������λ�õ�����
        invalidate();
    }

    public final void setOnItemSelectedListener(OnItemSelectedListener OnItemSelectedListener) {
        this.onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        remeasure();
        invalidate();
    }

    public final WheelAdapter getAdapter(){
        return adapter;
    }

    public final int getCurrentItem() {
        return selectedItem;
    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (adapter == null) {
            return;
        }
        //�ɼ���item����
        Object visibles[] = new Object[itemsVisible];
        //������Yֵ�߶ȳ�ȥÿ��Item�ĸ߶ȣ��õ������˶��ٸ�item����change��
        change = (int) (totalScrollY / itemHeight);
        try {
            //������ʵ�ʵ�Ԥѡ�е�item(���������м�λ�õ�item) �� ����ǰ��λ�� �� �������λ��
            preCurrentIndex = initPosition + change % adapter.getItemsCount();
        }catch (ArithmeticException e){
            System.out.println("�����ˣ�adapter.getItemsCount() == 0���������ݲ�ƥ��");
        }
        if (!isLoop) {//��ѭ�������
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > adapter.getItemsCount() - 1) {
                preCurrentIndex = adapter.getItemsCount() - 1;
            }
        } else {//ѭ��
            if (preCurrentIndex < 0) {//�ٸ����ӣ����������5��preCurrentIndex �� ��1����ôpreCurrentIndex��ѭ����˵����ʵ��0�����棬Ҳ����4��λ��
                preCurrentIndex = adapter.getItemsCount() + preCurrentIndex;
            }
            if (preCurrentIndex > adapter.getItemsCount() - 1) {//ͬ������,�Լ��Բ�һ��
                preCurrentIndex = preCurrentIndex - adapter.getItemsCount();
            }
        }

        //�������������йأ��ܻ���������ÿ��item�߶�ȡ�࣬��������һ���Ĺ�����ÿ��item��һ��������ӦRect��ģ����item��Ӧ���ӵ�ƫ��ֵ
        int itemHeightOffset = (int) (totalScrollY % itemHeight);
        // ����������ÿ��Ԫ�ص�ֵ
        int counter = 0;
        while (counter < itemsVisible) {
            int index = preCurrentIndex - (itemsVisible / 2 - counter);//����ֵ������ǰ�ڿؼ��м��item��������Դ���м䣬��������Դ����Դ��indexֵ

            //�ж��Ƿ�ѭ���������ѭ������ԴҲʹ�����ѭ����position��ȡ��Ӧ��itemֵ���������ѭ���򳬳�����Դ��Χʹ��""�հ��ַ�����䣬�ڽ������γɿհ������ݵ�item��
            if (isLoop) {
                index = getLoopMappingIndex(index);
                visibles[counter] = adapter.getItem(index);
            } else if (index < 0) {
                visibles[counter] = "";
            } else if (index > adapter.getItemsCount() - 1) {
                visibles[counter] = "";
            } else {
                visibles[counter] = adapter.getItem(index);
            }

            counter++;

        }

        //�м���������
        canvas.drawLine(0.0F, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintIndicator);
        //��λ��Label
        if(label != null) {
            int drawRightContentStart = measuredWidth - getTextWidth(paintCenterText,label);
            //���Ҳ�������϶
            canvas.drawText(label, drawRightContentStart - CENTERCONTENTOFFSET, centerY, paintCenterText);
        }
        counter = 0;
        while (counter < itemsVisible) {
            canvas.save();
            // L(����)=�������ȣ�* r(�뾶) �������ƣ�
            // �󻡶�--> (L * �� ) / (�� * r)   (����X��/��Բ�ܳ�)
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * counter - itemHeightOffset) * Math.PI) / halfCircumference;
            // ����ת���ɽǶ�(�Ѱ�Բ��Y��Ϊ��������ת90�ȣ�ʹ�䴦�ڵ�һ���޼���������
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            // ��ʮ�����ϵĲ�����
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                String contentText = getContentText(visibles[counter]);

                //���㿪ʼ���Ƶ�λ��
                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                float translateY = (float) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                //����Math.sin(radian)������canvas����ϵԭ�㣬Ȼ�����Ż�����ʹ�����ָ߶Ƚ������ţ��γɻ���3d�Ӿ���
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // ��Ŀ������һ����
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // ��Ŀ�����ڶ�����
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // �м���Ŀ
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - CENTERCONTENTOFFSET, paintCenterText);
                    int preSelectedItem = adapter.indexOf(visibles[counter]);
                    if(preSelectedItem != -1){
                        selectedItem = preSelectedItem;
                    }
                } else {
                    // ������Ŀ
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALECONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                }
                canvas.restore();
            }
            counter++;
        }
    }

    //�ݹ�������Ӧ��index
    private int getLoopMappingIndex(int index){
        if(index < 0){
            index = index + adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        }
        else if (index > adapter.getItemsCount() - 1) {
            index = index - adapter.getItemsCount();
            index = getLoopMappingIndex(index);
        }
        return index;
    }

    /**
     * ���ݴ������Ķ������getPickerViewText()����������ȡ��Ҫ��ʾ��ֵ
     * @param item
     * @return
     */
    private String getContentText(Object item) {
        String contentText = item.toString();
        try {
            Class<?> clz = item.getClass();
            Method m = clz.getMethod(GETPICKERVIEWTEXT);
            contentText = m.invoke(item, new Object[0]).toString();
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        } catch (Exception e){
        }
        return contentText;
    }

    private void measuredCenterContentStart(String content) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity){
            case Gravity.CENTER:
                drawCenterContentStart = (int)((measuredWidth - rect.width()) * 0.5);
                break;
            case Gravity.LEFT:
                drawCenterContentStart = 0;
                break;
            case Gravity.RIGHT:
                drawCenterContentStart = measuredWidth - rect.width();
                break;
        }
    }
    private void measuredOutContentStart(String content) {
        Rect rect = new Rect();
        paintOuterText.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity){
            case Gravity.CENTER:
                drawOutContentStart = (int)((measuredWidth - rect.width()) * 0.5);
                break;
            case Gravity.LEFT:
                drawOutContentStart = 0;
                break;
            case Gravity.RIGHT:
                drawOutContentStart = measuredWidth - rect.width();
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();
                totalScrollY = (int) (totalScrollY + dy);

                // �߽紦��
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (adapter.getItemsCount() - 1 - initPosition) * itemHeight;
                    if(totalScrollY - itemHeight*0.3 < top){
                        top = totalScrollY - dy;
                    }
                    else if(totalScrollY + itemHeight*0.3 > bottom){
                        bottom = totalScrollY - dy;
                    }

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        // ������ק�¼�
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        // ������Ŀ����¼�
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }
        invalidate();

        return true;
    }

    /**
     * ��ȡItem����
     * @return
     */
    public int getItemsCount() {
        return adapter != null ? adapter.getItemsCount() : 0;
    }

    /**
     * �������ұߵĵ�λ�ַ���
     * @param label
     */
    public void setLabel(String label){
        this.label = label;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}
