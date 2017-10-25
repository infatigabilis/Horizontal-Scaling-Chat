package otus.project.horizontal_scaling_chat.master_node.listener;

import otus.project.horizontal_scaling_chat.master_node.share.Sharer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;

@WebListener
public class ShareListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                new Sharer(7070).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}