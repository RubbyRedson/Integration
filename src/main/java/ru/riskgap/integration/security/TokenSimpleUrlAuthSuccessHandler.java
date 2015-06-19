package ru.riskgap.integration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenSimpleUrlAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static Logger log = LoggerFactory.getLogger(TokenSimpleUrlAuthSuccessHandler.class);

    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response) {

        String context = request.getContextPath();
        String fullURL = request.getRequestURI();
        if (log.isInfoEnabled()) {
            log.info("---- TokenSimpleUrlAuthSuccessHandler ----");
            log.info("context path: {}", context);
            log.info("full url: {}", fullURL);
        }
        return fullURL.substring(fullURL.indexOf(context) + context.length());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String url = determineTargetUrl(request, response);
        if (log.isInfoEnabled()) {
            log.info("---- onAuthenticationSuccess ----");
            log.info("url: {}", url);
        }
        request.getRequestDispatcher(url).forward(request, response);
    }
}
