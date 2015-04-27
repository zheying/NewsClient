package net.ym.zzy.domain.interactor.implementation;

import android.content.Context;

import net.ym.zzy.domain.entity.CommentList;
import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.CommentInteractor;
import net.ym.zzy.domain.respository.DataRepository;

import java.io.Serializable;

/**
 * Created by zengzheying on 15/4/20.
 */
public class CommentInteractorImpl implements CommentInteractor {

    private final DataRepository mDataRepository;

    public CommentInteractorImpl(DataRepository dataRepository) {
        mDataRepository = dataRepository;
    }

    @Override
    public void executePushComment(Context context, String uid, String token, int newsId, String content, final PushCommentCallback callback) {
        mDataRepository.commentNews(context, uid, token, newsId, content, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null){
                    callback.onCommentNewsSuccessfully();
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null){
                    callback.onCommentNewsError((JsonBase)errInfo);
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onException(ex);
                }
            }
        });
    }

    @Override
    public void executePullComment(Context context, String uid, String token, int newsId, final PullCommentListCallback callback) {
        mDataRepository.pullComments(context, uid, token, newsId, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null){
                    callback.onPullCommentListSuccessfully((CommentList)ser);
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null){
                    callback.onPullCommentListError((JsonBase)errInfo);
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onException(ex);
                }
            }
        });
    }

    @Override
    public void executeDeleteComment(Context context, String uid, String token, int cid, final DeleteCommentCallback callback) {
        mDataRepository.deleteComment(context, uid, token, cid, new DataRepository.ResponseCallback() {
            @Override
            public void onResponse(Serializable ser) {
                if (callback != null){
                    callback.onDeleteCommentSuccessfully();
                }
            }

            @Override
            public void onResponseError(Serializable errInfo) {
                if (callback != null){
                    callback.onDeleteCommentError((JsonBase)errInfo);
                }
            }

            @Override
            public void onException(Exception ex) {
                if (callback != null){
                    callback.onException(ex);
                }
            }
        });
    }
}
