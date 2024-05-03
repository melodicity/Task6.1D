package com.example.task61d;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {
    // Declare views
    TextView tvName;
    ImageView ivAvatar;
    RecyclerView rvQuizzes;
    Button btnInterests;

    List<QuizItem> quizzes = new ArrayList<>();
    HomepageQuizzesAdapter adapter;

    String username;
    String joinedInterests;
    List<String> interests;

    // DB Helper for changing selected interests in saved account
    AccountDBHelper dbHelper = new AccountDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get username and CSV interests from Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        joinedInterests = intent.getStringExtra("INTERESTS");

        // Initialise views
        tvName = findViewById(R.id.tvName);
        ivAvatar = findViewById(R.id.ivAvatar);
        rvQuizzes = findViewById(R.id.rvQuizzes);
        btnInterests = findViewById(R.id.btnInterests);

        // Set tvName to the logged-in username
        tvName.setText(username);

        // Assign a list of quizzes based on the user's preferred topics
        if (joinedInterests != null) {
            interests = Arrays.asList(joinedInterests.split(","));
            Collections.shuffle(interests);
            for (int i = 0; i < interests.size(); i++) {
                quizzes.add(new QuizItem(i+1, interests.get(i)));
            }
        } else {
            // The user has no interest currently, take them to interests activity
            intent = new Intent(HomepageActivity.this, InterestsActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        }


        // Setup rvQuizzes with adapter
        rvQuizzes.setLayoutManager(new LinearLayoutManager(HomepageActivity.this));
        adapter = new HomepageQuizzesAdapter(quizzes, HomepageActivity.this);
        rvQuizzes.setAdapter(adapter);

        // On click listener for interests button
        btnInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the InterestsActivity
                Intent intent = new Intent(HomepageActivity.this, InterestsActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                finish();
            }
        });
    }
}