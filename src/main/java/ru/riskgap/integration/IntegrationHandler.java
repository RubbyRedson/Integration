package ru.riskgap.integration;


import ru.riskgap.integration.models.Task;

/**
 * Interface for three classes (TFS, Trello, Ms Project) that update or create Task On Server or get info from Server
 */
public interface IntegrationHandler {

    /**
     *
     * @param task information from request that was parsed.
     * @return changed task
     */
    public Task createOrUpdateTask(Task task);

    /**
     *
     * @param task information about id, place of task, which info we want to get
     * @return info about Task
     */
    public Task getTaskInformation(Task task);
}
