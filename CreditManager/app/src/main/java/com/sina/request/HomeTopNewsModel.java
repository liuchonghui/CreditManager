package com.sina.request;

import com.sina.engine.base.db4o.Db4oInterface;

/**
 * Created by liuchonghui on 16/4/6.
 */
public class HomeTopNewsModel extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String absId;// 新闻id
    private String abstitle;// 新闻title
    private String summary;// 新闻摘要
    private String absImage;// 新闻缩略图
    private int newstype;// 资讯类型0:普通资讯/  1:视频资讯/  2:评测资讯
    private int comment_count;// 评论数量
    private String updateTime;// 资讯更新时间
    private String pinnedSectionName = null;
    private String score;

    public String getAbsId() {
        return absId;
    }

    public void setAbsId(String absId) {
        this.absId = absId;
    }

    public String getAbstitle() {
        return abstitle;
    }

    public void setAbstitle(String abstitle) {
        this.abstitle = abstitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAbsImage() {
        return absImage;
    }

    public void setAbsImage(String absImage) {
        this.absImage = absImage;
    }

    public int getNewstype() {
        return newstype;
    }

    public void setNewstype(int newstype) {
        this.newstype = newstype;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getPinnedSectionName() {
        return pinnedSectionName;
    }

    public void setPinnedSectionName(String pinnedSectionName) {
        this.pinnedSectionName = pinnedSectionName;
    }
    @Override
    public String toString() {
        return pinnedSectionName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    private String channelId;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    private int isRead;//是否阅读0未阅读 1已阅读

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

}