package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by andrey on 13.07.15.
 */
public abstract class BaseTrelloService {
    public final static SimpleDateFormat TRELLO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String GET_LISTS_OF_BOARD = "boards/{0}/lists";
    protected static final String GET_OR_CHANGE_CARD_BY_ID = "cards/{0}";
    protected static final String GET_LIST_BY_ID = "lists/{0}";
    protected static final String POST_CARD = "cards";
    protected static final String POST_COMMENT = "cards/{0}/actions/comments";
    protected static final String CHANGE_OR_DELETE_COMMENT = "cards/{0}/actions/{1}/comments";
    //protected static final String DELETE_COMMENT = "cards/{0}/actions/{1}/comments";
    protected static final String FIND_USER_BY_EMAIL = "search/members";

    static {
        TRELLO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC")); //trello gives and gets time in UTC
    }

    protected static final HashMap<Task.Status, String> STATUS_LIST_MAP = new HashMap<Task.Status, String>() {
        {
            put(Task.Status.OPEN, "To Do");
            put(Task.Status.IN_PROGRESS, "Doing");
            put(Task.Status.RESOLVED, "Done");
            put(Task.Status.CLOSED, "Closed");

        }
    };

    protected final Logger log = LoggerFactory.getLogger(BaseTrelloService.class);

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;
    public BaseTrelloService(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
