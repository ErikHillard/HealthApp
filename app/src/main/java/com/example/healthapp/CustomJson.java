package com.example.healthapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CustomJson {

    File file;
    Gson gson;
    String json_string = "{" +
            "\"food\":[" +
            "{\"Chicken Breast\":2,\"Frosted Flakes\":1,\"Pop Tart\":0.5}," +
            "{\"Chick-fil-A Sandwich\":1, \"Bagel\":1,\"Cream Cheese\":1.5}" +
            "]," +
            "\"exercise\":[" +
            "{\"Running\":20,\"Walking\":20}," +
            "{\"Pushups\":30, \"Walking\":30}" +
            "]," +
            "\"food_goals\":[{" +
            "\"Calories\":2200," +
            "\"Carbs\":60" +
            "}]," +
            "\"exercise_goals\":[{" +
            "\"Calories\":200," +
            "\"Steps\":10000" +
            "}]" +
            "}";
    HashMap<String, HashMap<String,Double>[]> data;

    public CustomJson(File file) {
        this.file = file;
        gson = new Gson();
        data = new HashMap<>();
        if (!file.exists()) {//change this to true to reset json file to default state (json_string)
            writeFile();
        }
        readFile();
    }

    public void addFoodGoal(String attr, double val) {
        HashMap<String, Double> goals = data.get("food_goals")[0];
        if (goals.containsKey(attr)) {
            goals.replace(attr, val);
        } else {
            goals.put(attr, val);
        }
    }

    public void addExerciseGoal(String attr, double val) {
        HashMap<String, Double> goals = data.get("exercise_goals")[0];
        if (goals.containsKey(attr)) {
            goals.replace(attr, val);
        } else {
            goals.put(attr, val);
        }
    }

    public void addFoodForDay(String food, double amount, int day) {
        HashMap<String,Double> day_data = data.get("food")[day];
        if (day_data.containsKey(food)) {
            day_data.replace(food, amount);
        } else {
            day_data.put(food, amount);
        }
    }

    public void addExerciseForDay(String exercise, double amount, int day) {
        HashMap<String,Double> day_data = data.get("exercise")[day];
        if (day_data.containsKey(exercise)) {
            day_data.replace(exercise, amount);
        } else {
            day_data.put(exercise, amount);
        }
    }

    public void writeFile() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (data.isEmpty()) {
                bufferedWriter.write(json_string);
            } else {
                bufferedWriter.write(gson.toJson(data).toString());
            }
            bufferedWriter.close();
        } catch (IOException e) {
            Log.w("CustomJson", "File can't be written to");
        }
    }

    private void readFile() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            Log.w("CustomJson", "File not found");
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.w("CustomJson", "Can't read from file");
        }
        String dta = stringBuilder.toString();

        JsonElement jsonElement = JsonParser.parseString(dta);
        JsonObject json_data = jsonElement.getAsJsonObject();

        data = new HashMap();

        JsonArray ja = json_data.get("food").getAsJsonArray();
        data.put("food", gson.fromJson(ja, HashMap[].class));

        ja = json_data.get("exercise").getAsJsonArray();
        data.put("exercise", gson.fromJson(ja, HashMap[].class));

        ja = json_data.get("food_goals").getAsJsonArray();
        data.put("food_goals", new HashMap[]{gson.fromJson(ja.get(0), HashMap.class)});

        ja = json_data.get("exercise_goals").getAsJsonArray();
        data.put("exercise_goals", new HashMap[]{gson.fromJson(ja.get(0), HashMap.class)});
    }

    public HashMap<String, Double> getFoodDay(int day) {
        return new HashMap(data.get("food")[day]);
    }

    public HashMap<String, Double>[] getAllFood() {
        HashMap<String,Double>[] ret = new HashMap[data.get("food").length];
        for (int i=0;i<ret.length;i++) {
            ret[i] = new HashMap(data.get("food")[i]);
        }
        return ret;
    }

    public HashMap<String, Double> getExerciseDay(int day) {
        return new HashMap(data.get("exercise")[day]);
    }

    public HashMap<String, Double>[] getAllExercise() {
        HashMap<String,Double>[] ret = new HashMap[data.get("exercise").length];
        for (int i=0;i<ret.length;i++) {
            ret[i] = new HashMap(data.get("exercise")[i]);
        }
        return ret;
    }

    public HashMap<String, Double> getFoodGoals() {
        return new HashMap(data.get("food_goals")[0]);
    }

    public HashMap<String, Double> getExerciseGoals() {
        return new HashMap(data.get("exercise_goals")[0]);
    }

    public void removeFoodFromDay(String food, int day) {
        if (day >= 0 && day < data.getOrDefault("food", new HashMap[]{}).length) {
            if (data.get("food")[day].containsKey(food)) {
                data.get("food")[day].remove(food);
            }
        }
    }

    public void removeExerciseFromDay(String exercise, int day) {
        if (day >= 0 && day < data.getOrDefault("exercise", new HashMap[]{}).length) {
            if (data.get("exercise")[day].containsKey(exercise)) {
                data.get("exercise")[day].remove(exercise);
            }
        }
    }

    public void removeFoodGoal(String attr) {
        if (data.get("food_goals")[0].containsKey(attr)) {
            data.get("food_goals")[0].remove(attr);
        }
    }

    public void removeExerciseGoal(String attr) {
        if (data.get("exercise_goals")[0].containsKey(attr)) {
            data.get("exercise_goals")[0].remove(attr);
        }
    }
}