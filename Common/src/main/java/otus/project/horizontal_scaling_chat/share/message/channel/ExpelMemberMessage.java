package otus.project.horizontal_scaling_chat.share.message.channel;

import otus.project.horizontal_scaling_chat.db.service.CommonChannelService;
import otus.project.horizontal_scaling_chat.share.message.Message;

public class ExpelMemberMessage extends ChannelMessage {
    private final long channelId;
    private final long userId;

    public ExpelMemberMessage(long channelId, long userId) {
        this.channelId = channelId;
        this.userId = userId;
    }

    @Override
    public void handleChannel(CommonChannelService channelService) {
        channelService.expelMember(channelId, userId);
    }
}
