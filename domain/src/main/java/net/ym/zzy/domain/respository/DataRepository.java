package net.ym.zzy.domain.respository;

import android.content.Context;

import net.ym.zzy.domain.entity.NewsList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengzheying on 15/3/28.
 */
public interface DataRepository {

    public static final int PAGE_SIZE = 20;

    public interface ResponseCallback{
        void onResponse(Serializable ser);
        void onResponseError(Serializable errInfo);
        void onException(Exception ex);
    }

    public void getNewsList(Context context, String uid, String token, int catalog, int pageIndex, boolean isRefresh, ResponseCallback callback);

    public void login(Context context, String uid, String name, String token, String avatar, ResponseCallback callback);

    public void commentNews(Context context, String uid, String token, int newsId, String content, ResponseCallback callback);

    public void pullComments(Context context, String uid, String token, int newsId, ResponseCallback callback);

    public void deleteComment(Context context, String uid, String token, int cid, ResponseCallback callback);

    public void addShortTimeHobby(Context context, String uid, String token, int newsId, List<String> tags, ResponseCallback callback);

    public void pushCollectedNews(Context context, String uid, String token, int newsId, ResponseCallback callback);

    public void pullCollectedNewsList(Context context, String uid, String token, int pageIndex, boolean isRefresh, ResponseCallback callback);

    public void pushDislikedNews(Context context, String uid, String token, int newsId, ResponseCallback callback);

}
