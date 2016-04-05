package com.sina.request;

import java.util.Date;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class AccountInfo extends BaseModel {

    public AccountInfo() {
    }

    private String account;
    private String protocol;
    private String accessToken;

    private String guid;
    private String gtoken;
    private String deadline;
    private String name;
    private String headUrl;
    private String bgImg;
    private String birthday;
    private String sex;

    private String score;
    private long expiresin;
    private String integral;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGtoken() {
        return gtoken;
    }

    public void setGtoken(String gtoken) {
        this.gtoken = gtoken;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public long getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(long expiresin) {
        this.expiresin = expiresin;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}