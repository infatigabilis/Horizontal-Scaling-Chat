package otus.project.horizontal_scaling_chat.db_node.db.dataset;

import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;

import java.util.ArrayList;
import java.util.List;

public class User extends CommonUser {
    private List<Channel> channels = new ArrayList<>();

    public User() {
    }

    public User(String sourceId, String authSource, String login, String token) {
        super(sourceId, authSource, login, token);
    }

    public List<Channel> getChannels() {
        return channels;
    }
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }
}
