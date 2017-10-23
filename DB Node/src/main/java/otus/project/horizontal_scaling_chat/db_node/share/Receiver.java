package otus.project.horizontal_scaling_chat.db_node.share;

import otus.project.horizontal_scaling_chat.share.DBNodeChannelService;
import otus.project.horizontal_scaling_chat.share.TransmittedData;
import otus.project.horizontal_scaling_chat.share.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Receiver {
    private static final int CAPACITY = 516;

    private StringBuilder readBuilder = new StringBuilder();

    private final DBNodeChannelService channelService;
    private final String sharerHostname;
    private final int sharerPort;

    public Receiver(DBNodeChannelService channelService, String sharerHostname, int sharerPort) {
        this.channelService = channelService;
        this.sharerHostname = sharerHostname;
        this.sharerPort = sharerPort;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(sharerHostname, sharerPort));

        while(true) {
            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
            int read = channel.read(buffer);
            if (read != -1) {
                String part = new String(buffer.array()).trim();
                readBuilder.append(part);

                if (part.length() != read) {
                    Message msg = (Message) TransmittedData.fromJson(readBuilder.toString());
                    msg.handle(channelService);

                    readBuilder = new StringBuilder();
                }
            }
        }
    }
}
