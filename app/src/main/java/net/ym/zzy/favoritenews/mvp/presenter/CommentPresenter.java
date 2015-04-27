package net.ym.zzy.favoritenews.mvp.presenter;

import net.ym.zzy.domain.entity.CommentList;
import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.CommentInteractor;
import net.ym.zzy.domain.interactor.implementation.CommentInteractorImpl;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.mvp.mapper.CommentModelMapper;
import net.ym.zzy.favoritenews.mvp.view.CommentListView;
import net.ym.zzy.favoritenews.mvp.view.CommentView;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentPresenter implements CommentInteractor.PushCommentCallback,
        CommentInteractor.PullCommentListCallback,
        CommentInteractor.DeleteCommentCallback{

    private final CommentListView mCommentListView;
    private final CommentView mCommentView;
    private final CommentInteractor mCommentInteractor;

    public CommentPresenter(CommentListView commentListView) {
        mCommentListView = commentListView;
        mCommentInteractor = new CommentInteractorImpl(DataReposityImpl.getInstance());
        mCommentView = null;
    }

    public CommentPresenter(CommentView commentView) {
        mCommentListView = null;
        mCommentInteractor = new CommentInteractorImpl(DataReposityImpl.getInstance());
        mCommentView = commentView;
    }

    public void pushNewsComment(String uid, String token, int news_id, String content){
        if (mCommentView == null) return;
        mCommentView.onSendingData();
        mCommentInteractor.executePushComment(mCommentView.getContext(), uid, token, news_id, content, this);
    }

    public void pullCommentList(String uid, String token, int news_id){
        if (mCommentListView == null) return;
        mCommentListView.onLoadingData();
        mCommentInteractor.executePullComment(mCommentListView.getContext(), uid, token, news_id, this);
    }

    public void deleteComment(String uid, String token, int cid){
        if (mCommentListView == null) return;
        mCommentListView.onSendingData();
        mCommentInteractor.executeDeleteComment(mCommentListView.getContext(), uid, token, cid, this);
    }

    @Override
    public void onDeleteCommentSuccessfully() {

    }

    @Override
    public void onDeleteCommentError(JsonBase errorInfo) {

    }

    @Override
    public void onCommentNewsSuccessfully() {
        if (mCommentView != null){
            mCommentView.onSendDataSuccessfully();
        }
    }

    @Override
    public void onCommentNewsError(JsonBase errorInfo) {

    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace(System.err);
        mCommentListView.onLoadDataError();
    }

    @Override
    public void onPullCommentListSuccessfully(CommentList commentList) {
        if (commentList != null) {
            mCommentListView.onLoadDataSuccessfully(CommentModelMapper.tranfrom(commentList.getCommentList()));
        }else{
            mCommentListView.onLoadDataSuccessfully(null);
        }
    }

    @Override
    public void onPullCommentListError(JsonBase errorInfo) {
        mCommentListView.onLoadDataError();
    }
}
