package ru.riskgap.integration.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import ru.riskgap.integration.exceptions.InvalidInputDataException;

import java.io.IOException;

/**
 * Created by andrey on 10.08.15.
 */
public class HttpClientWithTrelloValidator extends ApacheHttpClient {
    @Override
    public String extractEntity(CloseableHttpResponse response, boolean close) throws IOException {
        String entity = super.extractEntity(response, close);
        if (!(entity.startsWith("{") || entity.startsWith("[")))
            throw new InvalidInputDataException(entity.trim());
        return entity;
    }
}
