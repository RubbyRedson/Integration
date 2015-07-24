package ru.riskgap.integration.util;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrey on 05.07.15.
 */
public class ApacheHttpClient implements HttpClient {
    private CloseableHttpClient httpClient;
    private final Logger log = LoggerFactory.getLogger(ApacheHttpClient.class);

    public ApacheHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ApacheHttpClient() {
        this(HttpClientBuilder.create().build());
    }

    public CloseableHttpResponse get(String url, NameValuePair... headers) throws IOException {
        HttpGet get = new HttpGet(url);
        setHeaders(get, headers);
        log.info("[URL] GET: {}", url);
        return httpClient.execute(get);
    }

    public CloseableHttpResponse post(String url, String body, NameValuePair... headers) throws IOException {
        HttpPost post = new HttpPost(url);
        if (headers != null)
            setHeaders(post, headers);
        if (body != null)
            post.setEntity(new StringEntity(body));
        log.info("[URL] POST: {}", url);
        return httpClient.execute(post);
    }

    public CloseableHttpResponse put(String url, String body, NameValuePair... headers) throws IOException {
        HttpPut put = new HttpPut(url);
        if (headers != null)
            setHeaders(put, headers);
        if (body != null)
            put.setEntity(new StringEntity(body));
        log.info("[URL] PUT: {}", url);
        return httpClient.execute(put);
    }

    public CloseableHttpResponse delete(String url, NameValuePair... headers) throws IOException {
        HttpDelete delete = new HttpDelete(url);
        if (headers != null)
            setHeaders(delete, headers);
        log.info("[URL] DELETE: {}", url);
        return httpClient.execute(delete);
    }

    public CloseableHttpResponse patch(String url, String body, NameValuePair... headers) throws IOException {
        HttpPatch patch = new HttpPatch(url);
        if (headers != null)
            setHeaders(patch, headers);
        if (body != null)
            patch.setEntity(new StringEntity(body));
        log.info("[URL] PATCH: {}", url);
        return httpClient.execute(patch);
    }

    public String extractEntity(CloseableHttpResponse response, boolean close) throws IOException {
        String entity = null;
        try {
            entity = EntityUtils.toString(response.getEntity());
        } finally {
            if (close) HttpClientUtils.closeQuietly(response);
        }
        return entity;
    }

    public int extractStatus(CloseableHttpResponse response, boolean close) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        if (close) HttpClientUtils.closeQuietly(response);
        return status;
    }

    public Set<NameValuePair> extractHeaders(CloseableHttpResponse response, boolean close) throws IOException {
        Set<NameValuePair> headers = new HashSet<>();
        for (Header header : response.getAllHeaders()) {
            headers.add(new BasicNameValuePair(header.getName(), header.getValue()));
        }
        if (close)
            HttpClientUtils.closeQuietly(response);
        return headers;
    }

    private void setHeaders(HttpRequest request, NameValuePair... headers) {
        for (NameValuePair header : headers) {
            request.setHeader(header.getName(), header.getValue());
        }
    }

    public void close() {
        HttpClientUtils.closeQuietly(this.httpClient);
    }
}
