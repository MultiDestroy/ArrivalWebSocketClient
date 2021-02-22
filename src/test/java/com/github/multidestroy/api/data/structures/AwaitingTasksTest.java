package com.github.multidestroy.api.data.structures;

import com.github.multidestroy.api.APIAdapter;
import com.github.multidestroy.api.data.structures.AwaitingTasks;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AwaitingTasksTest {

    @Mock
    private APIAdapter adapter;
    private AwaitingTasks awaitingTasks;

    @BeforeEach
    public void prepare() {
        this.awaitingTasks = new AwaitingTasks();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddingTask() {
        Assertions.assertFalse(this.awaitingTasks.isTaskWaiting(1));
        this.awaitingTasks.addNewAwaitingTask(adapter);
        Assertions.assertTrue(this.awaitingTasks.isTaskWaiting(1));
    }

    @RepeatedTest(4)
    @DisplayName("Test if ID of every new task is different that the other task created at the same time.")
    public void testDifferentIDofNewTasks() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        var latch = new CountDownLatch(30);

        for (int i = 0; i < 30; i++)
            new Thread(() -> addTasks(counter, latch)).start();

        latch.await();

        var lastNumber = this.awaitingTasks.addNewAwaitingTask(adapter);
        counter.incrementAndGet();
        Assertions.assertEquals(lastNumber, counter.get());
    }

    @Test
    public void testDeletingTasks() throws InterruptedException {
        int taskID = this.awaitingTasks.addNewAwaitingTask(adapter);

        Thread.sleep(AwaitingTasks.EXPIRATION_TIME_MILLIS + 2_000);

        Assertions.assertFalse(this.awaitingTasks.isTaskWaiting(taskID));
    }

    @Test
    public void testFinishingTask() {
        var taskID = this.awaitingTasks.addNewAwaitingTask(adapter);
        var object = new Object();
        this.awaitingTasks.complete(taskID, object);

        Assertions.assertFalse(this.awaitingTasks.isTaskWaiting(taskID));
    }


    public void addTasks(AtomicInteger counter, CountDownLatch latch) {
        for (int i = 0; i < 10_000; i++) {
            this.awaitingTasks.addNewAwaitingTask(adapter);
            counter.incrementAndGet();
        }

        latch.countDown();
    }
}
