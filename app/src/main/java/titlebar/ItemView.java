package titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;


public class ItemView  extends RelativeLayout {

	protected TextView mtv_text,mtv_value;
	private  itemViewCallBack mBack=null;
	private ImageView imgview_arrows;
	public int mId;
	public interface itemViewCallBack
	{
		public void valueOnclick(int id);
	}
	public  void setitemViewCallBack(itemViewCallBack back)
	{
		mBack=back;
	}
	public ItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private Context mContext;

	@SuppressLint({ "NewApi", "Recycle" })
	public ItemView(Context context, AttributeSet attrs) {

		super(context, attrs);
		this.mContext=context;
		View view = View.inflate(context, R.layout.item_view, this);
		mtv_text = (TextView) view.findViewById(R.id.tv_text);
		mtv_value=(TextView)view.findViewById(R.id.tv_value);
		imgview_arrows=(ImageView)view.findViewById(R.id.imgview_arrows);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.item);
		CharSequence text = a.getText(R.styleable.item_item_text);
		boolean arrows_visibility=a.getBoolean(R.styleable.item_arrows_visibility,false);
		if(arrows_visibility)
		{
			imgview_arrows.setVisibility(View.VISIBLE);
		}
		if(text!=null) mtv_text.setText(text);
		CharSequence value=a.getText(R.styleable.item_item_value);
		if(value!=null) mtv_value.setText(value);



		mtv_value.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mBack!=null)
				{
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mtv_value.getWindowToken(), 0);
					mBack.valueOnclick(mId);
				}

			}
		});


	}
	public void mfindViewById(int id)
	{
		this.mId=id;
	}
	public void setValue(String value)
	{
		mtv_value.setText(value);
	}
	public String getValue()
	{
		return mtv_value.getText().toString();
	}

}
