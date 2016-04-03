package com.sina.request;

import com.sina.engine.base.enums.HttpMethodEnum;
import com.sina.engine.base.request.client.DefaultJsonFetcherFactory;
import com.sina.engine.base.request.client.JsonFetcher;
import com.sina.engine.base.request.model.TaskModel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author liu_chonghui
 */
@SuppressWarnings("serial")
public class OkHttpJsonFetcher implements JsonFetcher {

    @Override
    public String get(String url, TaskModel taskModel) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        if (taskModel != null && HttpMethodEnum.HTTPS == taskModel.getRequestOptions().getHttpRequestMethod()) {
            client.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            client.setSslSocketFactory(HttpsUtils.getSslSocketFactory());
        }
        String retJson = null;
        try {
            Response response;
            response = client.newCall(request).execute();
            retJson = response.body().string();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            if (taskModel != null) {
                taskModel.getRequestOptions().setConnectedTimeOut(true);
            }
            retJson = null;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (taskModel != null) {
                taskModel.getRequestOptions().setConnectedTimeOut(true);
            }
            retJson = null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            retJson = null;
        } catch (IOException e) {
            e.printStackTrace();
            retJson = null;
        } catch (Exception e) {
            e.printStackTrace();
            retJson = null;
        }
        return retJson;
    }

    @Override
    public String post(String postUrl, List<BasicNameValuePair> values,
                       TaskModel taskModel) {
        return new DefaultJsonFetcherFactory().newJsonFetcher().post(postUrl,
                values, taskModel);
    }

    @Override
    public String post(String postUrl, List<BasicNameValuePair> filelist,
                       List<BasicNameValuePair> values, TaskModel taskModel) {
        return new DefaultJsonFetcherFactory().newJsonFetcher().post(postUrl,
                filelist, values, taskModel);
    }

}
