package com.github.multidestroy;

import com.github.multidestroy.api.APIClient;
import com.github.multidestroy.manager.ServerManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.URI;

public class BungeeWebSocket extends Plugin {

    private APIClient client;
    private static final String LOCAL_SERVER_URI = "ws://localhost:8887";

    @Override
    public void onEnable() {
        var manager = new ServerManager(this);
        var client = new APIClient(URI.create(LOCAL_SERVER_URI));
//        try {
//            if (!client.connectBlocking())
//           //     manager.reconnectWithWebSocketServer(client);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDisable() {

    }

}

