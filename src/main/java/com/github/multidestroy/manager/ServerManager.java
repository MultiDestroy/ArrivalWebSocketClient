package com.github.multidestroy.manager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.time.Duration;

public class ServerManager {

    private final ServerJoinBlockade serverJoinBlockade;
    private final ProxyServer proxy;
    private final Plugin plugin;

    public ServerManager(Plugin plugin) {
        this.plugin = plugin;
        this.proxy = ProxyServer.getInstance();
        this.serverJoinBlockade = new ServerJoinBlockade();
    }

    public void closeServer(Duration timeToClose) {
        proxy.getScheduler().runAsync(plugin, new ServerCloser(this, timeToClose));
    }

    public void kickPlayersFromProxyServer(TextComponent message) {
        proxy.getPlayers().forEach(
                player -> player.disconnect(message)
        );
    }

    public void openProxyServer() {
        proxy.getPluginManager().unregisterListener(serverJoinBlockade);
    }

    public void suspendProxyServer(TextComponent message) {
        proxy.getPluginManager().registerListener(plugin, serverJoinBlockade);
        kickPlayersFromProxyServer(message);
    }

}
