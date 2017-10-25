package otus.project.horizontal_scaling_chat.share.message.user;

import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.db.service.CommonUserService;

public class RefreshUserTokenMessage extends UserMessage {
    private final CommonUser commonUser;

    public RefreshUserTokenMessage(CommonUser commonUser) {
        this.commonUser = commonUser;
    }

    @Override
    public void handleUser(CommonUserService userService) {
        userService.refreshToken(commonUser);
    }
}
