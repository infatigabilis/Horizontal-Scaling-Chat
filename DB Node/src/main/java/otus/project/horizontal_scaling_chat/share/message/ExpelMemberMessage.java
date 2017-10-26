package otus.project.horizontal_scaling_chat.share.message;

import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;

public class ExpelMemberMessage extends Message {
    private final long channelId;
    private final long userId;

    public ExpelMemberMessage(long channelId, long userId) {
        this.channelId = channelId;
        this.userId = userId;
    }

    @Override
    public void handle(DBNodeChannelService channelService) {
        channelService.expelMember(channelId, userId);
    }
}
