package com.github.multidestroy.api;

import com.github.multidestroy.api.data.structures.MessageObject;
import com.github.multidestroy.manager.ServerManager;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class APIClient extends WebSocketClient {

    private final APIManager manager;
    private final APIReconnector reconnector;

    public APIClient(URI serverUri) {
        super(serverUri);
        this.manager = new APIManager();
        this.reconnector = new APIReconnector(this);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected with the server");
    }

    @Override
    public void onMessage(String json) {
        Gson gson = new Gson();
        MessageObject messageObject = gson.fromJson(json, MessageObject.class);

        manager.processDeliveredMessage(messageObject);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (remote) {
            System.out.println("Server has been closed!");
        }

        startReconnectingIfNotRunning();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void executeTask(ActionCode code, Object object, APIAdapter adapter) {
        Gson gson = new Gson();
        var messageObject = manager.registerTask(code, object, adapter);

        var json = gson.toJson(messageObject);
        super.send(json);
    }

    public APIManager getManager() {
        return manager;
    }

    private void startReconnectingIfNotRunning() {
        if (!this.reconnector.isRunning()) {
            try {
                Thread.sleep(APIReconnector.SLEEP_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            new Thread(reconnector).start();
        }
    }

}