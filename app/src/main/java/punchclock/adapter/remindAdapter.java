package punchclock.adapter;


import java.util.List;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import punchclock.bean.remindBean;


/**已发任务适配器
 * @author asus
 *
 */
public class remindAdapter  extends BaseAdapter
{
	private LayoutInflater mInflater;
	public Context mContext;
	public  List<remindBean> mDatas;
	public remindAdapter(Context context, List<remindBean> Datas)
	{
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = Datas;

	}
	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	public void Updatas()
	{
		notifyDataSetChanged();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.remindlistview_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tv_txt = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.img_select = (ImageView)convertView.findViewById(R.id.img_select);
			viewHolder.rlyout_item=(RelativeLayout)convertView.findViewById(R.id.lyout_item);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_txt.setText(mDatas.get(position).text);
		boolean is=mDatas.get(position).selectstate;
		if(is)
		{
			int color_Selected=ContextCompat.getColor(mContext,R.color.rminditem_select);
			viewHolder.img_select.setBackgroundResource(R.drawable.cycle_check);
			viewHolder.rlyout_item.setBackgroundColor(color_Selected);
		}
		else {
			int color=ContextCompat.getColor(mContext,R.color.white);
			viewHolder.img_select.setBackgroundResource(0);
			viewHolder.rlyout_item.setBackgroundColor(color);
		}
		return convertView;
	}

	private final class ViewHolder{
		private TextView tv_txt;
		private ImageView img_select;
		private RelativeLayout rlyout_item;
	}



}