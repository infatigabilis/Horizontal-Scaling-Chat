package otus.project.horizontal_scaling_chat.db_node.db.service;

import org.apache.ibatis.session.SqlSession;
import otus.project.horizontal_scaling_chat.db.dataset.User;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db_node.db.DBService;
import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

import java.util.Optional;

public class ChannelService implements DBNodeChannelService {
    private final DBService dbService;

    public ChannelService(DBService dbService) {
        this.dbService = dbService;
    }

    public Optional<Channel> get(long id) {
        try(SqlSession session = dbService.openSession()) {
            return Optional.ofNullable(session.selectOne("channel_get", id));
        }
    }

    public void create(Channel channel, User creator) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_create", channel);
            session.insert("channel_add_member", new MapBuilder<String, Object>()
                    .put("channel_id", channel.getId())
                    .put("user", creator)
                    .build()
            );
            session.commit();
        }
    }

    @Override
    public void create(otus.project.horizontal_scaling_chat.db.dataset.Channel channel, User user) {

    }

    public void addMember(long channelId, User member) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_add_member", new MapBuilder<String, Object>()
                    .put("channel_id", channelId)
                    .put("user", member)
                    .build()
            );
            session.commit();
        }
    }

    @Override
    public void expelMember(long l, long l1) {

    }

    // TODO delete if members count is 0
    public void expelMember(long channelId, User member) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_expel_member", new MapBuilder<String, Object>()
                    .put("channel_id", channelId)
                    .put("user", member)
                    .build()
            );
            session.commit();
        }
    }
}
