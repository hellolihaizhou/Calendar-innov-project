package calendar.custom;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 因为android没有提供直接禁止Gridview滑动的API，
 * 也没有提供相应的属性来在XML布局文件中直接禁止滑动，
 * 当我们做菜单时要禁止Gridview上下滑动怎么办呢？ 
 * 1、自定义一个Gridview 
 * 2、通过重新dispatchTouchEvent方法来禁止滑动
 * 
 * @author chenwensen
 */
public class MyGridView extends GridView {


	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	/** 点击事件 */

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 设置不滚动
	 * 其中onMeasure函数决定了组件显示的高度与宽度；
	 * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
	 * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
	 * 之后在ScrollView中添加这个组件就OK了，同样的道理，ListView也适用。
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

	// 通过重新dispatchTouchEvent方法来禁止滑动
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		/*if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;// 禁止Gridview进行滑动
		}*/
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = event.getX();
		curP.y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = event.getX();
			downP.y = event.getY();
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰

			if (Math.abs(curP.y - downP.y) > Math.abs(curP.x - downP.x)) {

				getParent().requestDisallowInterceptTouchEvent(false);

			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		}

		/*// 当按下松开时
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.i("AA", "------action_up");
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if (Math.abs(downP.x - curP.x) < 10
					&& Math.abs(downP.y - curP.y) < 10) {
				Log.i("AA", "------单击");
				return true;
			}

			// if (downP.x == curP.x && downP.y == curP.y) {
			// Log.i("AA", "------单击");
			// onSingleTouch();r
			// return true;
			// }

		}*/
		return super.onTouchEvent(event);
	}
}
