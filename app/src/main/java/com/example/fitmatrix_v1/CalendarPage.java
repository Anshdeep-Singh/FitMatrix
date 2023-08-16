package com.example.fitmatrix_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitmatrix_v1.DatabaseOperator.DatabaseHelper;
import com.example.fitmatrix_v1.DatabaseOperator.ExerciseDetails;
import com.example.fitmatrix_v1.RecyclerViewOperator.WorkoutAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalendarPage extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView workoutRecyclerView;
    private WorkoutAdapter workoutAdapter;
    private List<String> selectedDateWorkouts;
    private DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        databaseHelper = new DatabaseHelper(this);

        calendarView = findViewById(R.id.calendarView);
        workoutRecyclerView = findViewById(R.id.workoutRecyclerView);


        TextView recyclerPlaceholder = findViewById(R.id.tv_placeholderSelectDate);

        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedDateWorkouts = new ArrayList<>();
        workoutAdapter = new WorkoutAdapter(selectedDateWorkouts);
        workoutRecyclerView.setAdapter(workoutAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format("%04d%02d%02d", year, month + 1, dayOfMonth);
                updateWorkouts(selectedDate);
                recyclerPlaceholder.setVisibility(View.GONE);
            }
        });

        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = calendar.getTimeInMillis();
        calendarView.setDate(currentTimeMillis, false, true);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateWorkouts(String selectedDate) {

        selectedDateWorkouts.clear();

        Log.d("---->",selectedDate);
        List<ExerciseDetails> allData = databaseHelper.getAllData();

        int total_sets = 0;
        int total_reps = 0;
        int total_weight = 0;
        int check = 0;
        String selectedExercise = "";

        Map<String, List<ExerciseDetails>> groupedWorkouts = groupWorkoutsByValue(allData);

        for( Map.Entry<String, List<ExerciseDetails>> groupedWorkout : groupedWorkouts.entrySet()) {
            total_sets = 0;
            total_reps = 0;
            total_weight = 0;
            selectedExercise = "";
            List<ExerciseDetails> workouts = groupedWorkout.getValue();

            for(ExerciseDetails workout : workouts){
                check = 0;
                if(workout.getId().split("_")[0].equals(selectedDate)) {
                    total_sets += 1;
                    total_reps += workout.getReps();
                    if(workout.getUnit().equals("kg")){
                        total_weight += (int) workout.getWeight()*2.205;
                    }
                    else{
                        total_weight += workout.getWeight();
                    }
                    Log.d("---->",total_reps+" "+total_sets+" "+total_weight);
                    check = 1;
                    selectedExercise = workout.getExercise();
                }

                Log.d("---->",workout.getExercise());
            }
            if(total_sets != 0 && total_reps != 0 && total_weight != 0 && check == 1){
                selectedDateWorkouts.add(total_reps+"/"+total_sets+"/"+total_weight+"/"+selectedExercise);
            }
        }
        workoutAdapter.notifyDataSetChanged();
    }

    private Map<String, List<ExerciseDetails>> groupWorkoutsByValue(List<ExerciseDetails> workouts) {
        return workouts.stream()
                .collect(Collectors.groupingBy(ExerciseDetails::getExercise));
    }

    public void goToHome(View view) {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
