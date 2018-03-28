package bauway.com.hanfang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.R;
import bauway.com.hanfang.View.WheelNumDialog;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.JsonBean;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.DialogUtil;
import bauway.com.hanfang.util.GetJsonDataUtil;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 编辑注册信息
 * danny
 */
public class EditInfoActivity extends BaseActivity implements WheelNumDialog.OnDialogBackListener{

    private static final String TAG = "EditInfoActivity";

    @BindView(R.id.et_perfectinfo_name)
    EditText et_perfectinfo_name;
    @BindView(R.id.et_perfectinfo_age)
    EditText et_perfectinfo_age;
    @BindView(R.id.tv_perfectinfo_height)
    TextView tv_perfectinfo_height;
    @BindView(R.id.tv_perfectinfo_weight)
    TextView tv_perfectinfo_weight;
    @BindView(R.id.radioGroup1)
    RadioGroup radioGroup1;
    @BindView(R.id.radio0)
    RadioButton radio0;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.et_perfectinfo_organization_name)
    EditText et_perfectinfo_organization_name;
    @BindView(R.id.et_perfectinfo_legal_representative)
    EditText et_perfectinfo_legal_representative;
    @BindView(R.id.et_perfectinfo_personinfo_head)
    EditText et_perfectinfo_personinfo_head;
    @BindView(R.id.et_perfectinfo_registration_mark)
    EditText et_perfectinfo_registration_mark;
    @BindView(R.id.tv_perfectinfo_address)
    TextView tv_perfectinfo_address;
    @BindView(R.id.bt_perfectinfo_register)
    Button bt_perfectinfo_register;
    @BindView(R.id.iv_return)
    ImageView iv_return;

    private Thread thread;
    private boolean isLoaded = false;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int MSG_LOAD_ADDRESS = 0x0004;
    private RadioButton radioButton;
    private String selectText = "男";
    private int currentButton = 0;
    private String currentTall = "155cm";
    private String currentWeight = "57.3kg";
    private Context ctx;

    private void showNumDialog(int value) {
        WheelNumDialog numDialog = new WheelNumDialog(this, R.style.dialog,
                value);
        numDialog.setListener(this);
        numDialog.setCanceledOnTouchOutside(true);
        numDialog.show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(PerfectInfoActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(PerfectInfoActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(EditInfoActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LOAD_ADDRESS:
                    tv_perfectinfo_address.setText(msg.obj.toString());
                    break;

            }
        }
    };

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                Message msg = new Message();
                msg.what = MSG_LOAD_ADDRESS;
                msg.obj = tx;//可以是基本类型，可以是对象，可以是List、map等；
                mHandler.sendMessage(msg);

//                Toast.makeText(PerfectInfoActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_editinfo;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        BmobSMS.initialize(this, Constants.BMOB_ID);
    }

    @Override
    protected void initView() {
        String emailHistory = userRxPreferences.getString(Constants.LOGIN_PHONE).get();
        tv_perfectinfo_height.setText(currentTall);
        tv_perfectinfo_weight.setText(currentWeight);
        radioGroup1
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioButton = (RadioButton) findViewById(radioGroup1
                                .getCheckedRadioButtonId());
                        selectText = radioButton.getText().toString();
                    }
                });
        queryData();
    }

    /*
        Bmob查询数据
         */
    public void queryData(){
        if (!NetworkUtil.isNetworkAvailable(this)){
            ToastUtil.showShortToast(ctx,"网络连接异常!");
            return;
        }
        String phone = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        BmobQuery query =new BmobQuery("_User");
        query.addWhereEqualTo("username", phone);
        query.setLimit(2);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if(e==null){
                    Log.i("bmob","查询成功："+ary.toString());
                    try {
                        JSONObject object = (JSONObject) ary.get(0);
                        if (object.has("info")){
                            et_perfectinfo_name.setText(object.optJSONArray("info").getString(0));
                            if (object.optJSONArray("info").getString(1).equals("男")){
                                radio0.setChecked(true);
                                radio0.isChecked();
                            }else {
                                radio1.setChecked(true);
                                radio1.isChecked();
                            }
//                            tv_personinfo_sex.setText(object.optJSONArray("info").getString(1));
                            et_perfectinfo_age.setText(object.optJSONArray("info").getString(2));
                            tv_perfectinfo_height.setText(object.optJSONArray("info").getString(3));
                            tv_perfectinfo_weight.setText(object.optJSONArray("info").getString(4));
                            et_perfectinfo_organization_name.setText(object.optJSONArray("info").getString(5));
                            et_perfectinfo_legal_representative.setText(object.optJSONArray("info").getString(6));
                            et_perfectinfo_personinfo_head.setText(object.optJSONArray("info").getString(7));
                            et_perfectinfo_registration_mark.setText(object.optJSONArray("info").getString(8));
                            tv_perfectinfo_address.setText(object.optJSONArray("info").getString(9));
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        ctx = EditInfoActivity.this;
    }


    @OnClick({R.id.iv_return, R.id.bt_perfectinfo_register, R.id.tv_perfectinfo_address,R.id.tv_perfectinfo_height,R.id.tv_perfectinfo_weight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_perfectinfo_height:
                currentButton = 0;
                showNumDialog(0);
                break;
            case R.id.tv_perfectinfo_weight:
                currentButton = 2;
                showNumDialog(2);
                break;
            case R.id.tv_perfectinfo_address:
                if (isLoaded) {
                    ShowPickerView();
                } else {
                    Toast.makeText(EditInfoActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.bt_perfectinfo_register:
                perfectinfoRegister();
                break;
            default:
                break;
        }
    }


    private void perfectinfoRegister() {
        String pname = et_perfectinfo_name.getText().toString().trim();
        String sex = selectText;
        String age = et_perfectinfo_age.getText().toString().trim();
        String height = tv_perfectinfo_height.getText().toString().trim();
        String weight = tv_perfectinfo_weight.getText().toString().trim();

        String name = et_perfectinfo_organization_name.getText().toString().trim();
        String representative = et_perfectinfo_legal_representative.getText().toString().trim();
        String head = et_perfectinfo_personinfo_head.getText().toString().trim();
        String mark = et_perfectinfo_registration_mark.getText().toString().trim();
        String address = tv_perfectinfo_address.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.plz_input_person_name);
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.plz_input_organization_name);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.plz_input_address);
            return;
        }
        String[] arr = {pname, sex, age, height, weight,name, representative, head, mark, address};

        DialogUtil.progressDialog(EditInfoActivity.this, getString(R.string.register_now), false);
        String objectId = BmobUser.getCurrentUser().getObjectId();
        final String phoneNumber = BmobUser.getCurrentUser().getMobilePhoneNumber();
        final User user = new User();
        user.setInfo(arr);
        user.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "更新成功" + phoneNumber);
                    userRxPreferences.getString(Constants.LOGIN_PHONE).set(phoneNumber);
                    finish();
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        DialogUtil.hide();//dismissDialog();
        super.onDestroy();
    }

    @Override
    public void back(String text) {
        if (currentButton == 0) {
            currentTall = text;
            tv_perfectinfo_height.setText(text + "cm");
            tv_perfectinfo_height.setTextColor(getResources().getColor(R.color.black));
        }
        if (currentButton == 2) {
            currentWeight = text;
            tv_perfectinfo_weight.setText(text + "kg");
            tv_perfectinfo_weight.setTextColor(getResources().getColor(
                    R.color.black));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }
}
