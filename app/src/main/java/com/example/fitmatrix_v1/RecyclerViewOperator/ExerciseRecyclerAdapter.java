package com.example.fitmatrix_v1.RecyclerViewOperator;

import android.content.Context;

import com.example.fitmatrix_v1.Exercise;
import com.example.fitmatrix_v1.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> {

    private ArrayList<Exercise> exerciseList;
    private Context context;

    public ExerciseRecyclerAdapter(ArrayList<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercise_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.nameTextView.setText(exercise.getName());
        holder.equipmentTextView.setText(exercise.getEquipment());
        holder.muscleTextView.setText(exercise.getMuscle());
        holder.instructionsTextView.setText(exercise.getInstructions());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView equipmentTextView;
        TextView muscleTextView;
        TextView instructionsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            equipmentTextView = itemView.findViewById(R.id.tv_equipment);
            muscleTextView = itemView.findViewById(R.id.tv_muscle);
            instructionsTextView = itemView.findViewById(R.id.tv_instructions);
        }
    }
}
