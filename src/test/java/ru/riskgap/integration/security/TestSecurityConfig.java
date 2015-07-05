package ru.riskgap.integration.security;

import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andrey on 03.07.15.
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "ru.riskgap.integration.security",
        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = TokenStore.class))
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthEntryPoint restAuthEntryPoint;
    @Autowired
    private NoOpAuthManager authManager;


    @Bean(name = "restTokenAuthFilter")
    public RestTokenAuthFilter restTokenAuthFilter() {
        return new RestTokenAuthFilter("/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").authenticated()
                .and().httpBasic().authenticationEntryPoint(restAuthEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(restTokenAuthFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return authManager;
    }

    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore = mock(TokenStore.class);
        when(tokenStore.isValidToken(Matchers.anyString())).thenReturn(false);
        when(tokenStore.isValidToken(Matchers.matches("TEST_TOKEN|TEST_TOKEN1|TEST_TOKEN2"))).thenReturn(true);
        return tokenStore;
    }
}
