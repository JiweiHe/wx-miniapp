package com.jiuhuar.miniapp.service;

import com.jiuhuar.miniapp.exception.WxMiniException;

/**
 * Created by mike on 17/5/11.
 */
public interface WxMiniAppService {

    /**
     * 获取accessToken
     *
     * @return
     */
    String getAccessToken() throws WxMiniException;

    /**
     * 获取accessToken 强制刷新
     *
     * @param force
     * @return
     */
    String getAccessToken(boolean force) throws WxMiniException;

    /**
     * 获取小程序码
     *
     * @param path
     * @param width
     * @return
     */
    byte[] getWxacode(String path, Integer width) throws WxMiniException;

    /**
     * 创建小程序二维码
     *
     * @return
     */
    byte[] createWxaqrcode(String path, Integer width) throws WxMiniException;
}
