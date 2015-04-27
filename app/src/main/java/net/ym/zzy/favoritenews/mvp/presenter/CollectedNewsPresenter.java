package net.ym.zzy.favoritenews.mvp.presenter;

import net.ym.zzy.domain.entity.News;
import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.NewsInteractor;
import net.ym.zzy.domain.interactor.implementation.NewsInteractorImpl;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.mvp.mapper.NewsModelMapper;
import net.ym.zzy.favoritenews.mvp.view.CollectedNewsView;

import java.util.List;

/**
 * Created by zengzheying on 15/4/27.
 */
public class CollectedNewsPresenter implements NewsInteractor.PushCollectedNewsCallback, NewsInteractor.PullCollectedNewsListCallback{

    private final CollectedNewsView mCollectedNewsView;
    private final NewsInteractor mNewsInteractor;

    public CollectedNewsPresenter(CollectedNewsView collectedNewsView) {
        mCollectedNewsView = collectedNewsView;
        mNewsInteractor = new NewsInteractorImpl(DataReposityImpl.getInstance());
    }

    public void pullCollectedNewsList(String uid, String token, int pageIndex, boolean isRefresh){
        mNewsInteractor.executePullCollectedNewsList(mCollectedNewsView.getContext(), uid, token, pageIndex, isRefresh, this);
    }

    public void pushCollectedNews(String uid, String token, int newsId){
        mNewsInteractor.executePushCollectedNews(mCollectedNewsView.getContext(), uid, token, newsId, this);
    }

    @Override
    public void onLoadedCollectedNewsListSuccessfully(List<News> newsList) {
        mCollectedNewsView.onLoadDataSuccessfully(NewsModelMapper.transform(newsList));
    }

    @Override
    public void onLoadedCollectedNewsListError(JsonBase errorInfo) {
        mCollectedNewsView.onLoadDataError();
    }

    @Override
    public void onPushCollectedNewsSuccessfully() {
        mCollectedNewsView.onSendDataSuccessfully();
    }

    @Override
    public void onPushCollectedNewsError(JsonBase errorInfo) {
        mCollectedNewsView.onSendDataError();
    }

    @Override
    public void onException(Exception ex) {
        mCollectedNewsView.onPullNewsListException(ex);
        mCollectedNewsView.onPushNewsException(ex);
    }
}
