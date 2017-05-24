package com.jiuhuar.miniapp.storage;

import com.jiuhuar.miniapp.entity.WxAccessToken;

/**
 * Created by mike on 17/5/11.
 */
public interface WxMiniAppConfigStorage {

    String getAccessToken();

    boolean isAccessTokenExpired();

    void expireAccessToken();

    void updateAccessToken(WxAccessToken accessToken);

    void updateAccessToken(String accessToken, int expiresInSeconds);

    String getJsapiTicket();

    boolean isJsapiTicketExpired();

    void expireJsapiTicket();

    void updateJsapiTicket(String jsapiTicket, int expiresInSeconds);

    String getAppId();

    String getSecret();

    String getPartnerId();

    String getPartnerKey();

    String getToken();

    String getAesKey();

    long getExpiresTime();
}
