package net.ym.zzy.favoritenews.mvp.mapper;

import net.ym.zzy.domain.entity.Comment;
import net.ym.zzy.favoritenews.mvp.model.CommentListModel;
import net.ym.zzy.favoritenews.mvp.model.CommentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentModelMapper {

    public static CommentModel transform(Comment comment){
        if (comment != null){
            CommentModel commentModel = new CommentModel();
            commentModel.setAvatarUrl(comment.getAvatarUrl());
            commentModel.setCommentContent(comment.getCommentContent());
            commentModel.setCommentTime(comment.getCommentTime());
            commentModel.setUid(comment.getUid());
            commentModel.setUserName(comment.getUserName());
            commentModel.setCid(comment.getCid());
            return commentModel;
        }
        return null;
    }


    public static CommentListModel tranfrom(List<Comment> comments){
        if (comments != null){
            CommentListModel commentListModel = new CommentListModel();
            ArrayList<CommentModel> commentModels = new ArrayList<>();
            for (Comment comment : comments){
                commentModels.add(transform(comment));
            }
            commentListModel.setCommentModels(commentModels);
            return commentListModel;
        }
        return null;
    }

}
