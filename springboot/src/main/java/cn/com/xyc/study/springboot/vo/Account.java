package cn.com.xyc.study.springboot.vo;

public class Account {
    private String name;
    private String loginId;
    private String ratetype;

    public Account(String name, String loginId, String ratetype){
        this.name = name;
        this.loginId = loginId;
        this.ratetype = ratetype;
    }

    public String getName() {
        return name;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getRatetype() {
        return ratetype;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setRatetype(String ratetype) {
        this.ratetype = ratetype;
    }
}
