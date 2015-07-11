package ru.riskgap.integration.endpoints;

import org.springframework.web.bind.annotation.*;
import ru.riskgap.integration.IntegrationHandler;
import ru.riskgap.integration.MSProjectHandler;
import ru.riskgap.integration.TrelloHandler;
import ru.riskgap.integration.api.tfs.TfsHandler;
import ru.riskgap.integration.exceptions.AbstractException;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.RequestParser;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;


@RestController
public class HttpReceiver {

    private RequestParser requestParser = new RequestParser();

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Response handleGet(@RequestBody String body) {
        Task task = null;
        try {
            task = getTask(body);
        } catch (IOException | ParseException e) {
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
     * @return JSON with task id, comments id
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public Response handlePost(@RequestBody String body) {
        Task task = null;
        try {
            task = getTask(body);
        } catch (IOException | ParseException e) {
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

    private Task getTask(String body) throws IOException, ParseException {
        Task task = requestParser.parse(body);
        return task;
    }

    private IntegrationHandler getHandler(Task task) throws AbstractException {
        Auth.TargetSystem targetSystem = task.getAuth().getTargetSystem();
        if (targetSystem == null)
            throw new AbstractException("There is no system for integration process");
        IntegrationHandler targetSystemHandler = null;
        switch (targetSystem) {
            case MS_PROJECT:
                targetSystemHandler = new MSProjectHandler();
                break;
            case TFS:
                targetSystemHandler = new TfsHandler();
                break;
            case TRELLO:
                targetSystemHandler = new TrelloHandler();
                break;
        }
        if (targetSystemHandler == null)
            throw new AbstractException("Undefined sytem");
        return targetSystemHandler;
    }

}
