package otus.project.horizontal_scaling_chat.share.message;

import otus.project.horizontal_scaling_chat.db.dataset.User;
import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;

public class AddMemberMessage extends Message {
    private final long channelId;
    private final User user;

    public AddMemberMessage(long channelId, User user) {
        this.channelId = channelId;
        this.user = user;
    }

    @Override
    public void handle(DBNodeChannelService channelService) {
        channelService.addMember(channelId, user);
    }
}
