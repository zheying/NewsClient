package net.ym.zzy.favoritenews.mvp.presenter;

import net.ym.zzy.domain.entity.News;
import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.NewsInteractor;
import net.ym.zzy.domain.interactor.implementation.NewsInteractorImpl;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.mvp.mapper.NewsModelMapper;
import net.ym.zzy.favoritenews.mvp.view.NewsListView;

import java.util.List;

/**
 * Created by zengzheying on 15/4/15.
 */
public class NewsListPresenter implements NewsInteractor.PullNewsListCallback, NewsInteractor.PushDislikeNewsCallback {

    private final NewsInteractor mNewsInteractor;
    private final NewsListView mNewsListView;

    public NewsListPresenter(NewsListView newsListView) {
        mNewsListView = newsListView;
        mNewsInteractor = new NewsInteractorImpl(DataReposityImpl.getInstance());
    }

    public void loadData(String uid, String token, int newsCatalog, int pageIndex, boolean isRefresh){
        mNewsListView.onLoadingData();
        mNewsInteractor.executePullNewsList(mNewsListView.getContext(), uid, token, newsCatalog, pageIndex, isRefresh, this);
    }

    public void dislikeNew(String uid, String token, int news_id){
        mNewsInteractor.executePushDislikeNews(mNewsListView.getContext(), uid, token, news_id, this);
    }

    @Override
    public void onLoadDataSuccessfully(List<News> newsList) {
        mNewsListView.onLoadDataSuccessfully(NewsModelMapper.transform(newsList));
    }

    @Override
    public void onLoadDataError() {
        mNewsListView.onLoadDataError();
    }

    @Override
    public void onPullDislikeNewsSuccessfully() {

    }

    @Override
    public void onPullDislikeNewsError(JsonBase errorInfo) {

    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace(System.err);
        mNewsListView.onLoadDataError();
    }
}
