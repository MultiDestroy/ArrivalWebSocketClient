package com.github.multidestroy.fake.servers;

import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;

public class NotRespondingServer extends AbstractFakeServer {

    public NotRespondingServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message arrived, but server will not respond!");
    }

}
