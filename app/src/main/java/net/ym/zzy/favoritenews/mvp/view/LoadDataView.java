package net.ym.zzy.favoritenews.mvp.view;

import android.content.Context;

import net.ym.zzy.favoritenews.mvp.model.Model;

/**
 * Created by zengzheying on 15/4/15.
 */
public interface LoadDataView {

    void onLoadingData();
    void onLoadDataSuccessfully(Model model);
    void onLoadDataError();

    void sendData();
    void onSendDataSuccessfully();
    void onSendDataError();

    Context getContext();
}
