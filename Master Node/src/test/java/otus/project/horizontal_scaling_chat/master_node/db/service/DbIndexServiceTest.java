package otus.project.horizontal_scaling_chat.master_node.db.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import otus.project.horizontal_scaling_chat.master_node.db.DBTest;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;

import static org.junit.Assert.assertEquals;

public class DbIndexServiceTest extends DBTest {
    @Autowired private DbIndexService dbIndexService;
    @Autowired private ChannelService channelService;

    @Before
    public void setup() {
        addUsers();
    }

    @After
    public void shutdown() {
        deleteAll();
    }

    @Test
    public void insert() {
        dbIndexService.insert(1, "host");

        Channel channel = new Channel("channel", 1);
        channelService.create(channel, user1.getId());
        assertEquals("host", channelService.getCur(channel).getHost());
    }

    @Test
    public void update() {
        dbIndexService.insert(1, "host");

        Channel channel = new Channel("channel", 1);
        channelService.create(channel, user1.getId());

        dbIndexService.update(1, "new_host");
        assertEquals("new_host", channelService.getCur(channel).getHost());
    }

    @Test
    public void save() {
        dbIndexService.save(1, "host");
        dbIndexService.save(1, "hello");

        Channel channel = new Channel("channel", 1);
        channelService.create(channel, user1.getId());
        assertEquals("hello", channelService.getCur(channel).getHost());
    }
}
