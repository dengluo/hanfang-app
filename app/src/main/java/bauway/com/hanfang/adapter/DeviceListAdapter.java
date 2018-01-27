package bauway.com.hanfang.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bauway.com.hanfang.R;
import bauway.com.hanfang.bean.ItemBean;

/**
 * Created by danny on 2018/1/27 .
 */
public class DeviceListAdapter extends BaseAdapter {

    private List<ItemBean> mData;
    private Context mContext;

    public DeviceListAdapter(Context mContext, List<ItemBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device_list, null);
            holder = new ViewHolder(convertView);
            holder.devicename = (TextView) convertView.findViewById(R.id.tv_device_item_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemBean itemObj = mData.get(position);
        holder.devicename.setTag(itemObj.getText());
        holder.devicename.setText(holder.devicename.getTag().toString());

        return convertView;
    }

    private class ViewHolder {
        private TextView devicename;

        public ViewHolder(View convertView) {
            devicename = (TextView) convertView.findViewById(R.id.tv_device_item_name);
        }
    }
}
