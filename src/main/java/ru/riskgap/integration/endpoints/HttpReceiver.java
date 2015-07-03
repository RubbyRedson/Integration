package ru.riskgap.integration.endpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.riskgap.integration.SmokeEntity;

import java.util.Date;

@RestController
public class HttpReceiver {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public SmokeEntity handleGet() {
        return new SmokeEntity(10, "Get Smoke Name", new Date());
        //TODO implement sync request handling
    }


    /**
     *
     * @return JSON with task id, comments id
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public SmokeEntity handlePost() {
        return new SmokeEntity(21, "Post Smoke Name", new Date());
        //TODO implement add task request handling
    }


}
