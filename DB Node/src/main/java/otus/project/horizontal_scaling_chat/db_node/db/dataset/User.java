package otus.project.horizontal_scaling_chat.db_node.db.dataset;

import com.google.gson.annotations.Expose;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.share.TransmittedData;

import java.util.ArrayList;
import java.util.List;

public class User extends TransmittedData implements CommonUser {
    @Expose private long id;
    @Expose private String sourceId;
    @Expose private String authSource;
    @Expose private String login;
    private String token;
    private List<Channel> channels = new ArrayList<>();

    public User() {
    }

    public User(String sourceId, String authSource, String login, String token) {
        this.sourceId = sourceId;
        this.authSource = authSource;
        this.login = login;
        this.token = token;
    }


    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String getAuthSource() {
        return authSource;
    }

    public void setAuthSource(String authSource) {
        this.authSource = authSource;
    }

    @Override
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public boolean equals(Object obj) {
        return id == (((User) obj).id);
    }
}
