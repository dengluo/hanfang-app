package bauway.com.hanfang.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bauway.com.hanfang.R;
import bauway.com.hanfang.bean.Pickers;
import bauway.com.hanfang.View.PickerScrollView.onSelectListener;

public class WheelNumDialog extends Dialog {
	private Context context;
	private PickerScrollView pickerscrlllview;
	private Button btn_ok,btn_cancel;
	private int value;
	private OnDialogBackListener listener;
	private List<Pickers> list_data;
	private String selected;
	private TextView tv_dialog_title;
	public OnDialogBackListener getListener() {
		return listener;
	}

	public void setListener(OnDialogBackListener listener) {
		this.listener = listener;
	}
	public WheelNumDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public WheelNumDialog(Context context, int theme,int value) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.value=value;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_wheel_num);
		initView();
	}

	private void initView() {
		tv_dialog_title=(TextView)findViewById(R.id.tv_dialog_title);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		pickerscrlllview = (PickerScrollView) findViewById(R.id.wheel_speed);
		pickerscrlllview.setOnSelectListener(pickerListener);
		initData();
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.back(selected);
				WheelNumDialog.this.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WheelNumDialog.this.dismiss();
			}
		});
	}

	// 定义回调事件，用于dialog的点击事件
	public interface OnDialogBackListener {
		public void back(String text);
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		DecimalFormat   df   =new   DecimalFormat("#.0");  
		DecimalFormat   intdf   =new   DecimalFormat("#");  
		list_data = new ArrayList<Pickers>();
		if(value==0){
			tv_dialog_title.setText("您的身高");
			for (int i = 60; i < 251; i++) {
				list_data.add(new Pickers(i+"", i+""));
			}
			selected=list_data.get(95).getShowConetnt();
			// 设置数据，默认选择第一条
			pickerscrlllview.setData(list_data);
			pickerscrlllview.setSelected(95);
		}else if(value==1){
			tv_dialog_title.setText("您的孕前体重");
			for (double i = 30; i < 151; i+=0.1) {
				list_data.add(new Pickers(df.format(i), df.format(i)));
			}
			selected=list_data.get(273).getShowConetnt();
			// 设置数据，默认选择第一条
			pickerscrlllview.setData(list_data);
			pickerscrlllview.setSelected(273);
		}else if(value==2){
			tv_dialog_title.setText("您的当前体重");
			for (double i = 30; i < 151; i+=0.1) {
				list_data.add(new Pickers(df.format(i), df.format(i)));
			}
			selected=list_data.get(273).getShowConetnt();
			// 设置数据，默认选择第一条
			pickerscrlllview.setData(list_data);
			pickerscrlllview.setSelected(273);
		}else if(value==3){
			tv_dialog_title.setText("您的年龄(岁)");
			for (int i = 20; i < 70; i+=1) {
				list_data.add(new Pickers(intdf.format(i), intdf.format(i)));
			}
			selected=list_data.get(2).getShowConetnt();
			// 设置数据，默认选择第一条
			pickerscrlllview.setData(list_data);
			pickerscrlllview.setSelected(273);
		}
		
	}

	// 滚动选择器选中事件
	onSelectListener pickerListener = new onSelectListener() {

		@Override
		public void onSelect(Pickers pickers) {
			selected=pickers.getShowConetnt();
			System.out.println("选择：" + pickers.getShowId() + "--劳动："
					+ pickers.getShowConetnt());
		}
	};
}
