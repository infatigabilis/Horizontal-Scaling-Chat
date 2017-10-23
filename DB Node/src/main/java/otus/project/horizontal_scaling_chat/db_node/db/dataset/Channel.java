package otus.project.horizontal_scaling_chat.db_node.db.dataset;

import com.google.gson.annotations.Expose;
import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;

import java.util.ArrayList;
import java.util.List;

public class Channel implements CommonChannel {
    @Expose private long id;
    @Expose private String name;
    private List<User> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Channel() {
    }

    public Channel(long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((Channel) obj).id;
    }
}
