package otus.project.horizontal_scaling_chat;

import org.apache.ibatis.session.SqlSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import otus.project.horizontal_scaling_chat.db.dataset.User;
import otus.project.horizontal_scaling_chat.db_node.beans.BeanConfig;
import otus.project.horizontal_scaling_chat.db_node.db.DBService;
import otus.project.horizontal_scaling_chat.db_node.db.service.UserService;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DBTest.TestDBBeanConfig.class})
public class DBTest {
    @Autowired private DBService dbService;
    @Autowired private UserService userService;

    protected User user1;
    protected User user2;

    @BeforeClass
    public static void setUp() {
        try(SqlSession session = new DBService("mybatis/test-config.xml").openSession()) {
            session.delete("clear_db");
            session.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addUsers() {
        user1 = new User("1", "google", "login1", "token1");
        user2 = new User("2", "vk", "login2", "token2");
        userService.add(user1);
        userService.add(user2);
        user1 = userService.get(user1.getSourceId(), user1.getAuthSource());
        user2 = userService.get(user2.getSourceId(), user2.getAuthSource());
    }

    protected void deleteAll() {
        try(SqlSession session = dbService.openSession()) {
            session.delete("clear_db");
            session.commit();
        }
    }

    @Test
    public void test() {

    }

    @Configuration
    public static class TestDBBeanConfig extends BeanConfig {
        @Override
        @Bean
        public DBService dbService() {
            try {
                return new DBService("mybatis/test-config.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}