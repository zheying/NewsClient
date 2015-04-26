package net.ym.zzy.favoritenews.mvp.model;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentModel implements Model{

    private int cid;

    private String userName;

    private long commentTime;

    private String commentContent;

    private String avatarUrl;

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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommentModel))
            return false;
        if (o == this)
            return true;
        return ((CommentModel)o).getCid() == cid;
    }
}
