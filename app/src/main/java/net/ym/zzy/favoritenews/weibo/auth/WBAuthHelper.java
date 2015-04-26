package net.ym.zzy.favoritenews.weibo.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.android.weibo.sdk.openapi.UsersAPI;
import com.sina.android.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.android.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;

import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.respository.DataRepository;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.Constants;
import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;

import java.io.Serializable;

/**
 * Created by zengzheying on 15/3/22.
 */
public class WBAuthHelper {


    private static final String TAG = WBAuthHelper.class.getName();

    private AuthInfo mAuthInfo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    /** 用户信息接口 **/
    private UsersAPI mUsersAPI;

    private Context context;

    private AuthorCallback mAuthorCallback;

    private static WBAuthHelper wbAuthHelper;

    public WBAuthHelper(Activity activity){
        context = activity;
        mAuthInfo = new AuthInfo(activity, Constants.WEIBO_APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
    }


    public static void initWBAuthHelper(Activity activity){
        wbAuthHelper = new WBAuthHelper(activity);
    }

    private void authorize(AuthorCallback authorCallback){
        this.mAuthorCallback = authorCallback;
        mSsoHandler.authorize(new AuthListener());
    }

    public static void authorizeWBAuthHelper(AuthorCallback authorCallback){
        if (wbAuthHelper != null){
            wbAuthHelper.authorize(authorCallback);
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     */

    private void onActivityResult(int requestCode, int resultCode, Intent data){
        if (mSsoHandler != null){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public static void onActivityResultWBAuthHelper(int requestCode, int resultCode, Intent data){
        if (wbAuthHelper != null){
            wbAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                Log.i("AccessToken", "token: " + mAccessToken.getToken() + " expires_in: " + mAccessToken.getExpiresTime() + " uid: " + mAccessToken.getUid());
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(context, mAccessToken);

                mUsersAPI = new UsersAPI(context, Constants.WEIBO_APP_KEY, mAccessToken);
                mUsersAPI.show(Long.parseLong(mAccessToken.getUid()), mListener);

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = context.getString(R.string.weibo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(context,
                    R.string.weibo_toast_auth_canceled, Toast.LENGTH_LONG).show();
            if (mAuthorCallback != null){
                mAuthorCallback.onCancel();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(context,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            if (mAuthorCallback != null){
                mAuthorCallback.onException();
            }
        }
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i(TAG, response);
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);

                if (user != null) {
                    AccessTokenKeeper.writeScreenName(context, user.screen_name);
                    AccessTokenKeeper.writeAvatar(context, user.avatar_large);
                    DataRepository dataRepository = DataReposityImpl.getInstance();

//                    Log.d("avatar", "image_url:" + user.profile_image_url);
//                    Log.d("avatar", "avatar hd:" + user.avatar_hd);
//                    Log.d("avatar", "avatar large:" + user.avatar_large);

                    dataRepository.login(context, mAccessToken.getUid(), user.screen_name, mAccessToken.getToken(), user.avatar_large, new DataRepository.ResponseCallback() {
                        @Override
                        public void onResponse(Serializable ser) {
                            Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                            if (mAuthorCallback != null) {
                                mAuthorCallback.onComplete();
                            }
                        }

                        @Override
                        public void onResponseError(Serializable errInfo) {
                            Toast.makeText(context, "登陆失败" + ((JsonBase) errInfo).getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Exception ex) {
                            ex.printStackTrace(System.err);
                        }
                    });
//
//                    Toast.makeText(context,
//                            R.string.weibo_toast_auth_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mAuthorCallback != null){
                mAuthorCallback.onException();
            }
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(context, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

}
