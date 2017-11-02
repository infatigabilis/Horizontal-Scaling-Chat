package otus.project.horizontal_scaling_chat.master_node.share.message;

import otus.project.horizontal_scaling_chat.db.service.DBService;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.master_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.share.message.Message;

public class MasterUserCreateMessage extends Message {
    private final User user;

    public MasterUserCreateMessage(User user) {
        this.user = user;
    }

    @Override
    public void handle(DBService dbService) {
        handleUser((UserService) dbService);
    }

    public void handleUser(UserService userService) {
        userService.add(user);
    }
}
