package com.rollonit.edugame;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighScoreActivity extends AppCompatActivity {

    DBHandler dbHandler;

    ListView scoreListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        dbHandler = new DBHandler(this);

        scoreListView = findViewById(R.id.top_scores);

        // get the top 3 high scores
        HashMap<String, Integer> topScores = dbHandler.getHighestThree();
        List<Map.Entry<String, Integer>> scoresList = new ArrayList<>(topScores.entrySet());

        // create an adapter for the list view that will fill the two fields in the list view item with the name and score by overriding the getView method
        ArrayAdapter<Map.Entry<String, Integer>> adapter = new ArrayAdapter<Map.Entry<String, Integer>>(this, R.layout.score_item, scoresList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final View parentView;
                final TextView nameView;
                final TextView scoreView;

                parentView = convertView == null ? getLayoutInflater().inflate(R.layout.score_item, parent, false) : convertView;

                nameView = parentView.findViewById(R.id.item_name);
                scoreView = parentView.findViewById(R.id.item_score);

                // set the name and score fields
                nameView.setText(scoresList.get(position).getKey());
                scoreView.setText(scoresList.get(position).getValue().toString());

                return parentView;
            }
        };

        // Set the list adapter to the list view.
        scoreListView.setAdapter(adapter);
    }
}