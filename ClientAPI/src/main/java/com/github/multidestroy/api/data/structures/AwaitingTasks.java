package com.github.multidestroy.api.data.structures;

import com.github.multidestroy.api.APIAdapter;
import com.github.multidestroy.utils.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AwaitingTasks {

    public final static long EXPIRATION_TIME_MILLIS = 10000L;
    private final ScheduledExecutorService expiredTaskRemover = Executors.newScheduledThreadPool(1);
    private final Map<Integer, Pair<Instant, APIAdapter>> awaitingTasks = new HashMap<>();
    private final AtomicInteger IDSender = new AtomicInteger();

    public AwaitingTasks() {
        expiredTaskRemover.scheduleAtFixedRate(this::removeExpired, EXPIRATION_TIME_MILLIS, 1000, TimeUnit.MILLISECONDS);
    }

    public int addNewAwaitingTask(APIAdapter adapter) {
        int taskID = IDSender.incrementAndGet();
        var pair = new Pair<>(Instant.now(), adapter);

        synchronized (awaitingTasks) {
            awaitingTasks.put(taskID, pair);
        }

        return taskID;
    }

    public void complete(int ID, Object object) {
        synchronized (awaitingTasks) {
            var adapter = awaitingTasks.remove(ID).getSecond();
            adapter.onRespond(object);
        }
    }

    public boolean isTaskWaiting(int taskID) {
        return awaitingTasks.containsKey(taskID);
    }

    private void removeExpired() {
        var now = Instant.now();
        var expiredTasksList = awaitingTasks.entrySet().stream()
                .filter(entry -> isExpired(now, entry.getValue().getFirst()))
                .collect(Collectors.toList());

        var expiredKeysList =
                expiredTasksList.stream()
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        var expiredValuesList =
                expiredTasksList.stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList());


        synchronized (awaitingTasks) {
            awaitingTasks.keySet().removeAll(expiredKeysList);
        }

        expiredValuesList.forEach(this::onFail);
    }

    private void onFail(Pair<Instant, APIAdapter> pair) {
        var adapter = pair.getSecond();
        adapter.fail();
    }

    private void onRespond(Pair<Instant, APIAdapter> pair, Object object) {
        var adapter = pair.getSecond();
        adapter.onRespond(object);
    }

    private static boolean isExpired(Instant now, Instant time) {
        return time.plusMillis(EXPIRATION_TIME_MILLIS).isBefore(now);
    }
}
