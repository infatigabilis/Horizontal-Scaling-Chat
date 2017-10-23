package otus.project.horizontal_scaling_chat.master_node.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class DBService {
    private SqlSessionFactory factory;

    public DBService(String configPath) throws IOException {
        factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(configPath));
    }

    public SqlSession openSession() {
        return factory.openSession();
    }
}
