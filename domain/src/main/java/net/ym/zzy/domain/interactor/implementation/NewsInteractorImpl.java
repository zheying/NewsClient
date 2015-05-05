package net.ym.zzy.domain.interactor.implementation;

import android.content.Context;

import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.entity.json.NewsJson;
import net.ym.zzy.domain.interactor.concept.NewsInteractor;
import net.ym.zzy.domain.respository.DataRepository;

import java.io.Serializable;

/**
 * Created by zengzheying on 15/4/15.
 */
public class NewsInteractorImpl implements NewsInteractor {

    private final DataRepository mDataRepository;

    public NewsInteractorImpl(DataRepository dataRepository) {
        mDataRepository = dataRepository;
    }

    @Override
    public void executePullNewsList(Context context, String uid, String token, int newsCatalog, int pageIndex, boolean isRefresh, final PullNewsListCallback callback) {
        mDataRepository.getNewsList(context, uid, token, newsCatalog, pageIndex, isRefresh, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null){
                    NewsJson newsJson = (NewsJson)ser;
                    if (newsJson != null && newsJson.getCode() == 0) {
                        callback.onLoadDataSuccessfully(newsJson.getData().getNewsList());
                    }
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null){
                    callback.onLoadDataError();
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onLoadDataError();
                }
            }
        });
    }

    @Override
    public void executePushCollectedNews(Context context, String uid, String token, int newsId, final PushCollectedNewsCallback callback) {
        mDataRepository.pushCollectedNews(context, uid, token, newsId, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null) {
                    callback.onPushCollectedNewsSuccessfully();
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null) {
                    callback.onPushCollectedNewsError((JsonBase) errInfo);
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onException(ex);
                }
            }
        });
    }

    @Override
    public void executePullCollectedNewsList(Context context, String uid, String token, int pageIndex, boolean isRefresh, final PullCollectedNewsListCallback callback) {
        mDataRepository.pullCollectedNewsList(context, uid, token, pageIndex, isRefresh, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null){
                    NewsJson newsJson = (NewsJson)ser;
                    if (newsJson != null && newsJson.getCode() == 0) {
                        callback.onLoadedCollectedNewsListSuccessfully(newsJson.getData().getNewsList());
                    }
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null){
                    callback.onLoadedCollectedNewsListError((JsonBase)errInfo);
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onException(ex);
                }
            }
        });
    }

    @Override
    public void executePushDislikeNews(Context context, String uid, String token, int news_id, final PushDislikeNewsCallback callback) {
        mDataRepository.pushDislikedNews(context, uid, token, news_id, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null){
                    JsonBase info = (JsonBase)ser;
                    if (info != null && info.getCode() == 0){
                        callback.onPullDislikeNewsSuccessfully();
                    }
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null){
                    callback.onPullDislikeNewsError((JsonBase)errInfo);
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onException(ex);
                }
            }
        });
    }
}
