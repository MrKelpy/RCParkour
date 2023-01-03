package com.mrkelpy.rcparkour.commons.utils;

import com.mrkelpy.rcparkour.commons.enums.PluginConstants;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Date;

public class CourseUtils {

    /**
     * Starts a course for the specified player, setting their course properties in the PDC
     * and letting them know.
     * @param player     The player to start the course for.
     */
    public static void startCourse(Player player) {

        // The player can only start a course if they are not already in one.
        if (player.getPersistentDataContainer().has(PluginConstants.EntrypointCoordinates, PersistentDataType.INTEGER_ARRAY)) {
            player.sendMessage("§cSorry! You can't start a course while you're already in one!");
            return;
        }

        // Set the player's course properties.
        player.getPersistentDataContainer().set(PluginConstants.CourseStartingTimestamp, PersistentDataType.LONG, new Date().getTime());
        player.sendMessage("§aYou're now competing in the parkour course, GO!");
    }

    /**
     * Clears the player's course properties from their PDC.
     * @param player The player to clear the course properties for.
     */
    public static void clearProperties(Player player) {

        PersistentDataContainer playerPDC = player.getPersistentDataContainer();

        playerPDC.remove(PluginConstants.CourseStartingTimestamp);
        playerPDC.remove(PluginConstants.LastCheckpoint);
        playerPDC.remove(PluginConstants.EntrypointCoordinates);
    }
}

