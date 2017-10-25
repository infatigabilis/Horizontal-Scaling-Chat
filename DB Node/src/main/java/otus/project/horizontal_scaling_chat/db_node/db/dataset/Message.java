package otus.project.horizontal_scaling_chat.db_node.db.dataset;

import com.google.gson.annotations.Expose;
import otus.project.horizontal_scaling_chat.db.dataset.User;
import otus.project.horizontal_scaling_chat.share.TransmittedData;

import java.util.Date;

public class Message extends TransmittedData {
    @Expose private long id;
    @Expose private String text;
    @Expose private Date createdAt;
    @Expose private User sender;
    private Channel channel;

    public Message() {
    }

    public Message(String text, User sender, Channel channel) {
        this.text = text;
        this.sender = sender;
        this.channel = channel;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((Message) obj).id;
    }
}
