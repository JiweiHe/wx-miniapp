package com.jiuhuar.miniapp.exception;

import com.jiuhuar.miniapp.entity.WxError;

/**
 * Created by mike on 17/5/11.
 */
public class WxMiniException extends Exception {

    private WxError wxError;

    public WxMiniException(Throwable e) {
        super(e);
    }

    public WxMiniException(WxError wxError) {
        this.wxError = wxError;
    }

    public WxMiniException(String desc) {
        super(desc);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public WxError getWxError() {
        return wxError;
    }
}
