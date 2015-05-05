package net.ym.zzy.favorite.data.respository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import net.ym.zzy.domain.entity.json.CommentListJson;
import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.entity.json.NewsJson;
import net.ym.zzy.domain.respository.DataRepository;
import net.ym.zzy.favorite.data.executor.JobExecutor;
import net.ym.zzy.favorite.data.loader.HttpDataLoder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zengzheying on 15/3/29.
 */
public class DataReposityImpl implements DataRepository {

    private final String HOST = "http://192.168.202.202:8000/";
//    private final String HOST = "http://120.25.217.247:8000/";
//    private final String HOST = "http://120.25.217.247:81/";
//    private static String HOST = "http://192.168.200.36:8000/";

    private static DataRepository mInstance;

    public static DataRepository getInstance() {
        if (mInstance == null){
            mInstance = new DataReposityImpl();
        }
        return mInstance;
    }

    final Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void getNewsList(final Context context, String uid, String token, final int catalog,final int pageIndex,final boolean isRefresh,final ResponseCallback callback){
        final String key = "newslist_" + catalog + "_" + pageIndex + "_" + PAGE_SIZE;
        JobExecutor.getInstance().cancel(key);
        final HashMap<String, String> params = new HashMap<>();
        params.put("cat", String.valueOf(catalog));
        params.put("page", String.valueOf(pageIndex));
        if (!("".equals(uid.trim())) && !("".equals(token.trim()))){
            params.put("uid", uid);
            params.put("token", token);
        }
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try {
                    final NewsJson newsJson = HttpDataLoder.getDataByGetMethod(context, HOST + "news/",
                            key, pageIndex, params, null, isRefresh, NewsJson.class, 9000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (newsJson.getCode() == 0){
                                callback.onResponse(newsJson);
                            }else {
                                callback.onResponseError(newsJson);
                            }
                        }
                    });
                }catch (final Exception e){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(e);
                        }
                    });
                }
            }
        };
        JobExecutor.getInstance().submit(loader, key);
    }

    @Override
    public void login(final Context context, final String uid, final String name, final String token, final String avatar, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("name", name);
                    params.put("token", token);
                    params.put("avatar", avatar);
                    final JsonBase  info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST+"login/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (info.getCode() == 0) {
                                callback.onResponse(info);
                            }else {
                                callback.onResponseError(info);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };
        JobExecutor.getInstance().submit(loader, "login");
    }

    @Override
    public void commentNews(final Context context, final String uid, final String token, final int newsId, final String content, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("news_id", Integer.toString(newsId));
                    params.put("content", content);
                    params.put("token", token);
                    final JsonBase info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST + "comment/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (info.getCode() == 0) {
                                callback.onResponse(info);
                            }else {
                                callback.onResponseError(info);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };
        JobExecutor.getInstance().submit(loader, "commentNews");
    }

    @Override
    public void pullComments(final Context context, final String uid, final String token, final int newsId, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("news_id", Integer.toString(newsId));
                    params.put("token", token);
                    final CommentListJson commentListJson = HttpDataLoder.getDataByPostMethodNotCache(context, HOST + "pull_comments/", params, null, CommentListJson.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (commentListJson.getCode() == 0) {
                                callback.onResponse(commentListJson.getData());
                            }else {
                                callback.onResponseError(commentListJson);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };

        JobExecutor.getInstance().submit(loader, "pullComments");
    }

    @Override
    public void deleteComment(final Context context, final String uid, final String token, final int cid, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("token", token);
                    params.put("cid", Integer.toString(cid));
                    final JsonBase info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST + "delete_comment/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (info.getCode() == 0) {
                                callback.onResponse(info);
                            }else {
                                callback.onResponseError(info);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };

        JobExecutor.getInstance().submit(loader, "deleteComment");
    }

    @Override
    public void addShortTimeHobby(final Context context, final String uid, final String token, final int newsId, final List<String> tags, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    String tagString = "";
                    if (tags != null && tags.size() > 0){
                        for (int i = 0; i < tags.size(); i++){
                            if (i == tags.size() - 1) {
                                tagString += tags.get(i);
                            }else{
                                tagString += tags.get(i) + "|";
                            }
                        }
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("token", token);
                    params.put("news_id", Integer.toString(newsId));
                    params.put("tag", tagString);
                    final JsonBase info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST + "hobby/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (info.getCode() == 0){
                                callback.onResponse(info);
                            }else{
                                callback.onResponseError(info);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };
        JobExecutor.getInstance().submit(loader, "hobby" + newsId);
    }

    @Override
    public void pushCollectedNews(final Context context, final String uid, final String token, final int newsId, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("token", token);
                    params.put("news_id", Integer.toString(newsId));
                    final JsonBase info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST + "collect/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (info.getCode() == 0){
                                callback.onResponse(info);
                            }else{
                                callback.onResponseError(info);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };

        JobExecutor.getInstance().submit(loader, "collect" + newsId);
    }

    @Override
    public void pullCollectedNewsList(final Context context, final String uid, final String token, final int pageIndex, final boolean isRefresh, final ResponseCallback callback) {
        final String key = "collectedNewsList_" + pageIndex + "_" + PAGE_SIZE;
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("token", token);
                    params.put("page", Integer.toString(pageIndex));
                    final NewsJson newsJson = HttpDataLoder.getDataByPostMethod(context, HOST + "pull_collects/", key, pageIndex, params, null, isRefresh, NewsJson.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (newsJson.getCode() == 0){
                                callback.onResponse(newsJson);
                            }else{
                                callback.onResponseError(newsJson);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };

        JobExecutor.getInstance().submit(loader, "pullCollectedNewsList");
    }

    @Override
    public void pushDislikedNews(final Context context, final String uid, final String token, final int newsId, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("token", token);
                    params.put("news_id", Integer.toString(newsId));
                    final JsonBase info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST + "dislike/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (info.getCode() == 0){
                                callback.onResponse(info);
                            }else{
                                callback.onResponseError(info);
                            }
                        }
                    });
                }catch (final Exception ex){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onException(ex);
                        }
                    });
                }
            }
        };

        JobExecutor.getInstance().submit(loader, "dislike" + newsId);
    }
}
