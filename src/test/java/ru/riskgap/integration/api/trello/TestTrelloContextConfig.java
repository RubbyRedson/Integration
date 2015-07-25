package ru.riskgap.integration.api.trello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.riskgap.integration.util.HttpClient;

/**
 * Created by andrey on 13.07.15.
 */
@Configuration
public class TestTrelloContextConfig {
    private HttpClient httpClient;
    {
        httpClient = new FakeTrelloHttpClient();

    }

    @Bean
    public CardService cardService() {
        return new CardService(httpClient);
    }

    @Bean
    public CommentService commentService() {
        return new CommentService(httpClient);
    }

    @Bean
    public ListService listService() {
        return new ListService(httpClient);
    }

    @Bean
    public UserService userService() {
        return new UserService(httpClient);
    }
}
