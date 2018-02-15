package com.abilashmenon.computerarchitecture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by abilashmenon on 3/2/18.
 */

public class JobAdapter extends ArrayAdapter<Job> {
    public JobAdapter(Context context, ArrayList<Job> jobArrayList){
        super(context, 0, jobArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Job job = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.job_item, parent, false);
        }
        TextView jobNo = convertView.findViewById(R.id.jobNoTV);
        TextView jobSize = convertView.findViewById(R.id.jobSizeTV);
        String jobNoFormat = "Job " + job.getJobNo();
        String jobSizeFormat = job.getJobSize() + "kb";

        jobNo.setText(jobNoFormat);
        jobSize.setText(jobSizeFormat);
        return convertView;
    }
}
