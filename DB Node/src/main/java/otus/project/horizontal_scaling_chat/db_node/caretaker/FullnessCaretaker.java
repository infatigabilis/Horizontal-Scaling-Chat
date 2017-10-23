package otus.project.horizontal_scaling_chat.db_node.caretaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.project.horizontal_scaling_chat.db_node.db.service.MessageService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FullnessCaretaker implements FullnessCaretakerMBean {
    private static final Logger logger = LogManager.getLogger();

    private final MessageService messageService;

    private long checkDelay = 1;
    private TimeUnit checkTimeUnit = TimeUnit.DAYS;

    private long alarmDelay = 1;
    private TimeUnit alarmTimeUnit = TimeUnit.MINUTES;

    private long minFreeSpace = 10L * 1024L * 1024L * 1024L;
    private float clearingProportion = 0.1f;
    private int clearingAmount = 10_000;
    private int minMessageThreshold = 1000;
    private int maxMessageThreshold = 100_000;

    public FullnessCaretaker(MessageService messageService, long checkDelay, TimeUnit checkTimeUnit, long alarmDelay, TimeUnit alarmTimeUnit, long minFreeSpace, float clearingProportion, int clearingAmount, int minMessageThreshold, int maxMessageThreshold) {
        this.messageService = messageService;
        this.checkDelay = checkDelay;
        this.checkTimeUnit = checkTimeUnit;
        this.alarmDelay = alarmDelay;
        this.alarmTimeUnit = alarmTimeUnit;
        this.minFreeSpace = minFreeSpace;
        this.clearingProportion = clearingProportion;
        this.clearingAmount = clearingAmount;
        this.minMessageThreshold = minMessageThreshold;
        this.maxMessageThreshold = maxMessageThreshold;
    }

    public FullnessCaretaker(MessageService messageService) {
        this.messageService = messageService;
    }

    public void run() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                Path root = FileSystems.getDefault().getRootDirectories().iterator().next();
                long freeSpace = Files.getFileStore(root).getUsableSpace();
                if (freeSpace < minFreeSpace)
                    clearMessages();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }, 0, checkDelay, checkTimeUnit);
    }

    void clearMessages() {
        boolean somethingChanged = false;
        for (Map.Entry<Long, Long> i : messageService.count().entrySet()) {
            if (i.getValue() > maxMessageThreshold) {
                messageService.clearLast(clearingAmount);
                somethingChanged = true;
            }
            else if (i.getValue() > minMessageThreshold) {
                messageService.clearLast((int) (clearingProportion * i.getValue()));
                somethingChanged = true;
            }
        }

        if (!somethingChanged) alarm();
    }

    void alarm() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            logger.fatal("ALARM!!! MEMORY GONNA END!!!");
        }, alarmDelay, alarmTimeUnit);
        try {
            executorService.awaitTermination(checkDelay, checkTimeUnit);
        } catch (InterruptedException e) {
            executorService.shutdown();
        }
    }


    @Override
    public long getCheckDelay() {
        return checkDelay;
    }

    @Override
    public void setCheckDelay(long checkDelay) {
        this.checkDelay = checkDelay;
    }

    @Override
    public TimeUnit getCheckTimeUnit() {
        return checkTimeUnit;
    }

    @Override
    public void setCheckTimeUnit(TimeUnit checkTimeUnit) {
        this.checkTimeUnit = checkTimeUnit;
    }

    @Override
    public long getAlarmDelay() {
        return alarmDelay;
    }

    @Override
    public void setAlarmDelay(long alarmDelay) {
        this.alarmDelay = alarmDelay;
    }

    @Override
    public TimeUnit getAlarmTimeUnit() {
        return alarmTimeUnit;
    }

    @Override
    public void setAlarmTimeUnit(TimeUnit alarmTimeUnit) {
        this.alarmTimeUnit = alarmTimeUnit;
    }

    @Override
    public long getMinFreeSpace() {
        return minFreeSpace;
    }

    @Override
    public void setMinFreeSpace(long minFreeSpace) {
        this.minFreeSpace = minFreeSpace;
    }

    @Override
    public float getClearingProportion() {
        return clearingProportion;
    }

    @Override
    public void setClearingProportion(float clearingProportion) {
        this.clearingProportion = clearingProportion;
    }

    @Override
    public int getClearingAmount() {
        return clearingAmount;
    }

    @Override
    public void setClearingAmount(int clearingAmount) {
        this.clearingAmount = clearingAmount;
    }

    @Override
    public int getMinMessageThreshold() {
        return minMessageThreshold;
    }

    @Override
    public void setMinMessageThreshold(int minMessageThreshold) {
        this.minMessageThreshold = minMessageThreshold;
    }

    @Override
    public int getMaxMessageThreshold() {
        return maxMessageThreshold;
    }

    @Override
    public void setMaxMessageThreshold(int maxMessageThreshold) {
        this.maxMessageThreshold = maxMessageThreshold;
    }
}
