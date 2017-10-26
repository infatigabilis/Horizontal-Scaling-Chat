package otus.project.horizontal_scaling_chat.share.message;

import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;

public class AddMemberMessage extends Message {
    private final long channelId;
    private final CommonUser commonUser;

    public AddMemberMessage(long channelId, CommonUser commonUser) {
        this.channelId = channelId;
        this.commonUser = commonUser;
    }

    @Override
    public void handle(DBNodeChannelService channelService) {
        channelService.addMember(channelId, commonUser);
    }
}
