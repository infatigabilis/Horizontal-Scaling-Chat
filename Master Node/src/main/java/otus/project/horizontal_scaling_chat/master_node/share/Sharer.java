package otus.project.horizontal_scaling_chat.master_node.share;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.master_node.db.service.ChannelService;
import otus.project.horizontal_scaling_chat.master_node.db.service.DbIndexService;
import otus.project.horizontal_scaling_chat.master_node.db.service.UserService;
import otus.project.horizontal_scaling_chat.master_node.share.message.MasterMessage;
import otus.project.horizontal_scaling_chat.share.MasterEndpoint;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.init.DbNodeInit;
import otus.project.horizontal_scaling_chat.share.init.MasterNodeInit;
import otus.project.horizontal_scaling_chat.share.init.NodeInit;
import otus.project.horizontal_scaling_chat.share.message.Message;
import otus.project.horizontal_scaling_chat.share.message.user.UserMessage;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Sharer {
    private static final Logger logger = LogManager.getLogger();
    private static final int READING_CAPACITY = 1024;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private final int port;
    private final DbIndexService dbIndexService;
    private final ChannelService channelService;
    private final UserService userService;
    private final MasterEndpoint[] masterEndpoints;

    private static Queue<Long> dbIndexes = new LinkedList<>();
    private static Map<String, SocketChannel> dbNodes = new HashMap<>();
    private static List<SocketChannel> masterNodes = new ArrayList<>();
    private static Map<Long, String> dbIndexToSocketAddressMap = new HashMap<>();

    private StringBuilder readBuilder = new StringBuilder();

    public Sharer(int port, DbIndexService dbIndexService, ChannelService channelService, UserService userService, MasterEndpoint[] masterEndpoints) {
        this.port = port;
        this.dbIndexService = dbIndexService;
        this.channelService = channelService;
        this.userService = userService;
        this.masterEndpoints = masterEndpoints;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));

            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

            connectToMasters(selector);

            logger.info("Started sharer on port: " + port);

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

    private void connectToMasters(Selector selector) throws IOException, InterruptedException {
        while (true) {
            int length = masterEndpoints.length;
            Queue<MasterEndpoint> queue = new LinkedList<>(Arrays.asList(masterEndpoints));

            while (!queue.isEmpty()) {
                MasterEndpoint endpoint = queue.poll();

                try {
                    bindMaster(selector, endpoint.getHost(), endpoint.getPort());

                    logger.info("Connected to " + endpoint.getHost() + ":" + endpoint.getPort());
                    length--;
                } catch (ConnectException e) {
                    queue.add(endpoint);
                }
            }

            if (length == 0) break;
            Thread.sleep(100);
        }
        logger.info("Connected to all masters");
    }

    private void bindMaster(Selector selector, String masterHost, int masterPort) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(masterHost, masterPort));

        String info = TransmittedData.toJson(new MasterNodeInit()) + MESSAGES_SEPARATOR;
        channel.write(ByteBuffer.wrap(info.getBytes()));

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        masterNodes.add(channel);
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
        if (data instanceof NodeInit) handleInit((NodeInit) data, channel);
        else if (data instanceof MasterMessage) ((MasterMessage) data).masterHandle(channelService);
        else if (data instanceof UserMessage) ((UserMessage) data).handleUser(userService);
    }

    private void handleInit(NodeInit nodeInit, SocketChannel channel) throws IOException {
        if (nodeInit instanceof DbNodeInit) {
            DbNodeInit dbNodeInit = (DbNodeInit) nodeInit;
            dbIndexes.add(dbNodeInit.getDbIndex());
            dbIndexToSocketAddressMap.put(dbNodeInit.getDbIndex(), channel.getRemoteAddress().toString());

            String host = channel.getRemoteAddress().toString().substring(1).split(":")[0];

            dbIndexService.save(dbNodeInit.getDbIndex(), host + ":" + dbNodeInit.getFrontPort());
        } else if (nodeInit instanceof MasterNodeInit) {
            masterNodes.add(channel);
        }
    }

    public static void send(long dbIndex, Message message) throws IOException {
        String json = new Gson().toJson(message) + MESSAGES_SEPARATOR;
        ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());

        while (buffer.hasRemaining()) {
            dbNodes.get(dbIndexToSocketAddressMap.get(dbIndex)).write(buffer);
        }
    }

    public static void sendToMasters(Message msg) throws IOException {
        String json = TransmittedData.toJson(msg) + MESSAGES_SEPARATOR;
        ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());

        for (SocketChannel channel : masterNodes) {
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }
    }

    public static long pickDB() {
        long index = dbIndexes.poll();
        dbIndexes.add(index);

        return index;
    }
}
