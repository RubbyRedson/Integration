package ru.riskgap.integration.util;

import ru.riskgap.integration.exceptions.InvalidInputDataException;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Task;

import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.INCORRECT;
import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.MISSED;

/**
 * Created by andrey on 20.07.15.
 */
public class TaskValidator {
    public static void validateForGet(Task task) throws InvalidInputDataException {
        validateAuthData(task);
        if (task.getTaskId() == null)
            throw new InvalidInputDataException("task-id", MISSED);
    }

    public static void validateForPost(Task task) throws InvalidInputDataException {
        validateAuthData(task);
        if (task.getName() == null)
            throw new InvalidInputDataException("name", MISSED);
        if (task.getDue() == null)
            throw new InvalidInputDataException("due", MISSED);
        if (task.getStatus() == null)
            throw new InvalidInputDataException("status", MISSED);
        if (task.getStatus() == Task.Status.UNKNOWN)
            throw new InvalidInputDataException("status", INCORRECT);
        if (task.getContainerId() == null)
            throw new InvalidInputDataException("container-id", MISSED);
        if (task.getRiskRef()==null)
            throw new InvalidInputDataException("risk-reference", MISSED);
    }

    private static void validateAuthData(Task task) throws InvalidInputDataException {
        if (task.getAuth() == null) {
            throw new InvalidInputDataException("auth", MISSED);
        }
        if (task.getAuth().getTargetSystem() == null) {
            throw new InvalidInputDataException("target-system", MISSED);
        }
        if (task.getAuth().getTargetSystem() == Auth.TargetSystem.UNKNOWN) {
            throw new InvalidInputDataException("target-system", INCORRECT);
        }
        switch (task.getAuth().getTargetSystem()) {
            case TRELLO:
                if (task.getAuth().getApplicationKey() == null)
                    throw new InvalidInputDataException("application-key", MISSED);
                if (task.getAuth().getUserToken() == null)
                    throw new InvalidInputDataException("user-token", MISSED);
                break;
            case MS_PROJECT:
                //TODO
                break;
            case TFS:
                //TODO
                break;
        }
    }
}
