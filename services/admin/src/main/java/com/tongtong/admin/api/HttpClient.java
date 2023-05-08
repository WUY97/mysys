package com.tongtong.admin.api;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private PoolingHttpClientConnectionManager cm;

    public HttpClient(int connPoolSize) {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(connPoolSize);
        cm.setDefaultMaxPerRoute(connPoolSize);
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private String formatUrlParams(Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder();
        if (params != null) {
            int paramsSize = params.keySet().size();
            if (paramsSize > 0) {
                int counter = 0;
                for (String paramKey : params.keySet()) {
                    String value = params.get(paramKey);
                    if (value == null) {
                        logger.warn("Key value is null for param {}", paramKey);
                        continue;
                    }
                    value = URLEncoder.encode(value, StandardCharsets.UTF_8);
                    urlBuilder.append(paramKey).append("=").append(value);
                    if (++counter < paramsSize) {
                        urlBuilder.append("&");
                    }
                }
            }
        }
        return urlBuilder.toString();
    }

    private URI buildUrl(String host, int port, String path, List<NameValuePair> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPort(port)
                .setPath(path);
        if (params != null && params.size() > 0) {
            uriBuilder.addParameters(params);
        }
        return uriBuilder.build();
    }

    public HttpClientResponse sendPost(String host, int port, String path, String authHeader,
                                       String contentType, Map<String, String> params) throws IOException {
        String body = formatUrlParams(params);
        logger.debug("Sending 'POST' request to host:{} path:{} ", host, path);
        logger.debug("Post parameters : {}", body);
        return sendPost(host, port, path, authHeader, contentType, body);
    }

    public HttpClientResponse sendPost(String host, int port, String path, String authHeader,
                                       String contentType, String body) throws IOException {
        return sendPostOrPut(host, port, path, authHeader, contentType, body, true);
    }

    public HttpClientResponse sendPut(String host, int port, String path, String authHeader,
                                      String contentType, String body) throws IOException {
        return sendPostOrPut(host, port, path, authHeader, contentType, body, false);
    }

    private HttpClientResponse sendPostOrPut(String host, int port, String path, String authHeader,
                                             String contentType, String body, boolean isPost) throws IOException {
        URI uri;
        try {
            uri = buildUrl(host, port, path, null);
        } catch (URISyntaxException e) {
            logger.error("Error building uri for host={}; path={};", host, path);
            throw new IOException(e);
        }
        HttpUriRequestBase httpRequest = isPost ? new HttpPost(uri) : new HttpPut(uri);
        if (authHeader != null) {
            httpRequest.setHeader("Authorization", authHeader);
        }
        httpRequest.setHeader("Accept-Language", "en-US,en;q=0.5");
        httpRequest.setHeader("Content-Type", contentType);
        //httpRequest.setHeader("Content-Length", String.valueOf(body.getBytes().length));

        StringEntity userEntity = new StringEntity(body);
        httpRequest.setEntity(userEntity);

        logger.debug("Sending POST request to URL {} ", uri);
        CloseableHttpResponse response;
        HttpClientResponse responseObject = new HttpClientResponse();
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        try {
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
            response = httpClient.execute(httpRequest);
            int statusCode = response.getCode();
            logger.info("Status code for request uri {} is {}", uri, statusCode);
            HttpEntity httpEntity = response.getEntity();
            String responseString = EntityUtils.toString(httpEntity);
            logger.info("Got response: {}", responseString.substring(0, Math.min(responseString.length(), 100)));
            responseObject.setStatus(statusCode);
            responseObject.setBody(responseString);
        } catch (ClientProtocolException cpe) {
            logger.error("Error calling uri={}; exception={}", uri, cpe.getMessage());
            throw new IOException(cpe);
        } catch (IOException ioe) {
            logger.error("Error calling uri={}; exception={}", uri, ioe.getMessage());
            throw ioe;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (responseObject.getStatus() != 200 && responseObject.getStatus() != 201
                && responseObject.getStatus() != 401 && responseObject.getStatus() != 403) {
            logger.error("Error calling uri={}; statusCode={}", uri, responseObject.getStatus());
            throw new IOException("Error calling uri " + uri);
        }
        return responseObject;
    }

    public HttpClientResponse sendGet(String host, int port, String path, String authHeader,
                                      String queryString) throws IOException {
        List<NameValuePair> params = null;
        if (queryString != null && queryString.length() > 0) {
            params = new ArrayList<>();
            String[] queryParts = queryString.split("&");
            for (String queryPart : queryParts) {
                String[] nameValue = queryPart.split("=");
                String name = URLDecoder.decode(nameValue[0], StandardCharsets.UTF_8);
                String value = nameValue.length > 1 ? URLDecoder.decode(nameValue[1], StandardCharsets.UTF_8) : "";
                params.add(new BasicNameValuePair(name, value));
            }
        }
        return sendGet(host, port, path, authHeader, params);
    }

    public HttpClientResponse sendGet(String host, int port, String path, String authHeader,
                                      Map<String, String> params) throws IOException {
        List<NameValuePair> paramsList = new ArrayList<>();
        if (params != null) {
            for (String paramKey : params.keySet()) {
                String value = params.get(paramKey);
                if (value == null) {
                    logger.warn("Key value is null for param {}", paramKey);
                    continue;
                }
                paramsList.add(new BasicNameValuePair(paramKey, value));
            }
        }
        return sendGet(host, port, path, authHeader, paramsList);
    }

    private HttpClientResponse sendGet(String host, int port, String path, String authHeader,
                                       List<NameValuePair> params) throws IOException {
        if (params == null) {
            params = new ArrayList<>();
        }
        URI uri;
        try {
            uri = buildUrl(host, port, path, params);
        } catch (URISyntaxException e) {
            logger.error("Error building uri for host={}; path={}; context={}", host, path, params);
            throw new IOException(e);
        }

        HttpGet httpGet = new HttpGet(uri);
        if (authHeader != null) {
            httpGet.setHeader("Authorization", authHeader);
        }
        logger.debug("Sending GET request to URL : " + uri);

        CloseableHttpResponse response;
        HttpClientResponse responseObject = new HttpClientResponse();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getCode();
            responseObject.setStatus(statusCode);
            if (statusCode >= 200 && statusCode <= 206) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    String responseString = EntityUtils.toString(httpEntity);
                    logger.info("Got response: {}", responseString.substring(0, Math.min(responseString.length(), 100)));
                    responseObject.setBody(responseString);
                } else {
                    logger.warn("No response body received from service");
                }
            } else {
                if (responseObject.getStatus() == 401 || responseObject.getStatus() == 403) {
                    logger.warn("User forbidden or unauthorized. uri={}; statusCode={}",
                            uri, responseObject.getStatus());
                } else {
                    logger.error("Error calling uri={}; statusCode={}", uri, responseObject.getStatus());
                    throw new IOException("Error calling uri " + uri);
                }
            }
        } catch (IOException ioe) {
            logger.error("Error calling uri={}; exception={}", uri, ioe.getMessage());
            throw ioe;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return responseObject;
    }

    public HttpClientResponse sendDelete(String host, int port, String path, String authHeader) throws IOException, ParseException {
        URI uri;
        try {
            uri = buildUrl(host, port, path, null);
        } catch (URISyntaxException e) {
            logger.error("Error building uri for host={}; path={};", host, path);
            throw new IOException(e);
        }

        HttpDelete httpDelete = new HttpDelete(uri);
        if (authHeader != null) {
            httpDelete.setHeader("Authorization", authHeader);
        }
        logger.debug("Sending DELETE request to URL : " + uri);

        CloseableHttpResponse response;
        HttpClientResponse responseObject = new HttpClientResponse();

        try {
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
            response = httpClient.execute(httpDelete);
            int statusCode = response.getCode();
            responseObject.setStatus(statusCode);
            if (statusCode >= 200 && statusCode <= 206) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    String responseString = EntityUtils.toString(httpEntity);
                    logger.info("Got response: {}", responseString.substring(0, Math.min(responseString.length(), 100)));
                    responseObject.setBody(responseString);
                } else {
                    logger.warn("No response body received from service");
                }
            } else {
                if (responseObject.getStatus() == 401 || responseObject.getStatus() == 403) {
                    logger.warn("User forbidden or unauthorized. uri={}; statusCode={}",
                            uri, responseObject.getStatus());
                } else {
                    logger.error("Error calling uri={}; statusCode={}", uri, responseObject.getStatus());
                    throw new IOException("Error calling uri " + uri);
                }
            }
        } catch (IOException | ParseException ioe) {
            logger.error("Error calling uri={}; exception={}", uri, ioe.getMessage());
            throw ioe;
        }
        return responseObject;
    }

}
