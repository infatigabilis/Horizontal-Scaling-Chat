package otus.project.horizontal_scaling_chat.share.message;

import otus.project.horizontal_scaling_chat.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db.dataset.User;
import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;

public class CreateChannelMessage extends Message {
    private final Channel channel;
    private final User user;

    public CreateChannelMessage(Channel channel, User user) {
        this.channel = channel;
        this.user = user;
    }

    @Override
    public void handle(DBNodeChannelService channelService) {
        channelService.create(channel, user);
    }
}
