package otus.project.horizontal_scaling_chat.db_node.db.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import otus.project.horizontal_scaling_chat.DBTest;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Message;
import otus.project.horizontal_scaling_chat.exception.AuthorizeFailedException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MessageServiceTest extends DBTest {
    @Autowired private MessageService messageService;
    @Autowired private ChannelService channelService;

    private Channel channel;

    @Before
    public void setup() {
        addUsers();
        channel = new Channel(1, "Channel");
        channelService.create(channel, user1);
        channel = channelService.get(1).get();
    }

    @After
    public void shutdown() {
        deleteAll();
    }

    @Test
    public void get() {
        for (int i = 0; i < MessageService.PAGINATION_LIMIT + 5; i++)
            messageService.write(new Message("text", user1, channel));

        assertEquals(MessageService.PAGINATION_LIMIT, messageService.get(channel.getId(), 0).size());
        assertEquals(5, messageService.get(channel.getId(), 1).size());
    }

    @Test
    public void getAnotherChannel() {
        assertEquals(0, messageService.get(2, 0).size());
    }

    @Test
    public void write() {
        assertEquals(0, messageService.get(channel.getId(), 0).size());
        messageService.write(new Message("Hello", user1, channel));
        assertEquals(1, messageService.get(channel.getId(), 0).size());
        assertEquals("Hello", messageService.get(channel.getId(), 0).get(0).getText());
        assertEquals(user1, messageService.get(channel.getId(), 0).get(0).getSender());
    }

    @Test(expected = AuthorizeFailedException.class)
    public void write_authorizeFailed() {
        messageService.write(new Message("Hello", user2, channel));
    }

    @Test
    public void count() {
        Channel channel2 = new Channel(2, "Channel2");
        Channel channel3 = new Channel(3, "Channel3");
        channelService.create(channel2, user1);
        channelService.create(channel3, user1);
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel2));
        messageService.write(new Message("Text", user1, channel2));
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel3));

        Map<Long, Long> map = messageService.count();
        assertEquals(3L, ((long) map.get(1L)));
        assertEquals(2L, ((long) map.get(2L)));
        assertEquals(1L, ((long) map.get(3L)));
    }

    @Test
    public void clearLast() {
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel));
        messageService.write(new Message("Text", user1, channel));

        List<Message> list = messageService.get(channel.getId(), 0);
        assertEquals(6, list.size());
        long firstItemId = list.get(0).getId();

        messageService.clearLast(3);
        list = messageService.get(channel.getId(), 0);
        assertEquals(3, list.size());
        assertEquals(firstItemId, list.get(0).getId());
    }
}
