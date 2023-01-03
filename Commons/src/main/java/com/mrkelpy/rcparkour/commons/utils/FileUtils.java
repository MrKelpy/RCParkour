package com.mrkelpy.rcparkour.commons.utils;

import com.mrkelpy.rcparkour.commons.configuration.InternalConfigs;
import com.mrkelpy.rcparkour.commons.enums.CompatibilityMode;
import com.mrkelpy.rcparkour.commons.enums.PluginConstants;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileUtils {


    /**
     * Creates a directory with a path relative to the parent path.
     *
     * @param path The path to the directory to create.
     * @return The created directory.
     */
    public static File makeDirectory(File parentPath, String path) {
        File directory = new File(parentPath, path);

        if (!directory.exists()) directory.mkdirs();
        return directory;
    }

    /**
     * Creates a directory with a path relative to the plugin's data folder.
     *
     * @param path The path to the directory to create.
     * @return The created directory.
     */
    public static File makeDirectory(String path) {
        File directory = new File(Bukkit.getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME).getDataFolder(), path);

        if (!directory.exists()) directory.mkdirs();
        return directory;
    }

    /**
     * Reads a file and returns a String containing the data.
     *
     * @return The data in the file
     */
    public static String readFile(File file) {

        try {
            // Creates a new file if it doesn't exist, and returns a string with the data in it.
            if (!file.exists()) file.createNewFile();
            return new String(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            // If any error happens, just throw null.
            return null;
        }
    }

    /**
     * Writes a string to a file.
     *
     * @param data The string to write to the file.
     */
    public static void writeFile(File file, String data) {

        try {
            // Creates a new file if it doesn't exist, and writes the data to it.
            if (!file.exists()) file.createNewFile();
            Files.write(file.toPath(), data.getBytes());

        } catch (IOException ignored) {
        }

    }
}

