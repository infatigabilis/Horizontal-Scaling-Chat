package otus.project.horizontal_scaling_chat.db_node.db.dataset;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Channel extends otus.project.horizontal_scaling_chat.db.dataset.Channel {
    private List<Message> messages = new ArrayList<>();

    public Channel() {
    }

    public Channel(long id, String name) {
        super(id, name);
    }

    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
