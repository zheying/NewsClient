package net.ym.zzy.favoritenews.weibo.auth;

/**
 * Created by zengzheying on 15/3/22.
 */
public interface AuthorCallback {
    public void onComplete();
    public void onCancel();
    public void onException();
}
