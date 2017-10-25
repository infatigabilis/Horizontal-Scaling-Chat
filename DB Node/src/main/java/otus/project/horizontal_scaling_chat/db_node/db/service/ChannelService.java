package otus.project.horizontal_scaling_chat.db_node.db.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.db.dataset.CommonChannel;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.db.service.CommonChannelService;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db_node.db.DBService;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

import java.util.Optional;

public class ChannelService implements CommonChannelService {
    private static final Logger logger = LogManager.getLogger();

    private final DBService dbService;
    private final UserService userService;

    public ChannelService(DBService dbService, UserService userService) {
        this.dbService = dbService;
        this.userService = userService;
    }

    public Optional<Channel> get(long id) {
        try(SqlSession session = dbService.openSession()) {
            return Optional.ofNullable(session.selectOne("channel_get", id));
        }
    }

    @Override
    public void create(CommonChannel channel, CommonUser creator) {
        if (userService.get(creator.getId()) == null) userService.add(creator);

        try (SqlSession session = dbService.openSession()) {
            session.insert("channel_create", channel);
            session.insert("channel_add_member", new MapBuilder<String, Object>()
                    .put("channel_id", channel.getId())
                    .put("user", creator)
                    .build());
            session.commit();
            logger.info("Created channel " + channel + " with creator " + creator);
        }
    }

    @Override
    public void addMember(long channelId, CommonUser member) {
        if (userService.get(member.getId()) == null) userService.add(member);

        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_add_member", new MapBuilder<String, Object>()
                    .put("channel_id", channelId)
                    .put("user", member)
                    .build()
            );
            session.commit();
            logger.info("Added member " + member + " of channel with id " + channelId);
        }
    }

    @Override
    public void expelMember(long channelId, long memberId) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("channel_expel_member", new MapBuilder<String, Object>()
                    .put("channel_id", channelId)
                    .put("user_id", memberId)
                    .build()
            );
            session.commit();
            logger.info("Expeled member with id " + memberId + " of channel with id " + channelId);
        }
    }
}
