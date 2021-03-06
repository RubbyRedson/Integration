package ru.riskgap.integration.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by andrey on 11.07.15.
 */
public class Auth {
    /**
     * Система, в которой находится задание
     */
    @JsonProperty("target-system")
    private TargetSystem targetSystem;

    /**
     * Ключ для идентификации приложения, через которое осуществляется доступ к API интегрируемой системы
     */
    @JsonProperty("application-key")
    private String applicationKey;

    @JsonProperty("user-token")
    private String userToken;

    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("workstation")
    private String workstation;

    public TargetSystem getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(TargetSystem targetSystem) {
        this.targetSystem = targetSystem;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auth auth = (Auth) o;

        if (applicationKey != null ? !applicationKey.equals(auth.applicationKey) : auth.applicationKey != null)
            return false;
        if (domain != null ? !domain.equals(auth.domain) : auth.domain != null) return false;
        if (login != null ? !login.equals(auth.login) : auth.login != null) return false;
        if (password != null ? !password.equals(auth.password) : auth.password != null) return false;
        if (targetSystem != auth.targetSystem) return false;
        if (userToken != null ? !userToken.equals(auth.userToken) : auth.userToken != null) return false;
        if (workstation != null ? !workstation.equals(auth.workstation) : auth.workstation != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "targetSystem=" + targetSystem +
                ", applicationKey='" + applicationKey + '\'' +
                ", userToken='" + userToken + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", domain='" + domain + '\'' +
                ", workstation='" + workstation + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = targetSystem != null ? targetSystem.hashCode() : 0;
        result = 31 * result + (applicationKey != null ? applicationKey.hashCode() : 0);
        result = 31 * result + (userToken != null ? userToken.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (workstation != null ? workstation.hashCode() : 0);
        return result;
    }

    public enum TargetSystem {
        TFS("TFS"),
        MS_PROJECT("MS Project"),
        TRELLO("Trello"),
        UNKNOWN(null);

        private String system;

        TargetSystem(String system) {
            this.system = system;
        }

        @JsonValue
        public String getSystem() {
            return system;
        }

        @JsonCreator
        public static TargetSystem fromString(String system) {
            if (system != null) {
                for (TargetSystem targetSystem : TargetSystem.values()) {
                    if (system.equals(targetSystem.getSystem()))
                        return targetSystem;
                }
            }
            return UNKNOWN;
        }
    }
}
