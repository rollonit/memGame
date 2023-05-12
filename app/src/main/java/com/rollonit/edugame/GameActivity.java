package com.rollonit.edugame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * A simple android game that first shows the user a sequence of cells that change colour.
 * Each step of the sequence is accompanied by a chime sound and one highlighted cell.
 * The user then has to repeat the sequence by clicking on the cells in the same order.
 * Every turn, the sequence gets longer by one.
 */
public class GameActivity extends AppCompatActivity {

    // user preferences
    int COUNTDOWN_LENGTH, SHOW_INTERVAL, BETWEEN_TURN_INTERVAL, FLASH_DURATION, STARTING_SCORE;
    boolean COUNTDOWN_ENABLE, SOUND_ENABLE;

    // Views
    GradientDrawable[][] cells;
    ImageView[][] cellViews;

    Button startButton, exitButton;
    TextView statusText, scoreText;

    // Game variables
    boolean running, showingSequence;
    /**
     * The current step of the sequence that the user is trying to repeat
     */
    int currentStep;

    /**
     * The sequence of cells that the user has to repeat
     */
    ArrayList<CellState> sequence;

    // Sound player
    SoundPool sounds;
    int chimeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // get the user's preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        COUNTDOWN_LENGTH = Integer.parseInt(preferences.getString("countdown_length", "3"));
        COUNTDOWN_ENABLE = preferences.getBoolean("countdown_enable", true);
        SOUND_ENABLE = preferences.getBoolean("sound_enable", true);
        SHOW_INTERVAL = Integer.parseInt(preferences.getString("show_interval", "1000"));
        BETWEEN_TURN_INTERVAL = Integer.parseInt(preferences.getString("between_turn_interval", "500"));
        FLASH_DURATION = Integer.parseInt(preferences.getString("flash_duration", "500"));
        STARTING_SCORE = Integer.parseInt(preferences.getString("starting_score", "0"));

        // initialise the SoundPool sound player to play the chime sound
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        sounds = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(attributes).build();
        chimeSound = sounds.load(this, R.raw.chime, 1);

        // initialise the arrays
        cells = new GradientDrawable[3][3];
        cellViews = new ImageView[3][3];

        // initialise the image views of cells in the grid
        cellViews[0][0] = findViewById(R.id.imageView1x1);
        cellViews[0][1] = findViewById(R.id.imageView1x2);
        cellViews[0][2] = findViewById(R.id.imageView1x3);

        cellViews[1][0] = findViewById(R.id.imageView2x1);
        cellViews[1][1] = findViewById(R.id.imageView2x2);
        cellViews[1][2] = findViewById(R.id.imageView2x3);

        cellViews[2][0] = findViewById(R.id.imageView3x1);
        cellViews[2][1] = findViewById(R.id.imageView3x2);
        cellViews[2][2] = findViewById(R.id.imageView3x3);

        // initialise the cells drawables for colour changes
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = (GradientDrawable) cellViews[i][j].getDrawable();
            }
        }

        // set the click listeners for the cells
        for (int i = 0; i < cellViews.length; i++) {
            for (int j = 0; j < cellViews.length; j++) {
                int finalI = i;
                int finalJ = j;
                cellViews[i][j].setOnClickListener(v -> handleCellClick(finalI, finalJ));
            }
        }


        // initialise the buttons and the text views
        startButton = findViewById(R.id.start);
        exitButton = findViewById(R.id.backToMenuButton);
        statusText = findViewById(R.id.status);
        scoreText = findViewById(R.id.score);


        exitButton.setOnClickListener(v -> finish());
        startButton.setOnClickListener(v -> {
            startButton.setVisibility(Button.GONE);
            statusText.setVisibility(TextView.VISIBLE);
            scoreText.setVisibility(TextView.VISIBLE);
            if (COUNTDOWN_ENABLE) {
                runCountDown();
            } else {
                showSequence();
            }
        });
    }

    /**
     * Runs a countdown timer, and then starts the game.
     */
    private void runCountDown() {
        CountDownTimer timer = new CountDownTimer(COUNTDOWN_LENGTH * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                statusText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                statusText.setText(R.string.go);
                showSequence();
            }
        };
        timer.start();
    }

    /**
     * Generates a sequence of cellStates of length i
     *
     * @param i the length of the sequence
     * @return the sequence of cellStates
     */
    private ArrayList<CellState> generateSequence(int i) {
        ArrayList<CellState> sequence = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            sequence.add(new CellState());
        }
        return sequence;
    }

    /**
     * Shows the sequence to the user. The user cannot click on the cells while the sequence is being shown.
     */
    private void showSequence() {
        // Generate a random sequence of cells, starting with a sequence of a default number cellStates, if this is the first turn
        if (!running) sequence = generateSequence(STARTING_SCORE);
        running = true;

        CountDownTimer timer = new CountDownTimer((long) sequence.size() * SHOW_INTERVAL, SHOW_INTERVAL) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                showingSequence = true;
                statusText.setText(R.string.watch);
                showCell(sequence.get(i));
                i++;
            }

            @Override
            public void onFinish() {
                showingSequence = false;
                statusText.setText(R.string.your_turn);
                currentStep = 0;
            }
        };
        timer.start();
    }

    /**
     * Handles the click on a cell
     *
     * @param i the x coordinate of the cell
     * @param j the y coordinate of the cell
     */
    private void handleCellClick(int i, int j) {
        if (showingSequence || !running) return;

        if (sequence.get(currentStep).getActiveCellX() == i && sequence.get(currentStep).getActiveCellY() == j) {
            flashCell(i, j, 0xFF90EE90);
            // the user clicked on the correct cell
            currentStep++;
            if (currentStep == sequence.size()) {
                // the user has completed the sequence
                statusText.setText(R.string.correct);
                scoreText.setText(String.format(getString(R.string.score), currentStep));
                sequence.add(new CellState());
                // wait 500ms before showing the next sequence
                new Handler(Looper.getMainLooper()).postDelayed(this::showSequence, BETWEEN_TURN_INTERVAL);
            }
        } else {
            // the user clicked on the wrong cell
            flashCell(i, j, Color.RED);
            startButton.setText(R.string.wrong);
            scoreText.setText(String.format(getString(R.string.score), sequence.size() - 1));
            running = false;
            startButton.setVisibility(Button.VISIBLE);
            statusText.setVisibility(TextView.GONE);
        }
    }

    /**
     * Shows a single CellState on the grid.
     */
    private void showCell(CellState cellState) {
        if (cellState == null) return;
        flashCell(cellState.getActiveCellX(), cellState.getActiveCellY(), 0xFF87CEEB);
    }

    /**
     * Flashes a cell with a given colour for a given amount of time.
     *
     * @param i     the x coordinate of the cell
     * @param j     the y coordinate of the cell
     * @param color the colour to flash the cell
     */
    private void flashCell(int i, int j, int color) {
        if (SOUND_ENABLE) sounds.play(chimeSound, 1, 1, 1, 0, 1);
        cellViews[i][j].setColorFilter(color);
        cellViews[i][j].postDelayed(() -> cellViews[i][j].setColorFilter(Color.BLACK), FLASH_DURATION);
    }
}