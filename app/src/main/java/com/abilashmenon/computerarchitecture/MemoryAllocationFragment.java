package com.abilashmenon.computerarchitecture;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemoryAllocationFragment extends Fragment {


    private Spinner spinner;
    private TextView jobNoText;
    private EditText jobSizeText;
    private TextView internalText;
    private TextView externalText;
    private Button addJobBtn;
    private Button deleteJobsBtn;
    private Button simulateBtn;
    private ListView jobsListView;
    private PieChart mChart;
    private ArrayList<Job> jobsArray;
    private ArrayList<MemoryBlock> memoryBlocks;
    private String jobKey;
    private int jobNo = 1;
    private JobAdapter jobAdapter;
    private MemoryAllocationAlgorithms memoryAllocationAlgorithms;
    private ImageView imageView;


    public MemoryAllocationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // prevents keyboard from adjusting layout
        initialise();
        btnListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memory_allocation, container, false);
    }



    private void initialise() {
        initialiseViews();
        initialiseSpinner();
        initialiseJobList();
        initialiseMemoryBlocks();
        displayChart(mChart, memoryBlocks);
        memoryAllocationAlgorithms = new MemoryAllocationAlgorithms(new MemoryAllocationAlgorithms.simulationListener() {
            @Override
            public void onAlgorithmSimulationCompleted(SimulatedJob simulatedJob) {
                updateSimulatedJob(simulatedJob);
            }
        });
    }

    private void initialiseViews(){
        spinner = getView().findViewById(R.id.spinner);
        jobNoText = getView().findViewById(R.id.jobNo);
        jobSizeText = getView().findViewById(R.id.jobSize);
        internalText = getView().findViewById(R.id.internalFrag);
        externalText = getView().findViewById(R.id.externalFrag);
        addJobBtn = getView().findViewById(R.id.addJobButton);
        deleteJobsBtn = getView().findViewById(R.id.clearJobsBtn);
        simulateBtn = getView().findViewById(R.id.simulateBtn);
        jobsListView = getView().findViewById(R.id.jobsList);
        mChart = getView().findViewById(R.id.chart1);
        imageView = getView().findViewById(R.id.imageView);
    }

    private void initialiseSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.memory_algorithm_select, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    jobKey = Keys.FirstFitMemory;
                } else if (i == 1) {
                    jobKey = Keys.BestFitMemory;
                } else if (i == 2) {
                    jobKey = Keys.WorstFitMemory;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                jobKey = Keys.FirstFitMemory;
            }
        });
    }

    private void initialiseJobList(){
        jobsArray = new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(), jobsArray);
        jobsListView.setAdapter(jobAdapter);
    }

    private void initialiseMemoryBlocks(){
        memoryBlocks = new ArrayList<>();
        memoryBlocks.add(new MemoryBlock(200, getResources().getColor(R.color.darkGrey)));
        memoryBlocks.add(new MemoryBlock(500, getResources().getColor(R.color.slateGrey)));
        memoryBlocks.add(new MemoryBlock(150,getResources().getColor(R.color.dimGrey)));
        memoryBlocks.add(new MemoryBlock(300, getResources().getColor(R.color.darkGrey)));
        memoryBlocks.add(new MemoryBlock(600, getResources().getColor(R.color.slateGrey)));
    }

    private void btnListeners() {
        addJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueIsValid()) {
                    addJob();
                    spinner.setEnabled(false);
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    createAlert();
                }
            }
        });

        deleteJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearJobs();
            }
        });

        simulateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simulateBtn.setEnabled(false);
                if (jobKey.equals(Keys.FirstFitMemory)) {
                    memoryAllocationAlgorithms.simulateFirstFitNonContiguous(jobsArray, memoryBlocks);
                } else if (jobKey.equals(Keys.BestFitMemory)) {
                    memoryAllocationAlgorithms.simulateBestFitContiguous(jobsArray, memoryBlocks);
                } else if (jobKey.equals(Keys.WorstFitMemory)) {
                    memoryAllocationAlgorithms.simulateWorstFitNonContiguous(jobsArray, memoryBlocks);
                }
            }
        });
    }

    private void displayChart(PieChart mChart, ArrayList<MemoryBlock> memoryBlocks) {
        mChart.setData(consolidatePieData(memoryBlocks));
        pieChartSetViewOptions(mChart);
    }

    private PieData consolidatePieData(ArrayList<MemoryBlock> memoryBlocks){
        ArrayList<PieEntry> values = new ArrayList<>();
        for (int i = 0; i < memoryBlocks.size(); i++) {
            if (memoryBlocks.get(i).getIsJob()) {
                values.add(new PieEntry(memoryBlocks.get(i).getSize(), memoryBlocks.get(i).getJobNo()));
            } else {
                values.add(new PieEntry(memoryBlocks.get(i).getSize(), "kb"));
            }
        }
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < memoryBlocks.size(); i++) {
            colors.add(i, memoryBlocks.get(i).getColor());
        }
        PieDataSet dataSet = new PieDataSet(values, "Memory Blocks");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    private void pieChartSetViewOptions(PieChart mChart){
        mChart.invalidate();
        mChart.getDescription().setEnabled(false);
        mChart.setDrawHoleEnabled(true);
        mChart.setDrawSlicesUnderHole(false);
        mChart.setMaxAngle(180);
        mChart.setRotationAngle(180);
        mChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        mChart.setRotationEnabled(false);
        mChart.setExtraBottomOffset(80);
        mChart.setCenterText("Memory Blocks");
        mChart.setCenterTextSize(15f);
        mChart.setEntryLabelTextSize(9f);
        mChart.setTransparentCircleColor(getResources().getColor(R.color.darkGrey));
        mChart.setHoleColor(getResources().getColor(R.color.blackGrey));
        mChart.setCenterTextColor(getResources().getColor(android.R.color.white));
        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleRadius(40f);
    }

    private boolean valueIsValid() {
        boolean isValid = false;
        if (!jobSizeText.getText().toString().isEmpty()) {
            isValid = true;
        }
        return isValid;
    }

    private void createAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.emptyValues));
        alertDialog.setMessage(getString(R.string.enterAllValues));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void addJob() {
        int jobSize;
        jobSize = Integer.parseInt(jobSizeText.getText().toString());
        Job job = new Job(jobNo, jobSize);
        jobsArray.add(job);
        jobNo++;
        jobNoText.setText("" + jobNo);
        jobSizeText.setText("");
        jobAdapter.notifyDataSetChanged();
    }

    private void clearJobs() {
        spinner.setEnabled(true);
        simulateBtn.setEnabled(true);
        jobAdapter.clear();
        jobNo = 1;
        jobNoText.setText("" + jobNo);
        jobSizeText.setText("");
        internalText.setText("");
        externalText.setText("");
        imageView.setVisibility(View.VISIBLE);
        displayChart(mChart, memoryBlocks);
    }

    private void updateSimulatedJob(SimulatedJob simulatedJob){
        float internalFragmentation = simulatedJob.getInternalFragmentation();
        float externalFragmentation = simulatedJob.getExternalFragmentation();
        ArrayList<MemoryBlock> memoryBlocks = simulatedJob.getSimulatedMemoryBlocks();
        displayChart(mChart, memoryBlocks);
        internalText.setText(internalFragmentation + " kb");
        externalText.setText(externalFragmentation + " kb");
    }




}
