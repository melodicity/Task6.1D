package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {
    // Declare views
    TextView tvTitle, tvDescription;
    RecyclerView rvQuiz;
    Button btnSubmit;

    List<QuestionItem> questions = new ArrayList<>();
    QuizAdapter adapter;

    String topic, title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise views
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        rvQuiz = findViewById(R.id.rvQuiz);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false); // disable button at first, until adapter is initialised

        // Get quiz topic from intent
        Intent intent = getIntent();
        topic = intent.getStringExtra("TOPIC");
        title = intent.getStringExtra("TITLE");
        description = intent.getStringExtra("DESCRIPTION");

        // Set title and description based on the topic
        tvTitle.setText(title);
        tvDescription.setText(description);

        // On click listener for submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get an array of the user's answers and correct answers
                String[] selectedAnswers = adapter.getSelectedAnswers();
                String[] correctAnswers = adapter.getCorrectAnswers();

                boolean allAnswered = !Arrays.asList(selectedAnswers).contains(null);
                if (!allAnswered) {
                    Toast.makeText(QuizActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create an intent to start the ResultsActivity
                Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);

                // Pass the answers
                intent.putExtra("SELECTED_ANSWERS", selectedAnswers);
                intent.putExtra("CORRECT_ANSWERS", correctAnswers);
                startActivity(intent);
                finish();
            }
        });

        // Get a generated quiz based on the topic, and update the recycler view
        fetchData();
    }

    // Get AI generated question data from the Flask API
    private void fetchData() {
        /* NOTE: API must be activated
         * In the Terminal:
         *  cd .\T-6.1D\
         *  pyEnv\Scripts\activate
         *  python main.py
         *
         * When finished:
         *  CTRL+C to quit
         *  deactivate
         */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10 minutes (IMPORTANT: If not your request will exceed the default read timeout)
                .build();

        QuizRequest request = retrofit.create(QuizRequest.class);

        request.getQuestions(topic).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuizResponse> call, @NonNull Response<QuizResponse> response) {
                if (response.isSuccessful()) {
                    QuizResponse quizResponse = response.body();
                    if (quizResponse != null) {
                        List<QuestionItem> questions = quizResponse.getQuestions();

                        adapter = new QuizAdapter(questions, QuizActivity.this);
                        rvQuiz.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
                        rvQuiz.setAdapter(adapter);

                        // Enable submit button after adapter has been initialised
                        btnSubmit.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizResponse> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Failed to fetch data", t);
            }
        });
    }
}