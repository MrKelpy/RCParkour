package com.mrkelpy.rcparkour.commons.database;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mrkelpy.rcparkour.commons.configuration.PluginConfigs;

import java.util.*;

/**
 * This class implements a database that stores any information related to the parkour courses.
 */
public class ParkourCourseDB {

    public static final ParkourCourseDB INSTANCE = new ParkourCourseDB();
    public static DB DATABASE;

    private ParkourCourseDB() {}

    /**
     * Estabilishes a connection to the database and prepares it for use internally
     */
    public static void connect() {
        String uri = Objects.requireNonNull(PluginConfigs.INSTANCE.getConfig().getString("database-uri"));
        MongoClient MONGO_CLIENT = new MongoClient(new MongoClientURI(uri));
        DATABASE = MONGO_CLIENT.getDB("rcparkour");
    }

    /**
     * Adds a new Document into a given collection of the database.
     * @param document The document to add to the collection
     */
    public static void add(DBObject document, String collectionName) {
        DATABASE.getCollection(collectionName).insert(document);
    }

    /**
     * Removes a document from a given collection of the database based on a query.
     * @param query The query to use to find the document to remove
     */
    public static void remove(DBObject query, String collectionName) {
        DATABASE.getCollection(collectionName).findAndRemove(query);
    }

    /**
     * Updates the first found document from a given collection of the database based on a query.
     * @param query          The query to find the document to update
     * @param document       The document to update the found document with
     * @param collectionName The name of the collection to update the document in
     */
    public static void update(DBObject query, DBObject document, String collectionName) {
        DBObject targetObject = DATABASE.getCollection(collectionName).find(query).one();

        // If the target object is null, then it doesn't exist in the database, so add it.
        if (targetObject == null) {
            ParkourCourseDB.add(document, collectionName);
            return;
        }

        // Only update the document if the new completion time is lower than the old one.
        if ((Long) document.get("completionTime") > (Long) targetObject.get("completionTime")) return;
        DATABASE.getCollection(collectionName).update(targetObject, document);
    }

    /**
     * Gets the top 10 leaderboard of completionTimes in the 'timings' collection.
     * @return List(TimingsDocument) The top 10 fastest completionTimes.
     */
    public static List<TimingsDocument> getLeaderboard() {

        // Gets all the Documents in the 'timings' collection, and sorts them in descending order, limiting the queries to the top 10.
        DBCursor cursor = DATABASE.getCollection("timings").find().sort(new BasicDBObject("completionTime", 1)).limit(10);
        List<TimingsDocument> leaderboard = new ArrayList<>();

        // Iterates through the cursor and adds each document to the leaderboard in the form of a TimingsDocument.
        for (DBObject object : cursor)
            leaderboard.add(new TimingsDocument(UUID.fromString(object.get("userUUID").toString()), (Long) object.get("completionTime")));

        return leaderboard;
    }

}

