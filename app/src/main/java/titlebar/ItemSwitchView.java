package titlebar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lihaizhou.mycalendar.R;


public class ItemSwitchView extends RelativeLayout {

	protected TextView mtv_text;
	protected ToggleButton mSwitchView;
	public  ItemSwitchViewCallBack mBack=null;
	public int mId;
	public interface ItemSwitchViewCallBack
	{
		/**Switch 返回值
		 * @param id
		 * @param is
		 */
		public void SwitchOnclick(int id,boolean is);
	}
	public  void setItemSwitchViewCallBack(ItemSwitchViewCallBack back)
	{
		mBack=back;
	}
	public ItemSwitchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@SuppressLint({ "NewApi", "Recycle" })
	public ItemSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.item_swith, this);
		mtv_text = (TextView) view.findViewById(R.id.tv_text);
		mSwitchView=(ToggleButton)view.findViewById(R.id.view_switch);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.item);
		CharSequence text = a.getText(R.styleable.item_item_text);
		CharSequence value=a.getText(R.styleable.item_item_value);
		if(value!=null)mSwitchView.setChecked(true);
		if(text!=null) mtv_text.setText(text);
		mSwitchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mBack!=null)
				{

					mBack.SwitchOnclick(mId,getValue());
				}
			}
		});





	}
	public void mfindViewById(int id)
	{
		this.mId=id;
	}
	public void setValue(Boolean isOpened)
	{
		mSwitchView.setChecked(isOpened);
	}
	public boolean getValue()
	{
		return mSwitchView.isChecked();
	}

}
