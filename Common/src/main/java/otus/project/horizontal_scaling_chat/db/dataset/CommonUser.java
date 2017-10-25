package otus.project.horizontal_scaling_chat.db.dataset;

import com.google.gson.annotations.Expose;

public class CommonUser {
    @Expose private long id;
    @Expose private String sourceId;
    @Expose private String authSource;
    @Expose private String login;
    private String token;

    public CommonUser() {
    }

    public CommonUser(String sourceId, String authSource, String login, String token) {
        this.sourceId = sourceId;
        this.authSource = authSource;
        this.login = login;
        this.token = token;
    }

    public enum Role {
        USER
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getAuthSource() {
        return authSource;
    }

    public void setAuthSource(String authSource) {
        this.authSource = authSource;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        return id == (((CommonUser) obj).id);
    }
}
