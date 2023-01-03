package com.mrkelpy.rcparkour.commons.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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
     * @param name           The name of the player.
     * @param completionTime The time it took the player to complete the course.
     */
    public void addEntry(String name, String completionTime) {

        int scoreIndex = this.board.getEntries().size() + 1;
        objective.getScore("§c" + name + ": §a" + completionTime).setScore(scoreIndex);
    }

    /**
     * Adds a blank line into the scoreboard.
     */
    public void addBlank() {
        this.addEntry(" ", " ");
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

