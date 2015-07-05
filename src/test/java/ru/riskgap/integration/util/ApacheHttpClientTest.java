package ru.riskgap.integration.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by andrey on 05.07.15.
 */
public class ApacheHttpClientTest {

    private static final String BASE_URL = "http://httpbin.org/";
    private static ApacheHttpClient client;

    @BeforeClass
    public static void setUpClass() {
        //seems it doesn't help - first test is still long :(
        SocketConfig config = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoTimeout(2000)
                .build();
        client = new ApacheHttpClient(HttpClients.custom()
                .setDefaultSocketConfig(config)
                .build());
    }

    @Test
    public void get_okStatus() throws IOException {
        CloseableHttpResponse response = client.get(BASE_URL + "get");
        assertEquals("Response status isn't OK", 200, client.extractStatus(response, true));
    }

    @Test
    public void get_withQueryParams() throws IOException {
        CloseableHttpResponse response = client.get(BASE_URL + "get?param1=value1&param2=value2");
        String entity = client.extractEntity(response, true);
        assertTrue(entity.contains("\"param1\": \"value1\""));
        assertTrue(entity.contains("\"param2\": \"value2\""));
    }

    @Test
    public void get_notEmptyEntity() throws IOException {
        CloseableHttpResponse response = client.get(BASE_URL + "get");
        String entity = client.extractEntity(response, true);
        assertTrue("Response body is empty", !(entity == null || entity.isEmpty()));
    }

    @Test
    public void get_bodyContainsHeader() throws IOException {
        CloseableHttpResponse response = client.get(BASE_URL + "get",
                new BasicNameValuePair("Test-Header", "RiskGap"));
        String entity = client.extractEntity(response, true);
        assertTrue("Request header wasn't set", entity.contains("\"Test-Header\": \"RiskGap\""));
    }

    @Test
    public void get_responseHeader() throws IOException {
        CloseableHttpResponse response = client.get(BASE_URL + "response-headers?aaa=bbb");
        Set<NameValuePair> nameValuePairs = client.extractHeaders(response, true);
        assertTrue("Can't find response header",
                nameValuePairs.contains(new BasicNameValuePair("aaa", "bbb")));
    }

    @Test
    public void post_bodyContainsData() throws IOException {
        CloseableHttpResponse response = client.post(BASE_URL + "post", "Body string");
        String entity = client.extractEntity(response, true);
        assertTrue("Body wasn't set", entity.contains("\"data\": \"Body string\""));
    }

    @Test
    public void put_bodyContainsData() throws IOException {
        CloseableHttpResponse response = client.put(BASE_URL + "put", "Body string");
        String entity = client.extractEntity(response, true);
        assertTrue("Body wasn't set", entity.contains("\"data\": \"Body string\""));
    }

    @Test
    public void patch_bodyContainsData() throws IOException {
        CloseableHttpResponse response = client.patch(BASE_URL + "patch", "Body string");
        String entity = client.extractEntity(response, true);
        assertTrue("Body wasn't set", entity.contains("\"data\": \"Body string\""));
    }







}