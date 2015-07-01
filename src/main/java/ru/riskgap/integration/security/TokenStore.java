package ru.riskgap.integration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenStore {
    private Set<String> tokens = new HashSet<String>();
    public static final Logger logger = LoggerFactory.getLogger(TokenStore.class);

    @PostConstruct
    public void initIt() throws Exception {
        Resource resource = new ClassPathResource("tokens.xml");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(resource.getFile());
        NodeList tokensList = document.getElementsByTagName("token");
        for (int i = 0; i < tokensList.getLength(); i++) {
            tokens.add(tokensList.item(i).getTextContent());
        }
        logger.info("*****Tokens****: {}", tokens);
    }

    public boolean isValidToken(String token) {
        return tokens.contains(token);
    }
}
