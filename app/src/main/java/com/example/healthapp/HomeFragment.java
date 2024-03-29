package com.example.healthapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    CustomJson cj;
    View view;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    String selectedGoal;
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

    private void createAddFoodDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addGoalView = getLayoutInflater().inflate(R.layout.add_goal_dialogue, null);

        // AutoCompleteTextView

        AutoCompleteTextView autoGoalComplete = (AutoCompleteTextView) addGoalView.findViewById(R.id.addGoalSpinner);
        ArrayList<String> choices = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_choices_food)));
        ArrayList<String> foodGoals = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_choices_food)));
        ArrayList<String> exerciseGoals = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.goal_choices_exercise)));
        choices.addAll(exerciseGoals);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, choices);
        autoGoalComplete.setThreshold(3);
        autoGoalComplete.setAdapter(myAdapter);

        autoGoalComplete.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                selectedGoal = (String) parent.getItemAtPosition(pos);
            }
        });

        Button addGoalButton = (Button) addGoalView.findViewById(R.id.addGoalButton);
        TextView total = (TextView) addGoalView.findViewById(R.id.goalTotal);

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedGoal.equals("")) {
                    String totalString = total.getText().toString();
                    if (!totalString.equals("")) {
                        // Make the goal call
                        if (foodGoals.contains(selectedGoal)) {
                            cj.addFoodGoal(selectedGoal, totalString);
                        } else {
                            if (selectedGoal.equals("Running (Miles)")) {
                                cj.addExerciseGoal("Running", totalString);
                            } else if (selectedGoal.equals("Walking (Miles)")) {
                                cj.addExerciseGoal("Walking", totalString);
                            } else if (selectedGoal.equals("Calories Burned")) {
                                cj.addExerciseGoal("Calories", totalString);
                            }
                        }
                        populateData();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Please enter the amount you are striving towards.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please click a goal.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setView(addGoalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
        MaterialButton clearGoalsButton = (MaterialButton) getActivity().findViewById(R.id.clearGoals);

        clearGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cj.removeAllGoals();
                populateData();
            }
        });

        MaterialButton addGoalButton = (MaterialButton) getActivity().findViewById(R.id.addGoal);
        addGoalButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        createAddFoodDialog();
                        populateData();
                    }
                }
        );

        HashMap<String, String> happinessData = cj.getHappiness();
        Boolean submitted = happinessData.get("submitted").equals("1");
        MaterialButton submitSlide = (MaterialButton) getActivity().findViewById(R.id.sliderSubmit);
        Slider slider = getActivity().findViewById(R.id.happySlider);
        TextView sliderLabel = getActivity().findViewById(R.id.sliderLabel);
        if (submitted) {
            slider.setVisibility(view.GONE);
            submitSlide.setText("Resubmit");
            sliderLabel.setText("Thank you! Click resubmit to \nedit your response.");
        }

        submitSlide.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, String> happyData = cj.getHappiness();
                        if (happyData.get("submitted").equals("1")) {
                            slider.setVisibility(view.VISIBLE);
                            cj.removeHappy();
                            cj.writeFile();
                            sliderLabel.setText("How are you feeling today?\n1 = Sad : 5 = happy");
                            submitSlide.setText("Submit");
                        } else {
                            String happiness = Integer.toString((int)slider.getValue());
                            cj.putHappy(happiness);
                            cj.writeFile();
                            submitSlide.setText("Resubmit");
                            sliderLabel.setText("Thank you! Click resubmit to \nedit your response.");
                            submitSlide.setText("Resubmit");
                            slider.setVisibility(view.GONE);
                        }
                    }
                }
        );
    }

    private void populateData() {
        if (cj == null) {
            cj = new CustomJson(new File(getContext().getFilesDir(), "data.json"));
        }
        ArrayList<GoalsView> arrayList = new ArrayList<GoalsView>();
        // add all the values from 1 to 15 to the arrayList
        // the items are of the type NumbersView
       if (cj != null) {
           HashMap<String, String> user = cj.getUser();
           TextView username = getActivity().findViewById(R.id.username);
           username.setText("Hello " + user.get("Username") + "!");

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

           HashMap<String, String> exerciseGoals = cj.getExerciseGoals();
           HashMap<String, String> exerciseOnLastDay = cj.getExerciseDay(currentDay);
           ArrayList<HashMap<String, String>> exerciseData = cj.getExerciseData();
           String running = exerciseOnLastDay.get("Running");
           if (running == null) {
               running = "0";
           }
           String walking = exerciseOnLastDay.get("Walking");
           if (walking == null) {
               walking = "0";
           }

           for (String s : exerciseGoals.keySet()) {
               if (s.equals("Calories")) {
                   //TODO: Find out how many colories burned in a day use exercise data
                   arrayList.add(new GoalsView("Calories Burned", "400", foodGoals.get(s)));
               } else if (s.equals("Running")) {
                   arrayList.add(new GoalsView("Miles Run", running, exerciseGoals.get(s)));
               } else if (s.equals("Walking")) {
                   arrayList.add(new GoalsView("Miles Walked", walking, exerciseGoals.get(s)));
               } else if (s.equals("Traveled")) {
                   arrayList.add(new GoalsView("Miles Traveled", Integer.toString(Integer.parseInt(running) + Integer.parseInt(walking)), exerciseGoals.get(s)));
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

    @Override
    public void onStop() {
        super.onStop();
        cj.writeFile();
        cj = null;
    }
}