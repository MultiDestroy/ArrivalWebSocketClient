package com.github.multidestroy.manager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class ServerCloser implements Runnable {

    private final ServerManager serverManager;
    private final Duration closureTime;
    public final AtomicBoolean stopped;
    private static final String NOTIFICATION_OF_CLOSURE = ChatColor.RED + "ATTENTION! Server will be closed within 30 seconds!";
    private static final String ABORT = ChatColor.GREEN + "Server shutdown has been stopped!";
    private static final String CLOSURE_MESSAGE = ChatColor.RED + "Server has been CLOSED!";
    private static final long LONG_SLEEP_TIME = 10000;
    private static final long SHORT_SLEEP_TIME = 1000;

    public ServerCloser(ServerManager serverManager, Duration closureTime) {
        this.stopped = new AtomicBoolean(false);
        this.closureTime = closureTime;
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        var proxy = ProxyServer.getInstance();

        if (longExpectancy(proxy, closureTime) || shortExpectancy(proxy)) {
            proxy.broadcast(TextComponent.fromLegacyText(ABORT));
            return;
        }

        serverManager.suspendProxyServer(new TextComponent(CLOSURE_MESSAGE));
    }


    private boolean longExpectancy(ProxyServer proxyServer, Duration duration) {
        while (duration.getSeconds() > 5) {
            try {
                Thread.sleep(LONG_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (stopped.get())
                return true;

            sendClosureNotification(proxyServer, duration.getSeconds());
            duration = duration.minus(LONG_SLEEP_TIME, ChronoUnit.MILLIS);
        }

        return false;
    }

    private boolean shortExpectancy(ProxyServer proxyServer) {
        var duration = Duration.ofSeconds(5);
        while (!duration.isNegative()) {
            try {
                Thread.sleep(SHORT_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (stopped.get())
                return true;

            sendClosureNotification(proxyServer, duration.getSeconds());
            duration = duration.minus(SHORT_SLEEP_TIME, ChronoUnit.MILLIS);
        }

        return false;
    }

    private void sendClosureNotification(ProxyServer proxyServer, long secondsToClose) {
        var message = TextComponent.fromLegacyText(NOTIFICATION_OF_CLOSURE + " " + secondsToClose);

        proxyServer.broadcast(message);
    }

}
