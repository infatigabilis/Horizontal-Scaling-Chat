package otus.project.horizontal_scaling_chat.share.message;

import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;

public class CreateChannelMessage extends Message {
    private final CommonChannel commonChannel;
    private final CommonUser commonUser;

    public CreateChannelMessage(CommonChannel commonChannel, CommonUser commonUser) {
        this.commonChannel = commonChannel;
        this.commonUser = commonUser;
    }

    @Override
    public void handle(DBNodeChannelService channelService) {
        channelService.create(commonChannel, commonUser);
    }
}
