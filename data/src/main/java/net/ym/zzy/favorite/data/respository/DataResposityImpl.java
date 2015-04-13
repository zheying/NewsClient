package net.ym.zzy.favorite.data.respository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import net.ym.zzy.domain.entity.json.JsonBase;
import net.ym.zzy.domain.entity.json.NewsJson;
import net.ym.zzy.domain.respository.DataRespository;
import net.ym.zzy.favorite.data.executor.JobExecutor;
import net.ym.zzy.favorite.data.loader.HttpDataLoder;

import java.util.HashMap;

/**
 * Created by zengzheying on 15/3/29.
 */
public class DataResposityImpl implements DataRespository {

//    private final String HOST = "http://192.168.202.202:8000/";
    private final String HOST = "http://120.25.217.247:8000/";

    private static DataRespository mInstance;

    public static DataRespository getInstance() {
        if (mInstance == null){
            mInstance = new DataResposityImpl();
        }
        return mInstance;
    }

    final Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void getNewsList(final Context context,final int catalog,final int pageIndex,final boolean isRefresh,final ResponseCallback callback){
        final String key = "newslist_" + catalog + "_" + pageIndex + "_" + PAGE_SIZE;
        JobExecutor.getInstance().cancel(key);
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("catalog", String.valueOf(catalog));
        params.put("page", String.valueOf(pageIndex));
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try {
                    final NewsJson newsJson = HttpDataLoder.getDataByGetMethod(context, HOST + "news/", key, pageIndex, params, null, isRefresh, NewsJson.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(newsJson);
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
    public void login(final Context context, final String uid, final String name, final String token, final ResponseCallback callback) {
        Runnable loader = new Runnable() {
            @Override
            public void run() {
                try{
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("uid", uid);
                    params.put("name", name);
                    params.put("token", token);
                    final JsonBase  info = HttpDataLoder.getDataByPostMethodNotCache(context, HOST+"login/", params, null, JsonBase.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(info);
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
}