package com.sina.engine.base.request.utils;

import android.content.Context;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * HTTPS协议  HTTPCLIENT 框架使用
 * SSLSocket扩展Socket并提供使用SSL或TLS协议的安全套接字
 */
public class TrustCertainHostNameFactory extends SSLSocketFactory {

	private static TrustCertainHostNameFactory mInstance;

	public TrustCertainHostNameFactory(KeyStore truststore) throws NoSuchAlgorithmException,
			KeyManagementException, KeyStoreException, UnrecoverableKeyException {
		super(truststore);
	}

	public static TrustCertainHostNameFactory getDefault(Context context, int cerResId) {
		KeyStore keystore = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream in;
//			in = context.getAssets().open("wan68cert.cer");
			in = context.getResources().openRawResource(cerResId);
			Certificate ca = cf.generateCertificate(in);

			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(null, null);
			keystore.setCertificateEntry("ca", ca);

			if (null == mInstance) {
				mInstance = new TrustCertainHostNameFactory(keystore);
			}
			mInstance.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {

		}
		return mInstance;
	}

	@Override
	public Socket createSocket() throws IOException {
		return super.createSocket();
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException {
		return super.createSocket(socket, host, port, autoClose);
	}

}
