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
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.activity.LoginActivity2;
import bauway.com.hanfang.activity.PersonInfoActivity;
import bauway.com.hanfang.R;

/**
 * Created by shun8 on 2017/12/28.
 */

public class FragmentMe extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragmentMe";
    private Context context;
    private View view_main;
    private LinearLayout ll_fragme_exit;
    private Intent csintent,piintent;
    private LinearLayout ll_fragme_accountinfo;
    private TextView tv_account_name;
    public RxSharedPreferences userRxPreferences;
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
        tv_account_name = (TextView) view_main.findViewById(R.id.tv_account_name);
        ll_fragme_exit.setOnClickListener(this);
        ll_fragme_accountinfo.setOnClickListener(this);
    }

    private void initDate() {
        if (userRxPreferences == null) {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
        String accountname = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        String pwd = userRxPreferences.getString(Constants.LOGIN_PWD).get();
        Log.e(TAG, accountname+"//"+pwd);
        tv_account_name.setText(accountname);
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
            default:
                break;
        }
    }

    public void jumpLogin() {
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
