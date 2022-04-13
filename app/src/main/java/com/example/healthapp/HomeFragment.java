package com.example.healthapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    CustomJson cj;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
        getActivity().findViewById(R.id.addGoal).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Pull up dialog box with goals
                        populateData();
                    }
                }
        );
    }

    private void populateData() {
        cj = new CustomJson(new File(getContext().getFilesDir(), "data.json"));
        ArrayList<GoalsView> arrayList = new ArrayList<GoalsView>();
        // add all the values from 1 to 15 to the arrayList
        // the items are of the type NumbersView
       if (cj != null) {
            HashMap<String, String> foodGoals = cj.getFoodGoals();
            int currentDay = cj.getFoodLastDay();
            HashMap<String, String> foodOnLastDay = cj.getFoodDay(currentDay);
            ArrayList<HashMap<String, String>> foodData = cj.getFoodData();

            int caloriesTotal = 0;
            int proteinTotal = 0;
            int sodiumTotal = 0;

            // Dear programming gods this is O(n^2) and I know it's horrible but i promise
            // it's not my fault. So please forgive me just this once :((
            for (String food: foodOnLastDay.keySet()) {
                int servings = Integer.parseInt(foodOnLastDay.get(food));

                for (int i = 0; i < foodData.size(); i ++) {
                    HashMap<String, String> currFoodData = foodData.get(i);
                    if (currFoodData.get("Name").equals(food)) {

                        caloriesTotal += Integer.parseInt(currFoodData.get("Calories")) * servings;
                        proteinTotal += Integer.parseInt(currFoodData.get("Protein")) * servings;
                        sodiumTotal += Integer.parseInt(currFoodData.get("Sodium")) * servings;

                        break;
                    }
                }
            }

            for (String s : foodGoals.keySet()) {
                if (s.equals("Calories")) {
                    arrayList.add(new GoalsView("Calories Eaten", Integer.toString(caloriesTotal), foodGoals.get(s)));
                } else if (s.equals("Protein")) {
                    arrayList.add(new GoalsView("Protein Eaten", Integer.toString(proteinTotal), foodGoals.get(s)));
                } else if (s.equals("Sodium")) {
                    arrayList.add(new GoalsView("Sodium Eaten", Integer.toString(sodiumTotal), foodGoals.get(s)));
                }
            }
        }

        // Now create the instance of the NumebrsViewAdapter and pass
        // the context and arrayList created above
        GoalAdapter numbersArrayAdapter = new GoalAdapter(getActivity().getApplicationContext(), arrayList);

        // create the instance of the ListView to set the numbersViewAdapter
        ListView numbersListView = getActivity().findViewById(R.id.listView);

        // set the numbersViewAdapter for ListView
        numbersListView.setAdapter(numbersArrayAdapter);
    }
}