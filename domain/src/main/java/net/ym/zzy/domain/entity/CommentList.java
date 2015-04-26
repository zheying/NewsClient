package net.ym.zzy.domain.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengzheying on 15/4/20.
 */
public class CommentList implements Serializable{

    @SerializedName("list")
    private List<Comment> mCommentList;

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public void setCommentList(List<Comment> commentList) {
        mCommentList = commentList;
    }
}
