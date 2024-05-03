package com.example.task61d;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomepageQuizzesAdapter extends RecyclerView.Adapter<HomepageQuizzesAdapter.ViewHolder> {
    private final List<QuizItem> quizzes;
    private final Context context;

    public HomepageQuizzesAdapter(List<QuizItem> quizzes, Context context) {
        this.quizzes = quizzes;
        this.context = context;
    }

    @NonNull
    @Override
    public HomepageQuizzesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.quiz_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageQuizzesAdapter.ViewHolder holder, int position) {
        QuizItem item = quizzes.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());

        // Set on click listener for each quiz's Go button to create an intent for QuizActivity
        holder.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the QuizActivity
                Intent intent = new Intent(context, QuizActivity.class);

                // Passes the topic of this quiz item with the intent
                intent.putExtra("TOPIC", item.getTopic());
                intent.putExtra("TITLE", item.getTitle());
                intent.putExtra("DESCRIPTION", item.getDescription());

                // Start QuizActivity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        ImageButton btnGo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnGo = itemView.findViewById(R.id.btnGo);
        }
    }
}
