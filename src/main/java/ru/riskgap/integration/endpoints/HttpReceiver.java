package ru.riskgap.integration.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.riskgap.integration.IntegrationHandler;
import ru.riskgap.integration.MSProjectHandler;
import ru.riskgap.integration.api.tfs.TfsHandler;
import ru.riskgap.integration.api.trello.TrelloHandler;
import ru.riskgap.integration.exceptions.AbstractException;
import ru.riskgap.integration.exceptions.InternalServerException;
import ru.riskgap.integration.exceptions.InvalidInputDataException;
import ru.riskgap.integration.exceptions.InvalidInputDataException.Reason;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.RequestConverter;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;


@RestController
public class HttpReceiver {

    @Autowired
    private ApplicationContext context;

    private RequestConverter requestConverter = new RequestConverter();

    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleGet(@RequestBody String body) throws Exception {
        Task task = null;
        task = getTask(body);
        //return Response.status(500).entity(e).build();
        IntegrationHandler targetSystemHandler;
        targetSystemHandler = getHandler(task);
        Task resultTask = targetSystemHandler.getTaskInformation(task);
        return new ResponseEntity<>(requestConverter.fromTaskToJSON(resultTask), HttpStatus.OK);
        // return new SmokeEntity(10, "Get Smoke Name", new Date());
        //TODO implement sync request handling
    }


    /**
     * @return JSON with task id, comments id
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public Response handlePost(@RequestBody String body) throws Exception {
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
        return Response.status(200).entity(requestConverter.fromTaskToJSON(resultTask)).build();
    }

    private Task getTask(String body) throws IOException, ParseException {
        Task task = requestConverter.fromJSONtoTask(body);
        return task;
    }

    private IntegrationHandler getHandler(Task task) throws AbstractException {
        Auth.TargetSystem targetSystem = task.getAuth().getTargetSystem();
        if (targetSystem == null)
            throw new InvalidInputDataException("targetSystem", Reason.MISSED);
        IntegrationHandler targetSystemHandler = null;
        switch (targetSystem) {
            case MS_PROJECT:
                targetSystemHandler = new MSProjectHandler();
                break;
            case TFS:
                targetSystemHandler = new TfsHandler();
                break;
            case TRELLO:
                targetSystemHandler = context.getBean(TrelloHandler.class);
                break;
            default:
                throw new InvalidInputDataException("targetSystem", Reason.INCORRECT);
        }
        return targetSystemHandler;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleOwnExceptions(Exception exception) {
        if (!(exception instanceof AbstractException))
            exception = new InternalServerException(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(),
                HttpStatus.valueOf(((AbstractException) exception).getStatus()));
    }

}
