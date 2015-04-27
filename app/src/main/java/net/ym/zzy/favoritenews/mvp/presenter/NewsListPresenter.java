package net.ym.zzy.favoritenews.mvp.presenter;

import net.ym.zzy.domain.entity.News;
import net.ym.zzy.domain.interactor.concept.NewsInteractor;
import net.ym.zzy.domain.interactor.implementation.NewsInteractorImpl;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.mvp.mapper.NewsModelMapper;
import net.ym.zzy.favoritenews.mvp.view.NewsListView;

import java.util.List;

/**
 * Created by zengzheying on 15/4/15.
 */
public class NewsListPresenter implements NewsInteractor.PullNewsListCallback {

    private final NewsInteractor mNewsInteractor;
    private final NewsListView mNewsListView;

    public NewsListPresenter(NewsListView newsListView) {
        mNewsListView = newsListView;
        mNewsInteractor = new NewsInteractorImpl(DataReposityImpl.getInstance());
    }

    public void loadData(int newsCatalog, int pageIndex, boolean isRefresh){
        mNewsListView.onLoadingData();
        mNewsInteractor.executePullNewsList(mNewsListView.getContext(), newsCatalog, pageIndex, isRefresh, this);
    }

    @Override
    public void onLoadDataSuccessfully(List<News> newsList) {
        mNewsListView.onLoadDataSuccessfully(NewsModelMapper.transform(newsList));
    }

    @Override
    public void onLoadDataError() {
        mNewsListView.onLoadDataError();
    }
}
