package otus.project.horizontal_scaling_chat.share;

public class DbNodeInit extends TransmittedData {
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
