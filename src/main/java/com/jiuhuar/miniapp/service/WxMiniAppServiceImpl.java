package com.jiuhuar.miniapp.service;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuhuar.miniapp.entity.WxAccessToken;
import com.jiuhuar.miniapp.entity.WxError;
import com.jiuhuar.miniapp.exception.WxMiniException;
import com.jiuhuar.miniapp.request.GetRequestExecutor;
import com.jiuhuar.miniapp.request.ImageFileRequestExecutor;
import com.jiuhuar.miniapp.request.PostRequestExecutor;
import com.jiuhuar.miniapp.request.RequestExecutor;
import com.jiuhuar.miniapp.storage.WxMiniAppConfigStorage;
import com.jiuhuar.miniapp.support.ObjectMapperUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by mike on 17/5/11.
 */
public class WxMiniAppServiceImpl implements WxMiniAppService {
    private static final Logger logger = LoggerFactory.getLogger(WxMiniAppServiceImpl.class);

    private static PostRequestExecutor postRequestExecutor = new PostRequestExecutor();
    private static GetRequestExecutor getRequestExecutor = new GetRequestExecutor();
    private static ImageFileRequestExecutor imageFileRequestExecutor = new ImageFileRequestExecutor();
    private ObjectMapper objectMapper;
    private OkHttpClient httpClient;
    private WxMiniAppConfigStorage storage;
    private static final Object accessTokenLock = new Object();
    private static final int MAX_RETRY_TIME = 3;

    public WxMiniAppServiceImpl() {
        this.objectMapper = ObjectMapperUtil.getObjectMapper();
    }

    public String getAccessToken() throws WxMiniException {
        return getAccessToken(false);
    }

    public String getAccessToken(boolean force) throws WxMiniException {
        if (force) {
            storage.expireAccessToken();
        }
        if (storage.isAccessTokenExpired()) {
            synchronized (accessTokenLock) {
                if (storage.isAccessTokenExpired()) {
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
                    WxAccessToken wxAccessToken = extractResult(getRequestExecutor.execute(format(url, storage.getAppId(), storage.getSecret()), httpClient, null),
                            WxAccessToken.class);
                    storage.updateAccessToken(wxAccessToken);
                }
            }
        }
        return storage.getAccessToken();
    }

    public byte[] getWxacode(String path, Integer width) throws WxMiniException {
        if (width == null) width = 430;
        String url = "https://api.weixin.qq.com/wxa/getwxacode";
        Map<String, Object> body = new HashMap<>();
        body.put("path", path);
        body.put("width", width);
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(body));
            return execute(url, imageFileRequestExecutor, requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public byte[] createWxaqrcode(String path, Integer width) throws WxMiniException {
        if (width == null) width = 430;
        String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode";
        Map<String, Object> body = new HashMap<>();
        body.put("path", path);
        body.put("width", width);
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(body));
            return execute(url, imageFileRequestExecutor, requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private synchronized <T> T executeInternal(String url, RequestExecutor<T> executor, RequestBody requestBody) throws WxMiniException {
        if (url.contains("access_token=")) {
            throw new IllegalArgumentException("uri参数中不允许有access_token: " + url);
        }
        String accessToken = getAccessToken(false);
        url += url.contains("?") ? "&access_token=" + accessToken : "?access_token=" + accessToken;
        try {
            return executor.execute(url, httpClient, requestBody);
        } catch (WxMiniException e) {
            WxError error = e.getWxError();
            // 强制设置wxAppConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
            if (error.getErrcode() == 42001 || error.getErrcode() == 40001) {
                storage.expireAccessToken();
            }
            if (error.getErrcode() != 0) {
                throw new WxMiniException(error);
            }
            return null;
        }
    }

    private synchronized <T> T execute(String url, RequestExecutor<T> executor, RequestBody requestBody) throws WxMiniException {
        int retryTimes = 0;

        do {
            try {
                return executeInternal(url, executor, requestBody);
            } catch (WxMiniException e) {
                WxError error = e.getWxError();
                if (error.getErrcode() != 0) {
                    retryTimes++;
                    logger.error("第{}次重试, error:{}", retryTimes, error.toString());
                }
            }
        } while (retryTimes < MAX_RETRY_TIME);
        throw new RuntimeException("微信服务器异常,超出重试次数");
    }

    private <T> T extractResult(String responseText, Class<T> clazz) throws WxMiniException {
        try {
            return objectMapper.readValue(responseText, clazz);
        } catch (IOException e) {
            throw new WxMiniException(responseText);
        }
    }

    public void setStorage(WxMiniAppConfigStorage storage) {
        this.storage = storage;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
