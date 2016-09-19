package constellation;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;


public class GViewAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	public Context mContext;
	public  List<Bean> mDatas;
	public GViewAdapter(Context context, List<Bean> Datas)
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
			convertView = mInflater.inflate(R.layout.constellation_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.item_image = (ImageView)convertView.findViewById(R.id.constellation_item_image); 
			viewHolder.tv_text = (TextView)convertView.findViewById(R.id.constellation_item_text); 
			viewHolder.tv_date = (TextView)convertView.findViewById(R.id.constellation_item_date); 

			convertView.setTag(viewHolder);
			
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.item_image.setBackgroundResource(mDatas.get(position).getResid());
		viewHolder.tv_text.setText(mDatas.get(position).getText());
		viewHolder.tv_date.setText(mDatas.get(position).getDate());
		return convertView;
	}
 
	private final class ViewHolder{
		 private ImageView item_image;
		 private TextView tv_text;
		 private TextView tv_date;
	}

	
}