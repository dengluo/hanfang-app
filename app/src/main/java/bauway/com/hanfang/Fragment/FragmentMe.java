package bauway.com.hanfang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.MyApplication;
import bauway.com.hanfang.R;
import bauway.com.hanfang.activity.AboutUsActivity;
import bauway.com.hanfang.activity.AgreementActivity;
import bauway.com.hanfang.activity.HelpDocsActivity;
import bauway.com.hanfang.activity.LoginActivity;
import bauway.com.hanfang.activity.LoginActivity2;
import bauway.com.hanfang.activity.PersonInfoActivity;
import bauway.com.hanfang.activity.RegisterActivity2;
import bauway.com.hanfang.activity.ValidateActivity;
import bauway.com.hanfang.interfaces.DialogCallback;
import bauway.com.hanfang.util.DateUtils;
import bauway.com.hanfang.util.DialogUtil;
import bauway.com.hanfang.util.MyUtil;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.PreferencesUtils;
import bauway.com.hanfang.util.ToastUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by shun8 on 2017/12/28.
 */

public class FragmentMe extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragmentMe";
    private Context context;
    private View view_main;
    private LinearLayout ll_fragme_exit;
    private Intent csintent, piintent;
    private LinearLayout ll_fragme_accountinfo, ll_fragme_help_docs, ll_fragme_about_us;
    private TextView tv_account_name, tv_account_nick, tv_version_num;
    public RxSharedPreferences userRxPreferences;
    public MyApplication myApplication;
    int num = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = this.getActivity();
        inintView();
        initDate();
        return view_main;
    }

    private void inintView() {
        view_main = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_me, null);
        ll_fragme_exit = (LinearLayout) view_main.findViewById(R.id.ll_fragme_exit);
        ll_fragme_accountinfo = (LinearLayout) view_main.findViewById(R.id.ll_fragme_accountinfo);
        ll_fragme_help_docs = (LinearLayout) view_main.findViewById(R.id.ll_fragme_help_docs);
        ll_fragme_about_us = (LinearLayout) view_main.findViewById(R.id.ll_fragme_about_us);
        tv_account_name = (TextView) view_main.findViewById(R.id.tv_account_name);
        tv_account_nick = (TextView) view_main.findViewById(R.id.tv_account_nick);
        tv_version_num = (TextView) view_main.findViewById(R.id.tv_version_num);
        ll_fragme_exit.setOnClickListener(this);
        ll_fragme_accountinfo.setOnClickListener(this);
        ll_fragme_help_docs.setOnClickListener(this);
        ll_fragme_about_us.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.setUserVisibleHint(hidden);
        if (hidden) {
            //可见时执行的操作
//            Log.e("isVisibleToUser11",hidden+"");
        } else {
            //不可见时执行的操作
//            Log.e("isVisibleToUser22",hidden+"");
            if (!NetworkUtil.isNetworkAvailable(context)) {
                ToastUtil.showShortToast(context, "网络连接异常!");
                return;
            }
        }
    }

    /*
        Bmob查询数据
         */
    public void queryData() {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            ToastUtil.showShortToast(context, "网络连接异常!");
            return;
        }
        String phone = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        BmobQuery query = new BmobQuery("_User");
        query.addWhereEqualTo("username", phone);
        query.setLimit(2);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + ary.toString());
                    try {
                        JSONObject object = (JSONObject) ary.get(0);
                        Log.i("createdAt", object.optString("createdAt"));
                        isVerify(object);
                        tv_account_nick.setText(object.optJSONArray("info").getString(0));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private Boolean isVerify(JSONObject object) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            DateUtils.daysBetween(DateUtils.ConverToDate(object.optString("createdAt")), DateUtils.ConverToDate(simpleDateFormat.format(date)));
            Log.i("createdAt2", "" + DateUtils.daysBetween(DateUtils.ConverToDate(object.optString("createdAt")), DateUtils.ConverToDate(simpleDateFormat.format(date))));
            Log.i("SMSBOOL", object.optBoolean("SMSBOOL") + "");
            num = DateUtils.daysBetween(DateUtils.ConverToDate(object.optString("createdAt")), DateUtils.ConverToDate(simpleDateFormat.format(date)));
            if (num > 30 && !object.optBoolean("SMSBOOL")) {
                DialogUtil.defaultDialog(context, getString(R.string.confirm_validate), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //验证
                                startActivity(new Intent(context, ValidateActivity.class));
                            }
                        });
                return true;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

    private void initDate() {
        String vsersion = MyUtil.getVersion(context);
        tv_version_num.setText("V" + vsersion);
        if (userRxPreferences == null) {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
        String accountname = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        String pwd = userRxPreferences.getString(Constants.LOGIN_PWD).get();
        Log.e(TAG, accountname + "//" + pwd);
        tv_account_name.setText(accountname);
        queryData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fragme_exit:
                jumpLogin();
                break;
            case R.id.ll_fragme_accountinfo:
                jumpPersonInfo();
                break;
            case R.id.ll_fragme_help_docs:
                startActivity(new Intent(context, HelpDocsActivity.class));
                break;
            case R.id.ll_fragme_about_us:
                startActivity(new Intent(context, AboutUsActivity.class));
                break;
            default:
                break;
        }
    }

    public void jumpLogin() {
        DialogUtil.defaultDialog(context, getString(R.string.confirm_log_out_app), null, null, new
                DialogCallback() {

                    @Override
                    public void execute(Object dialog, Object content) {
                        //确认退出
                        exitApp();
                    }
                });
    }

    private void exitApp() {
        BmobUser.logOut();
        PreferencesUtils.clearEntity(context);
        ToastUtils.cancel();
//        myApplication.exit();
        csintent = new Intent(context, LoginActivity2.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(csintent);
    }

    public void jumpPersonInfo() {
        piintent = new Intent(context, PersonInfoActivity.class);
        startActivity(piintent);
    }
}
