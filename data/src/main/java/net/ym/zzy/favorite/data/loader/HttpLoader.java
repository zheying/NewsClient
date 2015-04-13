package net.ym.zzy.favorite.data.loader;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import net.ym.zzy.favorite.data.config.AppDebugConfig;
import net.ym.zzy.favorite.data.exception.HttpException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Created by zengzheying on 15/3/22.
 */
public class HttpLoader {

    private static String TAG = HttpLoader.class.getName();

    private static String userAgent;

    private static String _MakeURL(String p_url, Map<String, String> params) {
        StringBuilder url = new StringBuilder(p_url);
        if(url.indexOf("?")<0)
            url.append('?');

        for(String name : params.keySet()){
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(params.get(name));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }

        return url.toString().replace("?&", "?");
    }

    private static String _MakeParamsString(Map<String, String> params){
        StringBuilder url = new StringBuilder('?');
        for (String name : params.keySet()){
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(params.get(name));
        }

        return url.toString().replace("?&", "");
    }

    public static String getDataByGetMethod(Context context, String url, HashMap<String, String> params, HashMap<String, String> headers, int timeout) throws IOException, HttpException{
        return get(context, _MakeURL(url, params), headers, timeout);
    }

    public static String getDataByGetMethod(Context context, String url, HashMap<String, String> params, HashMap<String, String> headers) throws IOException, HttpException{
        return get(context, _MakeURL(url, params), headers);
    }

    public static String get(Context context, String url, HashMap<String, String> headers, int timeout) throws IOException, HttpException{
        if (url == null || context == null){
            return null;
        }

        context = context.getApplicationContext();

        HttpClient httpClient = new DefaultHttpClient(createHttpParams(context, timeout));
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

        HttpGet httpGet = new HttpGet(url);

        //http报头
        if (headers == null){
            headers = new HashMap<String, String>();
        }
        headers.put("Accept-Encoding", "gzip");
        final String userAgent = getUserAgent();
        headers.put("User-Agent", userAgent);

        addHeaders(httpGet, headers);

        //开始请求
        HttpResponse rsp = httpClient.execute(httpGet);

        String result = getStringContentFromHttpResponse(rsp);

        if (AppDebugConfig.IS_DEBUG){
            Log.d("HttpLoader", String.format("[GET]:%s\n[Resp]:%s", url, result));
        }

        try{
            httpClient.getConnectionManager().shutdown();
        }catch (Throwable e){

        }

        return result;
    }

    public static String get(Context context, String url, HashMap<String, String> headers) throws IOException, HttpException{
        return get(context, url, headers, 3000);
    }


    public static String getDataByPostMethod(Context context, String url, HashMap<String, String> params, HashMap<String, String> headers, int timeout) throws IOException, HttpException{
        if (params == null || params.keySet().size() == 0){
            return null;
        }
//        byte[] data = _MakeParamsString(params).getBytes("UTF-8");
        return post(context, url, params, headers, timeout);
    }

    public static String getDataByPostMethod(Context context, String url, HashMap<String, String> params, HashMap<String, String> headers) throws IOException, HttpException{
        if (params == null || params.keySet().size() == 0){
            return null;
        }
//        byte[] data = _MakeParamsString(params).getBytes("UTF-8");
        return post(context, url, params, headers);
    }

    public static String post(Context context, String url, HashMap<String,String> postData, HashMap<String, String> headers, int timeout) throws IOException, HttpException{
        if (postData == null || postData==null || context == null){
            return null;
        }
        context = context.getApplicationContext();

        HttpClient httpClient = new DefaultHttpClient(createHttpParams(context, timeout));

        HttpPost httpPost = new HttpPost(url);
        //Http报头
        if (headers == null){
            headers = new HashMap<String, String>();
        }
        headers.put("Accept-Encoding", "gzip");
        final String userAgent = getUserAgent();
        addHeaders(httpPost, headers);

        //写入流
//        ByteArrayEntity entity = new ByteArrayEntity(postData);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<String> keySet = postData.keySet();
        for (String key : keySet){
            params.add(new BasicNameValuePair(key, postData.get(key)));
        }
        HttpEntity entity = new UrlEncodedFormEntity(params, "utf-8");
        httpPost.setEntity(entity);

        //开始请求
        HttpResponse rsp = httpClient.execute(httpPost);

        String result = getStringContentFromHttpResponse(rsp);

        if (AppDebugConfig.IS_DEBUG){
            Log.d("HttpLoader", String.format("[POST]:%s\n[Resp]:%s", url, result));
        }

        try {
            httpClient.getConnectionManager().shutdown();
        }catch (Throwable  e){

        }
        return result;
    }

