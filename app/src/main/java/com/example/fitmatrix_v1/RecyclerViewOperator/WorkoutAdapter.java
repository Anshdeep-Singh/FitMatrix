package com.example.fitmatrix_v1.RecyclerViewOperator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitmatrix_v1.R;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private List<String> workouts;

    public WorkoutAdapter(List<String> workouts) {
        this.workouts = workouts;
    }

    public void setWorkouts(List<String> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String workout = workouts.get(position);
        String[] workoutSplit = workout.split("/");
        Log.d("Split Lenght -->", String.valueOf(workoutSplit.length));
        Log.d("Name recieved -->", String.valueOf(workoutSplit[3]));
        String workoutreps = workoutSplit[0];
        String workoutsets = workoutSplit[1];
        String workoutweight = workoutSplit[2];
        String name = workoutSplit[3];
        holder.workoutName.setText(name);
        holder.workoutReps.setText(workoutreps);
        holder.workoutSets.setText(workoutsets);
        holder.workoutWeight.setText(workoutweight + " lbs");
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutName;
        public TextView workoutReps;
        public TextView workoutSets;
        public TextView workoutWeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.tv_workout_name);
            workoutReps = itemView.findViewById(R.id.tv_totalReps);
            workoutSets = itemView.findViewById(R.id.tv_totalSets);
            workoutWeight = itemView.findViewById(R.id.tv_totalWeight);
        }
    }
}
