package event.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import java.util.List;

import database.bean.addressBean;
import database.bean.eventBean;
import event.activity.sortsActivity;
import utils.ToastUtils;


/**已发任务适配器
 * @author asus
 *
 */
public class eventAdapter  extends BaseAdapter
{
	private LayoutInflater mInflater;
	public Context mContext;
	public List<eventBean> mDatas;
	public int mposition;
	public eventAdapter(Context context, List<eventBean> Datas)
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		this.mposition=position;
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.event_main_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tv_title = (TextView)convertView.findViewById(R.id.event_title);
			viewHolder.tv_addr = (TextView)convertView.findViewById(R.id.event_addr);
			viewHolder.nv = (LinearLayout)convertView.findViewById(R.id.nv);
			viewHolder.img_sorts=(ImageView)convertView.findViewById(R.id.img_sorts);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_title.setText(mDatas.get(position).getTitle());
		viewHolder.img_sorts.setBackgroundResource(sortsActivity.resid[mDatas.get(position).getSorts()]);

		addressBean aBean=mDatas.get(position).getaddressBean();
		String addr=mDatas.get(position).getaddressBean().getAddress();
		if(addr.length()<1)
		{
			viewHolder.tv_addr.setText("暂无地址");
			viewHolder.nv.setVisibility(View.GONE);
		}
		else {
			viewHolder.nv.setVisibility(View.VISIBLE);
			viewHolder.tv_addr.setText(aBean.getAddress());

		}


		viewHolder.nv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Double Latitude = mDatas.get(mposition).getaddressBean().getLatitude();
					Double Longitude = mDatas.get(mposition).getaddressBean().getLocation();
					String address = mDatas.get(mposition).getaddressBean().getAddress();
					String addressUrl = "geo:"+Latitude+","+Longitude+"?q="+address;
					Uri mUri = Uri.parse(addressUrl);
					Intent intent = new Intent(Intent.ACTION_VIEW,mUri);
					ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
					if (resolveInfo == null) {
						ToastUtils.toastMsg(mContext,"没有安装地图",2);
					}
					else {
						mContext.startActivity(intent); //启动调用
					}
				} catch (Exception e) {
					Log.e("EventListAdapter", "异常发生,启动地图失败") ;
				}
			}
		});
		return convertView;
	}

	private final class ViewHolder{
		private TextView tv_title;
		private TextView tv_addr;
		private LinearLayout nv;
		private ImageView img_sorts;

	}





}