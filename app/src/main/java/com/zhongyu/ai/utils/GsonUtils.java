package com.zhongyu.ai.utils;

import com.google.gson.Gson;

/**
 * Created by zhongyu on 1/22/2018.
 */

public class GsonUtils {
    private volatile static GsonUtils gsonUtils = new GsonUtils();
    Gson gson = new Gson();
    public GsonUtils() {
    }

    public static GsonUtils getInstance() {
        if(gsonUtils == null) {
            synchronized (GsonUtils.class) {
                if(gsonUtils == null) {
                    gsonUtils = new GsonUtils();
                }
            }
        }
        return gsonUtils;
    }

    public <T> T fromJson(String data, Class<T> classOfT) {
        return gson.fromJson(data, classOfT);
    }

    public String toJson(Object o) {
        return gson.toJson(o);
    }
}
