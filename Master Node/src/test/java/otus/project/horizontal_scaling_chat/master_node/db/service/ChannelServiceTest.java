package otus.project.horizontal_scaling_chat.master_node.db.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import otus.project.horizontal_scaling_chat.master_node.db.DBService;
import otus.project.horizontal_scaling_chat.master_node.db.DBTest;
import otus.project.horizontal_scaling_chat.master_node.db.dataset.Channel;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChannelServiceTest extends DBTest {
    @Autowired private DBService dbService;
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
    public void get() {
        assertEquals(0, channelService.get(0).size());

        channelService.create(new Channel("c1", "h1"), user1.getId());
        channelService.create(new Channel("c2", "h2"), user1.getId());

        List<Channel> list = channelService.get(0);
        assertEquals(2, list.size());
        assertEquals("c1", list.get(0).getName());
        assertEquals("h1", list.get(0).getHost());
        assertEquals(0, channelService.get(1).size());
    }

    @Test
    public void getByUser() {
        channelService.create(new Channel("c1", "h1"), user1.getId());
        channelService.create(new Channel("c2", "h2"), user1.getId());

        List<Channel> list = channelService.getByUser(user1.getId());
        assertEquals(2, list.size());
        assertEquals("c1", list.get(0).getName());
        assertEquals("h1", list.get(0).getHost());
    }

    @Test
    public void addMember() {
        Channel channel = new Channel("c1", "h1");
        channelService.create(channel, user2.getId());
        channel = channelService.getCur(channel);
        channelService.addMember(channel.getId(), user1.getId());

        assertEquals(2 , channelService.getById(channel.getId()).get().getMembers().size());
    }

    @Test
    public void expelMember() {
        Channel channel = new Channel("c1", "h1");
        channelService.create(channel, user2.getId());
        channel = channelService.getCur(channel);
        channelService.expelMember(channel.getId(), user2.getId());

        assertEquals(0 , channelService.getById(channel.getId()).get().getMembers().size());
    }

}
