package com.mrkelpy.rcparkour.commons.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mrkelpy.rcparkour.commons.enums.CommandRegistry;
import com.mrkelpy.rcparkour.commons.enums.PluginConstants;
import com.mrkelpy.rcparkour.commons.utils.CourseUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * This interface allows for an explicit declaration of the methods that will contain the actual method implementations,
 * and is needed to ensure that they exist, and for them to be called (in their actual implementation) through Reflection in the PluginCommandHandler.
 */
public interface ICommandImplementations {

    /**
     * The default implementation of this method iterates through every registered command and
     * builds a help menu command with the command usage, the description, and the needed permissions for every command.
     *
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean helpCommand(CommandSender commandSender, String[] args) {

        commandSender.sendMessage(String.format("§e----- §c%s Command List§e-----", PluginConstants.PLUGIN_NAME));

        for (CommandRegistry command : CommandRegistry.values()) {
            commandSender.sendMessage(String.format("§e%s §7-> §f%s", command.getUsage(), command.getDescription()) + "\n");
        }

        return true;
    }

    /**
     * Creates an initial point for the new parkour course, placing down a golden pressure plate.
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean entrypointCommand(CommandSender commandSender, String[] args) {
        return createPoint(commandSender, args, CommandRegistry.ENTRYPOINT, Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
    }

    /**
     * Creates an endpoint for the parkour course, placing down a golden pressure plate.
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean endpointCommand(CommandSender commandSender, String[] args) {
        return createPoint(commandSender, args, CommandRegistry.ENDPOINT, Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
    }

    /**
     * Creates a checkpoint for the specified parkour course, placing down an iron pressure plate.
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean checkpointCommand(CommandSender commandSender, String[] args) {
        return createPoint(commandSender, args, CommandRegistry.CHECKPOINT, Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
    }

    /**
     * Creates a point for a specific parkour course, placing down a given pressure plate type at the player's feet.
     * The point can be checkpoint, entrypoint or endpoint, based on the cmd registry that it is coming from; E.g. the entrypoint
     * command should pass in the CommandRegistry.ENTRYPOINT registry, which will then set the point type to "entrypoint".
     * @param commandSender     The sender of the command.
     * @param args              The command arguments.
     * @param cmdRegistry       The command registry that this method is being called from.
     * @param pressurePlateType The type of pressure plate to place down.
     * @return Boolean, Feedback to the sender.
     */
    default boolean createPoint(CommandSender commandSender, String[] args, CommandRegistry cmdRegistry, Material pressurePlateType) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        // Sets the gold pressure plate at the player's feet and changes its NBT data to include the point type and name.
        Player author = (Player) commandSender;
        author.getLocation().getBlock().setType(pressurePlateType);

        PersistentDataContainer blockPDC = new CustomBlockData(author.getLocation().getBlock(), Bukkit.getServer().getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME));
        blockPDC.set(PluginConstants.CoursePointType, PersistentDataType.STRING, cmdRegistry.name().toLowerCase());
        blockPDC.set(PluginConstants.PointYawAngle, PersistentDataType.FLOAT, author.getLocation().getYaw());

        // Lets the user know that the point has been created and teleports them one block back.
        commandSender.sendMessage("§eCreated a new §b" + cmdRegistry.name().toLowerCase() + " §efor the parkour §ecourse.");
        author.teleport(author.getLocation().add(author.getFacing().getDirection().multiply(-1)));
        return true;
    }

    /**
     * Teleports the player to the last saved checkpoint.
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean backCommand(CommandSender commandSender, String[] args) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        int[] lastCheckpoint = player.getPersistentDataContainer().get(PluginConstants.LastCheckpoint, PersistentDataType.INTEGER_ARRAY);

        // If the player has no last checkpoint, cancel the teleport
        if (lastCheckpoint == null) {
            player.sendMessage("§cYou don't have a checkpoint to go back to!");
            return true;
        }

        player.teleport(new Location(player.getWorld(), lastCheckpoint[0] + 0.5, lastCheckpoint[1] + 0.5, lastCheckpoint[2] + 0.5, lastCheckpoint[3], 0));
        return true;
    }

    /**
     * Teleports the player to the beginning of the course.
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean resetCommand(CommandSender commandSender, String[] args) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        int[] entryPoint = player.getPersistentDataContainer().get(PluginConstants.EntrypointCoordinates, PersistentDataType.INTEGER_ARRAY);

        // If the player has no entry point, cancel the teleport
        if (entryPoint == null) {
            player.sendMessage("§cYou can't reset the parkour course, because you haven't started one yet!");
            return true;
        }

        // Clears the player's properties and teleports them to the entry point
        player.teleport(new Location(player.getWorld(), entryPoint[0] + 0.5, entryPoint[1] + 0.5, entryPoint[2] + 0.5, entryPoint[3], 0));
        CourseUtils.clearProperties(player);
        player.sendMessage("§aYour progress has been reset!");
        return true;
    }

    /**
     * Resets the player's parkour course state (e.g. their current checkpoint, their time, etc.)
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean exitCommand(CommandSender commandSender, String[] args) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        // If the player isn't in a course, cancel the command
        if (!player.getPersistentDataContainer().has(PluginConstants.EntrypointCoordinates, PersistentDataType.INTEGER_ARRAY)) {
            player.sendMessage("§cYou're not in any parkour course yet!");
            return true;
        }

        // Resets the player's position to the beginning of the course and clears their properties (Duplicate of reset)
        CourseUtils.clearProperties(player);
        player.sendMessage("§aYou are no longer in a parkour course!");

        return true;
    }
}

