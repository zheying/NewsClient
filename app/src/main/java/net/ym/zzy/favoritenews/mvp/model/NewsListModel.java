package net.ym.zzy.favoritenews.mvp.model;

import java.util.List;

/**
 * Created by zengzheying on 15/4/15.
 */
public class NewsListModel implements Model {
    private List<NewsModel> mNewsModelList;

    public List<NewsModel> getNewsModelList() {
        return mNewsModelList;
    }

    public void setNewsModelList(List<NewsModel> newsModelList) {
        mNewsModelList = newsModelList;
    }
}
