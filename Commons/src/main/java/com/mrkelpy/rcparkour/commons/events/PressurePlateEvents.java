package com.mrkelpy.rcparkour.commons.events;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mongodb.BasicDBObject;
import com.mrkelpy.rcparkour.commons.database.ParkourCourseDB;
import com.mrkelpy.rcparkour.commons.database.TimingsDocument;
import com.mrkelpy.rcparkour.commons.enums.PluginConstants;
import com.mrkelpy.rcparkour.commons.utils.CourseUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Date;
import java.util.Objects;

/**
 * This class handles all the interactions between the player and the course points.
 */
public class PressurePlateEvents implements Listener {

    /**
     * Fired when a player interacts with an entrypoint pressure plate.
     * Starts a course for the player.
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onEntrypointPress(PlayerInteractEvent event) {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME);

        // Checks if the player is pressing a golden pressure plate
        if (!(event.getAction().equals(Action.PHYSICAL)) || event.getClickedBlock().getBlockPower() > 0) return;
        if (!(event.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE))) return;

        // Checks if the pressure plate is an entrypoint
        PersistentDataContainer blockPDC = new CustomBlockData(event.getClickedBlock(), plugin);
        if (!(blockPDC.get(PluginConstants.CoursePointType, PersistentDataType.STRING).equals("entrypoint"))) return;

        // Sets the entrypoint coordinates to the block the player is standing on.
        Float yaw = blockPDC.get(PluginConstants.PointYawAngle, PersistentDataType.FLOAT);
        Location entrypointLocation = event.getClickedBlock().getLocation().add(new Location(null, 0, 0, 0, yaw, 0).getDirection().multiply(-1));

        CourseUtils.startCourse(event.getPlayer());

        event.getPlayer().getPersistentDataContainer().set(PluginConstants.EntrypointCoordinates,
                PersistentDataType.INTEGER_ARRAY, new int[] {(int) entrypointLocation.getX(), (int) entrypointLocation.getY(), (int) entrypointLocation.getZ(), yaw.intValue(), 0});
    }

    /**
     * Fired when a player interacts with a checkpoint pressure plate.
     * Changes the last checkpoint data for the player into the current coordinates.
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onCheckpointPress(PlayerInteractEvent event) {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME);

        // Checks if the player is pressing a golden pressure plate
        if (!(event.getAction().equals(Action.PHYSICAL)) || event.getClickedBlock().getBlockPower() > 0) return;
        if (!(event.getClickedBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE))) return;

        // Checks if the pressure plate is a checkpoint
        PersistentDataContainer blockPDC = new CustomBlockData(event.getClickedBlock(), plugin);
        if (!(Objects.equals(blockPDC.get(PluginConstants.CoursePointType, PersistentDataType.STRING), "checkpoint"))) return;

        // The player can only set a checkpoint if they are in a course
        if (!event.getPlayer().getPersistentDataContainer().has(PluginConstants.EntrypointCoordinates, PersistentDataType.INTEGER_ARRAY)) {
            event.getPlayer().sendMessage("§cSorry! You can't set that checkpoint right now. ");
            return;
        }

        // Sets the checkpoint coordinates to the block the player is standing on
        Float yaw = blockPDC.get(PluginConstants.PointYawAngle, PersistentDataType.FLOAT);
        int[] coordinates = new int[] { event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), yaw.intValue(), 0};
        event.getPlayer().getPersistentDataContainer().set(PluginConstants.LastCheckpoint, PersistentDataType.INTEGER_ARRAY, coordinates);

        event.getPlayer().sendMessage("§aCheckpoint!");
    }

    /**
     * Fired when a player interacts with an endpoint pressure plate.
     * Ends the course for the player.
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onEndpointPress(PlayerInteractEvent event) {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME);

        // Checks if the player is pressing a golden pressure plate
        if (!(event.getAction().equals(Action.PHYSICAL)) || event.getClickedBlock().getBlockPower() > 0) return;
        if (!(event.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE))) return;

        // Checks if the pressure plate is an endpoint
        PersistentDataContainer blockPDC = new CustomBlockData(event.getClickedBlock(), plugin);
        if (!(Objects.equals(blockPDC.get(PluginConstants.CoursePointType, PersistentDataType.STRING), "endpoint"))) return;

        // The player can only end the course if they're in one.
        if (!event.getPlayer().getPersistentDataContainer().has(PluginConstants.EntrypointCoordinates, PersistentDataType.INTEGER_ARRAY)) {
            event.getPlayer().sendMessage("§cYou cannot finish a course you haven't started yet!");
            return;
        }

        // Clears the course properties for the player and adds their timing to the database.
        long completionTime = new Date().getTime() - event.getPlayer().getPersistentDataContainer().get(PluginConstants.CourseStartingTimestamp, PersistentDataType.LONG);
        TimingsDocument document = new TimingsDocument(event.getPlayer().getUniqueId(), completionTime);
        ParkourCourseDB.update(new BasicDBObject("userUUID", event.getPlayer().getUniqueId().toString()), document.toDBObject(), "timings");

        CourseUtils.clearProperties(event.getPlayer());
        event.getPlayer().sendMessage("§aYou finished the course in §b" + document.getFormattedTime() + "§a!");
    }

}

