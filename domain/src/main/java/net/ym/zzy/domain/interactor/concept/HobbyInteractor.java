package net.ym.zzy.domain.interactor.concept;

import android.content.Context;

import net.ym.zzy.domain.entity.json.JsonBase;

import java.util.List;

/**
 * Created by zengzheying on 15/4/27.
 */
public interface HobbyInteractor extends Interactor{

    interface Callback{
        void onAddHobbySuccessfully();
        void onAddHobbyError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    void executeAddHobby(Context context, String uid, String token, int newsId, List<String> tags, Callback callback);

}
