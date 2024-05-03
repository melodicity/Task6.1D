package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity {
    // Declare views
    RecyclerView rvAnswers;
    Button btnContinue;

    ResultsAdapter adapter;

    boolean[] answerStates;
    String[] selectedAnswers, correctAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise views
        rvAnswers = findViewById(R.id.rvAnswers);
        btnContinue = findViewById(R.id.btnContinue);

        // Get results from intent
        Intent intent = getIntent();
        selectedAnswers = intent.getStringArrayExtra("SELECTED_ANSWERS");
        correctAnswers = intent.getStringArrayExtra("CORRECT_ANSWERS");
        assert correctAnswers != null;
        int size = correctAnswers.length;
        answerStates = new boolean[size];

        // Populate answer states array
        for (int i = 0; i < size; i++) {
            if (selectedAnswers[i] != null && correctAnswers[i] != null) {
                answerStates[i] = selectedAnswers[i].equals(correctAnswers[i]);
            } else {
                answerStates[i] = false; // Consider it incorrect if either selected or correct answer is null
            }
        }

        // Setup rvQuizzes with adapter
        rvAnswers.setLayoutManager(new LinearLayoutManager(ResultsActivity.this));
        adapter = new ResultsAdapter(answerStates, selectedAnswers, correctAnswers, ResultsActivity.this);
        rvAnswers.setAdapter(adapter);

        // On click listener for continue button
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}