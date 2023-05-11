package com.rollonit.edugame;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A simple android game that first shows the user a sequence of cells that change colour.
 * Each step of the sequence is accompanied by a chime sound and one highlighted cell.
 * The user then has to repeat the sequence by clicking on the cells in the same order.
 * Every turn, the sequence gets longer by one.
 */
public class GameActivity extends AppCompatActivity {

    GradientDrawable[][] cells;
    ImageView[][] cellViews;

    MediaPlayer chimePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // initialise the chime player
        chimePlayer = MediaPlayer.create(this, R.raw.chime);

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
    }

    private void handleCellClick(int i, int j) {
        // change the colour of the cell
        cellViews[i][j].setColorFilter(Color.RED);

        // wait for 1 second and change it back to black
        cellViews[i][j].postDelayed(() -> cellViews[i][j].setColorFilter(Color.BLACK), 1000);

        // play the chime sound
        chimePlayer.start();
    }
}