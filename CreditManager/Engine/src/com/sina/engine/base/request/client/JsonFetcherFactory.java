package com.sina.engine.base.request.client;

/**
 *
 * @author liu_chonghui
 *
 */
public interface JsonFetcherFactory {

    /**
     * 生成JsonFetcher
     * @return
     */
    JsonFetcher newJsonFetcher();
}
