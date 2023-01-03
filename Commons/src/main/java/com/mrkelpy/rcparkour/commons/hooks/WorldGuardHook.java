package com.mrkelpy.rcparkour.commons.hooks;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WorldGuardHook {

    /**
     * Accesses the plugin manager and returns the WorldGuardHook plugin.
     * @return The WorldGuardHook plugin.
     */
    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuardHook");

        if (!(plugin instanceof WorldGuardPlugin)) return null;
        return (WorldGuardPlugin) plugin;
    }
}

