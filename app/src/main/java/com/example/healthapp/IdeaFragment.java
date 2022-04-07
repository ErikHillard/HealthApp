package com.example.healthapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IdeaFragment extends Fragment {

    CustomJson cj;
    final HashMap<String,String> replacements = new HashMap<String,String>(){{
        put("Pop Tarts", "Granola bars");
        put("Potato chips", "Veggie straws");
    }};
    final HashMap<String,String> additions = new HashMap<String,String>(){{
        put("Protein", "Yogurt in the morning");
    }};

    public IdeaFragment(File files_dir) {
        String files = files_dir.toString();
        cj = new CustomJson(new File(files, "data.json"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_idea, container, false);

        HashMap<String,String> food_goals = cj.getFoodGoals();
        HashMap<String,String> ex_goals = cj.getExerciseGoals();
        ArrayList<HashMap<String,String>> food = cj.getAllFood();
        ArrayList<HashMap<String,String>> ex = cj.getAllExercise();
        ArrayList<HashMap<String,String>> food_data = cj.getFoodData();
        ArrayList<HashMap<String,String>> ex_data = cj.getExerciseData();

        HashMap<String,Boolean>[] met_food_goals = new HashMap[food.size()];
        HashMap<String,Boolean>[] met_ex_goals = new HashMap[ex.size()];

        double sum;
        String name;
        boolean res;
        for (int i = 0;i < food.size(); i++) {
            met_food_goals[i] = new HashMap<>();
            met_ex_goals[i] = new HashMap<>();
            for (String fd : food_goals.keySet()) {
                sum = 0;
                for (HashMap<String,String> fdta : food_data) {
                    name = fdta.get("Name");
                    if (food.get(i).containsKey(name) && fdta.containsKey(fd)) {
                        sum += Double.parseDouble(food.get(i).get(name))*Double.parseDouble(fdta.get(fd));
                    }
                }
                res = Double.parseDouble(food_goals.get(fd)) <= sum;
                met_food_goals[i].put(fd, (fd.equals("Calories")) != res);
            }
            for (String exer : ex_goals.keySet()) {
                sum = 0;
                for (HashMap<String,String> exta : ex_data) {
                    name = exta.get("Name");
                    if (ex.get(i).containsKey(name) && exta.containsKey(exer)) {
                        sum += Double.parseDouble(ex.get(i).get(name))/60*Double.parseDouble(exta.get(exer));
                    }
                }
                met_ex_goals[i].put(exer, Double.parseDouble(ex_goals.get(exer)) <= sum);
            }
        }

        ArrayList<TextView> nutr_tvs = new ArrayList<>(Arrays.asList(new TextView[]{vw.findViewById(R.id.nutrition_1),
                vw.findViewById(R.id.nutrition_2),vw.findViewById(R.id.nutrition_3)}));
        ArrayList<TextView> exer_tvs = new ArrayList<>(Arrays.asList(new TextView[]{vw.findViewById(R.id.exercise_1),
                vw.findViewById(R.id.exercise_2)}));

        ArrayList<TextView> disp_nutr_tvs = new ArrayList<>();
        ArrayList<TextView> disp_exer_tvs = new ArrayList<>();

        for (HashMap<String,Boolean> goal : met_food_goals) {
            for (String attr : goal.keySet()) {
                if (!goal.get(attr) && !nutr_tvs.isEmpty()) {
                    disp_nutr_tvs.add(nutr_tvs.remove(0));
                    if (attr.equals("Protein")) {
                        disp_nutr_tvs.get(disp_nutr_tvs.size()-1).setText("If you're low on " + attr +
                                ", try adding some " + additions.get(attr));
                    } else {
                        disp_nutr_tvs.get(disp_nutr_tvs.size()-1).setText("If you want to cut calories, " +
                                "try replacing " + "Pop Tarts" + " with " + replacements.get("Pop Tarts"));
                    }
                }
            }
        }

        for (HashMap<String,Boolean> goal : met_ex_goals) {
            for (String attr : goal.keySet()) {
                if (!goal.get(attr) && !exer_tvs.isEmpty()) {
                    disp_exer_tvs.add(exer_tvs.remove(0));
                    disp_exer_tvs.get(disp_exer_tvs.size()-1).setText("Try increasing the time you spend running, " +
                            "and add walking in between stretches of running");
                }
            }
        }

        if (disp_nutr_tvs.isEmpty()) {
            disp_nutr_tvs.add(nutr_tvs.remove(0));
            disp_nutr_tvs.get(0).setText("Keep up the great work!");
        }

        if (disp_exer_tvs.isEmpty()) {
            disp_exer_tvs.add(exer_tvs.remove(0));
            disp_exer_tvs.get(0).setText("Keep up the great work!");
        }

        for (TextView tv : nutr_tvs) {
            tv.setVisibility(View.GONE);
        }
        for (TextView tv : exer_tvs) {
            tv.setVisibility(View.GONE);
        }

        return vw;
    }

    @Override
    public void onStop() {
        super.onStop();
        cj.writeFile();
    }

}