package otus.project.horizontal_scaling_chat.master_node.share.message;

import otus.project.horizontal_scaling_chat.db.service.DBService;
import otus.project.horizontal_scaling_chat.master_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.share.message.Message;

public abstract class MasterMessage extends Message {
    @Override
    public void handle(DBService dbService) {
        masterHandle((ChannelService) dbService);
    }

    public abstract void masterHandle(ChannelService channelService);
}
