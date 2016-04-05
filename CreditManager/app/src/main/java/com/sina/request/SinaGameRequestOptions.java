package com.sina.request;

import com.sina.engine.base.request.options.RequestOptions;

public class SinaGameRequestOptions extends RequestOptions {
    public SinaGameRequestOptions() {
        super();
        setCid("0");
        setPartner_id("10001");
        setVersionCode("15");
        setVersionName("5.0");
        setSn("1020981062");
        setKeyStr("0fe1ed9affcdd242451d9883f506a3dd");
        setIsEncrypt(true);
    }
}
