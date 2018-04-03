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

import java.util.Arrays;
import java.util.List;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.R;
import bauway.com.hanfang.bean.BUser;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.exception.BmobException;
import rx.Subscriber;

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
        mUser = getUserEntity();
        Log.e("getObjectId======", mUser.getObjectId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }

    /*
        Bmob查询数据
         */
    public void queryData() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtil.showShortToast(ctx, "网络连接异常!");
            return;
        }
        String phone = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        tv_personinfo_phone.setText(phone);
        queryPersonInfo(phone);
    }

    public void queryPersonInfo(String phone) {
        final BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username", phone);
        bmobQuery.setLimit(2);
        bmobQuery.order("createdAt");
        //先判断是否有缓存
//        boolean isCache = bmobQuery.hasCachedResult(User.class);
//        if (isCache) {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
//        } else {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
//        }
//		observable形式
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (User user : list) {
                        String str = Arrays.asList(user.getInfo()).get(0);
                        tv_personinfo_name.setText(Arrays.asList(user.getInfo()).get(0));
                        tv_personinfo_sex.setText(Arrays.asList(user.getInfo()).get(1));
                        tv_personinfo_age.setText(Arrays.asList(user.getInfo()).get(2));
                        tv_personinfo_height.setText(Arrays.asList(user.getInfo()).get(3));
                        tv_personinfo_weight.setText(Arrays.asList(user.getInfo()).get(4));
                        Log.e("dwmc===",Arrays.asList(user.getInfo()).get(5));
                        tv_personinfo_dwmc.setText(Arrays.asList(user.getInfo()).get(5));
                        tv_personinfo_frdb.setText(Arrays.asList(user.getInfo()).get(6));
                        tv_personinfo_fzr.setText(Arrays.asList(user.getInfo()).get(7));
                        tv_personinfo_zzh.setText(Arrays.asList(user.getInfo()).get(8));
                        tv_personinfo_address.setText(Arrays.asList(user.getInfo()).get(9));
                        if (user.getSMSBOOL()) {
                            ll_fragme_validate.setVisibility(View.GONE);
                        } else {
                            ll_fragme_validate.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
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

    @OnClick({R.id.iv_return, R.id.ll_fragme_accountinfo, R.id.ll_fragme_validate, R.id.tv_personinfo_edit})
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
