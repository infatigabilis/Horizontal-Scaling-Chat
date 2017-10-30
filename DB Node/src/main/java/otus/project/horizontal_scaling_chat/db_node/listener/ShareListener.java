package otus.project.horizontal_scaling_chat.db_node.listener;

import com.google.gson.Gson;
import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db_node.Main;
import otus.project.horizontal_scaling_chat.db_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.db_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.db_node.share.Receiver;
import otus.project.horizontal_scaling_chat.share.MasterEndpoint;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Executors;

@WebListener
public class ShareListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                new Receiver(
                        BeanHelper.getBean(ChannelService.class),
                        BeanHelper.getBean(UserService.class),
                        MasterEndpoint.get("masters.json")
                ).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
