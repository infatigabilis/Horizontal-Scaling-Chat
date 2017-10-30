package otus.project.horizontal_scaling_chat.share.init;

public class DbNodeInit extends NodeInit {
    private final long dbIndex;
    private final int frontPort;

    public DbNodeInit(long dbIndex, int frontPort) {
        this.dbIndex = dbIndex;
        this.frontPort = frontPort;
    }

    public long getDbIndex() {
        return dbIndex;
    }

    public int getFrontPort() {
        return frontPort;
    }
}
