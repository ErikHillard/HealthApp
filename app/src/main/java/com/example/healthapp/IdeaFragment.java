package com.example.healthapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.File;
import java.util.HashMap;

public class IdeaFragment extends Fragment {

    CustomJson cj;

    public IdeaFragment(File files_dir) {
        String files = files_dir.toString();
        cj = new CustomJson(new File(files, "data.json"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_idea, container, false);

        cj.addFoodGoal("Calories", 10.0);
        cj.addFoodGoal("Carbs", 67.0);

        HashMap<String,Double> goals = cj.getFoodGoals();
        goals = new HashMap();

        //TextView tv = vw.findViewById(R.id.ideaFragment);
        //tv.setText(cj.getFoodDay(0).keySet().toString());
        return vw;
    }

    @Override
    public void onStop() {
        super.onStop();
        cj.writeFile();
    }

}