package ru.riskgap.integration.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Receives the request body as json and parses it to create a Task object that holds all the received information
 * Created by Nikita on 16.06.2015.
 */
@Component
public class RequestParser {
    ObjectMapper objectMapper = new ObjectMapper();

    public Task parse(String jsonBody) throws IOException {

        Task result = new Task();

        //using DOM as string size is not large
        Map<String,String> myMap = new HashMap<String, String>();
        ObjectMapper objectMapper = new ObjectMapper();
        myMap = objectMapper.readValue(jsonBody, HashMap.class);
        System.out.println("Map is: " + myMap);

        return null;
    }
}
