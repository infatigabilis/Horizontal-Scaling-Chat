package otus.project.horizontal_scaling_chat.master_node.db.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.db.dataset.CommonUser;
import otus.project.horizontal_scaling_chat.db.service.CommonUserService;
import otus.project.horizontal_scaling_chat.master_node.db.DBService;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

import java.util.Optional;

public class UserService implements CommonUserService {
    private static Logger logger = LogManager.getLogger();
    private final DBService dbService;

    public UserService(DBService dbService) {
        this.dbService = dbService;
    }

    public User get(String sourceId, String authSource) {
        try (SqlSession session = dbService.openSession()) {
            return session.selectOne("user_get", new MapBuilder<String, Object>()
                    .put("sourceId", sourceId)
                    .put("authSource", authSource)
                    .build()
            );
        }
    }

    public User get(long id) {
        try(SqlSession session = dbService.openSession()) {
            return session.selectOne("user_get_by_id", id);
        }
    }

    @Override
    public Optional<CommonUser> get(String token) {
        try (SqlSession session = dbService.openSession()) {
            return Optional.ofNullable(session.selectOne("user_get_by_token", token));
        }
    }

    public void add(User user) {
        try (SqlSession session = dbService.openSession()) {
            session.insert("user_add", user);
            session.commit();
            logger.info("Created user " + user);
        }
    }

    @Override
    public void refreshToken(CommonUser user) {
        try (SqlSession session = dbService.openSession()) {
            session.update("user_refresh_token", user);
            session.commit();
            logger.info("Refreshed token of user " + user);
        }
    }
}
