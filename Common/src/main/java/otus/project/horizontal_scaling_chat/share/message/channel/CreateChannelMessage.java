package otus.project.horizontal_scaling_chat.share.message.channel;

import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.db.service.CommonChannelService;

public class CreateChannelMessage extends ChannelMessage {
    private final CommonChannel commonChannel;
    private final CommonUser commonUser;

    public CreateChannelMessage(CommonChannel commonChannel, CommonUser commonUser) {
        this.commonChannel = commonChannel;
        this.commonUser = commonUser;
    }

    @Override
    public void handleChannel(CommonChannelService channelService) {
        channelService.create(commonChannel, commonUser);
    }
}
