package otus.project.horizontal_scaling_chat.share.message.channel;

import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.db.service.CommonChannelService;

public class AddMemberMessage extends ChannelMessage {
    private final long channelId;
    private final CommonUser commonUser;

    public AddMemberMessage(long channelId, CommonUser commonUser) {
        this.channelId = channelId;
        this.commonUser = commonUser;
    }

    @Override
    public void handleChannel(CommonChannelService channelService) {
        channelService.addMember(channelId, commonUser);
    }
}
