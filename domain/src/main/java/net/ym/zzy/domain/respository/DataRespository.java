package net.ym.zzy.domain.respository;

import android.content.Context;

import net.ym.zzy.domain.entity.NewsList;

import java.io.Serializable;

/**
 * Created by zengzheying on 15/3/28.
 */
public interface DataRespository {

    public static final int PAGE_SIZE = 20;

    public interface ResponseCallback{
        void onResponse(Serializable ser);
        void onException(Exception ex);
    }

    public void getNewsList(Context context, int catalog, int pageIndex, boolean isRefresh, ResponseCallback callback);

    public void login(Context context, String uid, String name, String token, ResponseCallback callback);

}
