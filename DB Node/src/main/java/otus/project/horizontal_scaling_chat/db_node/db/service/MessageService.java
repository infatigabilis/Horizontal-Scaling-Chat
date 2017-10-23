package otus.project.horizontal_scaling_chat.db_node.db.service;

import org.apache.ibatis.session.SqlSession;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Message;
import otus.project.horizontal_scaling_chat.db_node.db.DBService;
import otus.project.horizontal_scaling_chat.exception.AuthorizeFailedException;
import otus.project.horizontal_scaling_chat.exception.DBEnitytNotFoundException;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageService {
    public static final int PAGINATION_LIMIT = 30;

    private final DBService dbService;
    private final ChannelService channelService;

    public MessageService(DBService dbService, ChannelService channelService) {
        this.dbService = dbService;
        this.channelService = channelService;
    }

    public void write(Message message) {
        Channel channel = channelService.get(message.getChannel().getId())
                .orElseThrow(() -> new DBEnitytNotFoundException(Channel.class, message.getChannel().getId()));
        if (!channel.getMembers().contains(message.getSender()))
            throw new AuthorizeFailedException();

        try(SqlSession session = dbService.openSession()) {
            session.insert("message_write", message);
            session.commit();
        }
    }

    public List<Message> get(long channelId, int page) {
        try(SqlSession session = dbService.openSession()) {
            return session.selectList("message_get", new MapBuilder<String, Object>()
                    .put("channelId", channelId)
                    .put("limit", PAGINATION_LIMIT)
                    .put("offset", PAGINATION_LIMIT * page)
                    .build()
            );
        }
    }

    public Map<Long, Long> count() {
        try(SqlSession session = dbService.openSession()) {
            List<Map<String, Long>> list = session.selectList("message_count");
            Map<Long, Long> result = new HashMap<>();
            for(Map<String, Long> i : list) {
               result.put(i.get("channel_id"), i.get("count"));
            }

            return result;
        }
    }

    public void clearLast(int amount) {
        try(SqlSession session = dbService.openSession()) {
            session.delete("message_clear_last", amount);
            session.commit();
        }
    }
}
