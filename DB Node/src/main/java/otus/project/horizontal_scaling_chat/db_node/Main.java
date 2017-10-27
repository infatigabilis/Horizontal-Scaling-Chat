package otus.project.horizontal_scaling_chat.db_node;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/")
public class Main extends Application {
    // todo transient
    public static final long dbIndex = 1;
    public static final int port = 9090;

    public static void main(String[] args) {
    }
}
