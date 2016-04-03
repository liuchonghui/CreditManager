package com.sina.engine.base.request.client;

import com.sina.engine.base.enums.HttpMethodEnum;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.utils.TrustCertainHostNameFactory;
import com.sina.engine.base.utils.MimeTypeUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author liu_chonghui
 */
@SuppressWarnings("serial")
class DefaultJsonFetcher implements JsonFetcher {

    private final int TIME_OUT = 15000;
    private static ClientConnectionManager manager;
    @Override
    public String get(String url, TaskModel taskModel) {
        String jsonStr = null;
        // 设置超时
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, TIME_OUT);

        HttpClient httpClient = getHttpClient(taskModel, params);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null && statusLine.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                jsonStr = EntityUtils.toString(entity, "UTF-8");

            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            if (taskModel != null) {
            	taskModel.getRequestOptions().setConnectedTimeOut(true);
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (taskModel != null) {
            	taskModel.getRequestOptions().setConnectedTimeOut(true);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return jsonStr;
    }

    @Override
    public String post(String postUrl, List<BasicNameValuePair> values, TaskModel taskModel) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, TIME_OUT);
        HttpClient client = getHttpClient(taskModel, params);
        // 创建一个请求
        HttpPost post = new HttpPost(postUrl);
        try {
            post.setEntity(new UrlEncodedFormEntity(values, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null
                    && response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                byte[] data = getStreamBytes(is);
                String returnData = new String(data);
                return returnData;
            } else {
                return null;
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            taskModel.getRequestOptions().setConnectedTimeOut(true);
            return null;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            taskModel.getRequestOptions().setConnectedTimeOut(true);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String post(String postUrl, List<BasicNameValuePair> filelist, List<BasicNameValuePair> values, TaskModel taskModel) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, TIME_OUT);
        HttpClient client = getHttpClient(taskModel, params);
        MultipartEntity entity = new MultipartEntity();
        for (BasicNameValuePair valuePaire : filelist) {
            File file = new File(valuePaire.getValue());
            if (file == null || !file.exists()) {
                continue;
            }
            String mimeType = null;
            String path = file.getAbsolutePath();
            String extension = MimeTypeUtil.getFileExtensionFromUrl(path);
            if (extension != null) {
                mimeType = MimeTypeUtil.mimeTypeFromExtension(extension);
            }
            if (mimeType != null && mimeType.length() > 0) {
                entity.addPart(valuePaire.getName(), new FileBody(file,
                        mimeType));
            } else {
                entity.addPart(valuePaire.getName(), new FileBody(file));
            }
        }
        for (BasicNameValuePair valuePaire : values) {
            try {
                entity.addPart(valuePaire.getName(),
                        new StringBody(valuePaire.getValue(), Charset.forName("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // 创建一个请求
        HttpPost post = new HttpPost(postUrl);
        try {
            post.setEntity(entity);// new UrlEncodedFormEntity(values,
            // HTTP.UTF_8)
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null
                    && response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                byte[] data = getStreamBytes(is);
                String returnData = new String(data);
                return returnData;
            } else {
                return null;
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            if (taskModel != null) {
            	taskModel.getRequestOptions().setConnectedTimeOut(true);
            }
            return null;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (taskModel != null) {
            	taskModel.getRequestOptions().setConnectedTimeOut(true);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    byte[] getStreamBytes(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] result = baos.toByteArray();
        is.close();
        baos.close();
        return result;
    }

    private HttpClient getHttpClient(TaskModel taskModel, HttpParams params){
        HttpClient httpClient;
        if (taskModel != null && HttpMethodEnum.HTTPS == taskModel.getRequestOptions().getHttpRequestMethod()) {
            if(manager==null) {
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https",  TrustCertainHostNameFactory.getDefault(
                        EngineManager.getInstance().getContext(),
                        EngineManager.getInstance().getEngineConfig().getCerResourceId()), 443));//
                manager = new ThreadSafeClientConnManager(params, registry);
            }
            httpClient = new DefaultHttpClient(manager, params);
        }
        else{
            httpClient = new DefaultHttpClient(params);
        }
        return httpClient;
    }
}
