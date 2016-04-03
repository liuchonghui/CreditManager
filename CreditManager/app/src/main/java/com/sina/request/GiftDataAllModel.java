package com.sina.request;

import com.sina.engine.base.db4o.Db4oInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class GiftDataAllModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String result;
    private String message;
    private List<GiftDataModel> focus_list = new ArrayList<GiftDataModel>();
    private List<GiftDataModel> privilege_list = new ArrayList<GiftDataModel>();

    public GiftDataAllModel() {
        super();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GiftDataModel> getFocus_list() {
        return focus_list;
    }

    public void setFocus_list(List<GiftDataModel> focus_list) {
        this.focus_list.clear();
        this.focus_list.addAll(focus_list);
    }

    public List<GiftDataModel> getPrivilege_list() {
        return privilege_list;
    }

    public void setPrivilege_list(List<GiftDataModel> privilege_list) {
        this.privilege_list.clear();
        this.privilege_list.addAll(privilege_list);
    }

}
