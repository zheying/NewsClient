package net.ym.zzy.domain.interactor.concept;

import android.content.Context;

import net.ym.zzy.domain.entity.News;
import net.ym.zzy.domain.entity.json.NewsJson;

import java.util.List;

/**
 * Created by zengzheying on 15/4/15.
 */
public interface NewsListInteractor extends Interactor {
    interface Callback{
        void onLoadDataSuccessfully(List<News> newsList);
        void onLoadDataError();
    }

    void execute(Context context, int newsCatalog, int pageIndex, boolean isRefresh, Callback callback);

}
