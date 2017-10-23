package otus.project.horizontal_scaling_chat.db_node.listener;

import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db_node.caretaker.FullnessCaretaker;
import otus.project.horizontal_scaling_chat.db_node.db.service.MessageService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CaretakerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new FullnessCaretaker(BeanHelper.getBean(MessageService.class)).run();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
