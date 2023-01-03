package com.mrkelpy.rcparkour.commons.scoreboards;

import com.mrkelpy.rcparkour.commons.database.TimingsDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class implements a very convenient object that can be used to interact with
 */
public class TopTimingsLeaderboard {

    public static TopTimingsLeaderboard INSTANCE = new TopTimingsLeaderboard();
    private final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective objective;

    /**
     * Main constructor for the TopTimingsLeaderboard class.
     * Checks if the objective already exists in the scoreboard, and if not, creates it.
     */
    private TopTimingsLeaderboard() {

        if (board.getObjective("topTimings") == null) {
            this.objective = board.registerNewObjective("topTimings", "dummy", "§6§lTop 10 Completions");
            this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        this.objective = board.getObjective("topTimings");
    }

    /**
     * Clears all the scores from the scoreboard
     */
    public void clearLeaderboard() {
        board.getEntries().forEach(board::resetScores);
    }

    /**
     * Adds a score into the scoreboard.
     *
     * @param name           The name of the player.
     * @param completionTime The time it took the player to complete the course.
     * @return Whether the entry was added or not.
     */
    public void addEntry(String name, String completionTime) {

        // Check if the entry is already in the scoreboard, if so, return.
        String entryString = name + " > " + completionTime;
        if (board.getEntries().contains(entryString)) return;

        // Add the entry to the scoreboard.
        objective.getScore(entryString).setScore(0);
        board.getEntries().forEach(entry -> objective.getScore(entry).setScore(objective.getScore(entry).getScore() + 1));
    }

    /**
     * Adds a list of entries into the scoreboard.
     *
     * @param entries The list of entries to add.
     */
    public void addEntries(List<TimingsDocument> entries) {

        // Clear the scoreboard if the list of entries is empty
        if (entries.isEmpty()) {
            this.clearLeaderboard();
            return;
        }

        // If there are no new entries, return.
        if (entries.stream().allMatch(entry -> board.getEntries().stream().anyMatch(string -> string.equals("§d" + entry.getPlayer().getName() + " > §a" + entry.getFormattedTime())))) return;

        // If there are new entries, clear the scoreboard and add the new entries.
        this.clearLeaderboard();
        entries.forEach(entry -> this.addEntry("§d" + entry.getPlayer().getName(), "§a" + entry.getFormattedTime()));
    }

    /**
     * Adds a blank line into the scoreboard.
     */
    public void addBlank() {
        objective.getScore(" ").setScore(0);
        board.getEntries().forEach(entry -> objective.getScore(entry).setScore(objective.getScore(entry).getScore() + 1));
    }

    /**
     * Re-adds the last few lines of the scoreboard, corresponding to the personal best time of the player.
     * @param completionTime The time it took the player to complete the course.
     */
    public void addPersonalBest(String completionTime) {

        // If the exact same personal best is already in the scoreboard, then return.
        if (board.getEntries().contains("§bPersonal Best§a" + " > " + completionTime)) return;

        // Clear the personal best score and the blank line if they exist.
        List<String> entries = board.getEntries().stream().filter(entry -> entry.contains("Personal Best")).collect(Collectors.toList());

        if (!entries.isEmpty()) {
            board.resetScores(entries.get(0));
            board.resetScores(" ");
        }

        // Re-add them with the updated info
        this.addBlank();
        this.addEntry("§bPersonal Best§a", completionTime);
    }

    /**
     * Shows the leaderboard to a player.
     * @param player The player to show the leaderboard to.
     */
    public void showToPlayer(Player player) {
        player.setScoreboard(board);
    }

    /**
     * Hides the leaderboard from a player.
     * @param player The player to hide the leaderboard from.
     */
    public void hideFromPlayer(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }









}

