package com.sat.restClient.traverson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

//本质上Traverson也是调用的接口RestOperations中方法取访问也就是RestTemplate
public class TacoClientTraversonHttps {
	private MyResponseErrorHandler myResponseErrorHandler = new MyResponseErrorHandler();
	private ClientHttpsRequestFactory clientHttpsRequestFactory = new ClientHttpsRequestFactory();
	private static final URI uri = URI.create("HTTPS://***");
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private RestTemplate restTemplate;
	private Traverson traverson;
	{
		if("https".equals(uri.getScheme())){
			//https
			clientHttpsRequestFactory.setConnectTimeout(1000);//单位毫秒
			clientHttpsRequestFactory.setReadTimeout(1000);//毫秒
			restTemplate = new RestTemplate(clientHttpsRequestFactory);
			//参考new Traverson的构造过程：相比TacoClientHttps.java多了这个
			restTemplate.setMessageConverters(Traverson.getDefaultMessageConverters(MediaTypes.HAL_JSON));
		}else{
			//http
			SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
			clientHttpRequestFactory.setConnectTimeout(1000);//单位毫秒
			clientHttpRequestFactory.setReadTimeout(1000);//毫秒
			restTemplate = new RestTemplate(clientHttpRequestFactory);
			//参考new Traverson的构造过程：相比TacoClientHttps.java多了这个
			restTemplate.setMessageConverters(Traverson.getDefaultMessageConverters(MediaTypes.HAL_JSON));
		}
		restTemplate.setErrorHandler(myResponseErrorHandler);
		traverson = new Traverson(uri,MediaTypes.HAL_JSON);
		traverson.setRestOperations(restTemplate);//重新设置traverson的restTemplate
		
	}
	//traverson的操作：
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//自定义异常处理器，已解决非200停止处理，这里对500和401的特殊处理了
	//500在apigateway中是系统错误，401是授权码错误
	class MyResponseErrorHandler extends DefaultResponseErrorHandler{

		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			// TODO Auto-generated method stub
			if(HttpStatus.INTERNAL_SERVER_ERROR == response.getStatusCode()
					|| HttpStatus.UNAUTHORIZED == response.getStatusCode()){
				return true;
			}
			return super.hasError(response);
		}
		
	}
	//用于访问https
	class ClientHttpsRequestFactory extends SimpleClientHttpRequestFactory{
		@Override
		protected void prepareConnection(HttpURLConnection connection,
				String httpMethod) throws IOException {
			if (!(connection instanceof HttpsURLConnection)) {
                throw new RuntimeException("An instance of HttpsURLConnection is expected");
            }
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
			try {
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, new TrustManager[] { new MyX509TrustManager() }, new java.security.SecureRandom());
				httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
				httpsConnection.setHostnameVerifier(new MyHostNameVerifier());
			} catch (Exception e) {
				throw new RuntimeException("httpsConnection set error",e);
			}
            super.prepareConnection(httpsConnection, httpMethod);
		}
	}
	
	
	class MyX509TrustManager implements X509TrustManager {

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
		 */
		@Override
		public void checkClientTrusted(
				X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
		 */
		@Override
		public void checkServerTrusted(
				X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
		 */
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class MyHostNameVerifier implements HostnameVerifier{

		/* (non-Javadoc)
		 * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String, javax.net.ssl.SSLSession)
		 */
		@Override
		public boolean verify(String paramString, SSLSession paramSSLSession) {
			System.out.println("WARNING: Hostname is not matched for cert.");
			return true;
		}

	}
}
