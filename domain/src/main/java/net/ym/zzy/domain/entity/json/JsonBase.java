package net.ym.zzy.domain.entity.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zengzheying on 15/3/28.
 */
public class JsonBase<T> implements Base<T>, Serializable {
    @SerializedName("c")
    private int mCode = -1;

    @SerializedName("d")
    private T mData;

    @SerializedName("msg")
    private String mMsg;

    @Override
    public T getData() {
        return mData;
    }

    @Override
    public void setData(T data) {
        mData = data;
    }

    @Override
    public int getCode() {
        return mCode;
    }

    @Override
    public void setCode(int code) {
        mCode = code;
    }

    @Override
    public String getMsg() {
        return mMsg;
    }

    @Override
    public void setMsg(String msg) {
        mMsg = msg;
    }
}
