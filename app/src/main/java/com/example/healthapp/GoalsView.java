package com.example.healthapp;

public class GoalsView {

    // the resource ID for the imageView
    private String goalLabel;

    // TextView 1
    private String progress;

    // TextView 1
    private String totalGoal;

    // create constructor to set the values for all the parameters of the each single view
    public GoalsView(String goalLabelText, String progressIn, String totalGoalIn) {
        goalLabel = goalLabelText;
        progress = progressIn;
        totalGoal = totalGoalIn;
    }

    // getter method for returning the ID of the imageview
    public String getGoalLabel() {
        return goalLabel;
    }

    // getter method for returning the ID of the TextView 1
    public String getProgress() {
        return progress;
    }

    // getter method for returning the ID of the TextView 2
    public String getTotalGoal() {
        return totalGoal;
    }
}