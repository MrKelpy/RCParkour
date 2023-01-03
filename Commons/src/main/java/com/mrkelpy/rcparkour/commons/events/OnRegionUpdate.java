package com.mrkelpy.rcparkour.commons.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Iterator;

/**
 * This class
 */
public class OnRegionUpdate implements Listener {

    /**
     * Check if any players enter a WorldGuardHook region. If so, call OnRegionUpdate.
     * @param event The event that is called when a player moves.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(event.getPlayer().getWorld()));
        if (regionManager == null) return;

        ApplicableRegionSet regions = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(event.getPlayer().getLocation()));
        if ((long) regions.getRegions().size() > 0) onRegionUpdate(event.getPlayer(), regions);
    }

    /**
     * Called when a player enters or leaves a WorldGuardHook region.
     * Check if a player is inside a region called "parkour", if so, display the top leaderboard.
     * @param player The player that entered or left a region.
     * @param regions The regions that the player entered or left.
     */
    public void onRegionUpdate(Player player, ApplicableRegionSet regions) {

        // Check if a player is inside at least one region called "parkour".
        for (Iterator<ProtectedRegion> iterator = regions.iterator(); iterator.hasNext(); ) {
            ProtectedRegion region = iterator.next();
            if (region.getId().equalsIgnoreCase("parkour")) break;

            // If after checking all the regions, the player is not in a region named "parkour", return.
            if (!iterator.hasNext()) return;
        }

        // TODO: Query the top 10 leaderboard and display it to the player in a scoreboard
    }

}

