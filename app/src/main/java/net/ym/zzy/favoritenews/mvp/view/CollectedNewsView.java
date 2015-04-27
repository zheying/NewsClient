package net.ym.zzy.favoritenews.mvp.view;

/**
 * Created by zengzheying on 15/4/27.
 */
public interface CollectedNewsView extends LoadDataView{

    void onPullNewsListException(Exception ex);
    void onPushNewsException(Exception ex);

}
