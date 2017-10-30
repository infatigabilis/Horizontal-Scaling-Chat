package otus.project.horizontal_scaling_chat.master_node.share;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.master_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.master_node.db.service.DbIndexService;
import otus.project.horizontal_scaling_chat.master_node.share.message.MasterMessage;
import otus.project.horizontal_scaling_chat.share.DbNodeInit;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Sharer {
    private static final Logger logger = LogManager.getLogger();
    private static final int READING_CAPACITY = 128;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private final int port;
    private final DbIndexService dbIndexService;
    private final ChannelService channelService;

    private static Queue<Long> dbIndexes = new LinkedList<>();
    private static Map<String, SocketChannel> dbNodes = new HashMap<>();
    private static Map<Long, String> dbIndexToSocketAddressMap = new HashMap<>();

    private StringBuilder readBuilder = new StringBuilder();

    public Sharer(int port, DbIndexService dbIndexService, ChannelService channelService) {
        this.port = port;
        this.dbIndexService = dbIndexService;
        this.channelService = channelService;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));

            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

            logger.info("Started on port: " + port);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    try {
                        if (key.isAcceptable()) accept(selector, serverSocketChannel);
                        else if (key.isReadable()) read(key);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    } finally {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void accept(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel channel = serverChannel.accept();
        String remoteAddress = channel.getRemoteAddress().toString();
        logger.info("Connection Accepted: " + remoteAddress);

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        dbNodes.put(channel.getRemoteAddress().toString(), channel);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(READING_CAPACITY);

        int read = channel.read(buffer);
        if (read != -1) {
            String part = new String(buffer.array()).trim();
            readBuilder.append(part);

            if (part.length() != read) {
                parseMessage(readBuilder.toString(), channel);
                readBuilder = new StringBuilder();
            }
        } else {
            key.cancel();
            String remoteAddress = channel.getRemoteAddress().toString();
            dbNodes.remove(remoteAddress);
            logger.info("Connection closed, key canceled by " + remoteAddress);
        }
    }

    private void parseMessage(String msg, SocketChannel channel) throws IOException {
        TransmittedData data = TransmittedData.fromJson(msg);
        if (data instanceof DbNodeInit) handleInit((DbNodeInit) data, channel);
        else if (data instanceof MasterMessage) handleMasterMessage((MasterMessage) data);
    }

    private void handleMasterMessage(MasterMessage msg) {
        msg.masterHandle(channelService);
    }

    private void handleInit(DbNodeInit data, SocketChannel channel) throws IOException {
        dbIndexes.add(data.getDbIndex());
        dbIndexToSocketAddressMap.put(data.getDbIndex(), channel.getRemoteAddress().toString());

        String host = channel.getRemoteAddress().toString().substring(1).split(":")[0];

        dbIndexService.save(data.getDbIndex(), host + ":" + data.getFrontPort());
    }

    public static void send(long dbIndex, Message message) throws IOException {
        String json = new Gson().toJson(message) + MESSAGES_SEPARATOR;
        ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());

        while (buffer.hasRemaining()) {
            dbNodes.get(dbIndexToSocketAddressMap.get(dbIndex)).write(buffer);
        }
    }

    public static long pickDB() {
        long index = dbIndexes.poll();
        dbIndexes.add(index);

        return index;
    }
}
