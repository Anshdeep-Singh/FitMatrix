package com.example.fitmatrix_v1;

public class Exercise {
    private String name;
    private String equipment;
    private String muscle;
    private String instructions;

    public Exercise(String name, String equipment, String muscle, String instructions) {
        this.name = name;
        this.equipment = equipment;
        this.muscle = muscle;
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getMuscle() {
        return muscle;
    }
}
