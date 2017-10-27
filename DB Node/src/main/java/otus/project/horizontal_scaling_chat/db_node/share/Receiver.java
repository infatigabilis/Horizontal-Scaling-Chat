package otus.project.horizontal_scaling_chat.db_node.share;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import otus.project.horizontal_scaling_chat.db_node.Main;
import otus.project.horizontal_scaling_chat.db_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.db_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.message.Message;
import otus.project.horizontal_scaling_chat.share.message.channel.ChannelMessage;
import otus.project.horizontal_scaling_chat.share.message.channel.CreateChannelMessage;
import otus.project.horizontal_scaling_chat.share.message.user.UserMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Receiver {
    private static final int CAPACITY = 516;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private StringBuilder readBuilder = new StringBuilder();

    private final ChannelService channelService;
    private final UserService userService;
    private final String sharerHostname;
    private final int sharerPort;

    public Receiver(ChannelService channelService, UserService userService, String sharerHostname, int sharerPort) {
        this.channelService = channelService;
        this.userService = userService;
        this.sharerHostname = sharerHostname;
        this.sharerPort = sharerPort;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(sharerHostname, sharerPort));

        String info = getSelfInfo();
        channel.write(ByteBuffer.wrap(info.getBytes()));

        while(true) {
            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
            int read = channel.read(buffer);
            if (read != -1) {
                String part = new String(buffer.array()).trim();
                readBuilder.append(part);

                if (part.length() != read) {
                    Message msg = (Message) TransmittedData.fromJson(readBuilder.toString());

                    if (msg instanceof ChannelMessage) ((ChannelMessage) msg).handleChannel(channelService);
                    else if (msg instanceof UserMessage) ((UserMessage) msg).handleUser(userService);

                    readBuilder = new StringBuilder();
                }
            }
        }
    }

    public String getSelfInfo() {
        return Main.dbIndex + ":" + Main.port + MESSAGES_SEPARATOR;
    }
}
