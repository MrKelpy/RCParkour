package com.mrkelpy.rcparkour;

import com.mrkelpy.rcparkour.commons.commands.PluginCommandHandler;
import com.mrkelpy.rcparkour.commons.database.ParkourCourseDB;
import com.mrkelpy.rcparkour.commons.events.OnPlayerJoin;
import com.mrkelpy.rcparkour.commons.events.PressurePlateEvents;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RCParkour extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("RCParkour has been enabled!");
        getCommand("parkour").setExecutor(new PluginCommandHandler(new CommandImplementations()));

        this.getServer().getPluginManager().registerEvents(new PressurePlateEvents(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        ParkourCourseDB.connect();
    }

    @Override
    public void onDisable() {
        getLogger().info("RCParkour has been disabled!");
    }
}

