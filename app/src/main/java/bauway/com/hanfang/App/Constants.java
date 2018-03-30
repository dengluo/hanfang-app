package bauway.com.hanfang.App;

/**
 * Created by shun8 on 2017/10/23.
 */

public class Constants {
    public static final String LOGIN_EMAIL = "login_email"; //登录邮箱
    public static final String LOGIN_PWD = "login_pwd"; //登录密码
    public static final String SessionToken = "SessionToken"; //登录SessionToken
    public static  String LOGIN_PHONE = "login_phone"; //登录电话号码
    public static final String USER_INFO = "user_info";
    public static final int HTTP_TIME_OUT = 30; //网络请求超时时间
    public static final String LOG_TAG = "SMA-WATCH"; //log Tag
    //toast开关
    public static boolean toastFlag=true;//默认开启
    //bmob短信
    public static final String BMOB_ID="cb944948052b02a43fb1e2f7d905e8a3";
    /**
     * SharedPreferences属性信息文件
     */
    public static final String EXTRA_WEAC_SHARE = "extra_weac_shared_preferences_file";
    /**
     * 标题
     */
    public static final String TITLE = "title";

    /**
     * 详情
     */
    public static final String DETAIL = "detail";

    /**
     * 确定按钮文字
     */
    public static final String SURE_TEXT = "sure_text";
    /**
     * 访问本地相册类型:0，默认；1，扫码；2，造码
     */
    public static final String REQUEST_LOCAL_ALBUM_TYPE = "request_local_album_type";


    /**
     * 自定义壁纸存放地址
     */
    public static final String DIY_WALLPAPER_PATH = "/wallpaper/theme.jpg";
    /**
     * 保存的壁纸地址
     */
    public static final String WALLPAPER_PATH = "wallpaper_path";


    /**
     * 自定义二维码logo存放地址
     */
    public static final String DIY_QRCODE_LOGO_PATH = "/qrcode/logo.jpg";

    /**
     * 自定义二维码图片存放地址
     */
    public static final String QRCODE_PATH = "/WeaAlarmClock/picture/qrcode";
    /**
     * 保存的壁纸key
     */
    public static final String WALLPAPER_NAME = "wallpaper_name";
    /**
     * 保存的壁纸value
     */
    public static final String DEFAULT_WALLPAPER_NAME = "wallpaper_0";
    /**
     * 图片地址
     */
    public static final String IMAGE_URL = "image_url";

    /**
     * 保存的自定义二维码的logo path
     */
    public static final String QRCODE_LOGO_PATH = "qrcode_logo_path";

    /**
     * 保存的二维码前景色
     */
    public static final String FORE_COLOR = "foreground_color";

    /**
     * 保存的二维码背景色
     */
    public static final String BACK_COLOR = "background_color";


}
