package otus.project.horizontal_scaling_chat.share.message;

import otus.project.horizontal_scaling_chat.db.service.CommonChannelService;
import otus.project.horizontal_scaling_chat.db.service.DBService;
import otus.project.horizontal_scaling_chat.share.TransmittedData;

public abstract class Message extends TransmittedData {
    public abstract void handle(DBService dbService);
}
