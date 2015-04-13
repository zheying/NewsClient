package net.ym.zzy.domain.entity.json;

/**
 * Created by zengzheying on 15/3/28.
 */
public interface Base<T> {

    public T getData();

    public void setData(T data);

    public int getCode();

    public void setCode(int code);

    public String getMsg();

    public void setMsg(String msg);
}
