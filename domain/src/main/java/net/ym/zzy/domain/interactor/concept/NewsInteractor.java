package net.ym.zzy.domain.interactor.concept;

import android.content.Context;

import net.ym.zzy.domain.entity.News;
import net.ym.zzy.domain.entity.json.JsonBase;

import java.util.List;

/**
 * Created by zengzheying on 15/4/15.
 */
public interface NewsInteractor extends Interactor {
    interface PullNewsListCallback {
        void onLoadDataSuccessfully(List<News> newsList);
        void onLoadDataError();
        void onException(Exception ex);
    }

    interface PushCollectedNewsCallback{
        void onPushCollectedNewsSuccessfully();
        void onPushCollectedNewsError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    interface PullCollectedNewsListCallback{
        void onLoadedCollectedNewsListSuccessfully(List<News> newsList);
        void onLoadedCollectedNewsListError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    interface PushDislikeNewsCallback {
        void onPullDislikeNewsSuccessfully();
        void onPullDislikeNewsError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    void executePullNewsList(Context context, String uid, String token, int newsCatalog, int pageIndex, boolean isRefresh, PullNewsListCallback callback);

    void executePushCollectedNews(Context context, String uid, String token, int newsId, PushCollectedNewsCallback callback);

    void executePullCollectedNewsList(Context context, String uid, String token, int pageIndex, boolean isRefresh, PullCollectedNewsListCallback callback);

    void executePushDislikeNews(Context context, String uid, String token, int news_id, PushDislikeNewsCallback callback);
}
