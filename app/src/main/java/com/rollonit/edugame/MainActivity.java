package com.rollonit.edugame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /**
     * Buttons on the main screen
     */
    Button startButton, exitButton, settingsButton, helpButton, highScoreButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setup the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise the buttons
        startButton = findViewById(R.id.startButton);
        settingsButton = findViewById(R.id.settingsButton);
        exitButton = findViewById(R.id.exitButton);
        helpButton = findViewById(R.id.helpButton);
        highScoreButton = findViewById(R.id.hsButton);


        // set the click listeners for the buttons
        startButton.setOnClickListener(v -> startGame());
        settingsButton.setOnClickListener(v -> openSettings());
        exitButton.setOnClickListener(v -> exitApp());
        helpButton.setOnClickListener(v -> openHelp());
        highScoreButton.setOnClickListener(v -> openHighScore());
    }

    /**
     * Opens the high score screen when the user presses the high score button from the main screen
     */
    private void openHighScore() {
        // start the high score activity
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the game when the user presses the start button from the main screen
     */
    private void startGame() {
        // start the game activity
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the settings screen when the user presses the settings button from the main screen
     */
    private void openSettings() {
        // start the settings activity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Displays a help dialog that explains how to play the game.
     */
    private void openHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.help_message).setTitle(R.string.help_title).setPositiveButton(R.string.ok, (dialog, id) -> {
            // close the dialog
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Exit the app when the user presses the exit button from the main screen
     */
    private void exitApp() {
        finish();
        System.exit(0);
    }
}
