package otus.project.horizontal_scaling_chat.master_node.share;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private final int PORT;


    private static Queue<String> hosts = new LinkedList<>();
    private static Map<String, SocketChannel> dbNodes = new HashMap<>();

    public Sharer(int port) {
        PORT = port;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

            logger.info("Server start on port " + PORT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    try {
                        if (key.isAcceptable()) accept(selector, serverSocketChannel);
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

        dbNodes.put(channel.getRemoteAddress().toString(), channel);
        hosts.add(channel.getRemoteAddress().toString());
    }

    public static void send(String host, Message message) throws IOException {
        String json = new Gson().toJson(message) + MESSAGES_SEPARATOR;
        ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());

        while (buffer.hasRemaining()) {
            dbNodes.get(host).write(buffer);
        }
    }

    public static String pickDB() {
        String host = hosts.poll();
        hosts.add(host);

        return host;
    }
}
