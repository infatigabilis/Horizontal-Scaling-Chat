package otus.project.horizontal_scaling_chat.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class BeanHelper {
    private static ApplicationContext context;

    public static void autowire(Object obj) {
        getApplicationContext().getAutowireCapableBeanFactory().autowireBean(obj);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static ApplicationContext getApplicationContext() {
        if (context == null) context = ContextLoader.getCurrentWebApplicationContext();
        return context;
    }
}
