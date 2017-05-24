package com.jiuhuar.miniapp.entity;

import java.io.IOException;
import java.io.Serializable;

import com.jiuhuar.miniapp.support.ObjectMapperUtil;

/**
 * Created by mike on 17/5/11.
 */
public class WxError implements Serializable {
    private int errcode;
    private String errmsg;

    public WxError() {
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String toString() {
        return "{\"errcode:" + this.errcode + ", \"errmsg:" + this.errmsg + "\"}";
    }

    public static WxError toError(String errorText) throws IOException {
        return ObjectMapperUtil.getObjectMapper().readValue(errorText, WxError.class);
    }
}
