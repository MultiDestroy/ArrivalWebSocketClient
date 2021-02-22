package com.github.multidestroy.api;

import com.github.multidestroy.fake.FakeAdapter;
import com.github.multidestroy.fake.servers.NotRespondingServer;
import com.github.multidestroy.fake.servers.RespondingServer;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;

public class APIClientIT {

    private InetSocketAddress address = new InetSocketAddress(8887);
    private URI uri = URI.create("ws://0.0.0.0:8887");
    @Mock
    private boolean executedTestFunction1;
    private boolean executedTestFunction2;


    @BeforeEach
    public void prepare() {
        executedTestFunction1 = executedTestFunction2 = false;
    }


    @Test
    public void testReconnection() throws InterruptedException, IOException {
        APIClient client = new APIClient(uri);
        client.connectBlocking();

        Assertions.assertFalse(client.isOpen());
        Thread.sleep(10_000);
        Assertions.assertFalse(client.isOpen());

        WebSocketServer server = new NotRespondingServer(address);
        server.setReuseAddr(true);
        server.start();

        Thread.sleep(15_000);
        Assertions.assertTrue(client.isOpen());
        client.close();
        server.stop();
    }

    @Test
    public void testClientWhenRespondMessageIsValid() throws InterruptedException, IOException {
        var adapterOne = new FakeAdapter();
        var adapterTwo = new FakeAdapter();

        WebSocketServer server = new RespondingServer(address);
        server.setReuseAddr(true);
        server.start();

        APIClient client = new APIClient(uri);
        client.connectBlocking();

        client.getManager().addConsumers(ActionCode.TEST, List.of(this::testConsumer1));
        client.getManager().addConsumers(ActionCode.TEST2, List.of(this::testConsumer2));
        client.executeTask(ActionCode.TEST, "Hello world!", adapterOne);
        client.executeTask(ActionCode.TEST2, 1.0, adapterTwo);

        Thread.sleep(1_500);

        Assertions.assertTrue(executedTestFunction1 && executedTestFunction2);
        Assertions.assertTrue(adapterOne.wasRequestFuncExecuted() && adapterOne.wasRespondFuncExecuted() && !adapterOne.wasFailFuncExecuted());
        Assertions.assertTrue(adapterTwo.wasRequestFuncExecuted() && adapterTwo.wasRespondFuncExecuted() && !adapterTwo.wasFailFuncExecuted());

        client.close();
        server.stop();
    }

    @Test
    public void testClientWhenServerDoesNotRespond() throws InterruptedException, IOException {
        var adapterOne = new FakeAdapter();
        var adapterTwo = new FakeAdapter();
        WebSocketServer server = new NotRespondingServer(address);
        server.setReuseAddr(true);
        server.start();

        APIClient client = new APIClient(uri);
        client.connectBlocking();

        client.getManager().addConsumers(ActionCode.TEST, List.of(this::testConsumer1));
        client.getManager().addConsumers(ActionCode.TEST2, List.of(this::testConsumer2));
        client.executeTask(ActionCode.TEST, "Hello world!", adapterOne);
        client.executeTask(ActionCode.TEST2, 1.0, adapterTwo);

        Thread.sleep(11_000);

        Assertions.assertTrue(!executedTestFunction1 && !executedTestFunction2);
        Assertions.assertTrue(adapterOne.wasRequestFuncExecuted() && !adapterOne.wasRespondFuncExecuted() && adapterOne.wasFailFuncExecuted());
        Assertions.assertTrue(adapterTwo.wasRequestFuncExecuted() && !adapterTwo.wasRespondFuncExecuted() && adapterTwo.wasFailFuncExecuted());

        client.close();
        server.stop();
    }


    public void testConsumer1(Object object) {
        executedTestFunction1 = true;
        String message = (String) object;
        System.out.println("Delivered string message: " + message);
    }

    public void testConsumer2(Object object) {
        executedTestFunction2 = true;
        System.out.println("Delivered double message: " + object);
    }

}
