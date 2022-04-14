package com.example.healthapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class GoalAdapter extends ArrayAdapter<GoalsView> {

    // invoke the suitable constructor of the ArrayAdapter class
    public GoalAdapter(@NonNull Context context, ArrayList<GoalsView> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        GoalsView currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        TextView goalLabel = currentItemView.findViewById(R.id.goalLabel);
        assert currentNumberPosition != null;
        String progress = currentNumberPosition.getProgress();
        String total = currentNumberPosition.getTotalGoal();
//        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());
        goalLabel.setText(currentNumberPosition.getGoalLabel());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView goalOutOf = currentItemView.findViewById(R.id.goalOutOf);
        goalOutOf.setText(progress + "/" + total);

        // then according to the position of the view assign the desired TextView 2 for the same
        ProgressBar progressBar = currentItemView.findViewById(R.id.goalProgressBar);
        progressBar.setMax(Integer.parseInt(total));
        progressBar.setProgress(Integer.parseInt(progress));

        // then return the recyclable view
        return currentItemView;
    }
}
