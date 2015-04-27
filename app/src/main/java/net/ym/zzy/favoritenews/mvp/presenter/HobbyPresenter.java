package net.ym.zzy.favoritenews.mvp.presenter;

import android.widget.Toast;

import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.HobbyInteractor;
import net.ym.zzy.domain.interactor.implementation.HobbyInteractorImpl;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.mvp.view.HobbyView;

import java.util.List;

/**
 * Created by zengzheying on 15/4/27.
 */
public class HobbyPresenter {

    private final HobbyView mHobbyView;
    private final HobbyInteractor mHobbyInteractor;

    public HobbyPresenter(HobbyView hobbyView) {
        mHobbyView = hobbyView;
        mHobbyInteractor = new HobbyInteractorImpl(DataReposityImpl.getInstance());
    }

    public void addHobby(String uid, String token, int newsId, List<String> tags){
        mHobbyView.onSendingData();
        mHobbyInteractor.executeAddHobby(mHobbyView.getContext(), uid, token, newsId, tags, new HobbyInteractor.Callback() {
            @Override
            public void onAddHobbySuccessfully() {
                mHobbyView.onSendDataSuccessfully();
            }

            @Override
            public void onAddHobbyError(JsonBase errorInfo) {
                Toast.makeText(mHobbyView.getContext(), errorInfo.getMsg(), Toast.LENGTH_SHORT).show();
                mHobbyView.onSendDataError();
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace(System.err);
                mHobbyView.onSendDataError();
            }
        });
    }
}
