/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bauway.com.hanfang.zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.squareup.otto.Subscribe;

import com.bestmafen.easeblelib.util.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.Result;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.Fragment.FragmentOrderTake;
import bauway.com.hanfang.R;
import bauway.com.hanfang.activity.LocalAlbumActivity;
import bauway.com.hanfang.activity.MyDialogActivitySingle;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.Event.ScanCodeEvent;
import bauway.com.hanfang.bean.QRcode;
import bauway.com.hanfang.util.AudioPlayer;
import bauway.com.hanfang.util.MyUtil;
import bauway.com.hanfang.util.OttoAppConfig;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.zxing.camera.CameraManager;
import bauway.com.hanfang.zxing.decode.DecodeThread;
import bauway.com.hanfang.zxing.decode.DecodeUtils;
import bauway.com.hanfang.zxing.utils.BeepManager;
import bauway.com.hanfang.zxing.utils.CaptureActivityHandler;
import bauway.com.hanfang.zxing.utils.InactivityTimer;
import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import rx.Subscriber;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback,
        View.OnClickListener {

    private static final String LOG_TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    @BindView(R.id.capture_preview)
    SurfaceView scanPreview;
    @BindView(R.id.capture_container)
    LinearLayout scanContainer;
    @BindView(R.id.capture_crop_view)
    LinearLayout scanCropView;
    @BindView(R.id.capture_scan_line)
    ImageView scanLine;
    @BindView(R.id.et_input_yaopin_code)
    EditText inputCode;
    @BindView(R.id.bt_submit_yaopin_code)
    Button submitCode;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(2000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        initButton();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        OttoAppConfig.getInstance().register(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private FloatingActionButton mLightBtn;

    private void initButton() {
        ImageView backBtn = (ImageView) findViewById(R.id.action_back);
        TextView albumBtn = (TextView) findViewById(R.id.action_album);
        mLightBtn = (FloatingActionButton) findViewById(R.id.action_light);

        submitCode.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        albumBtn.setOnClickListener(this);
        mLightBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        offLight();
        OttoAppConfig.getInstance().unregister(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(LOG_TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();

        if (rawResult == null) {
            Snackbar.make(scanContainer, getString(R.string.decode_null), Snackbar.LENGTH_SHORT).show();
            return;
        }

//        beepManager.playBeepSoundAndVibrate();
        AudioPlayer.getInstance(this).playRaw(R.raw.scan, false, false);
//        MyUtil.vibrate(this);

        operateResult(rawResult);
    }

    private void operateResult(Result rawResult) {
        String codeType = rawResult.getBarcodeFormat().toString();
        String scanResult = rawResult.getText();
        L.e(LOG_TAG, "codeType: " + codeType);
        // 二维码
        if ("QR_CODE".equals(codeType) || "DATA_MATRIX".equals(codeType)) {
            boolean isUrl = MyUtil.checkWebSite(scanResult);
            // 不是标准网址
            if (!isUrl) {
                // 如果是没有添加协议的网址
                if (MyUtil.checkWebSitePath(scanResult)) {
                    scanResult = "http://" + scanResult;
                    isUrl = true;
                }
            }

            // 扫描结果为网址
            if (isUrl) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    Uri uri = Uri.parse(scanResult);
                    intent.setData(uri);
                    myFinish(intent);
                } catch (Exception e) {
                    L.e(LOG_TAG, "handleDecode: " + e.toString());
                    displayResult(scanResult, 0);
                }
            } else {
                displayResult(scanResult, 0);
            }
            // 条形码
        } else if ("EAN_13".equals(codeType)) {
            displayResult(scanResult, 1);
        } else if ("UPC_A".equals(codeType)) {
            displayResult(scanResult, 1);
        } else if ("UPC_E".equals(codeType)) {
            displayResult(scanResult, 1);
        } else if ("EAN_8".equals(codeType)) {
            displayResult(scanResult, 1);
        } else if ("RSS_14".equals(codeType)) {
            displayResult(scanResult, 1);
        } else if ("CODE_128".equals(codeType)) {
            displayResult(scanResult, 1);
        } else {
            Snackbar.make(scanContainer, getString(R.string.decode_null), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static final String SCAN_RESULT = "scan_result";
    public static final String SCAN_TYPE = "scan_type";

    /**
     * 显示扫描结果
     *
     * @param scanResult 扫描内容
     * @param type       扫描类型：0，二维码；1，条形码
     */
    private void displayResult(final String scanResult, int type) {
        L.e("scanResult____" + scanResult);
//        if (scanResult.length() < 5) {
//            ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.product_code_short));
//            return;
//        }
        BmobQuery<QRcode> bmobQuery = new BmobQuery<QRcode>();
        bmobQuery.addWhereEqualTo("code", scanResult);
        bmobQuery.setLimit(1);
        bmobQuery.order("createdAt");
        bmobQuery.findObjectsObservable(QRcode.class)
                .subscribe(new Subscriber<List<QRcode>>() {
                    @Override
                    public void onCompleted() {
//                        Log.e("scanResult===", "scanResult：" + scanResult);
//                        Log.e("substring===", "substring：" + scanResult.substring(0, 4));
                        Message message = new Message();
                        if (getIntent().getStringExtra("shebei").equals("device1")) {
                            if (scanResult.substring(0, 2).equals("69")) {
                                Log.e("2===", "scanResult：" + 23);
                                SelectData1(scanResult);
                                message.what = 101;
                            } else if (scanResult.equals("1H")) {
                                message.what = 111;
                            } else if (scanResult.equals("3MIN")) {
                                message.what = 111;
                            } else if (scanResult.equals("45H")) {
                                Log.e("2===", "scanResult：" + 23);
//                                SelectData1(scanResult);
                                message.what = 111;
                            } else if (scanResult.equals("90H")) {
//                                SelectData2(scanResult);
                                message.what = 112;
                            } else if (scanResult.equals("180H")) {
//                                SelectData2(scanResult);
                                message.what = 113;
                            } else {
                                SelectData1(scanResult);
                                message.what = 102;
                            }

                        } else {
                            SelectDataDeviceSN(scanResult);
                            message.what = 18;
                        }
                        message.obj = scanResult;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("bmob", "失败：" + e.getMessage() + ",");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.product_code_error));
                    }

                    @Override
                    public void onNext(List<QRcode> persons) {
                        Log.e("查询成功：共", persons.size() + "条数据。");
                    }
                });

//        SharedPreferences sp = this.getSharedPreferences("SCAN",
//                Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        String str = getIntent().getStringExtra("shebei");
//        if (str.equals("device1")) {
//            editor.putString("scanResult1", scanResult);
//        } else if (str.equals("device2")) {
//            editor.putString("scanResult2", scanResult);
//        } else if (str.equals("device3")) {
//            editor.putString("scanResult3", scanResult);
//        } else if (str.equals("device4")) {
//            editor.putString("scanResult4", scanResult);
//        } else if (str.equals("device5")) {
//            editor.putString("scanResult5", scanResult);
//        }
//        editor.commit();
        finish();
    }

    //手动输入产品码,验证code
    private void SelectData(String code) {
        final String bql = "select * from QRcode where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    Log.i("count", "" + count);
                    Log.i("list", "" + list.size());
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询QRcode表中的code
    private void SelectData1(String code) {
        final String bql = "select * from QRcode where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    Log.i("count", "" + count);
                    Log.i("list", "" + list.size());
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询V0001表中的code
    private void SelectData2(String code) {
        final String bql = "select * from V0001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询IIa01表中的code
    private void SelectData3(String code) {
        final String bql = "select * from IIa01 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询Iva01表中的code
    private void SelectData4(String code) {
        final String bql = "select * from Iva01 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询Ivb01表中的code
    private void SelectData5(String code) {
        final String bql = "select * from Ivb01 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询IIb01表中的code
    private void SelectData6(String code) {
        final String bql = "select * from IIb01 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询B0001表中的code
    private void SelectData7(String code) {
        final String bql = "select * from B0001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询T0001表中的code
    private void SelectData8(String code) {
        final String bql = "select * from T0001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询A0001表中的code
    private void SelectData9(String code) {
        final String bql = "select * from A0001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询VI001表中的code
    private void SelectData10(String code) {
        final String bql = "select * from VI001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询Ib001表中的code
    private void SelectData11(String code) {
        final String bql = "select * from Ib001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询Ia001表中的code
    private void SelectData12(String code) {
        final String bql = "select * from Ia001 where code = '" + code + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.authorized_code_error));
                        Message message = new Message();
                        message.what = 16;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    //查询Device_SN表中的code
    private void SelectDataDeviceSN(String code) {
        String str = "";
        if (code.length() > 12) {
            str = code.substring(code.length() - 12, code.length());
        } else {
            str = code;
        }
        final String bql = "select * from Device_SN where SN = '" + str + "'";
        new BmobQuery<QRcode>().doSQLQuery(bql, new SQLQueryListener<QRcode>() {

            @Override
            public void done(BmobQueryResult<QRcode> result, BmobException e) {
                if (e == null) {
                    int count = result.getCount();//这里得到符合条件的记录数
                    List<QRcode> list = (List<QRcode>) result.getResults();
                    Log.i("list.size", list.size() + "");
                    if (list.size() > 0) {

                    } else {
                        Log.i("smile", "查询成功，无数据");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.device_code_error));
                        Message message = new Message();
                        message.what = 17;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    private void myFinish(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, 0);
        finish();
        overridePendingTransition(0, 0);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(LOG_TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(LOG_TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(LOG_TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private static final int REQUEST_MY_DIALOG = 1;

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        Intent intent = new Intent(this, MyDialogActivitySingle.class);
        intent.putExtra(Constants.TITLE, getString(R.string.prompt));
        intent.putExtra(Constants.DETAIL, getString(R.string.camera_error));
        startActivityForResult(intent, REQUEST_MY_DIALOG);

/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_MY_DIALOG) {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void initCrop() {
        // http://skillcollege.github.io/2015/02/05/-打造极致二维码扫描系列-基于ZBar的Android平台解码/

        // 预览图的高度，也即camera的分辨率宽高
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        // 布局文件中扫描框的左上角定点坐标
        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        // 布局文件中扫描框的宽高
        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean mIsLightOpen;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交产品码
            case R.id.bt_submit_yaopin_code:
                String code = inputCode.getText().toString();
                validateCode(code);//验证产品码
                break;
            // 返回按钮
            case R.id.action_back:
                finish();
                break;
            // 开关灯按钮
            case R.id.action_light:
                operateLight();
                break;
            // 相册按钮
            case R.id.action_album:
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }

                offLight();

                Intent intent = new Intent(this, LocalAlbumActivity.class);
                intent.putExtra(Constants.REQUEST_LOCAL_ALBUM_TYPE, 1);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, 0);
                break;
        }
    }

    private void validateCode(final String code) {
        if (inputCode.getText().toString().trim().equals("")) {
            ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.product_code_null));
            return;
        }
//        if (inputCode.getText().toString().length() < 5) {
//            ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.product_code_short));
//            return;
//        }
        L.e("code____" + code);
        BmobQuery<QRcode> bmobQuery = new BmobQuery<QRcode>();
        bmobQuery.addWhereEqualTo("code", code);
        bmobQuery.setLimit(1);
        bmobQuery.order("createdAt");
        bmobQuery.findObjectsObservable(QRcode.class)
                .subscribe(new Subscriber<List<QRcode>>() {
                    @Override
                    public void onCompleted() {
//                        Log.e("scanResult===", "scanResult：" + code);
//                        Log.e("substring===", "substring：" + code.substring(0, 4));
                        Message message = new Message();
                        if (getIntent().getStringExtra("shebei").equals("device1")) {
                            if (code.equals("45H")) {
//                                SelectData13(code);
                                message.what = 112;
                            } else if (code.equals("3MIN")) {
//                                SelectData14(code);
                                message.what = 112;
                            } else if (code.equals("90H")) {
//                                SelectData14(code);
                                message.what = 112;
                            } else if (code.equals("180H")) {
//                                SelectData15(code);
                                message.what = 112;
                            } else if (code.equals("1H")) {
                                message.what = 112;
                            }else {
//                                SelectData1(code);
                                message.what = 112;
                            }

                        } else {
                            SelectDataDeviceSN(code);
                            message.what = 18;
                        }
                        message.obj = code;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("bmob", "失败：" + e.getMessage() + ",");
                        ToastUtil.showShortToast(CaptureActivity.this, getString(R.string.product_code_error));
                    }

                    @Override
                    public void onNext(List<QRcode> persons) {
                        Log.e("查询成功：共", persons.size() + "条数据。");
                    }
                });
        finish();
    }

    /**
     * 开关灯
     */
    private void operateLight() {
        if (!mIsLightOpen) {
            cameraManager.openLight();
            mIsLightOpen = true;
            mLightBtn.setImageResource(R.drawable.light_pressed);
        } else {
            cameraManager.offLight();
            mIsLightOpen = false;
            mLightBtn.setImageResource(R.drawable.light_normal);
        }
    }

    /**
     * 关灯
     */
    private void offLight() {
        if (mIsLightOpen) {
            cameraManager.offLight();
            mIsLightOpen = false;
            mLightBtn.setImageResource(R.drawable.light_normal);
        }
    }

    private ViewGroup progressBarLlyt;

    @Subscribe
    public void scanQRcodeEvent(ScanCodeEvent event) {
        progressBarLlyt = (ViewGroup) findViewById(R.id.progress_bar_llyt);
        progressBarLlyt.setVisibility(View.VISIBLE);

        String imagePath = event.getImageUrl();
        if (!TextUtils.isEmpty(imagePath)) {
            int myWidth = getResources().getDisplayMetrics().widthPixels;
            int myHeight = getResources().getDisplayMetrics().heightPixels;

            Glide.with(getApplicationContext()).load("file://" + imagePath).asBitmap().into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    progressBarLlyt.setVisibility(View.GONE);

                    Result resultZxing = new DecodeUtils(DecodeUtils.DECODE_DATA_MODE_ALL)
                            .decodeWithZxing(resource);
                    handleDecode(resultZxing, null);
                }
            });
        }
    }
}
