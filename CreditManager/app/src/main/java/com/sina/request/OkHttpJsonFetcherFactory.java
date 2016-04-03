package com.sina.request;

import com.sina.engine.base.request.client.JsonFetcher;
import com.sina.engine.base.request.client.JsonFetcherFactory;

/**
 * 
 * @author liu_chonghui
 *
 */
public class OkHttpJsonFetcherFactory implements JsonFetcherFactory {

	@Override
	public JsonFetcher newJsonFetcher() {
		return new OkHttpJsonFetcher();
	}

}
