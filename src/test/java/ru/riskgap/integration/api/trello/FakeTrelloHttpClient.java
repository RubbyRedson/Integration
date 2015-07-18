package ru.riskgap.integration.api.trello;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.params.HttpParams;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by andrey on 06.07.15.
 */
public class FakeTrelloHttpClient implements HttpClient {

    private String lastUrl;
    private LinkedHashMap<Pattern, CloseableHttpResponse> uriResponseMapper;
    private LinkedHashMap<CloseableHttpResponse, String> responseEntityMapper;
    public static CloseableHttpResponse GET_ALL_LISTS_OF_BOARD = new FakeHttpResponse();
    public static CloseableHttpResponse GET_LIST_BY_ID = new FakeHttpResponse();
    public static CloseableHttpResponse POST_CARD = new FakeHttpResponse();
    public static CloseableHttpResponse POST_COMMENT = new FakeHttpResponse();

    public FakeTrelloHttpClient() {
        responseEntityMapper = new LinkedHashMap<>();
        uriResponseMapper = new LinkedHashMap<>();
        uriResponseMapper.put(Pattern.compile("https://api\\.trello\\.com/1/cards/.*/actions/comments.*"), POST_COMMENT);
        uriResponseMapper.put(Pattern.compile("https://api\\.trello\\.com/1/boards/.*/lists.*"), GET_ALL_LISTS_OF_BOARD);
        uriResponseMapper.put(Pattern.compile("https://api\\.trello\\.com/1/lists/.*\\?.*"), GET_LIST_BY_ID);
        uriResponseMapper.put(Pattern.compile("https://api\\.trello\\.com/1/cards.*"), POST_CARD);


    }

    @Override
    public CloseableHttpResponse get(String url, NameValuePair... headers) throws IOException {
        lastUrl = url;
        for (Map.Entry<Pattern, CloseableHttpResponse> entry : uriResponseMapper.entrySet()) {
            if (entry.getKey().matcher(url).matches())
                return entry.getValue();
        }
        return null;
    }

    @Override
    public CloseableHttpResponse post(String url, String body, NameValuePair... headers) throws IOException {
        lastUrl = url;
        for (Map.Entry<Pattern, CloseableHttpResponse> entry : uriResponseMapper.entrySet()) {
            if (entry.getKey().matcher(url).matches())
                return entry.getValue();
        }
        return null;
    }

    @Override
    public CloseableHttpResponse put(String url, String body, NameValuePair... headers) throws IOException {
        lastUrl = url;
        for (Map.Entry<Pattern, CloseableHttpResponse> entry : uriResponseMapper.entrySet()) {
            if (entry.getKey().matcher(url).matches())
                return entry.getValue();
        }
        return null;
    }

    @Override
    public CloseableHttpResponse delete(String url, NameValuePair... headers) throws IOException {
        lastUrl = url;
        for (Map.Entry<Pattern, CloseableHttpResponse> entry : uriResponseMapper.entrySet()) {
            if (entry.getKey().matcher(url).matches())
                return entry.getValue();
        }
        return null;
    }

    @Override
    public CloseableHttpResponse patch(String url, String body, NameValuePair... headers) throws IOException {
        lastUrl = url;
        for (Map.Entry<Pattern, CloseableHttpResponse> entry : uriResponseMapper.entrySet()) {
            if (entry.getKey().matcher(url).matches())
                return entry.getValue();
        }
        return null;
    }

    @Override
    public String extractEntity(CloseableHttpResponse response, boolean close) throws IOException {
        return responseEntityMapper.get(response);
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

    public void setEntityForResponse(CloseableHttpResponse response, String entity) {
        responseEntityMapper.put(response, entity);
    }

    public static class FakeHttpResponse implements CloseableHttpResponse {

        @Override
        public void close() throws IOException {

        }

        @Override
        public StatusLine getStatusLine() {
            return null;
        }

        @Override
        public void setStatusLine(StatusLine statusLine) {

        }

        @Override
        public void setStatusLine(ProtocolVersion protocolVersion, int i) {

        }

        @Override
        public void setStatusLine(ProtocolVersion protocolVersion, int i, String s) {

        }

        @Override
        public void setStatusCode(int i) throws IllegalStateException {

        }

        @Override
        public void setReasonPhrase(String s) throws IllegalStateException {

        }

        @Override
        public HttpEntity getEntity() {
            return null;
        }

        @Override
        public void setEntity(HttpEntity httpEntity) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public void setLocale(Locale locale) {

        }

        @Override
        public ProtocolVersion getProtocolVersion() {
            return null;
        }

        @Override
        public boolean containsHeader(String s) {
            return false;
        }

        @Override
        public Header[] getHeaders(String s) {
            return new Header[0];
        }

        @Override
        public Header getFirstHeader(String s) {
            return null;
        }

        @Override
        public Header getLastHeader(String s) {
            return null;
        }

        @Override
        public Header[] getAllHeaders() {
            return new Header[0];
        }

        @Override
        public void addHeader(Header header) {

        }

        @Override
        public void addHeader(String s, String s1) {

        }

        @Override
        public void setHeader(Header header) {

        }

        @Override
        public void setHeader(String s, String s1) {

        }

        @Override
        public void setHeaders(Header[] headers) {

        }

        @Override
        public void removeHeader(Header header) {

        }

        @Override
        public void removeHeaders(String s) {

        }

        @Override
        public HeaderIterator headerIterator() {
            return null;
        }

        @Override
        public HeaderIterator headerIterator(String s) {
            return null;
        }

        @Override
        public HttpParams getParams() {
            return null;
        }

        @Override
        public void setParams(HttpParams httpParams) {

        }
    }
}