    public static String post(Context context, String url, HashMap<String, String> params, HashMap<String, String> headers) throws IOException, HttpException{
        return post(context, url, params, headers, 3000);
    }

    /**
     * 从httpResponse中读取字符串内容，会根据返回的Content-Encoding对压缩的信息进行解码
     *
     * @param rsp
     * @return
     * @throws java.io.IOException
     */

    public static String getStringContentFromHttpResponse(HttpResponse rsp) throws IOException, HttpException{
        if (rsp == null){
            return null;
        }
        if (AppDebugConfig.IS_DEBUG){
            Log.d(TAG, rsp.getStatusLine().getProtocolVersion()
                    + " " + rsp.getStatusLine().getStatusCode()
                    + "  " + rsp.getStatusLine().getReasonPhrase());
        }

        if (rsp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity entity = rsp.getEntity();
            if (entity == null){
                return null;
            }
            Header header = entity.getContentEncoding();

            if (header == null || header.getValue() == null){
                //说明没有压缩
                return EntityUtils.toString(entity);
            }
            String encoding = header.getValue().toLowerCase();

            if ("gzip".equals(encoding)){
                //gzip压缩
                return loadStringFromGzipStream(entity.getContent());
            }

            return EntityUtils.toString(entity);
        }else if (HttpException.isNetworkErrorCode(rsp.getStatusLine().getStatusCode())){
            throw new HttpException(rsp.getStatusLine().getStatusCode());
        }

        return null;
    }

    /**
     * 将entity转为String， 通过Gzip压缩
     * @param stream
     * @return
     * @throws IOException
     */
    public static String loadStringFromGzipStream(InputStream stream) throws IOException{
        GZIPInputStream gzin = new GZIPInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buff = new byte[1024];
        int size = buff.length;
        while((len = gzin.read(buff, 0, size)) > 0){
            baos.write(buff, 0, len);
        }
        baos.flush();
        gzin.close();
        String rt = baos.toString("utf-8");
        baos.close();
        return rt;
    }

    /**
     * 往http请求加入报头
     * @param req
     * @param headers
     */
    public static void addHeaders(HttpRequest req, HashMap<String, String> headers){
        try{
            if (headers == null || req == null){
                return;
            }
            Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String value = entry.getValue();
                req.addHeader(key, value);
            }
        }catch (Throwable e){

        }
    }

    /**
     * 获取UserAgent
     * @return
     */
    public static String getUserAgent(){
        if (userAgent == null) {

            try {

                StringBuilder sb = new StringBuilder(256);
                sb.append("Mozilla/5.0 (Linux; U; Android ");
                sb.append(Build.VERSION.RELEASE);
                sb.append("; ");
                Locale locale = Locale.getDefault();
                sb.append(String.format("%s-%s", locale.getLanguage(), locale.getCountry()).toLowerCase());
                sb.append("; ");
                sb.append(Build.MODEL);
                sb.append(" Build/");
                sb.append(Build.ID);
                sb.append(") AppleWebkit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

                userAgent = sb.toString();

            } catch (Throwable e) {
                e.printStackTrace();
                return "";
            }

        }
        return userAgent;
    }


    /**
     * 创建http请求参数
     */
    public static HttpParams createHttpParams(Context context, int timeout){
        BasicHttpParams params = new BasicHttpParams();
        //设置http超时
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        //设置socket超时
        HttpConnectionParams.setSoTimeout(params, timeout);
        //设置处理自动重定向
        HttpClientParams.setRedirecting(params, true);
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);

        return params;
    }

}
