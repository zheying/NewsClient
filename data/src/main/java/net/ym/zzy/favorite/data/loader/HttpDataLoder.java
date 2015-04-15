package net.ym.zzy.favorite.data.loader;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.ym.zzy.favorite.data.exception.HttpException;
import net.ym.zzy.favorite.data.tools.CacheManager;
import net.ym.zzy.favorite.data.tools.NetworkTools;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**利用泛型封装具体的网络逻辑
 * Created by zengzheying on 15/3/28.
 */
public class HttpDataLoder {

    /**
     * 解释JSON
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    private static <T extends Serializable> T fromJson(String json, Class<T> classOfT){
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, classOfT);
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param pageIndex 当前页数
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param timeout 超时时间
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByGetMethod(Context context, String url, String key, int pageIndex, HashMap<String, String> params,
                                                                HashMap<String, String> header, boolean isRefresh, Class<T> classOfT, int timeout) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context) && (!CacheManager.isReadableDataCache(context, key) || isRefresh)){
            try{
                String json = HttpLoader.getDataByGetMethod(context, url, params, header, timeout);
                if (json != null && pageIndex == 0){
                    data = fromJson(json, classOfT);
                    CacheManager.writeCacheObject(context, data, key);
                }
            }catch (Exception ex){
                data = (T)CacheManager.readCacheObject(context, key);
                if (data == null){
                    throw ex;
                }
            }
        }else{
            data = (T)CacheManager.readCacheObject(context, key);
        }
        return data;
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param timeout 超时时间
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByGetMethod(Context context, String url, String key, HashMap<String, String> params,
                                                                HashMap<String, String> header, boolean isRefresh, Class<T> classOfT, int timeout) throws  Exception{
        return getDataByGetMethod(context, url, key, 0, params, header, isRefresh, classOfT, timeout);
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param pageIndex 当前页数
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByGetMethod(Context context, String url, String key, int pageIndex, HashMap<String, String> params,
                                                                HashMap<String, String> header,boolean isRefresh, Class<T> classOfT) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context) && (!CacheManager.isReadableDataCache(context, key) || isRefresh)){
            try{
                String json = HttpLoader.getDataByGetMethod(context, url, params, header);
                if (json != null && pageIndex == 0){
                    data = fromJson(json, classOfT);
                    CacheManager.writeCacheObject(context, data, key);
                }
            }catch (Exception ex){
                ex.printStackTrace(System.err);
                Log.d(context.getPackageName(), "get data cause Exception!");
                data = (T)CacheManager.readCacheObject(context, key);
                if (data == null){
                    throw ex;
                }
            }
        }else{
            data = (T)CacheManager.readCacheObject(context, key);
        }
        return data;
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByGetMethod(Context context, String url, String key, HashMap<String, String> params,
                                                                HashMap<String, String> header, boolean isRefresh, Class<T> classOfT) throws Exception{
        return getDataByGetMethod(context, url, key, 0, params, header, isRefresh, classOfT);
    }



    /**
     * 通过 POST 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param pageIndex 当前页数
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param timeout 超时时间
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByPostMethod(Context context, String url, String key, int pageIndex, HashMap<String, String> params,
                                                                 HashMap<String, String> header, boolean isRefresh, Class<T> classOfT, int timeout) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context) && (!CacheManager.isReadableDataCache(context, key) || isRefresh)){
            try{
                String json = HttpLoader.getDataByPostMethod(context, url, params, header, timeout);
                if (json != null && pageIndex == 0){
                    data = fromJson(json, classOfT);
                    CacheManager.writeCacheObject(context, data, key);
                }
            }catch (Exception ex){
                data = (T)CacheManager.readCacheObject(context, key);
                if (data == null){
                    throw  ex;
                }
            }
        }else{
            data = (T)CacheManager.readCacheObject(context, key);
        }
        return data;
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param pageIndex 当前页数
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByPostMethod(Context context, String url, String key, int pageIndex, HashMap<String, String> params,
                                                                 HashMap<String, String> header, boolean isRefresh, Class<T> classOfT) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context) && (!CacheManager.isReadableDataCache(context, key) || isRefresh)){
            try{
                String json = HttpLoader.getDataByPostMethod(context, url, params, header);
                if (json != null && pageIndex == 0){
                    data = fromJson(json, classOfT);
                    CacheManager.writeCacheObject(context, data, key);
                }
            }catch (Exception ex){
                data = (T)CacheManager.readCacheObject(context, key);
                if (data == null){
                    throw  ex;
                }
            }
        }else{
            data = (T)CacheManager.readCacheObject(context, key);
        }
        return data;
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param timeout 超时时间
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByPostMethod(Context context, String url, String key, HashMap<String, String> params,
                                                                 HashMap<String, String> header, boolean isRefresh, Class<T> classOfT, int timeout) throws Exception{
        return getDataByGetMethod(context, url, key, 0, params, header, isRefresh, classOfT, timeout);
    }

    /**
     * 通过 GET 方法获取数据
     * @param context 程序上下文
     * @param url  请求url
     * @param key  缓存文件名
     * @param params 请求参数
     * @param header 请求头部
     * @param isRefresh 是否刷新
     * @param classOfT 可序列化类
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByPostMethod(Context context, String url, String key, HashMap<String, String> params,
                                                                 HashMap<String, String> header, boolean isRefresh, Class<T> classOfT) throws Exception{
        return getDataByGetMethod(context, url, key, 0, params, header, isRefresh, classOfT);
    }

    /**
     * 通过 GET 方法获取数据，不缓存
     * @param context 程序上下文
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头部
     * @param classOfT 可序列化类
     * @param timeout 超时时间
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByGetMethodNotCache(Context context, String url, HashMap<String, String> params,
                                                                        HashMap<String, String> headers, Class<T> classOfT, int timeout) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context)){
            String json = HttpLoader.getDataByGetMethod(context, url, params, headers, timeout);
            data = fromJson(json, classOfT);
        }
        return data;
    }

    /**
     * 通过 GET 方法获取数据，不缓存
     * @param context 程序上下文
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头部
     * @param classOfT 可序列化类
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByGetMethodNotCache(Context context, String url, HashMap<String, String> params,
                                                                        HashMap<String, String> headers, Class<T> classOfT) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context)){
            String json = HttpLoader.getDataByGetMethod(context, url, params, headers);
            data = fromJson(json, classOfT);
        }
        return data;
    }

    /**
     * 通过 POST 方法获取数据，不缓存
     * @param context 程序上下文
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头部
     * @param classOfT 可序列化类
     * @param timeout 超时时间
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByPostMethodNotCache(Context context, String url, HashMap<String, String> params,
                                                                         HashMap<String, String> headers, Class<T> classOfT, int timeout) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context)){
            String json = HttpLoader.getDataByPostMethod(context, url,params, headers, timeout);
            data = fromJson(json, classOfT);
        }
        return data;
    }

    /**
     * 通过 POST 方法获取数据，不缓存
     * @param context 程序上下文
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头部
     * @param classOfT 可序列化类
     * @param <T> 泛型
     * @return 泛型对象
     * @throws Exception
     */
    public static <T extends Serializable> T getDataByPostMethodNotCache(Context context, String url, HashMap<String, String> params,
                                                                         HashMap<String, String> headers, Class<T> classOfT) throws Exception{
        T data = null;
        if (NetworkTools.isNetworkConnected(context)){
            String json = HttpLoader.getDataByPostMethod(context, url, params, headers);
            data = fromJson(json, classOfT);
        }
        return data;
    }
}
