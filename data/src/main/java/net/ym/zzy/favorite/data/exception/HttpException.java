package net.ym.zzy.favorite.data.exception;

/**
 * Created by zengzheying on 15/3/28.
 */
public class HttpException extends Exception {
    private int statusCode;

    public HttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    public static boolean isNetworkErrorCode(int statusCode){
        return statusCode >= 400;
    }

    public void makeToast(){

    }
}
