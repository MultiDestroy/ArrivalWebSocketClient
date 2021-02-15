package com.github.multidestroy.manager;

import com.github.multidestroy.api.APIClient;

class APIReconnector implements Runnable {

    private final APIClient client;
    private static final long SLEEP_TIME = 10000;

    public APIReconnector(APIClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (reconnectIfNotOpen()) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

}
