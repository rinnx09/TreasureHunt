package com.example.rinnxyii.treasurehunt;

public class Rank {
    private String place;
    private String name;
    private String score;

    public Rank() {
    }

    public Rank(String place, String name, String score) {
        this.place = place;
        this.name = name;
        this.score = score;
    }

    public Rank(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
