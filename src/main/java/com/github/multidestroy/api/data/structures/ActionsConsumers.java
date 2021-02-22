package com.github.multidestroy.api.data.structures;

import com.github.multidestroy.api.ActionCode;

import java.util.*;
import java.util.function.Consumer;

public class ActionsConsumers {

    private final Map<ActionCode, List<Consumer<Object>>> consumers;

    public ActionsConsumers() {
        this.consumers = new HashMap<>();

        //Adds all keys
        Arrays.stream(ActionCode.values())
                .map(k -> new AbstractMap.SimpleEntry<ActionCode, List<Consumer<Object>>>(k, new ArrayList<>()))
                .forEach(obj -> consumers.put(obj.getKey(), obj.getValue()));

    }

    public void addConsumer(ActionCode code, Consumer<Object> func) {
        consumers.compute(code, (actionCode, list) -> expandConsumersList(list, func));
    }

    public List<Consumer<Object>> getConsumers(ActionCode actionCode) {
        return List.copyOf(consumers.get(actionCode));
    }

    private static List<Consumer<Object>> expandConsumersList(List<Consumer<Object>> list, Consumer<Object> func) {
        if (list == null) {
            return new ArrayList<>();
        } else {
            list.add(func);
            return list;
        }
    }

}
