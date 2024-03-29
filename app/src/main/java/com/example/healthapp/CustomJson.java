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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomJson {

    File file;
    Gson gson;
    String json_string = "{" +
            "\"food\":[" +
            "{\"Chicken Breast\":\"6\",\"Potato Chips\":\"3\",\"Pop Tarts\":\"1\"}," +
            "{\"Potato Chips\":\"3\",\"Pop Tarts\":\"3\"}" +
            "]," +
            "\"exercise\":[" +
            "{\"Running\":\"30\",\"Walking\":\"20\"}," +
            "{\"Pushups\":\"30\", \"Running\":\"30\"}" +
            "]," +
            "\"food_goals\":[{" +
            "\"Calories\":\"2200\"," +
            "\"Protein\":\"50\"" +
            "}]," +
            "\"exercise_goals\":[{" +
            "\"Calories\":\"200\"" +
            "}]," +
            "\"food_data\":[" +
            "{\"Name\":\"Chicken Breast\",\"Calories\":\"130\",\"Protein\":\"20\",\"Sodium\":\"5\"}," +
            "{\"Name\":\"Potato Chips\",\"Calories\":\"150\",\"Protein\":\"2\",\"Sodium\":\"10\"}," +
            "{\"Name\":\"Pop Tarts\",\"Calories\":\"240\",\"Protein\":\"2\",\"Sodium\":\"2\"}" +
            "]," +
            "\"exercise_data\":[" +
            "{\"Name\":\"Running\",\"Calories\":\"300\"}," +
            "{\"Name\":\"Walking\",\"Calories\":\"80\"}" +
            "]," +
            "\"calories_for_graph\":[" +
            "{\"Calories\":\"1200\"}," +
            "{\"Calories\":\"1400\"}," +
            "{\"Calories\":\"2000\"}," +
            "{\"Calories\":\"2100\"}," +
            "{\"Calories\":\"1800\"}" +
            "]," +
            "\"exercise_for_graph\":[" +
            "{\"Calories\":\"200\"}," +
            "{\"Calories\":\"300\"}," +
            "{\"Calories\":\"40\"}," +
            "{\"Calories\":\"500\"}," +
            "{\"Calories\":\"300\"}" +
            "]," +
            "\"user\":[{" +
            "\"Username\":\"user\"," +
            "\"Password\":\"pass\"," +
            "\"Height\":\"190\"," +
            "\"Weight\":\"200\"," +
            "\"Age\":\"50\"" +
            "}]," +
            "\"happy\":[{" +
            "\"submitted\":\"0\"," +
            "\"0\":\"2\"," +
            "\"1\":\"2\"," +
            "\"2\":\"2\"," +
            "\"3\":\"2\"," +
            "\"4\":\"2\"" +
            "}]" +
            "}";

    HashMap<String, ArrayList<HashMap<String, String>>> data;

    public CustomJson(File file) {
        this.file = file;
        gson = new Gson();
        data = new HashMap<>();
        if (!file.exists()) {//change this to true to reset json file to default state (json_string)
            writeFile();
        }
        readFile();
    }

    public int getFoodLastDay() {
        return data.get("food").size()-1;
    }

    public void saveFood(HashMap<String, String> item) {
        data.get("food_data").add(item);
    }

    public void saveExercise(HashMap<String, String> item) {
        data.get("exercise_data").add(item);
    }

    public void addFoodGoal(String attr, String val) {
        HashMap<String, String> goals = data.get("food_goals").get(0);
        if (goals.containsKey(attr)) {
            goals.replace(attr, val);
        } else {
            goals.put(attr, val);
        }
    }

    public void addExerciseGoal(String attr, String val) {
        HashMap<String, String> goals = data.get("exercise_goals").get(0);
        if (goals.containsKey(attr)) {
            goals.replace(attr, val);
        } else {
            goals.put(attr, val);
        }
    }

    public void addFoodForDay(String food, String amount, int day) {
        if (day >= data.get("food").size()) {
            data.get("food").ensureCapacity(day + 1);
            data.get("food").add(day, new HashMap<String, String>());
        }
        Map<String, String> day_data = data.get("food").get(day);
        if (day_data.containsKey(food)) {
            day_data.replace(food, String.valueOf(Double.parseDouble(amount) + Double.parseDouble(day_data.get(food))));
        } else {
            day_data.put(food, amount);
        }
    }

    public void addExerciseForDay(String exercise, String amount, int day) {
        if (day >= data.get("exercise").size()) {
            data.get("exercise").ensureCapacity(day + 1);
            data.get("exercise").add(day, new HashMap<String, String>());
        }
        Map<String, String> day_data = data.get("exercise").get(day);
        if (day_data.containsKey(exercise)) {
            day_data.replace(exercise, String.valueOf(Double.parseDouble(amount) + Double.parseDouble(day_data.get(exercise))));
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
        data.put("food", gson.fromJson(ja, ArrayList.class));

        ja = json_data.get("exercise").getAsJsonArray();
        data.put("exercise", gson.fromJson(ja, ArrayList.class));

        ja = json_data.get("food_goals").getAsJsonArray();
        data.put("food_goals", new ArrayList(Arrays.asList(gson.fromJson(ja.get(0), HashMap.class))));

        ja = json_data.get("exercise_goals").getAsJsonArray();
        data.put("exercise_goals", new ArrayList(Arrays.asList(gson.fromJson(ja.get(0), HashMap.class))));

        ja = json_data.get("food_data").getAsJsonArray();
        data.put("food_data", gson.fromJson(ja, ArrayList.class));

        ja = json_data.get("exercise_data").getAsJsonArray();
        data.put("exercise_data", gson.fromJson(ja, ArrayList.class));

        ja = json_data.get("calories_for_graph").getAsJsonArray();
        data.put("calories_for_graph", gson.fromJson(ja, ArrayList.class));

        ja = json_data.get("user").getAsJsonArray();
        data.put("user", gson.fromJson(ja, ArrayList.class));

        ja = json_data.get("happy").getAsJsonArray();
        data.put("happy", gson.fromJson(ja, ArrayList.class));

    }

    public ArrayList<HashMap<String, String>> getGraphStats() {
        ArrayList<HashMap<String, String>> ret = new ArrayList(data.get("calories_for_graph").size());
        for (int i = 0; i < data.get("calories_for_graph").size(); i ++) {
            ret.add(new HashMap(data.get("calories_for_graph").get(i)));
        }
        return ret;
    }

    public ArrayList<HashMap<String, String>> getGraphStatsE() {
        ArrayList<HashMap<String, String>> ret = new ArrayList(data.get("exercise_for_graph").size());
        for (int i = 0; i < data.get("exercise_for_graph").size(); i ++) {
            ret.add(new HashMap(data.get("exercise_for_graph").get(i)));
        }
        return ret;

    }
  
    public HashMap<String, String> getFoodDay(int day) {
        return new HashMap(data.get("food").get(day));
    }

    public ArrayList<HashMap<String, String>> getAllFood() {
        ArrayList<HashMap<String, String>> ret = new ArrayList(data.get("food").size());
        for (int i = 0; i < data.get("food").size(); i++) {
            ret.add(new HashMap(data.get("food").get(i)));
        }
        return ret;
    }

    public void putHappy(String s) {
        Map<String, String> userData = data.get("happy").get(0);
        userData.replace(Integer.toString(userData.size()- 2), s);
        userData.replace("submitted", "1");
    }

    public void removeHappy() {
        Map<String, String> userData = data.get("happy").get(0);
        userData.remove(Integer.toString(userData.size() - 2));
        userData.replace("submitted", "0");
    }

    public HashMap<String, String> getExerciseDay(int day) {
        return new HashMap(data.get("exercise").get(day));
    }

    public HashMap<String, String> getHappiness() {
        return new HashMap(data.get("happy").get(0));
    }

    public ArrayList<HashMap<String, String>> getAllExercise() {
        ArrayList<HashMap<String, String>> ret = new ArrayList(data.get("exercise").size());
        for (int i = 0; i < data.get("exercise").size(); i++) {
            ret.add(new HashMap(data.get("exercise").get(i)));
        }
        return ret;
    }

    public HashMap<String, String> getFoodGoals() {
        return new HashMap(data.get("food_goals").get(0));
    }

    public HashMap<String, String> getUser() {
        HashMap <String, String> userData = new HashMap(data.get("user").get(0));
        return new HashMap(data.get("user").get(0));
    }

    public HashMap<String, String> getExerciseGoals() {
        return new HashMap(data.get("exercise_goals").get(0));
    }

    public ArrayList<HashMap<String, String>> getFoodData() {
        ArrayList<HashMap<String, String>> ret = new ArrayList(data.get("food_data").size());
        for (int i = 0; i < data.get("food_data").size(); i++) {
            ret.add(new HashMap(data.get("food_data").get(i)));
        }
        return ret;
    }

    public ArrayList<HashMap<String, String>> getExerciseData() {
        ArrayList<HashMap<String, String>> ret = new ArrayList(data.get("exercise_data").size());
        for (int i = 0; i < data.get("exercise_data").size(); i++) {
            ret.add(new HashMap(data.get("exercise_data").get(i)));
        }
        return ret;
    }

    public void removeAllData() {
        data.replace("food",new ArrayList<>());
        data.replace("exercise",new ArrayList<>());
        data.replace("food_goals",new ArrayList<>());
        data.replace("exercise_goals",new ArrayList<>());
        data.replace("happy", new ArrayList<>());
    }

    public void removeFoodFromDay(String food, int day) {
        if (day >= 0 && day < data.getOrDefault("food", new ArrayList()).size()) {
            if (data.get("food").get(day).containsKey(food)) {
                data.get("food").get(day).remove(food);
            }
        }
    }

    public void removeExerciseFromDay(String exercise, int day) {
        if (day >= 0 && day < data.getOrDefault("exercise", new ArrayList()).size()) {
            if (data.get("exercise").get(day).containsKey(exercise)) {
                data.get("exercise").get(day).remove(exercise);
            }
        }
    }

    public void removeFoodGoal(String attr) {
        if (data.get("food_goals").get(0).containsKey(attr)) {
            data.get("food_goals").get(0).remove(attr);
        }
    }

    public void removeExerciseGoal(String attr) {
        if (data.get("exercise_goals").get(0).containsKey(attr)) {
            data.get("exercise_goals").get(0).remove(attr);
        }
    }

    public void removeAllGoals() {
        data.get("exercise_goals").get(0).clear();
        data.get("food_goals").get(0).clear();
    }

    public void replaceUser(String username, String password, String age, String height,
                                String weight) {
        Map <String, String> userData = data.get("user").get(0);
        userData.replace("Username", username);
        userData.replace("Password", password);
        userData.replace("Age", age);
        userData.replace("Height", height);
        userData.replace("Weight", weight);
    }

    public String getUserName() {
        Map<String, String> userData = data.get("user").get(0);
        return userData.get("Username");
    }

    public String getUserPassword() {
        Map<String, String> userData = data.get("user").get(0);
        return userData.get("Password");
    }
}
