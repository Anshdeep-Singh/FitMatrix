package com.example.fitmatrix_v1.RecyclerViewOperator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitmatrix_v1.DatabaseOperator.ExerciseDetails;
import com.example.fitmatrix_v1.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ExerciseViewHolder> {

    private List<ExerciseDetails> exerciseList;
    private OnItemClickListener listener;

    public RecyclerAdapter(List<ExerciseDetails> exerciseList, OnItemClickListener listener) {
        this.exerciseList = exerciseList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ExerciseDetails exercise);
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        public TextView exerciseNameTextView;
        public TextView weightTextView;
        public TextView repsTextView;
        public TextView unitTextView;
        public TextView setTextView;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exercise_name_textview);
            weightTextView = itemView.findViewById(R.id.weight_textview);
            repsTextView = itemView.findViewById(R.id.reps_textview);
            unitTextView = itemView.findViewById(R.id.unit_textview);
            setTextView = itemView.findViewById(R.id.tv_set_number);
        }

        public void bind(final ExerciseDetails exercise, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("-->","Clicked " + exercise.getWeight());
                    listener.onItemClick(exercise);
                }
            });
        }
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_layout, parent, false);
        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseDetails exercise = exerciseList.get(position);
        holder.exerciseNameTextView.setText(exercise.getExercise());
        holder.weightTextView.setText("Weight : " + String.valueOf(exercise.getWeight()));
        holder.repsTextView.setText("Reps : " + String.valueOf(exercise.getReps()));
        holder.unitTextView.setText( " " + exercise.getUnit());
        holder.setTextView.setText( "Set " + exercise.getSet_no());
        holder.bind(exercise, listener);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
