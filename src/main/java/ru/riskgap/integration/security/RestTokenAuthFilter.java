package ru.riskgap.integration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.riskgap.integration.models.RestToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

public class RestTokenAuthFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private TokenStore tokenStore;

    private static final Logger logger = LoggerFactory.getLogger(RestTokenAuthFilter.class);
    public static final String HEADER_SERVER_TOKEN = "X-ServerToken";


    public RestTokenAuthFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(new NoOpAuthManager());
        setAuthenticationSuccessHandler(new TokenSimpleUrlAuthSuccessHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws
            AuthenticationException, IOException, ServletException {
        String token = req.getHeader(HEADER_SERVER_TOKEN);
        if (logger.isInfoEnabled()) {
            logger.info("token found: {}", token);
        }
        AbstractAuthenticationToken authToken = authByToken(token);
        if (authToken == null) {
            throw new BadCredentialsException(MessageFormat.format("Error | {0}", "Bad Token"));
        }
        return authToken;
    }

    private AbstractAuthenticationToken authByToken(String token) {
        if (token == null || !tokenStore.isValidToken(token)) {
            return null;
        }
        return new RestToken("USER", token);
    }
}
