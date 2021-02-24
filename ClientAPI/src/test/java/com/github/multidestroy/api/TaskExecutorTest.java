package com.github.multidestroy.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.function.Predicate;

class TaskExecutorTest {

    private TaskExecutor taskExecutor;

    @BeforeEach
    public void prepare() {
        taskExecutor = new TaskExecutor();
    }

    @Test
    public void testRunning() throws InterruptedException {
        var listOfRunnable = new ArrayList<Runnable>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            listOfRunnable.add(() -> System.out.println(finalI + ": I have been displayed!"));
        }

        var tasks = taskExecutor.scheduleTasks(listOfRunnable);

        Thread.sleep(3_000);

        var undoneTasks = tasks.values().stream()
                .filter(Predicate.not(Future::isDone))
                .count();

        Assertions.assertEquals(undoneTasks, 0);
    }

}