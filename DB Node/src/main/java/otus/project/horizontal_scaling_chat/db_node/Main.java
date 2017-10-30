package otus.project.horizontal_scaling_chat.db_node;

import otus.project.horizontal_scaling_chat.db_node.share.Receiver;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.init.DbNodeInit;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.InputStream;
import java.util.Properties;

@ApplicationPath("/api/")
public class Main extends Application {
    public static void main(String[] args) {
    }
}
