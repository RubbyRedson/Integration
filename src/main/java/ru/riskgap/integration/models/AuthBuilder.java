package ru.riskgap.integration.models;

/**
 * Created by andrey on 11.07.15.
 */
public class AuthBuilder {

    private Auth auth;

    public AuthBuilder() {
        auth = new Auth();
    }

    public AuthBuilder setTargetSystem(Auth.TargetSystem targetSystem) {
        auth.setTargetSystem(targetSystem);
        return this;
    }

    public AuthBuilder setApplicationKey(String applicationKey) {
        auth.setApplicationKey(applicationKey);
        return this;
    }

    public AuthBuilder setUserToken(String userToken) {
        auth.setUserToken(userToken);
        return this;
    }

    public AuthBuilder setLogin(String login) {
        auth.setLogin(login);
        return this;
    }

    public AuthBuilder setPassword(String password) {
        auth.setPassword(password);
        return this;
    }

    public Auth build() {
        return auth;
    }
}
