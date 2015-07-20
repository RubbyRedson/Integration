package ru.riskgap.integration.api.trello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.riskgap.integration.IntegrationHandler;
import ru.riskgap.integration.models.Task;

import java.io.IOException;
import java.text.ParseException;

@Component
public class TrelloHandler implements IntegrationHandler {

    @Autowired
    private CardService cardService;

    @Override
    public Task createOrUpdateTask(Task task) throws IOException {
        return cardService.save(
                task,
                task.getAuth().getApplicationKey(),
                task.getAuth().getUserToken());
    }

    @Override
    public Task getTaskInformation(Task task) throws IOException, ParseException {
        return cardService.getById(
                task.getTaskId(),
                task.getAuth().getApplicationKey(),
                task.getAuth().getUserToken());
        //return null;
    }

    /**
     * Method that will be called if task must be updated
     * @param task
     * @throws Exception
     * @return changed task
     */
    private Task updateTask(Task task) throws Exception{
        return null;
    }

    /**
     * Method that will be called if task must be created
     * @param task
     * @throws Exception
     * @return changed task
     */
    private Task createTask(Task task) throws Exception{
        return null;
    }
}
