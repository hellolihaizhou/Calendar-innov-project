
package com.bigkoo.pickerview.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;

import com.bigkoo.pickerview.listener.OnDismissListener;
import com.lihaizhou.mycalendar.R;

/**
 * Created by Sai on 15/11/22.
 * ����iOSPickerViewController�ؼ�
 */
public class BasePickerView {
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );

    private Context context;
    protected ViewGroup contentContainer;
    private ViewGroup decorView;//activity�ĸ�View
    private ViewGroup rootView;//����View �� ��View

    private OnDismissListener onDismissListener;
    private boolean isDismissing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.BOTTOM;

    public BasePickerView(Context context){
        this.context = context;

        initViews();
        init();
        initEvents();
    }

    protected void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.setLayoutParams(params);
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    protected void initEvents() {
    }
    /**
     * show��ʱ�����
     *
     * @param view ���View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }
    /**
     * ������View��Activity�ĸ���ͼ
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }
    /**
     * ����View�ǲ����Ѿ���ӵ�����ͼ
     *
     * @return �����ͼ�Ѿ����ڸ�View����true
     */
    public boolean isShowing() {
        View view = decorView.findViewById(R.id.outmost_container);
        return view != null;
    }
    public void dismiss() {
        if (isDismissing) {
            return;
        }

        //��ʧ����
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        //��activity����ͼ�Ƴ�
                        decorView.removeView(rootView);
                        isDismissing = false;
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss(BasePickerView.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contentContainer.startAnimation(outAnim);
        isDismissing = true;
    }
    public Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public BasePickerView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        }
        else{
            view.setOnTouchListener(null);
        }
        return this;
    }
    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id){
        return contentContainer.findViewById(id);
    }
}
