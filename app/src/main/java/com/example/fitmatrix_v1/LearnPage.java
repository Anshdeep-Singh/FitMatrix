package com.example.fitmatrix_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fitmatrix_v1.RecyclerViewOperator.ExerciseRecyclerAdapter;
import com.example.fitmatrix_v1.RecyclerViewOperator.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnPage extends AppCompatActivity {

    private ArrayList<Exercise> exercise;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private RecyclerView.LayoutManager layoutManager;
    private String[] muscles = {"Select Muscle Group","abdominals","abductors","adductors","biceps","calves","chest","forearms","glutes","hamstrings","lats","lower back","middle back","neck","quadriceps","shoulders","traps","triceps"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_page);

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Spinner muscleSpinner = findViewById(R.id.spinner_muscle);
        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscles);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleSpinner.setAdapter(muscleAdapter);

        muscleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position !=0){
                    String selectedMuscle = (String) parent.getItemAtPosition(position);
                    fetchExercises(selectedMuscle);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                muscleSpinner.setSelection(0);
            }
        });

    }

    public void fetchExercises(String selectedMuscle){

        String url = "https://api.api-ninjas.com/v1/exercises?muscle=" + selectedMuscle;
        String apiKey = "irRdRhOJy/BZiCmPgMP7bQ==liB3Mw1edTxzulRW";
        exercise = new ArrayList<>();

        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {

                for (int i = 0; i < response.length(); i++) {
                    JSONObject exerciseObject = response.getJSONObject(i);
                    String name = exerciseObject.optString("name");
                    String equipment = exerciseObject.optString("equipment");
                    String muscle = exerciseObject.optString("muscle");
                    String instructions = exerciseObject.optString("instructions");
                    Log.d("exercise", name);
                    Exercise exercise_ = new Exercise(name, equipment, muscle, instructions);
                    exercise.add(exercise_);
                }
                ExerciseRecyclerAdapter adapter = new ExerciseRecyclerAdapter(exercise,this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", apiKey);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    };

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
