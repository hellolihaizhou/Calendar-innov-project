package countdown.Adapter;

/**
 * Created by lihaizhou on 16-6-20.
 */
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import database.bean.CountBean;


public class CountListAdapter extends BaseAdapter {
    Context context;
    List<CountBean> rowItems;

    public CountListAdapter(Context context, List<CountBean> items) {
        this.context = context;
        this.rowItems = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.count_main_item, null);
            holder = new ViewHolder();
            holder.countTitle = (TextView) convertView.findViewById(R.id.count_title);
            holder.countDays = (TextView) convertView.findViewById(R.id.count_days);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        CountBean rowItem = (CountBean) getItem(position);
        holder.countTitle.setText(rowItem.getCountTitle());
        holder.countDays.setText(rowItem.getCountDays()+"");
        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView countTitle;
        TextView countDays;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

	public void Updatas() {
		// TODO Auto-generated method stub
		 notifyDataSetChanged();
	}
}
