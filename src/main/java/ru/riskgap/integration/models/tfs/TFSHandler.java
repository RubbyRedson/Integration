package ru.riskgap.integration.models.tfs;

import ru.riskgap.integration.IntegrationHandler;
import ru.riskgap.integration.models.Task;

public class TfsHandler implements IntegrationHandler {
    @Override
    public Task createOrUpdateTask(Task task) {
        //TODO implement method
        return null;
    }

    @Override
    public Task getTaskInformation(Task task) {
        //TODO implement method
        return null;
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
