package com.mrkelpy.rcparkour.commons.enums;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Logger;

public class PluginConstants {

    public static String PLUGIN_NAME = "RCParkour";
    public static Logger LOGGER = Bukkit.getLogger();

    /**
     * (Long) The timestamp of when the player started the course.
     */
    public static NamespacedKey CourseStartingTimestamp = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME), "courseStartingTimestamp");

    /**
     * (String) The type of point for a given pressure plate. This can be either a checkpoint, entrypoint or endpoint.
     */
    public static NamespacedKey CoursePointType = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME), "coursePointType");

    /**
     * (String) The name of the last checkpoint the player passed.
     */
    public static NamespacedKey LastCheckpoint = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME), "lastCheckpoint");

    /**
     * (Integer[]) The coordinates of the entrypoint for the current course.
     */
    public static NamespacedKey EntrypointCoordinates = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME), "entrypointCoordinates");

    /**
     * (Float) The horizontal orientation of the player's head when they start the course.
     */
    public static NamespacedKey PointYawAngle = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin(PLUGIN_NAME), "pointYawAngle");
}

