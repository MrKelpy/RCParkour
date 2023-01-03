package com.mrkelpy.rcparkour.commons.events;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mrkelpy.rcparkour.commons.database.ParkourCourseDB;
import com.mrkelpy.rcparkour.commons.database.TimingsDocument;
import com.mrkelpy.rcparkour.commons.scoreboards.TopTimingsLeaderboard;
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
import java.util.List;

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

        // Get the region manager for the world the player is in.
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(event.getPlayer().getWorld()));
        if (regionManager == null) return;

        // Get all the player's current regions based on their location.
        ApplicableRegionSet regions = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(event.getPlayer().getLocation()));

        // If the player is at least in one region, call OnRegionUpdate.
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

            // If after checking all the regions, the player is not in a region named "parkour", hide the scoreboard and return.
            if (!iterator.hasNext() && player.getScoreboard().getObjective("topTimings") != null) TopTimingsLeaderboard.INSTANCE.hideFromPlayer(player);
            if (!iterator.hasNext()) return;
        }

        // Create a scoreboard to display the top 10 completion times.
        TopTimingsLeaderboard leaderboard = TopTimingsLeaderboard.INSTANCE;
        List<TimingsDocument> completionTimes = ParkourCourseDB.getLeaderboard();

        // Clear the scoreboard and add the top 10 completion times.
        leaderboard.clearLeaderboard();
        completionTimes.forEach(timing -> leaderboard.addEntry("§d" + timing.getPlayer().getName(), "§a" + timing.getFormattedTime()));
        leaderboard.addBlank();

        // Add the player's own completion time.
        DBObject playerQuery = ParkourCourseDB.DATABASE.getCollection("timings").find(new BasicDBObject("userUUID", player.getUniqueId().toString())).one();
        leaderboard.addEntry("§bPersonal Best: §a", playerQuery != null ? new TimingsDocument(playerQuery).getFormattedTime() : "N/A");
        leaderboard.showToPlayer(player);
    }

}

