package com.github.multidestroy.api.data.structures;

public class MessageObject {

    private Integer actionId;
    private Integer taskId;
    private Object body;

    public MessageObject(int actionId, int taskId, Object body) {
        this.actionId = actionId;
        this.taskId = taskId;
        this.body = body;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
