package ru.riskgap.integration.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.riskgap.integration.*;
import ru.riskgap.integration.exceptions.AbstractException;
import ru.riskgap.integration.models.TargetSystemEnum;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.parsing.RequestParser;

import javax.ws.rs.core.Response;
import java.io.IOException;


@RestController
public class HttpReceiver {

    @Autowired
    private RequestParser requestParser;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Response handleGet(@RequestBody String body) {
        Task task = null;
        try {
            task = getTask(body);
        } catch (IOException e) {
            return Response.status(500).entity(e).build();
        }
        IntegrationHandler targetSystemHandler;
        try {
            targetSystemHandler = getHandler(task);
        } catch (AbstractException e) {
            return Response.status(400).entity(e).build();
        }
        Task resultTask = targetSystemHandler.getTaskInformation(task);
        return Response.status(200).entity(resultTask.toJson()).build();
       // return new SmokeEntity(10, "Get Smoke Name", new Date());
        //TODO implement sync request handling
    }


    /**
     *
     * @return JSON with task id, comments id
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public Response handlePost(@RequestBody String body) {
        Task task = null;
        try {
            task = getTask(body);
        } catch (IOException e) {
            return Response.status(500).entity(e).build();
        }
        IntegrationHandler targetSystemHandler;
        try {
            targetSystemHandler = getHandler(task);
        } catch (AbstractException e) {
            return Response.status(400).entity(e).build();
        }
        Task resultTask = targetSystemHandler.createOrUpdateTask(task);
        return Response.status(200).entity(resultTask.toJson()).build();
    }

    private Task getTask (String body) throws IOException{
        Task task = requestParser.parse(body);
        return task;
    }

     private IntegrationHandler getHandler (Task task) throws AbstractException {
         TargetSystemEnum targetSystem = task.getTargetSystem();
         if (targetSystem == null)
             throw new AbstractException("There is no system for integration process");
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
             throw new AbstractException("Undefined sytem");
         return targetSystemHandler;
     }

}
