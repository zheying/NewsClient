package net.ym.zzy.favoritenews.weibo.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import net.ym.zzy.favoritenews.Constants;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;

/**
 * Created by zengzheying on 15/4/26.
 */
public class WBShareHelper {

    private final String TAG = WBShareHelper.class.getName();

    IWeiboShareAPI mShareAPI;
    BaseRequest mBaseRequest;
    Activity mActivity;

    public WBShareHelper(Activity activity){
        mActivity = activity;
        mShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, Constants.WEIBO_APP_KEY);
        mShareAPI.registerApp();
    }

    public void handleWeiboRequest(Intent intent, IWeiboHandler.Request request){
        mShareAPI.handleWeiboRequest(intent, request);
    }

    public void handleWeiboResponse(Intent intent, IWeiboHandler.Response response){
        mShareAPI.handleWeiboResponse(intent, response);
    }

    public void onRequest(BaseRequest baseRequest){
        mBaseRequest = baseRequest;
    }

    public void shareWeibo(NewsModel news){
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = news.getTitle() + " " + news.getSource_url();
        weiboMessage.textObject = textObject;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        AuthInfo authInfo = new AuthInfo(mActivity, Constants.WEIBO_APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mActivity);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mShareAPI.sendRequest(mActivity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException ex) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(mActivity, newToken);
                Toast.makeText(mActivity, "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
