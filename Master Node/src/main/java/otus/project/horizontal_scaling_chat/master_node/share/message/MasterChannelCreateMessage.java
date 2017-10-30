package otus.project.horizontal_scaling_chat.master_node.share.message;

import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.master_node.db.service.ChannelService;

public class MasterChannelCreateMessage extends MasterMessage {
    private Channel channel;
    private long userId;

    public MasterChannelCreateMessage(Channel channel, long userId) {
        this.channel = channel;
        this.userId = userId;
    }

    @Override
    public void masterHandle(ChannelService channelService) {
        channelService.create(channel, userId);
    }
}
