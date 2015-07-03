package ru.riskgap.integration.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.riskgap.integration.*;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.parsing.RequestParser;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

import static ru.riskgap.integration.models.TargetSystemEnum.*;

@RestController
public class HttpReceiver {

    @Autowired
    private RequestParser requestParser;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Response handleGet(@RequestBody String body) {
        Task task = null;
        try {
            task = requestParser.parse(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (task == null) {
            return null;
        }
        TargetSystemEnum targetSystem = task.getTargetSystem();
        if (targetSystem == null)
            return null;
        IntegrationHandler targetSystemHandler = null;
        switch (targetSystem){
            case MS_PROJECT:
                targetSystemHandler = new MSProjectHandler(); break;
            case TFS:
                targetSystemHandler = new TFSHandler(); break;
            case TRELLO:
                targetSystemHandler = new TrelloHandler(); break;
        }
        if (targetSystemHandler == null)
            return null;
        Task resultTask = targetSystemHandler.getTaskInformation(task);
        return Response.status(200).entity(requestParser.taskToJSON(resultTask)).build();
       // return new SmokeEntity(10, "Get Smoke Name", new Date());
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
