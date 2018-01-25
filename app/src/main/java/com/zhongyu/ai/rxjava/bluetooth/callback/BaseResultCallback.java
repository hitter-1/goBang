package com.zhongyu.ai.rxjava.bluetooth.callback;

/**
 * Created by zhongyu on 1/16/2018.
 */

public interface BaseResultCallback<D> {
    /**
     * 成功拿到数据
     *
     * @param data 回传的数据
     */
    void onSuccess(D data);

    /**
     * 操作失败
     *
     * @param msg 失败的返回的异常信息
     */
    void onFail(String msg);
}
