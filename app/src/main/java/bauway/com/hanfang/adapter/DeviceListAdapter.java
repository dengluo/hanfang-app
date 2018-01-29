package bauway.com.hanfang.adapter;

import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bauway.com.hanfang.Fragment.FragmentOrderTake;
import bauway.com.hanfang.R;
import bauway.com.hanfang.activity.DeviceListActivity;
import bauway.com.hanfang.bean.ItemBean;

/**
 * Created by danny on 2018/1/27 .
 */
public class DeviceListAdapter extends BaseAdapter {

    private List<ItemBean> mData;
    private Context mContext;
    private ArrayList<String> str;

    public DeviceListAdapter(Context mContext, List<ItemBean> mData,ArrayList<String> str) {
        this.mContext = mContext;
        this.mData = mData;
        this.str = str;
    }

    @Override
    public int getCount() {
        if (str.equals(null)){
            return mData.size();
        }else {
            return str.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (str.equals(null)){
            return mData.get(position);
        }else {
            return str.get(position);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device_list, null);
            holder.devicename2 = (TextView) convertView.findViewById(R.id.tv_device_item_name);
            holder.delete = convertView.findViewById(R.id.delete_button);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (str.equals(null)){
            ItemBean itemObj = mData.get(position);
            holder.devicename2.setTag(itemObj.getText());
            holder.devicename2.setText(holder.devicename2.getTag().toString());
        }else {
            String str2 = str.get(position);
            holder.devicename2.setTag(str2);
            holder.devicename2.setText(holder.devicename2.getTag().toString());
        }

        holder.delete = convertView.findViewById(R.id.delete_button);
        holder.delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Message message = new Message();
                message.what = 29;
                message.arg1 = position;
                message.obj = str.get(position);
                DeviceListActivity.mHandler.sendMessage(message);
            }
        });

        holder.ivItem = (ImageView) convertView.findViewById(R.id.iv_device_list_item);
        holder.ivItem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Message message = new Message();
                message.what = 39;
                message.obj = str.get(position);
                DeviceListActivity.mHandler.sendMessage(message);
            }
        });


        return convertView;
    }

    private class ViewHolder {
        public TextView devicename2;
        public View delete;
        public ImageView ivItem;
        public LinearLayout llItem;

    }
}
