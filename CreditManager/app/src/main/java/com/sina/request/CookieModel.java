package com.sina.request;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class CookieModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String name;
    private boolean httponly;
    private boolean secure;
    private String value;
    private String expires;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getHttponly() {
        return httponly;
    }
    public void setHttponly(boolean httponly) {
        this.httponly = httponly;
    }
    public boolean getSecure() {
        return secure;
    }
    public void setSecure(boolean secure) {
        this.secure = secure;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getExpires() {
        return expires;
    }
    public void setExpires(String expires) {
        this.expires = expires;
    }
}
