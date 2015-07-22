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
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Task;
import ru.riskgap.integration.util.RequestConverter;
import ru.riskgap.integration.util.TaskValidator;

import java.io.IOException;
import java.text.ParseException;


@RestController
public class HttpReceiver {

    @Autowired
    private ApplicationContext context;

    private RequestConverter requestConverter = new RequestConverter();

    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleGet(@RequestBody String body) throws Exception {
        Task task = getTask(body);
        TaskValidator.validateForGet(task);
        IntegrationHandler targetSystemHandler = getHandler(task);
        Task resultTask = targetSystemHandler.getTaskInformation(task);
        return new ResponseEntity<>(requestConverter.fromTaskToJSON(resultTask), HttpStatus.OK);
        //TODO implement sync request handling
    }


    /**
     * @return JSON with task id, comments id
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handlePost(@RequestBody String body) throws Exception {
        Task task = getTask(body);
        TaskValidator.validateForPost(task);
        IntegrationHandler targetSystemHandler= getHandler(task);
        Task resultTask = targetSystemHandler.createOrUpdateTask(task);
        return new ResponseEntity<>(requestConverter.fromTaskToJSON(resultTask), HttpStatus.OK);
    }

    private Task getTask(String body) throws IOException, ParseException {
        Task task = requestConverter.fromJSONtoTask(body);
        return task;
    }

    private IntegrationHandler getHandler(Task task) throws AbstractException {
        Auth.TargetSystem targetSystem = task.getAuth().getTargetSystem();
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
        }
        return targetSystemHandler;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleOwnExceptions(Throwable exception) {
        exception.printStackTrace();
        if (exception.getCause() instanceof AbstractException) {
            exception = exception.getCause();
        } else if (!(exception instanceof AbstractException)) {
            exception = new InternalServerException(exception.getMessage());
        }
        return new ResponseEntity<>(exception.getMessage(),
                HttpStatus.valueOf(((AbstractException) exception).getStatus()));
    }

}
