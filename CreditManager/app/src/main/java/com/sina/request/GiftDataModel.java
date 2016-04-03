package com.sina.request;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class GiftDataModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    public static final int IS_FOCUS = 1;
    public static final int IS_PRIVILEGE = 2;

    public GiftDataModel() {
        super();
    }

    private int columType;

    public int getColumType() {
        return columType;
    }

    public void setColumType(int columType) {
        this.columType = columType;
    }

    // common
    private String giftId;
    private String thumbnailUrl;

    // privilege only
    private String hot;
    private String name;
    private String content;
    private int remainCardNum;
    private int totalCardNum;
    private String left;
    private String updateTime;
    private String updatetype;
    private String attention;
    private String type;
    private String style;
    private String screen;
    private int label;
    private String gameName;
    private int state;
    private String shareUrl;
    private String giftName;



    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRemainCardNum() {
        return remainCardNum;
    }

    public void setRemainCardNum(int remainCardNum) {
        this.remainCardNum = remainCardNum;
    }

    public int getTotalCardNum() {
        return totalCardNum;
    }

    public void setTotalCardNum(int totalCardNum) {
        this.totalCardNum = totalCardNum;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(String updatetype) {
        this.updatetype = updatetype;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    boolean simpleType = false;

    public void setSimpleType(boolean simpleType) {
        this.simpleType = simpleType;
    }

    public void setSimpleType() {
        simpleType = true;
    }

    public boolean isSimpleType() {
        return this.simpleType;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

}