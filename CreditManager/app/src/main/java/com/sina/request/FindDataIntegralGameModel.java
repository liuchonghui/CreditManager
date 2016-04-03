package com.sina.request;

import com.sina.engine.base.db4o.Db4oInterface;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class FindDataIntegralGameModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    // integral_game
    private String id;
    private String name;// 小游戏名称
    private String pic;// 小游戏icon
    private String label;// 小游戏标签
    private String content;// 小游戏说明
    private String starttime;// 起始时间
    private String endtime;// 结束时间
    private String score;// 小游戏送出的积分

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
