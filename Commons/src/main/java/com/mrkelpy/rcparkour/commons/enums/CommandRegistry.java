package com.mrkelpy.rcparkour.commons.enums;

/**
 * This class holds all the existent commands, their needed permissions, the description, and
 * usage.
 */
public enum CommandRegistry {

    MASTER("/parkour", "parkour.*", "Master command", false),
    HELP("/parkour help", "parkour.help", "Displays the help menu."),
    ENTRYPOINT("/parkour entrypoint <course name>", "parkour.entrypoint.create", "Creates a new parkour entrypoint."),
    CHECKPOINT("/parkour checkpoint <course name>", "parkour.checkpoint.create", "Creates a new parkour checkpoint."),
    ENDPOINT("/parkour endpoint <course name>", "parkour.endpoint.create", "Creates a new parkour endpoint."),
    BACK("/parkour back", "parkour.back", "Teleports you to the last checkpoint you passed."),
    RESET("/parkour reset", "parkour.reset", "Resets your current parkour course."),
    EXIT("/parkour exit", "parkour.exit", "Exits your current parkour course, performing a reset on both the progress and stats.");

    private final String usage;
    private final String permission;
    private final String description;
    private final boolean visible;

    /**
     * Main constructor for the CommandRegistry enum.
     *
     * @param usage       The usage of the command.
     * @param permission  The permission needed to execute the command.
     * @param description The command description.
     * @param visible     Whether the command should be visible in the help menu or not.
     */
    CommandRegistry(String usage, String permission, String description, boolean visible) {
        this.usage = usage;
        this.permission = permission;
        this.description = description;
        this.visible = visible;
    }

    /**
     * Shortcut constructor for the CommandRegistry enum. Sets visibility to true.
     *
     * @param usage       The usage of the command.
     * @param permission  The permission needed to execute the command.
     * @param description The command description.
     */
    CommandRegistry(String usage, String permission, String description) {
        this(usage, permission, description, true);
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVisible() {
        return visible;
    }

}

