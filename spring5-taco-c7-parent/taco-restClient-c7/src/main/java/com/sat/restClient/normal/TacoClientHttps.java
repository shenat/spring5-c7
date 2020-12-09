package com.sat.restClient.normal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TacoClientHttps {
	private MyResponseErrorHandler myResponseErrorHandler = new MyResponseErrorHandler();
	private ClientHttpsRequestFactory clientHttpsRequestFactory = new ClientHttpsRequestFactory();
	private static final URI uri = URI.create("HTTPS://***");
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private RestTemplate restTemplate;
	
	{
		if("https".equals(uri.getScheme())){
			//https
			clientHttpsRequestFactory.setConnectTimeout(1000);//单位毫秒
			clientHttpsRequestFactory.setReadTimeout(1000);//毫秒
			//使用拦截器打印请求和响应内容的时候需要两次读取内容，需要使用到BufferingClientHttpRequestFactory
//			restTemplate = new RestTemplate(clientHttpsRequestFactory);
			restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpsRequestFactory));
		}else{
			//http
			SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
			clientHttpRequestFactory.setConnectTimeout(1000);//单位毫秒
			clientHttpRequestFactory.setReadTimeout(1000);//毫秒
			//使用拦截器打印请求和响应内容的时候需要两次读取内容，需要使用到BufferingClientHttpRequestFactory
//			restTemplate = new RestTemplate(clientHttpRequestFactory);
			restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpsRequestFactory));
		}
		//添加自定以错误处理器
		restTemplate.setErrorHandler(myResponseErrorHandler);
		//添加自定义拦截器，打印请求和响应内容
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LoggingClientHttpRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
		
	}
	
	//调用restTempalte中的操作
	/**
	if(body != null){
		requestEntity = new HttpEntity<Object>(body, headers);
		try {
			logger.info(appId + "调用" + interfaceName + "发送内容:" + mapper.writeValueAsString(body));
		} catch (Exception e) {
			logger.info(appId + "调用" + interfaceName + "发送内容转为json表示错误，重新打印:" + body.toString());
		}
	}else{
		requestEntity = new HttpEntity<Object>(headers);
		logger.info(appId + "调用" + interfaceName + "发送开始发送无body请求");
	}
	
	//如果该地址中包含特殊字符，需要用URLEncoder.encode("特殊字符", "utf-8")编码一下
//	URI uri = URI.create("https://apisit.dir.saicgmac.com:8443/mbas/v1.0/contract/queryContract");
//	URI uri = URI.create(API_INTRANET + WHETHER_CONTRACT );
	
	//这边如果返回结果不能映射为返回实体类会报错，在请求头中指定accept指定如何转换，如json
	//apigateway目前返回一直都是json，但是也不排除后续后返回其他的
	response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, clazz);
	
	HttpStatus httpStatus = response.getStatusCode();
	responseBody = response.getBody();
	String responseBodyString = null;
	if(responseBody != null){
		//所以要求返回泛型实体类T需要重写ToString方法
		responseBodyString = responseBody.toString();
	}else{
		responseBodyString = "";
	}
	
	if(whetherDispose500And401){
		//401错误
		if(HttpStatus.UNAUTHORIZED == httpStatus){
			logger.error(appId + "调用" + interfaceName + "接口返回401,授权错误,返回内容：" + responseBody.toString());
			return responseBody;
		}
		//500错误，由于自定义异常处理器放过了500错误，所以这边可以捕获到
		if(HttpStatus.INTERNAL_SERVER_ERROR == httpStatus){
			logger.error(appId + "调用" + interfaceName + "接口返回500,服务器错误,返回内容：" + responseBody.toString());
			return responseBody;
		}
	}
	logger.error(appId + "调用" + interfaceName + "返回:" + responseBodyString);
	
	return responseBody;
	**/
	
	
	
	
	
	
	
	//自定义异常处理器，以解决非200程序停止抛异常处理，这里对500和401的特殊处理了
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
	
	//自定义拦截器，以输出请求和响应原内容,这里有个疑问，如果请求体中是非String类型如何打印出来呢，还是不打印？
	class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	    @Override
	    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
	        tranceRequest(request, body);
	        ClientHttpResponse response = execution.execute(request, body);
	        traceResponse(response);
	        return response;
	    }

	    private void tranceRequest(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
	    	System.out.println("=========================== request begin ===========================");
	    	System.out.println("uri : {" + request.getURI() + "}");
	    	System.out.println("method : {" + request.getMethod() + "}");
	    	System.out.println("headers : {" + request.getHeaders() + "}");
	    	System.out.println(" request body : {" + new String(body, "utf-8") + "}");
	    	System.out.println("============================ request end ============================");
	    }

	    private void traceResponse(ClientHttpResponse httpResponse) throws IOException {
	        StringBuilder inputStringBuilder = new StringBuilder();
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getBody(), "UTF-8"));
	        String line = bufferedReader.readLine();
	        while (line != null) {
	            inputStringBuilder.append(line);
	            inputStringBuilder.append('\n');
	            line = bufferedReader.readLine();
	        }
	        System.out.println("============================ response begin ============================");
	        System.out.println("Status code  : {"+httpResponse.getStatusCode()+"}" );
	        System.out.println("Status text  : {"+httpResponse.getStatusText()+"}");
	        System.out.println("Headers      : {"+httpResponse.getHeaders()+"}");
	        System.out.println("Response body: {"+inputStringBuilder.toString()+"}");
	        System.out.println("============================= response end =============================");
	    }

	}
}
