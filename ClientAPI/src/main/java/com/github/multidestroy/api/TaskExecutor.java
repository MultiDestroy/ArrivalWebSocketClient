package com.github.multidestroy.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class TaskExecutor {

    private final ExecutorService executor;

    public TaskExecutor() {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(numberOfCores);
    }

    public Map<Runnable, Future<?>> scheduleTasks(List<Runnable> tasks) {
        Map<Runnable, Future<?>> map = new HashMap<>();
        for (var task : tasks) {
            var future = executor.submit(task);
            map.put(task, future);
            //TODO: change runnable to Callable
        }

        return Collections.unmodifiableMap(map);
    }
}
