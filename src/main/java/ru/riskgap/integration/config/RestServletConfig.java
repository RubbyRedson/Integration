package ru.riskgap.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.riskgap.integration.api.trello.CardService;
import ru.riskgap.integration.api.trello.CommentService;
import ru.riskgap.integration.api.trello.ListService;
import ru.riskgap.integration.api.trello.UserService;
import ru.riskgap.integration.util.HttpClient;
import ru.riskgap.integration.util.HttpClientWithTrelloValidator;

/**
 * Created by andrey on 03.07.15.
 */
@Configuration
@EnableWebMvc
@ComponentScan({"ru.riskgap.integration.endpoints", "ru.riskgap.integration.api.tfs",
        "ru.riskgap.integration.api.trello"})
public class RestServletConfig {
    private HttpClient trelloHttpClient;

    {
        trelloHttpClient = new HttpClientWithTrelloValidator();
    }

    @Bean
    public CardService cardService() {
        return new CardService(trelloHttpClient);
    }

    @Bean
    public CommentService commentService() {
        return new CommentService(trelloHttpClient);
    }

    @Bean
    public ListService listService() {
        return new ListService(trelloHttpClient);
    }

    @Bean
    public UserService userService() {
        return new UserService(trelloHttpClient);
    }
}
