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

public class ProcessAdapter extends ArrayAdapter<Process> {
    public ProcessAdapter(Context context, ArrayList<Process> processArrayList){
        super(context, 0, processArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Process process = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.process_item, parent, false);
        }
        TextView processName = convertView.findViewById(R.id.processName);
        TextView processDetails = convertView.findViewById(R.id.processDetails);
        String processNameFormat = "P" + process.getProcessNo();
        String processDetailsFormat;
        if(process.isPriorityNP()){
            processDetailsFormat = "BT: " + process.getBurstTime() + " PT: " + process.getPriority();
        }else if(process.isRoundRobin()){
            processDetailsFormat = "BT: " + process.getBurstTime() + " TQ: " + process.getTimeQuantum();
        }else if(process.isSJFNP()){
            processDetailsFormat = "BT: " + process.getBurstTime();
        }
        else {
            processDetailsFormat = "AT: " + process.getArrivalTime() + " BT: " + process.getBurstTime();
        }
        processName.setText(processNameFormat);
        processDetails.setText(processDetailsFormat);
        return convertView;
    }
}
