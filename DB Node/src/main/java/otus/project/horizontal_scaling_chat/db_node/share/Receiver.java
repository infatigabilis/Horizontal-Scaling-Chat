package otus.project.horizontal_scaling_chat.db_node.share;

import com.sun.org.apache.regexp.internal.RE;
import otus.project.horizontal_scaling_chat.db_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.db_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.share.MasterEndpoint;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.init.DbNodeInit;
import otus.project.horizontal_scaling_chat.share.message.Message;
import otus.project.horizontal_scaling_chat.share.message.channel.ChannelMessage;
import otus.project.horizontal_scaling_chat.share.message.user.UserMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Properties;

public class Receiver {
    private static final int CAPACITY = 516;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private StringBuilder readBuilder = new StringBuilder();

    private final ChannelService channelService;
    private final UserService userService;
    private final MasterEndpoint[] masterEndpoints;

    public Receiver(ChannelService channelService, UserService userService, MasterEndpoint[] masterEndpoints) {
        this.channelService = channelService;
        this.userService = userService;
        this.masterEndpoints = masterEndpoints;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws IOException {
        Selector selector = Selector.open();
        String info = getSelfInfo();

        for (MasterEndpoint endpoint : masterEndpoints) {
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(endpoint.getHost(), endpoint.getPort()));

            channel.write(ByteBuffer.wrap(info.getBytes()));

            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        }

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                try {
                    if (key.isReadable()) read((SocketChannel) key.channel());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    iterator.remove();
                }
            }
        }
    }

    private void read(SocketChannel channel) throws IOException {
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

    private String getSelfInfo() {
        try(InputStream input = Receiver.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            long dbIndex = Long.parseLong(prop.getProperty("db_index"));
            int frontPort = Integer.parseInt(prop.getProperty("front_port"));

            DbNodeInit data = new DbNodeInit(dbIndex, frontPort);
            return TransmittedData.toJson(data) + MESSAGES_SEPARATOR;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
