package net.ym.zzy.domain.interactor.implementation;

import android.content.Context;

import net.ym.zzy.domain.entity.json.NewsJson;
import net.ym.zzy.domain.interactor.concept.NewsListInteractor;
import net.ym.zzy.domain.respository.DataRepository;

import java.io.Serializable;

/**
 * Created by zengzheying on 15/4/15.
 */
public class NewsListInteractorImpl implements NewsListInteractor {

    private final DataRepository mDataRepository;

    public NewsListInteractorImpl(DataRepository dataRepository) {
        mDataRepository = dataRepository;
    }

    @Override
    public void execute(Context context, int newsCatalog, int pageIndex, boolean isRefresh, final Callback callback) {
        mDataRepository.getNewsList(context, newsCatalog, pageIndex, isRefresh, new DataRepository.ResponseCallback() {
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
}
