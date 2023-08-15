package com.example.fitmatrix_v1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fitmatrix_v1.DatabaseOperator.DatabaseHelper;
import com.example.fitmatrix_v1.DatabaseOperator.ExerciseDetails;
import com.example.fitmatrix_v1.RecyclerViewOperator.ChecklistAdapter;
import com.example.fitmatrix_v1.RecyclerViewOperator.RecyclerAdapter;
import com.example.fitmatrix_v1.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener  {

    private ActivityMainBinding binding;

    private RecyclerView recyclerView;
    private ChecklistAdapter checklistAdapter;
    private String exerciseName;
    private String unit;
    private float weight;
    private Integer reps;

    private boolean flag = true;
    private DatabaseHelper databaseHelper;
    private RequestQueue requestQueue;

    private RecyclerAdapter adapter;
    private List<ExerciseDetails> exerciseList;
    private ExerciseDetails exercise;

    private List<String> workoutList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String[] listExercise = ExerciseDetails.getListExercise();
        exerciseList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        FloatingActionButton fab_workoutPlan = findViewById(R.id.fab_workoutPlan);

        ArrayAdapter<String> auto_complete_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listExercise);
        Switch switch_unit = findViewById(R.id.switch_unit);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        RecyclerView recyclerView = findViewById(R.id.rv_past_sets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(exerciseList, this);
        recyclerView.setAdapter(adapter);
        binding.etSearchWorkout.setAdapter(auto_complete_adapter);

        switch_unit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_unit.setText("Lb");
                } else {
                    switch_unit.setText("Kg");
                }
            }
        });

        //HOME PAGE BUTTONS
        binding.btnEdit.setVisibility(View.INVISIBLE);
        binding.tvCurrentSet.setVisibility(View.VISIBLE);
        binding.btnEdit.setEnabled(false);
        binding.tvSetNumberOnEdit.setVisibility(View.INVISIBLE);
        binding.btnLogWorkout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onClick(View view) {
                try{
                    exerciseName = binding.etSearchWorkout.getText().toString();
                    weight = Float.parseFloat(binding.etWeight.getText().toString());
                    reps = Integer.parseInt(binding.etReps.getText().toString());
                }catch (Exception e){
                    exerciseName = "Dummy";
                    weight = 0.0f;
                    reps = 10;
                }

                boolean is_checked = binding.switchUnit.isChecked();

                if(is_checked){
                        unit = "lb";
                    }else{
                        unit = "kg";
                    }

                int set_no = Integer.parseInt(binding.tvCurrentSet.getText().toString().split(" ")[1]);

                databaseHelper.insertData(currentDate+set_no,exerciseName,weight,set_no,reps,unit);
                exercise = new ExerciseDetails(currentDate+set_no,exerciseName,weight,set_no,reps,unit);
                exerciseList.add(exercise);

                set_no +=1;
                binding.tvCurrentSet.setText("Set "+ set_no);
                adapter.notifyDataSetChanged();

                binding.etSearchWorkout.setEnabled(false);
            }
        });
        binding.btnStartNew.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onClick(View view) {
                exerciseList.clear();
                binding.etSearchWorkout.setEnabled(true);
                adapter.notifyDataSetChanged();
                binding.tvCurrentSet.setText("Set 1");
                binding.tvCurrentSet.setVisibility(View.VISIBLE);
                binding.tvSetNumberOnEdit.setVisibility(View.INVISIBLE);
                Log.d("-->","Clicked");
                binding.etSearchWorkout.setText("");
                binding.etWeight.setText("");
                binding.etReps.setText("");
            }
        });

        fab_workoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWorkoutPlan();
            }
        });
    }

    @Override
    public void onItemClick(ExerciseDetails exercise) {
        updateText(exercise,exercise.getId());
    }

    @SuppressLint("SetTextI18n")
    public void updateText(ExerciseDetails exercise, String id){
        String e_name = exercise.getExercise();
        int set_selected = exercise.getSet_no();
        Toast.makeText(getApplicationContext(), "Set : " + set_selected, Toast.LENGTH_SHORT).show();
        binding.tvSetStatus.setText("update set");
        databaseHelper = new DatabaseHelper(this);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if(exercise.getUnit().equals("kg")){
            binding.switchUnit.setChecked(false);
        }else{
            binding.switchUnit.setChecked(true);
        }
        binding.etSearchWorkout.setText(e_name);
        binding.etReps.setText(Integer.toString(exercise.getReps()));
        binding.etWeight.setText(Float.toString(exercise.getWeight()));

        binding.btnEdit.setVisibility(View.VISIBLE);
        binding.btnLogWorkout.setVisibility(View.INVISIBLE);
        binding.btnEdit.setEnabled(true);
        binding.btnLogWorkout.setEnabled(false);
        binding.etSearchWorkout.setEnabled(true);

        binding.tvSetNumberOnEdit.setVisibility(View.VISIBLE);
        binding.tvCurrentSet.setVisibility(View.INVISIBLE);
        binding.tvSetNumberOnEdit.setText("Set "+set_selected);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                boolean is_checked = binding.switchUnit.isChecked();

                try{
                    exerciseName = binding.etSearchWorkout.getText().toString();
                    weight = Float.parseFloat(binding.etWeight.getText().toString());
                    reps = Integer.parseInt(binding.etReps.getText().toString());
                }catch (Exception e){
                    exerciseName = "Dummy";
                    weight = 0.0f;
                    reps = 10;
                }

                if(is_checked){
                    unit = "lb";
                }else{
                    unit = "kg";
                }

                for(ExerciseDetails e : exerciseList){
                    if(e.getId().equals(id)){
                        e.setExercise(exerciseName);
                        e.setReps(reps);
                        e.setWeight(weight);
                        e.setUnit(unit);
                    }
                }
                databaseHelper.updateData(id,exerciseName,weight,reps,unit);
                adapter.notifyDataSetChanged();
                binding.btnEdit.setEnabled(false);
                binding.btnLogWorkout.setEnabled(true);
                binding.btnEdit.setVisibility(View.INVISIBLE);
                binding.tvSetNumberOnEdit.setVisibility(View.INVISIBLE);
                binding.tvCurrentSet.setVisibility(View.VISIBLE);
                binding.btnLogWorkout.setVisibility(View.VISIBLE);
                binding.tvSetStatus.setText("current set");
            }
        });

    }

    public void profilePage(MenuItem item){
        Intent i = new Intent(this,ProfilePage.class);
        startActivity(i);
    }
    public void settingsPage(MenuItem item){
        Intent i = new Intent(this,SettingsPage.class);
        startActivity(i);
    }
    public void learnPage(MenuItem item){
        Intent i = new Intent(this, LearnPage.class);
        startActivity(i);
    }
    public void calendarPage(MenuItem item){
        Intent i = new Intent(this,CalendarPage.class);
        startActivity(i);
    }


    private void createWorkoutPlan(){
        String url = "https://workout-api.vercel.app/api/random";

        if(flag){
            flag =false;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
                try {
                    workoutList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        String workoutName = response.getString(i);
                        workoutList.add(workoutName);
                    }

                    // Inflate the layout containing the ListView
                    View layoutView = getLayoutInflater().inflate(R.layout.popup_workout_checklist, null);
                    ListView listView = layoutView.findViewById(R.id.plan_listView); // Replace with the correct ID
                    listView.setAdapter(new ChecklistAdapter(MainActivity.this, android.R.layout.simple_list_item_1, workoutList));

                    Log.d("--->", "Got data from API");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Today's Workout Challenge")
                            .setAdapter(new ChecklistAdapter(this, android.R.layout.simple_list_item_multiple_choice, workoutList),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Handle item click here
                                        }
                                    })
                            .setPositiveButton("Done", null);

                    AlertDialog dialog = builder.create();
                    ListView listView1 = dialog.getListView();
                    listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    dialog.show();

                } catch (JSONException e) {
                    Log.d("--->", "Failed data from API");
                    e.printStackTrace();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("--->", "Failed data from API" + error.getMessage());
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonArrayRequest);

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Workouts")
                    .setAdapter(new ChecklistAdapter(this, android.R.layout.simple_list_item_multiple_choice, workoutList),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Handle item click here
                                }
                            })
                    .setPositiveButton("Done", null);

            AlertDialog dialog = builder.create();
            ListView listView1 = dialog.getListView();
            listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            dialog.show();
        }
    }





}