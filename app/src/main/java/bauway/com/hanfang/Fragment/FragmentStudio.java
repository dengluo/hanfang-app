package bauway.com.hanfang.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bauway.com.hanfang.R;

/**
 * Created by shun8 on 2017/12/28.
 */

public class FragmentStudio extends Fragment {

    private Context context;
    private View view_main;
    private WebView mywebview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        inintView();
        return view_main;
    }

    private void inintView() {
        view_main = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_buy, null);
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
        mywebview=(WebView)view_main.findViewById(R.id.mywebview);

        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mywebview.getSettings().setSupportMultipleWindows(true);
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.setWebChromeClient(new WebChromeClient());
        mywebview.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        mywebview.getSettings().setSupportZoom(true);
        mywebview.getSettings().setBuiltInZoomControls(true);
        mywebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mywebview.getSettings().setCacheMode(mywebview.getSettings().LOAD_CACHE_ELSE_NETWORK);
        mywebview.getSettings().setDomStorageEnabled(true);
        mywebview.getSettings().setDatabaseEnabled(true);
        mywebview.loadUrl("http://www.baidu.com");
    }
}
