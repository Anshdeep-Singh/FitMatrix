package com.example.fitmatrix_v1;

public class UserDetails {
    private int age;
    private double weight;
    private double height;

    public UserDetails(int age, double weight, double height) {
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }
}
