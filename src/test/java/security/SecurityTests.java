package security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import ru.riskgap.integration.config.ApplicationContextConfig;
import ru.riskgap.integration.config.RestServletConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrey on 03.07.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {ApplicationContextConfig.class, RestServletConfig.class, TestSecurityConfig.class})
@WebAppConfiguration
public class SecurityTests {
    @Autowired
    WebApplicationContext webAppContext;

    private static Logger log = LoggerFactory.getLogger(SecurityTests.class);

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webAppContext)
                .addFilter(new DelegatingFilterProxy("springSecurityFilterChain", webAppContext), "/*")
                .dispatchOptions(true).build();
    }

    @Test
    public void notFound_noToken() throws Exception {
        mockMvc.perform(get("/aaaaaaaa"))
                .andExpect(status().is(401));
    }

    @Test
    public void notFound_correctToken() throws Exception {
        mockMvc.perform(get("aaaaaaaa/?token=TEST_TOKEN1"))
                .andExpect(status().is(404));
    }

    @Test
    public void found_correctToken() throws Exception {
        mockMvc.perform(get("/get?token=TEST_TOKEN"))
                .andExpect(status().is(200));
    }

    @Test
    public void found_invalidToken() throws Exception {
        mockMvc.perform(get("/get?token=INVALID_TOKEN"))
                .andExpect(status().is(401));
    }

}
