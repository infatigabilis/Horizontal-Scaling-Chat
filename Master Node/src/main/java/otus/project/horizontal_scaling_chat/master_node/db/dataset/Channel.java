package otus.project.horizontal_scaling_chat.master_node.db.dataset;

import com.google.gson.annotations.Expose;
import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;

import java.util.ArrayList;
import java.util.List;

public class Channel implements CommonChannel {
    @Expose private long id;
    @Expose private String name;
    @Expose private String host;
    private List<User> members = new ArrayList<>();

    public Channel() {
    }

    public Channel(String name, String host) {
        this.name = name;
        this.host = host;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
