package bauway.com.hanfang.bean;

import java.lang.reflect.Array;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhaotaotao on 2017/8/10.
 */

public class User extends BmobUser {

    private String app_msg;
    private String app_name;
    private Boolean isPerson;
    private Boolean SMSBOOL;
    private String [] info;
    private Boolean blacklist;

    public String getApp_msg() {
        return app_msg;
    }

    public void setApp_msg(String app_msg) {
        this.app_msg = app_msg;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public Boolean getIsPerson() {
        return isPerson;
    }

    public void setIsPerson(Boolean isPerson) {
        this.isPerson = isPerson;
    }

    public String [] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public Boolean getSMSBOOL() {
        return SMSBOOL;
    }

    public void setSMSBOOL(Boolean SMSBOOL) {
        this.SMSBOOL = SMSBOOL;
    }

    public Boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Boolean blacklist) {
        this.blacklist = blacklist;
    }
}
