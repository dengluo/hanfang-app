package bauway.com.hanfang.bean;

import java.sql.Array;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by danny on 2018/1/25.
 */

public class BUser extends BmobObject {

    private String objectId;
    private String username;
    private String password;
    private Array info;

    public BUser() {
        this.setTableName("_User");
    }

    public Array getInfo() {
        return info;
    }

    public void setInfo(Array info) {
        this.info = info;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
