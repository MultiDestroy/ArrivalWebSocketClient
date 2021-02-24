package com.github.multidestroy.api;

import org.java_websocket.client.WebSocketClient;

class APIReconnector implements Runnable {

    private boolean isRunning = false;
    private final WebSocketClient client;
    public static final long SLEEP_TIME = 10_000;

    public APIReconnector(WebSocketClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        isRunning = true;

        while (!reconnectIfNotOpen()) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isRunning = false;
    }

    /**
     * @return True if client is connected, otherwise false.
     */

    private boolean reconnectIfNotOpen() {
        synchronized (client) {
            try {
                return client.isOpen() || client.reconnectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }
}
