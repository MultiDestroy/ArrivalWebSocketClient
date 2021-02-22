package com.github.multidestroy.api;

import com.github.multidestroy.api.data.structures.ActionsConsumers;
import com.github.multidestroy.api.data.structures.AwaitingTasks;
import com.github.multidestroy.api.data.structures.MessageObject;
import com.github.multidestroy.manager.ServerManager;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class APIManager {

    private final AwaitingTasks awaitingTasks;
    private final TaskExecutor taskExecutor;
    private final ActionsConsumers actionsConsumers;

    APIManager() {
        this.awaitingTasks = new AwaitingTasks();
        this.taskExecutor = new TaskExecutor();
        this.actionsConsumers = new ActionsConsumers();
    }

    void processDeliveredMessage(MessageObject object) {
        var actionCode = ActionCode.getFromID(object.getActionId());

        List<Runnable> consumersToExecute =
                actionsConsumers.getConsumers(actionCode).stream()
                        .<Runnable>map(consumer -> () -> consumer.accept(object.getBody()))
                        .collect(Collectors.toList());

        if (object.getTaskId() != null)
            awaitingTasks.complete(object.getTaskId(), object.getBody());
        taskExecutor.scheduleTasks(consumersToExecute);
    }


    MessageObject registerTask(ActionCode actionCode, Object object, APIAdapter adapter) {
        int taskID = awaitingTasks.addNewAwaitingTask(adapter);
        adapter.onRequest();

        return new MessageObject(actionCode.ID, taskID, object);
    }

    public synchronized void addConsumers(ActionCode code, List<Consumer<Object>> consumers) {
        consumers.forEach(func -> actionsConsumers.addConsumer(code, func));
    }

}
