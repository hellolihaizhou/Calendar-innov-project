package punchclock.adapter;
import java.util.List;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import database.bean.punchclockBean;
import database.dao.record_punchDao;
import punchclock.activity.punchclockActivity;
import punchclock.service.PunchService;


/**已发任务适配器
 * @author asus
 *
 */
public class punchcolckAdapter  extends BaseAdapter implements OnClickListener
{
	private static final int[] bg={R.drawable.frist_finsh,R.drawable.second_finsh,R.drawable.thrid_finsh,R.drawable.four_finsh,R.drawable.five_finsh};
	private LayoutInflater mInflater;
	public Context mContext;
	public  List<punchclockBean> mDatas;
	private int pos=-1;
	private ViewHolder mHolder;
	private record_punchDao rPunchDao;
	public punchcolckAdapter(Context context, List<punchclockBean> Datas)
	{

		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = Datas;
		rPunchDao=new record_punchDao(context);
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
		this.pos=position;
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.punch_main_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.title);
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.ballcolor_icon);
			convertView.setTag(viewHolder);
			this.mHolder=viewHolder;
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtTitle.setText(mDatas.get(position).getTitle());

		if(mDatas.get(position).getState()==1)
		{
			viewHolder.imageView.setImageResource(bg[mDatas.get(position).getImageId()]);
		}
		else {
			viewHolder.imageView.setImageResource(R.drawable.unfinished);
		}

		viewHolder.imageView.setOnClickListener(this);
	/*	viewHolder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewHolder.imageView.setImageResource(bg[mDatas.get(position).getImageId()]);

			}
		});*/
		return convertView;
	}

	private final class ViewHolder{
		ImageView imageView;
		TextView txtTitle;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int state=mDatas.get(pos).getState();
		punchclockActivity.operate=1;
		if(state==0)
		{

			if( rPunchDao.add(mDatas.get(pos))>0)
			{

				mDatas.get(pos).setState(1);
				PunchService.mThread.remove( mDatas.get(pos).getPid());;
			}


		}
		else {
			if( rPunchDao.delete(mDatas.get(pos).getPid())>0)
			{
				mDatas.get(pos).setState(0);
			}

		}

		notifyDataSetChanged();
	}



}