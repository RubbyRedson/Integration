package ru.riskgap.integration.models.trello;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.riskgap.integration.FakeHttpClient;
import ru.riskgap.integration.util.ApacheHttpClient;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrey on 06.07.15.
 */
public class TrelloServiceTest {

    private static TrelloService trelloServiceFakeHttp;
    private static TrelloService trelloServiceRealHttp;
    private static FakeHttpClient fakeHttpClient;

    private static final String BOARD_ID = "3RNfcaVO";
    private static final String KEY = "f433f7804eef6a96aadd2fb12e2689d8";
    private static final String TOKEN = "831c4009e4e62f85fc3699e8cfba95f0aca0fe70511b8607c3c6cc13b4e21853";

    @BeforeClass
    public static void initTest() {
        fakeHttpClient = new FakeHttpClient();
        trelloServiceFakeHttp = new TrelloService(fakeHttpClient);
        //due to invalid 'expires' attribute in cookies
        trelloServiceRealHttp = new TrelloService(
                new ApacheHttpClient(HttpClientBuilder.create().setDefaultRequestConfig(
                        RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()
                ).build()));

    }

    @Test
    public void getListIdByStatus_testRequestUrl() throws IOException {
        trelloServiceFakeHttp.getListIdByStatus("IN_PROGRESS", "myBoardId", "myAppKey", "myUserToken");
        assertEquals("https://api.trello.com/1/boards/myBoardId/lists?key=myAppKey&token=myUserToken",
                fakeHttpClient.getLastUrl());

    }

    @Test
    public void getListIdByStatus_testToDoBoard() throws IOException {
        String toDoTask = trelloServiceRealHttp.getListIdByStatus("new", BOARD_ID, KEY, TOKEN);
        assertEquals("559381ce9af4e9c91ab2dbae", toDoTask);
    }

    @Test
    public void getListIdByStatus_testDoneBoard() throws IOException {
        String toDoTask = trelloServiceRealHttp.getListIdByStatus("finished", BOARD_ID, KEY, TOKEN);
        assertEquals("559381cf9af4e9c91ab2dbb0", toDoTask);
    }

}