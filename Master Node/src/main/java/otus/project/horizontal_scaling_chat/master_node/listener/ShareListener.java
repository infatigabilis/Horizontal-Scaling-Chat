package otus.project.horizontal_scaling_chat.master_node.listener;

import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.master_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.master_node.db.service.DbIndexService;
import otus.project.horizontal_scaling_chat.master_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.master_node.share.Sharer;
import otus.project.horizontal_scaling_chat.share.MasterEndpoint;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.Executors;

@WebListener
public class ShareListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        int sharePort;
        try(InputStream input = ShareListener.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            sharePort = Integer.parseInt(prop.getProperty("share_port"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                new Sharer(sharePort,
                        BeanHelper.getBean(DbIndexService.class),
                        BeanHelper.getBean(ChannelService.class),
                        BeanHelper.getBean(UserService.class),
                        MasterEndpoint.get("masters.json")
                )
                        .start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}