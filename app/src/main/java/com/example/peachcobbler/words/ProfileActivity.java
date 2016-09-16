package com.example.peachcobbler.words;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peachcobbler.words.Fragments.ConfirmNameFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends Activity {

    private String user;
    private int storyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference pName = db.getReference("users/" + user + "/penName");

        pName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.getValue();
                EditText nameField = (EditText) findViewById(R.id.penNameField);
                if (name.length() == 0) {
                    nameField.setHint("New Username");
                }
                else {
                    nameField.setText(name, TextView.BufferType.NORMAL);
                    nameField.setFocusable(false);
                    ((Button) findViewById(R.id.commitButton)).setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });

        DatabaseReference sCount = db.getReference("users/" + user + "/storyCount");

        sCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = (long) dataSnapshot.getValue();
                storyCount = (int) count;
                ((TextView) findViewById(R.id.storyCounter)).setText("Stories Remaining: " + String.valueOf(count));
                if (count == 0) {
                    ((Button) findViewById(R.id.createStoryButton)).setEnabled(false);
                }
                else {
                    ((Button) findViewById(R.id.createStoryButton)).setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });
    }

    public void commitPenName(View view) {
        String name = ((EditText) findViewById(R.id.penNameField)).getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        bundle.putString("name", name);

        ConfirmNameFragment cnFrag = new ConfirmNameFragment();
        cnFrag.setArguments(bundle);
        cnFrag.show(getFragmentManager(), "cnFrag");
    }

    public void createStory(View view) {
        storyCount--;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("users/" + user + "/storyCount").setValue(new Integer(storyCount));

        Intent intent = new Intent(this, StoryOptionsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
