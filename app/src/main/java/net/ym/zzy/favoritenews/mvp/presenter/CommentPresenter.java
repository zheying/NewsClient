package net.ym.zzy.favoritenews.mvp.presenter;

import net.ym.zzy.domain.entity.CommentList;
import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.interactor.concept.NewsCommentInteractor;
import net.ym.zzy.domain.interactor.implementation.NewsCommentInteractorImpl;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.mvp.mapper.CommentModelMapper;
import net.ym.zzy.favoritenews.mvp.view.CommentListView;
import net.ym.zzy.favoritenews.mvp.view.CommentView;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentPresenter implements NewsCommentInteractor.PushCommentCallback,
        NewsCommentInteractor.PullCommentListCallback,
        NewsCommentInteractor.DeleteCommentCallback{

    private final CommentListView mCommentListView;
    private final CommentView mCommentView;
    private final NewsCommentInteractor mNewsCommentInteractor;

    public CommentPresenter(CommentListView commentListView) {
        mCommentListView = commentListView;
        mNewsCommentInteractor = new NewsCommentInteractorImpl(DataReposityImpl.getInstance());
        mCommentView = null;
    }

    public CommentPresenter(CommentView commentView) {
        mCommentListView = null;
        mNewsCommentInteractor = new NewsCommentInteractorImpl(DataReposityImpl.getInstance());
        mCommentView = commentView;
    }

    public void pushNewsComment(String uid, String token, int news_id, String content){
        if (mCommentView == null) return;
        mCommentView.onSendingData();
        mNewsCommentInteractor.executePushComment(mCommentView.getContext(), uid, token, news_id, content, this);
    }

    public void pullCommentList(String uid, String token, int news_id){
        if (mCommentListView == null) return;
        mCommentListView.onLoadingData();
        mNewsCommentInteractor.executePullComment(mCommentListView.getContext(), uid, token, news_id, this);
    }

    public void deleteComment(String uid, String token, int cid){
        if (mCommentListView == null) return;
        mCommentListView.onSendingData();
        mNewsCommentInteractor.executeDeleteComment(mCommentListView.getContext(), uid, token, cid, this);
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
