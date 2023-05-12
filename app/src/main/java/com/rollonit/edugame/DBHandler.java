package com.rollonit.edugame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;

/**
 * This class handles the SQLite database that stores the high scores. It contains methods to add a new high score, get the highest score, and get the top 3 high scores.
 * It uses a local SQLite database to store the high scores.
 */
public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "scores.db";
    final int DBScoreLimit = 1000;
    Context parentContext;
    String createTableSql = "CREATE TABLE IF NOT EXISTS high_scores (id INTEGER PRIMARY KEY, score INTEGER, name TEXT);";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        parentContext = context;
    }

    /**
     * Get the highest score from the SQLite database.
     *
     * @return the highest score
     */
    public int getHS() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {"score"};

        Cursor cursor = db.query("high_scores", projection, null, null, null, null, "score DESC", "1");
        if (cursor.getCount() == 0) {
            return 0;
        }
        cursor.moveToFirst();
        int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
        cursor.close();
        return score;
    }

    /**
     * Show an error dialog to the user.
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(parentContext).setTitle("Error").setMessage(message).setPositiveButton("OK", null).show();
    }

    /**
     * Add a new high score to the SQLite database.
     *
     * @param score the score to add
     * @param name  the name of the player
     */
    public void addHS(int score, String name) {
        SQLiteDatabase readableDb = getReadableDatabase();
        SQLiteDatabase db = getWritableDatabase();
        if (name.equals("")) name = "Anonymous";

        // Get the current number of high scores.
        int numHighScores;
        try (Cursor cursor = readableDb.rawQuery("SELECT id FROM high_scores;", null)) {
            numHighScores = cursor.getCount();
        }

        // If the current number of high scores is greater than the limit, delete the lowest score.
        if (numHighScores > DBScoreLimit) {
            db.execSQL("DELETE FROM high_scores WHERE id = (SELECT id FROM high_scores ORDER BY score ASC LIMIT 1);");
        }

        // Insert the new high score into the table.
        db.execSQL("INSERT INTO high_scores (score, name) VALUES (" + score + ", '" + name + "');");
    }


    /**
     * Get the top 3 high scores from the SQLite database in a HashMap. If there are less than 3 high scores, the HashMap will contain less than 3 entries.
     *
     * @return a HashMap containing the top 3 high scores
     */
    public HashMap<String, Integer> getHighestThree() {
        SQLiteDatabase db = getReadableDatabase();
        // Create a map to store the high scores.
        HashMap<String, Integer> highScores = new HashMap<>();
        try (Cursor cursor = db.rawQuery("SELECT name, score FROM high_scores ORDER BY score DESC LIMIT 3;", null)) {

            // Iterate through the database and add the high scores to the map.
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));

                // Add the high score to the map.
                highScores.put(name, score);
            }
        } catch (Exception e) {
            showErrorDialog("Database error: " + e.getMessage());
        }

        return highScores;
    }

    /**
     * Called when the database is created for the first time. This creates the SQLite tables that store the high scores.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the high scores table.
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing. This database does not need to be upgraded.
    }
}
