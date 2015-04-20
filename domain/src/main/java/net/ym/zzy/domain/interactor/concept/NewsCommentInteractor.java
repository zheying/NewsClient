package net.ym.zzy.domain.interactor.concept;

import android.content.Context;

import net.ym.zzy.domain.entity.CommentList;
import net.ym.zzy.domain.entity.json.JsonBase;

/**
 * Created by zengzheying on 15/4/20.
 */
public interface NewsCommentInteractor extends Interactor {

    interface PushCommentCallback{
        void onCommentNewsSuccessfully();
        void onCommentNewsError(JsonBase errorInfo);
    }

    interface PullCommentListCallback{
        void onPullCommentListSuccessfully(CommentList commentList);
        void onPullCommentListError(JsonBase errorInfo);
    }

    interface DeleteCommentCallback{
        void onDeleteCommentSuccessfully();
        void onDeleteCommentError(JsonBase errorInfo);
    }

    void executePushComment(Context context, String uid, int newsId, String content, PushCommentCallback callback);

    void executePullComment(Context context, String uid, int cid, PullCommentListCallback callback);

    void executeDeleteComment(Context context, String uid, int cid, DeleteCommentCallback callback);
}
