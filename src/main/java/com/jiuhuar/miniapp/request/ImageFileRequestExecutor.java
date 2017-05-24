package com.jiuhuar.miniapp.request;

import java.io.IOException;

import com.jiuhuar.miniapp.entity.WxError;
import com.jiuhuar.miniapp.exception.WxMiniException;

import okhttp3.*;

/**
 * Created by mike on 17/5/12.
 */
public class ImageFileRequestExecutor implements RequestExecutor<byte[]> {

    @Override
    public byte[] execute(String url, OkHttpClient client, RequestBody body) throws WxMiniException {
        byte[] bytes;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.isSuccessful()) {
            try {
                ResponseBody responseBody = response.body();
                MediaType mediaType = responseBody.contentType();
                String type = mediaType.type();
                if (type.equals("image")) {
                    bytes = responseBody.bytes();
                } else {
                    String responseText = responseBody.string();
                    WxError wxError = WxError.toError(responseText);
                    throw new WxMiniException(wxError);
                }
                return bytes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException(response.toString());
        }
    }
}
