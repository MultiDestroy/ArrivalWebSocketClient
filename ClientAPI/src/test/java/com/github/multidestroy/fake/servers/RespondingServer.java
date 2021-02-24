package com.github.multidestroy.fake.servers;

import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;

public class RespondingServer extends AbstractFakeServer {

    public RespondingServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message arrived! Sending it back!");
        conn.send(message);
    }
}
