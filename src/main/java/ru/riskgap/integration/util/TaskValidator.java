package ru.riskgap.integration.util;

import ru.riskgap.integration.exceptions.InvalidInputDataException;
import ru.riskgap.integration.models.Auth;
import ru.riskgap.integration.models.Task;

import java.util.Arrays;

import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.INCORRECT;
import static ru.riskgap.integration.exceptions.InvalidInputDataException.Reason.MISSED;

/**
 * Created by andrey on 20.07.15.
 */
public class TaskValidator {
    public static void validateForGet(Task task) throws InvalidInputDataException {
        validateAuthData(task);
        if (task.getTaskId() == null)
            throw new InvalidInputDataException("task-id",MISSED);
    }

    private static void validateAuthData(Task task) throws InvalidInputDataException {
        if (task.getAuth() == null) {
            throw new InvalidInputDataException("auth", MISSED);
        }
        if (task.getAuth().getTargetSystem() == null) {
            throw new InvalidInputDataException("target-system", MISSED);
        }
        if (!Arrays.asList(Auth.TargetSystem.values()).contains(task.getAuth().getTargetSystem())) {
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
