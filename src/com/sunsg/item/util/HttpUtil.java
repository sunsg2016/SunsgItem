/**
 * 
 */
package com.sunsg.item.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

/**
 * @author 尚传亮
 * @email sclmhy@gmail.com
 * 
 * @描述
 */
public class HttpUtil {
	private HttpUtil() {}
	
	/**
	 * Http Get 同步操作，直接返回相应结果的字符串
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String httpGet(String uri, HttpParams params, boolean useGzip) throws IllegalStateException, IOException {
		HttpGet httpGet = new HttpGet(uri);
		if(useGzip) httpGet.addHeader("Accept-Encoding", "gzip");
		HttpResponse httpResponse = null;
		if(HttpUtil.isHttps(uri)) {
			HttpClient httpClient = HttpUtil.getSSLHttpClient();
			httpResponse = httpClient.execute(httpGet);
		} else {
			httpResponse = new DefaultHttpClient(params).execute(httpGet);
		}
		if(httpResponse == null) return null;
		return HttpUtil.httpEntityToString(httpResponse.getEntity());
	}
	
	/**
	 * 将HttpEntity传化成String
	 * </br>
	 * 区分Gzip编码和其他编码
	 */
	public static String httpEntityToString(HttpEntity entity) throws IllegalStateException, IOException {
		String result = null;
		if(entity != null) {
			if(entity.getContentEncoding() != null) {
				if("gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())){
					result =  HttpUtil.uncompressGzipInputStream(entity.getContent());
				}
	        } else {
	            result = EntityUtils.toString(entity, "UTF-8");
	        }
		}
		return result;
	}

	/**
	 * 将数据流转化成String
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 将gzip编码的数据流转化成Strng
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String uncompressGzipInputStream(InputStream is) throws IOException {
		StringBuilder value = new StringBuilder();

		GZIPInputStream gzipIn = null;
		InputStreamReader inputReader = null;
		BufferedReader reader = null;

		try {
			gzipIn = new GZIPInputStream(is);
			inputReader = new InputStreamReader(gzipIn, "UTF-8");
			reader = new BufferedReader(inputReader);

			String line = "";
			while ((line = reader.readLine()) != null) {
				value.append(line + "\n");
			}
		} finally {
			try {
				if (gzipIn != null) {
					gzipIn.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return value.toString();
	}
	
	public static boolean isHttps(String uri) {
		if(!Tools.isEmpty(uri)) return uri.startsWith("https");
		return false;
	}
	
	/**
	 * 获得SSLHttpClient
	 * <br/>
	 * 默认端口 443
	 * @return
	 */
	public static HttpClient getSSLHttpClient() {
		return getSSLHttpClient(443);
	}
	
	/**
	 * 获得SSLHttpClient
	 * @return
	 */
	public static HttpClient getSSLHttpClient(int port) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new EasySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", sf, port));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
	
	private static class EasySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);

		public EasySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
