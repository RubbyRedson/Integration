package ru.riskgap.integration.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.Set;

/**
 * Interface of HTTP client for making requests (GET, POST, PUT, PATCH) and getting details of response.
 * Created by andrey on 05.07.15.
 */
public interface HttpClient {
    CloseableHttpResponse get(String url, NameValuePair... headers) throws IOException;
    CloseableHttpResponse post(String url, String body, NameValuePair... headers) throws IOException;
    CloseableHttpResponse put(String url, String body, NameValuePair... headers) throws IOException;
    CloseableHttpResponse patch(String url, String body, NameValuePair... headers) throws IOException;

    /**
     * Getting response body as string
     * @param response http response
     * @param close close underlying connection
     * @return String representation of body in response
     */
    String extractEntity(CloseableHttpResponse response, boolean close) throws IOException;

    /**
     * Getting status code of response
     * @param response http response
     * @param close close underlying connection
     * @return status code of response
     */
    int extractStatus(CloseableHttpResponse response, boolean close) throws IOException;

    /**
     * Getting headers of response
     * @param response http response
     * @param close close underlying connection
     * @return set of headers of response
     */
    Set<NameValuePair> extractHeaders(CloseableHttpResponse response, boolean close) throws IOException;
}
