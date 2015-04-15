package net.ym.zzy.favoritenews;

/**
 * Created by zengzheying on 15/3/22.
 */
public interface Constants {

    public static String HOST = "http://192.168.202.202:8000/";
//    public static String HOST = "http://120.25.217.247:8000/";

    public static final String WEIBO_APP_KEY = "942411083";

    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

}
