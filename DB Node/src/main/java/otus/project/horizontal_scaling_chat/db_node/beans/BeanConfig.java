package otus.project.horizontal_scaling_chat.db_node.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import otus.project.horizontal_scaling_chat.db_node.db.DBService;
import otus.project.horizontal_scaling_chat.db_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.db_node.db.service.MessageService;
import otus.project.horizontal_scaling_chat.db_node.db.service.UserService;

import java.io.IOException;

@Configuration
public class BeanConfig {
    @Bean
    public DBService dbService() {
        try {
            return new DBService("mybatis/config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Bean
    public ChannelService channelService() {
        return new ChannelService(dbService(), userService());
    }

    @Bean
    public MessageService messageService() {
        return new MessageService(dbService(), channelService());
    }

    @Bean
    public UserService userService() {
        return new UserService(dbService());
    }
}