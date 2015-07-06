package ru.riskgap.integration;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.util.Set;

/**
 * Created by andrey on 06.07.15.
 */
public class FakeHttpClient implements HttpClient {

    private String lastUrl;

    @Override
    public CloseableHttpResponse get(String url, NameValuePair... headers) throws IOException {
        lastUrl = url;
        return null;
    }

    @Override
    public CloseableHttpResponse post(String url, String body, NameValuePair... headers) throws IOException {
        lastUrl = url;
        return null;
    }

    @Override
    public CloseableHttpResponse put(String url, String body, NameValuePair... headers) throws IOException {
        lastUrl = url;
        return null;
    }

    @Override
    public CloseableHttpResponse patch(String url, String body, NameValuePair... headers) throws IOException {
        lastUrl = url;
        return null;
    }

    @Override
    public String extractEntity(CloseableHttpResponse response, boolean close) throws IOException {
        return "[]";
    }

    @Override
    public int extractStatus(CloseableHttpResponse response, boolean close) throws IOException {
        return 200;
    }

    @Override
    public Set<NameValuePair> extractHeaders(CloseableHttpResponse response, boolean close) throws IOException {
        return null;
    }

    public String getLastUrl() {
        return lastUrl;
    }
}
