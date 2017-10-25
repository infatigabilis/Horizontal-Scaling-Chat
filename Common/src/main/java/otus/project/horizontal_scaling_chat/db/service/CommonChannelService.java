package otus.project.horizontal_scaling_chat.db.service;

import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;

public interface CommonChannelService extends DBService {
    void create(CommonChannel commonChannel, CommonUser commonUser);
    void addMember(long channelId, CommonUser commonUser);
    void expelMember(long channelId, long userId);
}
