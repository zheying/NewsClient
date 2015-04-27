package net.ym.zzy.domain.interactor.concept;

import android.content.Context;

import net.ym.zzy.domain.entity.CommentList;
import net.ym.zzy.domain.entity.json.JsonBase;

/**
 * Created by zengzheying on 15/4/20.
 */
public interface CommentInteractor extends Interactor {

    interface PushCommentCallback{
        void onCommentNewsSuccessfully();
        void onCommentNewsError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    interface PullCommentListCallback{
        void onPullCommentListSuccessfully(CommentList commentList);
        void onPullCommentListError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    interface DeleteCommentCallback{
        void onDeleteCommentSuccessfully();
        void onDeleteCommentError(JsonBase errorInfo);
        void onException(Exception ex);
    }

    void executePushComment(Context context, String uid, String token, int newsId, String content, PushCommentCallback callback);

    void executePullComment(Context context, String uid, String token, int newsId, PullCommentListCallback callback);

    void executeDeleteComment(Context context, String uid, String token, int cid, DeleteCommentCallback callback);
}
