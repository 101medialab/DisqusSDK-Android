package com.hkm.disqus.api;

import com.google.gson.annotations.SerializedName;
import com.hkm.disqus.api.model.threads.Thread;

import java.io.Serializable;

/**
 * Created by hayton on 8/2/2018.
 */

public class ThreadDetails implements Serializable {

    private static final long serialVersionUID = 7885542230874422050L;

    @SerializedName("code")
    private int code;
    @SerializedName("response")
    private Thread threadResponse;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Thread getThreadResponse() {
        return threadResponse;
    }

    public void setThreadResponse(Thread threadResponse) {
        this.threadResponse = threadResponse;
    }
}
