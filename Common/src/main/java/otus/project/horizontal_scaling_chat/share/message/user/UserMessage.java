package otus.project.horizontal_scaling_chat.share.message.user;

import otus.project.horizontal_scaling_chat.db.service.CommonUserService;
import otus.project.horizontal_scaling_chat.db.service.DBService;
import otus.project.horizontal_scaling_chat.share.message.Message;

public abstract class UserMessage extends Message {
    @Override
    public void handle(DBService dbService) {
        if (dbService instanceof CommonUserService) handleUser((CommonUserService) dbService);
    }

    public abstract void handleUser(CommonUserService userService);
}
