package com.mrkelpy.rcparkour.commons.configuration;

import com.mrkelpy.rcparkour.commons.enums.PluginConstants;
import com.mrkelpy.rcparkour.commons.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * This class implements a plugin configuration that stores any needed values.
 */
public class PluginConfigs {

    public static final PluginConfigs INSTANCE = new PluginConfigs();
    private final File configFile = new File(Bukkit.getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME).getDataFolder(), "config.yml");
    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(this.configFile);

    /**
     * Main constructor for the PluginConfigs class. Just adds the default values to the config
     * and saves it.
     */
    public PluginConfigs() {
        this.addDefaults();
        this.save();
    }

    /**
     * Returns the YamlConfiguration object to use to get and set values.
     *
     * @return The player's name
     */
    public YamlConfiguration getConfig() {
        return this.config;
    }

    /**
     * Saves the config into memory.
     */
    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the default data values to the file.
     */
    private void addDefaults() {

        if (!this.getConfig().contains("database-uri")) {
            this.getConfig().set("database-uri", "mongodb://localhost:27017");
        }

        this.save();
    }
}

