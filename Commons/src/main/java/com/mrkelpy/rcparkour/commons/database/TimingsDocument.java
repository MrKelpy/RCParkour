package com.mrkelpy.rcparkour.commons.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * This class implements a document that stores the timings of a player's run in any given course.
 */
public class TimingsDocument {

    private UUID userUUID;
    private long completionTime;

    /**
     * Main constructor for the TimingsDocument class. Builds a TimingsDocument
     * object based on given parameters.
     * @param userUUID       The player's UUID
     * @param completionTime The time it took the player to complete the course
     */
    public TimingsDocument(UUID userUUID, long completionTime) {
        this.userUUID = userUUID;
        this.completionTime = Math.round(completionTime);
    }

    /**
     * Formats the timestamp into a readable format of Minutes:Seconds:Milliseconds.
     * @return The formatted timestamp as a string
     */
    public String getFormattedTime() {
        return new SimpleDateFormat("mm:ss:SSS").format(new Date(this.completionTime));
    }

    /**
     * Returns a player by their UUID.
     * @return (OfflinePlayer) The player
     */
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.userUUID);
    }

    /**
     * Secondary constructor for the TimingsDocument class. Builds a TimingsDocument object
     * based on a given DBObject.
     * @param dbObject The DBObject to build the TimingsDocument object from
     */
    public TimingsDocument(DBObject dbObject) {
        this.userUUID = UUID.fromString((String) dbObject.get("userUUID"));
        this.completionTime = (long) dbObject.get("completionTime");
    }

    /**
     * Modifies the TimingsDocument completionTime value.
     * @param completionTime The new completionTime value
     */
    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * Returns a DBObject that can be used to insert into the database based on the
     * values of this document.
     * @return The DBObject to insert into the database
     */
    public DBObject toDBObject() {

        return new BasicDBObject("userUUID", this.userUUID.toString())
                .append("completionTime", this.completionTime);
    }

}

