package com.jiuhuar.miniapp.request;

import com.jiuhuar.miniapp.exception.WxMiniException;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by mike on 17/5/12.
 */
public interface RequestExecutor<T> {

    T execute(String url, OkHttpClient client, RequestBody body) throws WxMiniException;
}
