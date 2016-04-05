package com.sina.request;

import com.sina.engine.base.db4o.Db4oInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchonghui on 16/4/6.
 */
public class HomeDataModel extends BaseModel {
    private static final long serialVersionUID = 1L;

//    private List<HomeRecommendSearchModel> recommendSearch = new ArrayList<HomeRecommendSearchModel>();
//    private List<HomeListFocusDataModel> focus = new ArrayList<HomeListFocusDataModel>();
//    private List<HomeRecommendScrollModel> recommendScroll = new ArrayList<HomeRecommendScrollModel>();
//    private List<HomeRecommendGridModel> recommendGrid = new ArrayList<HomeRecommendGridModel>();
    private List<HomeTopNewsModel> topNews = new ArrayList<HomeTopNewsModel>();

    // private HomeListThematicModel thematic;
    // private List<HomeListRecommendModel> recommend = new
    // ArrayList<HomeListRecommendModel>();

//    public List<HomeRecommendSearchModel> getRecommendSearch() {
//        return recommendSearch;
//    }
//
//    public void setRecommendSearch(
//            List<HomeRecommendSearchModel> recommendSearch) {
//        this.recommendSearch.clear();
//        this.recommendSearch.addAll(recommendSearch);
//    }
//
//    public List<HomeListFocusDataModel> getFocus() {
//        return focus;
//    }
//
//    public void setFocus(List<HomeListFocusDataModel> focus) {
//        this.focus.clear();
//        this.focus.addAll(focus);
//    }
//
//    public List<HomeRecommendScrollModel> getRecommendScroll() {
//        return recommendScroll;
//    }
//
//    public void setRecommendScroll(
//            List<HomeRecommendScrollModel> recommendScroll) {
//        this.recommendScroll.clear();
//        this.recommendScroll.addAll(recommendScroll);
//    }
//
//    public List<HomeRecommendGridModel> getRecommendGrid() {
//        return recommendGrid;
//    }
//
//    public void setRecommendGrid(List<HomeRecommendGridModel> recommendGrid) {
//        this.recommendGrid.clear();
//        this.recommendGrid.addAll(recommendGrid);
//    }

    public List<HomeTopNewsModel> getTopNews() {
        return topNews;
    }

    public void setTopNews(List<HomeTopNewsModel> topNews) {
        this.topNews.clear();
        this.topNews.addAll(topNews);
    }

//    @Override
//    public void objectUpdate(HomeDataModel object) {
//        if (object == null) {
//            return;
//        }
//        setRecommendSearch(object.getRecommendSearch());
//        setFocus(object.getFocus());
//        setRecommendScroll(object.getRecommendScroll());
//        setRecommendGrid(object.getRecommendGrid());
//        setTopNews(object.getTopNews());
//    }

}