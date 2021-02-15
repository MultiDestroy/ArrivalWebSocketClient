package com.github.multidestroy.manager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

class ServerJoinBlockade implements Listener {

    private static final String BLOCKADE_MESSAGE = ChatColor.RED + "Server is closed!";

    @EventHandler
    public void onJoin(PreLoginEvent event) {
        var message = TextComponent.fromLegacyText(BLOCKADE_MESSAGE);
        event.setCancelReason(message);
    }

}
