package com.sina.request;

import com.sina.engine.base.request.model.RequestModel;

/**
 * Created by liuchonghui on 16/4/6.
 */
public class HomeDataRequestModel extends RequestModel {
    private static final long serialVersionUID = 1L;
    private String action = "";
    private int page;//当前列表加载的页数（初始值=1的时候会获取全部数据 >1的时候只有头条新闻列表数据）
    private int count;//当前页请求个数
    private String max_id;//page >1时用到，当前列表最后一条数据的id(no)

    public HomeDataRequestModel(String domainName, String phpName) {
        super(domainName, phpName);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }
}

