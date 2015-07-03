package ru.riskgap.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.riskgap.integration.security.NoOpAuthManager;
import ru.riskgap.integration.security.RestAuthEntryPoint;
import ru.riskgap.integration.security.RestTokenAuthFilter;

/**
 * Created by andrey on 03.07.15.
 */
@Configuration
@EnableWebSecurity
@ComponentScan("ru.riskgap.integration.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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
}
