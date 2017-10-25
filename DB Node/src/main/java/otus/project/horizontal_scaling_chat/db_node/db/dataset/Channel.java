package otus.project.horizontal_scaling_chat.db_node.db.dataset;

import com.google.gson.annotations.Expose;
import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;

import java.util.ArrayList;
import java.util.List;

public class Channel extends CommonChannel {
    private List<User> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Channel() {
    }

    public Channel(long id, String name) {
        super(id, name);
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
}
