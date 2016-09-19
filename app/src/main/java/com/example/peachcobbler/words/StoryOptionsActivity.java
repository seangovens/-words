package com.example.peachcobbler.words;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoryOptionsActivity extends AppCompatActivity {

    private String user;
    private int wordCount = 1, wordMax = 10;
    private final int[] MAXES = new int[] {20, 50, 100, 200, 500, 1000, 2500, 5000, 10000, 50000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_options);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        SeekBar wordCountBar = (SeekBar) findViewById(R.id.wordAmountBar);
        wordCountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                wordCount = i + 1;
                ((TextView) findViewById(R.id.wordNum)).setText(String.valueOf(wordCount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        });

        SeekBar wordMaxBar = (SeekBar) findViewById(R.id.wordMaxBar);
        wordMaxBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                wordMax = MAXES[i];
                ((TextView) findViewById(R.id.wordMax)).setText(String.valueOf(wordMax));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        });
    }

    public void createStory(View view) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference story = db.getReference("stories").push();
        story.child("title").setValue(((EditText) findViewById(R.id.titleText)).getText().toString());
        story.child("wordCount").setValue(new Integer(wordCount));
        story.child("wordMax").setValue(new Integer(wordMax));

        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }
}
