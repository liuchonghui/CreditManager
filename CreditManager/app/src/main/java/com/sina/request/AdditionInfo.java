package com.sina.request;

import java.io.Serializable;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class AdditionInfo implements Serializable {

    private static final long serialVersionUID = -3095920742274351973L;

    public AdditionInfo() {
        super();
    }

    private String share;
    private String friendInfo;

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getFriendInfo() {
        return friendInfo;
    }

    public void setFriendInfo(String friendInfo) {
        this.friendInfo = friendInfo;
    }

}