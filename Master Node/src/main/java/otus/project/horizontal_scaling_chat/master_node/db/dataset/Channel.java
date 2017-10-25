package otus.project.horizontal_scaling_chat.master_node.db.dataset;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Channel extends otus.project.horizontal_scaling_chat.db.dataset.Channel {
    @Expose private String host;

    public Channel() {
    }

    public Channel(String name, String host) {
        this.setName(name);
        this.host = host;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
