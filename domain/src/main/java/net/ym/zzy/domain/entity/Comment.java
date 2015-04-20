package net.ym.zzy.domain.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zengzheying on 15/4/20.
 */
public class Comment {

    @SerializedName("name")
    private String userName;

    @SerializedName("time")
    private long commentTime;

    @SerializedName("content")
    private String commentContent;

    @SerializedName("avatar")
    private String avatarUrl;

    @SerializedName("uid")
    private String uid;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
