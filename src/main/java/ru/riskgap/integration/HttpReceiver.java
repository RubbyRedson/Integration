package ru.riskgap.integration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Date;

/**
 * Created by Nikita on 16.06.2015.
 */
@RestController
public class HttpReceiver {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public SmokeEntity handleGet() {
        if (authenticate()) {
            Task task = RequestParser.parse();
            return new SmokeEntity(10,"Get Smoke Name", new Date());
            //TODO implement sync request handling
        }
        return null;
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public SmokeEntity handlePost() {
        if (authenticate()) {
            Task task = RequestParser.parse();
            return new SmokeEntity(21,"Post Smoke Name", new Date());
            //TODO implement add task request handling
        }
        return null;
    }

    private boolean authenticate() {
        //TODO implement authentication
        return true;
    }


}
