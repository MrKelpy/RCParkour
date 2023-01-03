package com.mrkelpy.rcparkour.commons.events;

import com.mrkelpy.rcparkour.commons.enums.PluginConstants;
import com.mrkelpy.rcparkour.commons.utils.CourseUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class OnPlayerJoin implements Listener {

    /**
     * When a player joins the server, we want to clear their course properties from their PDC, and
     * let them know that they have been cleared because they left the server with a course in progress.
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // If the player has course properties in their PDC, clear them and let them know.
        if (event.getPlayer().getPersistentDataContainer().has(PluginConstants.CourseStartingTimestamp, PersistentDataType.LONG)) {
            CourseUtils.clearProperties(event.getPlayer());
            event.getPlayer().sendMessage("§cYour previous course progress has been reset because you left the server midway through.");
        }

    }

}

