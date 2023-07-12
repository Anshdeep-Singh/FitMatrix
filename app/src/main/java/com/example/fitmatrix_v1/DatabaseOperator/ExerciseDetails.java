package com.example.fitmatrix_v1.DatabaseOperator;

import com.example.fitmatrix_v1.Exercise;

public class ExerciseDetails {

    private static String[] listStoredExercise = {
            "Crunches", "Plank", "Russian Twist", "Hanging Leg Raises", "Reverse Crunches", "Sit-ups",
            "Bench Press", "Push-ups", "Dumbbell Flyes", "Chest Press Machine", "Incline Bench Press", "Cable Chest Flyes",
            "Deadlifts", "Pull-ups", "Seated Cable Rows", "Lat Pulldowns", "Barbell Rows", "T-Bar Rows",
            "Shoulder Press", "Dumbbell Lateral Raises", "Front Raises", "Arnold Press", "Upright Rows", "Lateral Raises",
            "Barbell Curls", "Hammer Curls", "Preacher Curls", "Cable Curls", "EZ-Bar Curls", "Concentration Curls",
            "Tricep Dips", "Tricep Pushdowns", "Skull Crushers", "Close-grip Bench Press", "Tricep Kickbacks", "Overhead Tricep Extensions",
            "Squats", "Lunges", "Leg Press", "Calf Raises", "Leg Extensions", "Hamstring Curls",
            "Hip Thrusts", "Glute Bridges", "Walking Lunges", "Donkey Kicks", "Fire Hydrants", "Sumo Squats",
            "Romanian Deadlifts", "Leg Curls", "Good Mornings", "Glute-Ham Raises", "Single-Leg Deadlifts", "Sumo Deadlifts",
            "Leg Extensions", "Hack Squats", "Bulgarian Split Squats", "Front Squats", "Step-ups", "Plyometric Squats",
            "Standing Calf Raises", "Seated Calf Raises", "Donkey Calf Raises", "Calf Press on Leg Press Machine", "Jump Rope", "Box Jumps",
            "Wrist Curls", "Reverse Curls", "Farmer's Walk", "Plate Pinches", "Grip Strengtheners", "Towel Pull-ups",
            "Dips", "Close-grip Push-ups", "Chest Dips", "Tricep Bench Dips",
            "Military Press", "Clean and Press", "Barbell Shrugs", "Dumbbell Shrugs",
            "Hip Abduction Machine", "Hip Adduction Machine", "Barbell Hip Thrusts", "Box Squats",
            "Burpees", "Mountain Climbers", "Jumping Jacks", "Plank Jacks", "Bear Crawls", "Squat Jumps",

    };

    private String id;
    private String exercise;
    private float weight;
    private int reps;
    private int set_no;
    private String unit;

    public static String[] getListExercise() {
        return listStoredExercise;
    }

    public ExerciseDetails(String id, String exercise, float weight, int set_no, int reps, String unit) {
        this.id = id;
        this.exercise = exercise;
        this.weight = weight;
        this.reps = reps;
        this.unit = unit;
        this.set_no = set_no;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setSet_no(int set_no) {
        this.set_no = set_no;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getSet_no() {
        return set_no;
    }

    public String getId() {
        return id;
    }

    public String getExercise() {
        return exercise;
    }

    public String getUnit() {
        return unit;
    }

    public float getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }
}
