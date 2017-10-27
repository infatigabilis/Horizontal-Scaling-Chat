package otus.project.horizontal_scaling_chat.master_node.db.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.master_node.db.DBService;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

public class DbIndexService {
    private static final Logger logger = LogManager.getLogger();
    private final DBService dbService;

    public DbIndexService(DBService dbService) {
        this.dbService = dbService;
    }

    public void insert(long id, String host) {
        try(SqlSession session = dbService.openSession()) {
            session.insert("insert", new MapBuilder<String, Object>()
                    .put("id", id)
                    .put("host", host)
                    .build()
            );
            session.commit();
            logger.info("Added dbIndex with id " + id + " and host " + host);
        }
    }

    public void update(long id, String host) {
        try(SqlSession session = dbService.openSession()) {
            session.update("update", new MapBuilder<String, Object>()
                    .put("id", id)
                    .put("host", host)
                    .build()
            );
            session.commit();
            logger.info("Updated dbIndex with id " + id + " and host " + host);
        }
    }

    public void save(long id, String host) {
        try(SqlSession session = dbService.openSession()) {
            if (session.selectOne("select", id) == null) insert(id, host);
            else update(id, host);
        }
    }
}
