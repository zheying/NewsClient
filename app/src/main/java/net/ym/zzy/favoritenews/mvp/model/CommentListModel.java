package net.ym.zzy.favoritenews.mvp.model;

import java.util.List;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentListModel implements Model {
    private List<CommentModel> mCommentModels;

    public List<CommentModel> getCommentModels() {
        return mCommentModels;
    }

    public void setCommentModels(List<CommentModel> commentModels) {
        mCommentModels = commentModels;
    }
}
