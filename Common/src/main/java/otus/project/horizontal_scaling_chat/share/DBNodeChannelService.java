package otus.project.horizontal_scaling_chat.share;

import otus.project.horizontal_scaling_chat.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db.dataset.User;

public interface DBNodeChannelService {
    void create(Channel channel, User user);
    void addMember(long channelId, User user);
    void expelMember(long channelId, long userId);
}
