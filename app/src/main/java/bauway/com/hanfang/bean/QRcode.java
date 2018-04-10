package bauway.com.hanfang.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by danny on 2018/1/31.
 */

public class QRcode extends BmobObject {
    private String code;

//    public QRCode() {
//        this.setTableName("QRcode");
//    }
//
//    public String getTableName() {
//        return "QRcode";
//    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
