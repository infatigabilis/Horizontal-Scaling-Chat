package otus.project.horizontal_scaling_chat.share.message.channel;

import otus.project.horizontal_scaling_chat.db.service.CommonChannelService;
import otus.project.horizontal_scaling_chat.db.service.DBService;
import otus.project.horizontal_scaling_chat.share.message.Message;

public abstract class ChannelMessage extends Message {
    @Override
    public void handle(DBService dbService) {
        if (dbService instanceof CommonChannelService) handleChannel((CommonChannelService) dbService);
    }

    public abstract void handleChannel(CommonChannelService channelService);
}
