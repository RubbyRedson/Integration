package ru.riskgap.integration.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by andrey on 03.07.15.
 */
@Configuration
@EnableWebMvc
@ComponentScan({"ru.riskgap.integration.endpoints","ru.riskgap.integration.parsing","ru.riskgap.integration.models.tfs"})
public class RestServletConfig {

}
