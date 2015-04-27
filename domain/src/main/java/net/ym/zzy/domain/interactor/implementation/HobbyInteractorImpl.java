package net.ym.zzy.domain.interactor.implementation;

import android.content.Context;

import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.HobbyInteractor;
import net.ym.zzy.domain.respository.DataRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengzheying on 15/4/27.
 */
public class HobbyInteractorImpl implements HobbyInteractor {


    private final DataRepository mDataRepository;


    public HobbyInteractorImpl(DataRepository dataRepository) {
        mDataRepository = dataRepository;
    }

    @Override
    public void executeAddHobby(Context context, String uid, String token, int newsId, List<String> tags, final Callback callback) {
        mDataRepository.addShortTimeHobby(context, uid, token, newsId, tags, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                callback.onAddHobbySuccessfully();
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                callback.onAddHobbyError((JsonBase)errInfo);
            }

            @Override
            public void onException(Exception ex) {
                callback.onException(ex);
            }
        });
    }
}
