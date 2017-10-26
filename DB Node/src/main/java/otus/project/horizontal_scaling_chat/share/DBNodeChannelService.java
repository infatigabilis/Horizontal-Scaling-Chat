package otus.project.horizontal_scaling_chat.share;

import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;

public interface DBNodeChannelService {
    void create(CommonChannel commonChannel, CommonUser commonUser);
    void addMember(long channelId, CommonUser commonUser);
    void expelMember(long channelId, long userId);
}
