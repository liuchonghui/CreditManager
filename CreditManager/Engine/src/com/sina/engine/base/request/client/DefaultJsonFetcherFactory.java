package com.sina.engine.base.request.client;

/**
 *
 * @author liu_chonghui
 *
 */
public class DefaultJsonFetcherFactory implements JsonFetcherFactory {

    @Override
    public JsonFetcher newJsonFetcher() {
        return new DefaultJsonFetcher();
    }
}
