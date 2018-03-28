package bauway.com.hanfang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.R;
import bauway.com.hanfang.bean.BUser;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.exception.BmobException;

import static com.wx.wheelview.util.WheelUtils.log;

public class PersonInfoActivity extends BaseActivity {

    private static final String TAG = "PersonInfoActivity";
    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.ll_fragme_accountinfo)
    LinearLayout mLlAccountinfo;
    @BindView(R.id.tv_personinfo_phone)
    TextView tv_personinfo_phone;
    @BindView(R.id.tv_personinfo_name)
    TextView tv_personinfo_name;
    @BindView(R.id.tv_personinfo_sex)
    TextView tv_personinfo_sex;
    @BindView(R.id.tv_personinfo_age)
    TextView tv_personinfo_age;
    @BindView(R.id.tv_personinfo_height)
    TextView tv_personinfo_height;
    @BindView(R.id.tv_personinfo_weight)
    TextView tv_personinfo_weight;
    @BindView(R.id.ll_fragme_validate)
    LinearLayout ll_fragme_validate;
    @BindView(R.id.tv_personinfo_dwmc)
    TextView tv_personinfo_dwmc;
    @BindView(R.id.tv_personinfo_frdb)
    TextView tv_personinfo_frdb;
    @BindView(R.id.tv_personinfo_fzr)
    TextView tv_personinfo_fzr;
    @BindView(R.id.tv_personinfo_zzh)
    TextView tv_personinfo_zzh;
    @BindView(R.id.tv_personinfo_address)
    TextView tv_personinfo_address;
    @BindView(R.id.tv_personinfo_edit)
    TextView tv_personinfo_edit;

    private Context ctx;
    private User mUser;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personinfo;
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
        mUser = getUserEntity();
        Log.e("getObjectId======",mUser.getObjectId());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        tv_personinfo_phone.setText(phone);
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
                            tv_personinfo_name.setText(object.optJSONArray("info").getString(0));
                            tv_personinfo_sex.setText(object.optJSONArray("info").getString(1));
                            tv_personinfo_age.setText(object.optJSONArray("info").getString(2));
                            tv_personinfo_height.setText(object.optJSONArray("info").getString(3));
                            tv_personinfo_weight.setText(object.optJSONArray("info").getString(4));
                            tv_personinfo_dwmc.setText(object.optJSONArray("info").getString(5));
                            tv_personinfo_frdb.setText(object.optJSONArray("info").getString(6));
                            tv_personinfo_fzr.setText(object.optJSONArray("info").getString(7));
                            tv_personinfo_zzh.setText(object.optJSONArray("info").getString(8));
                            tv_personinfo_address.setText(object.optJSONArray("info").getString(9));
                        }
                        if (object.getBoolean("SMSBOOL")){
                            ll_fragme_validate.setVisibility(View.GONE);
                        }else {
                            ll_fragme_validate.setVisibility(View.VISIBLE);
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
    protected void initView() {
        queryData();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ctx = PersonInfoActivity.this;
    }

    @OnClick({R.id.iv_return, R.id.ll_fragme_accountinfo,R.id.ll_fragme_validate,R.id.tv_personinfo_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.ll_fragme_accountinfo:
                Intent intent = new Intent(PersonInfoActivity.this, ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_fragme_validate:
                startActivity(new Intent(PersonInfoActivity.this, ValidateActivity.class));
                break;
            case R.id.tv_personinfo_edit:
                startActivity(new Intent(PersonInfoActivity.this, EditInfoActivity.class));
                break;
            default:
                break;
        }
    }

}
