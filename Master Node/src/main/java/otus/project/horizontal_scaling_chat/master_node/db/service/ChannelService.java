package otus.project.horizontal_scaling_chat.master_node.db.service;

import org.apache.ibatis.session.SqlSession;
import otus.project.horizontal_scaling_chat.master_node.db.DBService;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

import java.util.List;
import java.util.Optional;

public class ChannelService {
    private static final int PAGINATION_SIZE = 40;

    private final DBService dbService;

    public ChannelService(DBService dbService) {
        this.dbService = dbService;
    }

    public List<Channel> get(int page) {
        try (SqlSession session = dbService.openSession()) {
            return session.selectList("channel_get_all", new MapBuilder<String, Object>()
                    .put("limit", PAGINATION_SIZE)
                    .put("offset", PAGINATION_SIZE * page)
                    .build()
            );
        }
    }

    public Optional<Channel> getById(long id) {
        try(SqlSession session = dbService.openSession()) {
            return Optional.ofNullable(session.selectOne("channel_get", id));
        }
    }

    public List<Channel> getByUser(long id) {
        try(SqlSession session = dbService.openSession()) {
            return session.selectList("channel_get_by_user", id);
        }
    }

    public Channel getCur(Channel channel) {
        try(SqlSession session = dbService.openSession()) {
            return session.selectOne("channel_get_cur", channel);
        }
    }

    public Channel create(Channel channel, long creatorId) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_create", channel);
            session.commit();

            Channel cur = getCur(channel);
            addMember(cur.getId(), creatorId);
            session.commit();

            return cur;
        }
    }

    public void addMember(long channelId, long userId) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_add_member", new MapBuilder<String, Object>()
                    .put("channel_id", channelId)
                    .put("user_id", userId)
                    .build()
            );
            session.commit();
        }
    }

    public void expelMember(long channelId, long userId) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_expel_member", new MapBuilder<String, Object>()
                    .put("channel_id", channelId)
                    .put("user_id", userId)
                    .build()
            );
            session.commit();
        }
    }
}
