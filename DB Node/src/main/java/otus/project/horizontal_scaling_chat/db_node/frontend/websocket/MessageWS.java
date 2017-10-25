package otus.project.horizontal_scaling_chat.db_node.frontend.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.beans.BeanHelper;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Message;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.User;
import otus.project.horizontal_scaling_chat.db_node.frontend.JsonFrontend;
import otus.project.horizontal_scaling_chat.db_node.db.service.MessageService;
import otus.project.horizontal_scaling_chat.exception.DBEnitytNotFoundException;
import otus.project.horizontal_scaling_chat.share.TransmittedData;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/messages")
public class MessageWS extends JsonFrontend {
    private static final Logger logger = LogManager.getLogger();

    private final MessageService messageService = BeanHelper.getBean(MessageService.class);

    private Map<User, Session> userSessionMap = new HashMap<>();
    private Map<Session, User> sessionUserMap = new HashMap<>();

    @OnMessage
    public void handleMsg(Session session, String msg) {
        TransmittedData data = TransmittedData.fromJson(msg);

        if (data instanceof Token) {
            User user = (User) userService.get(((Token) data).value)
                    .orElseThrow(() -> new DBEnitytNotFoundException(User.class, ((Token) data).value));
            userSessionMap.put(user, session);
            sessionUserMap.put(session, user);
        } else if (data instanceof Message) {
            Message message = (Message) data;

            message.setSender(sessionUserMap.get(session));
            checkMembership(message.getChannel().getId(), message.getSender().getToken());
            messageService.write(message);
            broadcastToMembers(message);
        }
    }

    private void broadcastToMembers(Message message) {
        for (User member : message.getChannel().getMembers()) {
            try {
                userSessionMap.get(member).getBasicRemote().sendText(respond(message));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private class Token extends TransmittedData {
        private String value;
    }

    @OnClose
    public void close(Session session) {
        User user = sessionUserMap.get(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(user);
    }
}
