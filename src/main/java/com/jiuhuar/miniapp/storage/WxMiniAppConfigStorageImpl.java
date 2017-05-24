package com.jiuhuar.miniapp.storage;

import com.jiuhuar.miniapp.entity.WxAccessToken;

/**
 * 微信小程序相关配置存储类
 */
public class WxMiniAppConfigStorageImpl implements WxMiniAppConfigStorage {
    protected volatile String appId;
    protected volatile String secret;
    protected volatile String partnerId;
    protected volatile String partnerKey;
    protected volatile String token;
    protected volatile String accessToken;
    protected volatile String aesKey;
    protected volatile long expiresTime;
    protected volatile long jsapiTicketExpiresTime;
    protected volatile String jsapiTicket;

    public WxMiniAppConfigStorageImpl() {
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }

    public void expireAccessToken() {
        this.expiresTime = 0;
    }

    public synchronized void updateAccessToken(WxAccessToken accessToken) {
        this.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    /**
     * expiresInSeconds的单位是秒,表示为多少秒后过期
     *
     * @param accessToken
     * @param expiresInSeconds
     */
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }

    public String getJsapiTicket() {
        return this.jsapiTicket;
    }

    public boolean isJsapiTicketExpired() {
        return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
    }

    public void expireJsapiTicket() {
        this.jsapiTicketExpiresTime = 0L;
    }

    /**
     * expiresInSeconds的单位是秒,表示为多少秒后过期
     *
     * @param jsapiTicket
     * @param expiresInSeconds
     */
    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.jsapiTicket = jsapiTicket;
        this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public String getPartnerKey() {
        return this.partnerKey;
    }

    public String getToken() {
        return this.token;
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public long getExpiresTime() {
        return this.expiresTime;
    }

    public long getJsapiTicketExpiresTime() {
        return this.jsapiTicketExpiresTime;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }
}
