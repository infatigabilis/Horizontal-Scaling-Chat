package otus.project.horizontal_scaling_chat.db_node.caretaker;

import java.util.concurrent.TimeUnit;

public interface FullnessCaretakerMBean {
    long getCheckDelay();

    void setCheckDelay(long checkDelay);

    TimeUnit getCheckTimeUnit();

    void setCheckTimeUnit(TimeUnit checkTimeUnit);

    long getAlarmDelay();

    void setAlarmDelay(long alarmDelay);

    TimeUnit getAlarmTimeUnit();

    void setAlarmTimeUnit(TimeUnit alarmTimeUnit);

    long getMinFreeSpace();

    void setMinFreeSpace(long minFreeSpace);

    float getClearingProportion();

    void setClearingProportion(float clearingProportion);

    int getClearingAmount();

    void setClearingAmount(int clearingAmount);

    int getMinMessageThreshold();

    void setMinMessageThreshold(int minMessageThreshold);

    int getMaxMessageThreshold();

    void setMaxMessageThreshold(int maxMessageThreshold);
}
