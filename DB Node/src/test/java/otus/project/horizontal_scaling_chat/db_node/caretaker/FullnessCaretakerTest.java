package otus.project.horizontal_scaling_chat.db_node.caretaker;

import org.junit.Before;
import org.junit.Test;
import otus.project.horizontal_scaling_chat.db_node.db.service.MessageService;
import otus.project.horizontal_scaling_chat.utils.MapBuilder;

import static org.mockito.Mockito.*;

public class FullnessCaretakerTest {
    private MessageService messageService;
    private FullnessCaretaker fullnessCaretaker;

    @Before
    public void setup() {
        messageService = mock(MessageService.class);
        fullnessCaretaker = spy(new FullnessCaretaker(messageService));
    }

    @Test
    public void run() throws InterruptedException {
        fullnessCaretaker.setMinFreeSpace(10_000L * 1024L * 1024L * 1024L);
        fullnessCaretaker.run();
        Thread.sleep(500);
        verify(fullnessCaretaker, times(1)).clearMessages();
    }

    @Test
    public void run2() throws InterruptedException {
        fullnessCaretaker.setMinFreeSpace(1);
        fullnessCaretaker.run();
        Thread.sleep(500);
        verify(fullnessCaretaker, never()).clearMessages();
    }

    @Test
    public void clearMessages() {
        doReturn(new MapBuilder<Long, Long>()
                .put(1L, 11L)
                .put(2L, 40L)
                .put(3L, 1L)
                .build()
        ).when(messageService).count();

        fullnessCaretaker.setMinMessageThreshold(10);
        fullnessCaretaker.setClearingProportion(0.1f);
        fullnessCaretaker.setMaxMessageThreshold(30);
        fullnessCaretaker.setClearingAmount(5);

        fullnessCaretaker.clearMessages();

        verify(messageService, times(1)).clearLast(1);
        verify(messageService, times(1)).clearLast(5);
    }

    @Test
    public void clearMessages2() {
        doReturn(new MapBuilder<Long, Long>()
                .put(1L, 11L)
                .put(2L, 40L)
                .put(3L, 1L)
                .build()
        ).when(messageService).count();

        fullnessCaretaker.setMinMessageThreshold(100);
        fullnessCaretaker.setMaxMessageThreshold(300);
        doNothing().when(fullnessCaretaker).alarm();

        fullnessCaretaker.clearMessages();

        verify(messageService, never()).clearLast(anyInt());
        verify(fullnessCaretaker, times(1)).alarm();
    }
}
