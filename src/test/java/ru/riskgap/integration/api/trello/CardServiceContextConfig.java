package ru.riskgap.integration.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.riskgap.integration.models.Comment;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andrey on 25.07.15.
 */
@Configuration
public class CardServiceContextConfig {
    public static final String OPEN_LIST = "OPEN";
    public static final String IN_PROGRESS_LIST = "IN_PROGRESS";
    public static final String RESOLVED_LIST = "RESOLVED";
    public static final String CLOSED_LIST = "CLOSED";


    private HttpClient httpClient;

    {
        httpClient = new FakeTrelloHttpClient();

    }

    @Bean
    public CardService cardService() {
        return new CardService(httpClient);
    }

    //comments are always empty in card service tests
    @Bean
    public CommentService commentService() throws ParseException {
        CommentService commService = mock(CommentService.class);
        when(commService.getFromActions(Matchers.<JsonNode>anyObject())).thenReturn(new ArrayList<Comment>());
        return commService;
    }

    @Bean
    public UserService userService() throws IOException, URISyntaxException {
        UserService userService = mock(UserService.class);
        when(userService.findIdByEmail(anyString(),anyString(), anyString())).thenReturn("USER_ID");
        return userService;
    }

    @Bean
    public ListService listService() throws IOException, URISyntaxException {
        ListService listService = mock(ListService.class);
        when(listService.getByStatus(
                Matchers.<Task.Status>anyObject(),
                anyString(), anyString(), anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                Task.Status status = (Task.Status) args[0];
                switch (status) {
                    case OPEN:
                        return OPEN_LIST;
                    case CLOSED:
                        return CLOSED_LIST;
                    case IN_PROGRESS:
                        return IN_PROGRESS_LIST;
                    case RESOLVED:
                        return RESOLVED_LIST;
                }
                return null;
            }
        });
        when(listService.getStatusByList(anyString(), anyString(), anyString()))
                .thenAnswer(new Answer<Task.Status>() {
                    @Override
                    public Task.Status answer(InvocationOnMock invocationOnMock) throws Throwable {
                        Object[] args = invocationOnMock.getArguments();
                        String listId = ((String) args[0]);
                        switch (listId) {
                            case OPEN_LIST: return Task.Status.OPEN;
                            case CLOSED_LIST: return Task.Status.CLOSED;
                            case IN_PROGRESS_LIST: return Task.Status.IN_PROGRESS;
                            case RESOLVED_LIST: return Task.Status.RESOLVED;
                        }
                        return Task.Status.UNKNOWN;
                    }
                });

        return listService;
    }
}
