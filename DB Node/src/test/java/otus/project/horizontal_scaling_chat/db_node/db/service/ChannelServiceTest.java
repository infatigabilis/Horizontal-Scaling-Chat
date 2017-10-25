package otus.project.horizontal_scaling_chat.db_node.db.service;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import otus.project.horizontal_scaling_chat.DBTest;
import otus.project.horizontal_scaling_chat.db_node.db.dataset.Channel;
import otus.project.horizontal_scaling_chat.db_node.db.DBService;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.message.channel.CreateChannelMessage;

import static org.junit.Assert.assertEquals;

public class ChannelServiceTest extends DBTest {
    @Autowired private DBService dbService;
    @Autowired private ChannelService channelService;

    private Channel channel1;

    @Before
    public void setup() {
        addUsers();
        channel1 = new Channel(1, "Channel 1");
        channelService.create(channel1, user1);
    }

    @After
    public void shutdown() {
        deleteAll();
    }

    @Test
    public void create() {
        Channel exp = new Channel(2, "Channel 2");
        exp.getMembers().add(user2);

        channelService.create(exp, user2);

        assertEquals(exp, channelService.get(exp.getId()).get());
    }

    @Test
    public void test() {
        String json = "{\"commonChannel\":{\"host\":\"/127.0.0.1:59946\",\"members\":[],\"id\":62,\"name\":\"Channel 1\"},\"commonUser\":{\"channels\":[],\"id\":85,\"sourceId\":\"100984083937515165319\",\"authSource\":\"google\",\"login\":\"Данил Иванов\",\"token\":\"ya29.GlzvBELTdS3FiQacoWUA_saqCDLpNMty6PLxTODSzKpbO-WaRQ2U12L81D81ojgkWouTm10iPEGH-zMvKN5O8cX57mzYoBNe4JxGCYPyZ8bWiQF5BOP5gP89BefMfA\"},\"className\":\"otus.project.horizontal_scaling_chat.share.message.channel.CreateChannelMessage\"}";
        TransmittedData msg = (TransmittedData) new Gson().fromJson(json, CreateChannelMessage.class);
    }

    @Test
    public void addMember() {
        channelService.addMember(channel1.getId(), user2);
        channel1 = channelService.get(channel1.getId()).get();

        assertEquals(2, channel1.getMembers().size());
        assertEquals(user2, channel1.getMembers().get(1));
    }

    @Test
    public void expelMember() {
        channelService.expelMember(channel1.getId(), user1.getId());
        channel1 = channelService.get(channel1.getId()).get();

        assertEquals(0, channel1.getMembers().size());
    }
}
