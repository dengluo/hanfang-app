package bauway.com.hanfang.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by danny on 2018/1/31.
 */

public class Ia001 extends BmobObject {
    private String code;
    private Boolean Twice;
    private Boolean accredit;
    private String times;

    public Ia001() {
        this.setTableName("Ia001");
    }

    public String getTableName() {
        return "Ia001";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getTwice() {
        return Twice;
    }

    public void setTwice(Boolean twice) {
        this.Twice = twice;
    }

    public Boolean getAccredit() {
        return accredit;
    }

    public void setAccredit(Boolean accredit) {
        this.accredit = accredit;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
