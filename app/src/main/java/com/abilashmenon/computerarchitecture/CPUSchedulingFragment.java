package com.abilashmenon.computerarchitecture;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;


public class CPUSchedulingFragment extends Fragment {

    private Spinner spinner;
    private TextView processNoView;
    private TextView averageTurnaroundTimeDisplay;
    private TextView averageWaitingTimeDisplay;
    private EditText arrivalText;
    private EditText burstText;
    private EditText priorityText;
    private EditText quantumText;
    private ShimmerTextView turnaroundTimeDisplay;
    private ShimmerTextView waitingTimeDisplay;
    private ListView processList;
    private Button addProcessButton;
    private Button simulateButton;
    private Button resetAllButton;
    private int processNo = 1;
    private String algorithmKey;
    private ArrayList<Process> processArrayList;
    private ProcessAdapter processAdapter;
    private CPUSchedulingAlgorithms cpuSchedulingAlgorithms;
    private ImageView imageView;




    public CPUSchedulingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cpuscheduling, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // prevents keyboard from adjusting layout
        initialise();
        btnListeners();
    }


    private void initialise() {
        initialiseViews();
        initialiseSpinner();
        initialiseProcessList();
        disableEditTexts();
        String processFormat = "P" + processNo;
        processNoView.setText(processFormat);
        cpuSchedulingAlgorithms = new CPUSchedulingAlgorithms(new CPUSchedulingAlgorithms.simulationListener() {
            @Override
            public void onAlgorithmSimulationCompleted(boolean success, SimulatedProcess simulatedProcess) {
                updateSimulatedProcess(simulatedProcess);

            }
        });
    }

    private void initialiseViews() {
        spinner = getView().findViewById(R.id.spinner);
        arrivalText = getView().findViewById(R.id.arrivalTime);
        burstText = getView().findViewById(R.id.burstTime);
        priorityText = getView().findViewById(R.id.priorityText);
        quantumText = getView().findViewById(R.id.timeQuantum);
        turnaroundTimeDisplay = getView().findViewById(R.id.ttText);
        waitingTimeDisplay = getView().findViewById(R.id.wtText);
        addProcessButton = getView().findViewById(R.id.addProcessButton);
        resetAllButton = getView().findViewById(R.id.resetAll);
        simulateButton = getView().findViewById(R.id.simulateBtn);
        processList = getView().findViewById(R.id.processList);
        processNoView = getView().findViewById(R.id.processNo);
        imageView = getView().findViewById(R.id.imageView2);
        averageTurnaroundTimeDisplay = getView().findViewById(R.id.textView6);
        averageWaitingTimeDisplay = getView().findViewById(R.id.textView4);
        setResultVisibility(false);

    }

    private void setResultVisibility(boolean visible) {
        if (visible) {
            turnaroundTimeDisplay.setVisibility(View.VISIBLE);
            waitingTimeDisplay.setVisibility(View.VISIBLE);
            averageWaitingTimeDisplay.setVisibility(View.VISIBLE);
            averageTurnaroundTimeDisplay.setVisibility(View.VISIBLE);
        } else {
            turnaroundTimeDisplay.setVisibility(View.INVISIBLE);
            waitingTimeDisplay.setVisibility(View.INVISIBLE);
            averageWaitingTimeDisplay.setVisibility(View.INVISIBLE);
            averageTurnaroundTimeDisplay.setVisibility(View.INVISIBLE);
        }
    }

    private void initialiseSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.algorithm_select, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    arrivalText.setEnabled(true);
                    burstText.setEnabled(true);
                    priorityText.setEnabled(false);
                    quantumText.setEnabled(false);
                    algorithmKey = Keys.FCFS;
                } else if (i == 1) {
                    arrivalText.setEnabled(false);
                    burstText.setEnabled(true);
                    priorityText.setEnabled(false);
                    quantumText.setEnabled(false);
                    algorithmKey = Keys.SJFNP;
                } else if (i == 2) {
                    arrivalText.setEnabled(false);
                    burstText.setEnabled(true);
                    priorityText.setEnabled(true);
                    quantumText.setEnabled(false);
                    algorithmKey = Keys.PriorityNP;
                } else if (i == 3) {
                    arrivalText.setEnabled(false);
                    burstText.setEnabled(true);
                    priorityText.setEnabled(false);
                    quantumText.setEnabled(true);
                    algorithmKey = Keys.RR;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                arrivalText.setEnabled(true);
                burstText.setEnabled(true);
                priorityText.setEnabled(false);
                quantumText.setEnabled(false);
                algorithmKey = Keys.FCFS;
            }
        });
    }

    private void initialiseProcessList() {
        processArrayList = new ArrayList<>();
        processAdapter = new ProcessAdapter(getActivity(), processArrayList);
        processList.setAdapter(processAdapter);
        processList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showProcessDetails(i);
                return true;
            }
        });
    }

    private void disableEditTexts() {
        arrivalText.setEnabled(false);
        burstText.setEnabled(false);
        priorityText.setEnabled(false);
        quantumText.setEnabled(false);
    }

    private void btnListeners() {
        addProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valuesAreValid()) {
                    addProcess();
                    spinner.setEnabled(false);
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    createAlert();
                }
            }
        });
        resetAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        simulateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (algorithmKey.equals(Keys.FCFS)) {
                    simulateFCFS();
                } else if (algorithmKey.equals(Keys.SJFNP)) {
                    simulateSJFNP();
                } else if (algorithmKey.equals(Keys.PriorityNP)) {
                    simulatePriorityNP();
                } else if (algorithmKey.equals(Keys.RR)) {
                    simulateRR();
                }

                simulateButton.setEnabled(false);
            }
        });
    }

private void configureShimmer(){
    Shimmer shimmer1 = new Shimmer();
    shimmer1.setDuration(1000);
    Shimmer shimmer = new Shimmer();
    shimmer.setDuration(1000);
    shimmer1.start(turnaroundTimeDisplay);
    shimmer.start(waitingTimeDisplay);
}
    private void reset() {
        spinner.setEnabled(true);
        processAdapter.clear();
        processNo = 1;
        String processFormat = getString(R.string.p) + processNo;
        processNoView.setText(processFormat);
        setResultVisibility(false);
        simulateButton.setEnabled(true);
        quantumText.setText("");
        arrivalText.setText("");
        burstText.setText("");
        priorityText.setText("");
        quantumText.setText("");
        imageView.setVisibility(View.VISIBLE);
        if(algorithmKey.equals(Keys.RR)){
            quantumText.setEnabled(true);
        }
    }

    private boolean valuesAreValid() {
        boolean isValid = false;
        if (algorithmKey.equals(Keys.FCFS)) {
            if (!arrivalText.getText().toString().isEmpty() && !burstText.getText().toString().isEmpty()) {
                isValid = true;
            }
        } else if (algorithmKey.equals(Keys.SJFNP)) {
            if (!burstText.getText().toString().isEmpty()) {
                isValid = true;
            }
        } else if (algorithmKey.equals(Keys.PriorityNP)) {
            if (!burstText.getText().toString().isEmpty() && !priorityText.getText().toString().isEmpty()) {
                isValid = true;
            }
        } else if (algorithmKey.equals(Keys.RR)) {
            if (!burstText.getText().toString().isEmpty() && !quantumText.getText().toString().isEmpty()) {
                isValid = true;
            }
        }
        return isValid;
    }


    private void addProcess() {
        String arrivalTime, burstTime, priority, timeQuantum;
        int at = 0;
        int bt;
        int pt = 0;
        int tq = 0;
        arrivalTime = arrivalText.getText().toString();
        burstTime = burstText.getText().toString();
        priority = priorityText.getText().toString();
        timeQuantum = quantumText.getText().toString();
        bt = Integer.parseInt(burstTime);
        if (algorithmKey.equals(Keys.FCFS)) {
            at = Integer.parseInt(arrivalTime);
        }
        if (algorithmKey.equals(Keys.PriorityNP)) {
            pt = Integer.parseInt(priority);
        }
        if (algorithmKey.equals(Keys.RR)) {
            tq = Integer.parseInt(timeQuantum);
            quantumText.setEnabled(false); //Only set Time Quantum once
        }
        Process process = new Process(algorithmKey, processNo, at, bt, pt, tq);
        processArrayList.add(process);
        processAdapter.notifyDataSetChanged(); //Update the Process ListView
        processNo++; //Increment Process No
        String processFormat = getString(R.string.p) + processNo;
        processNoView.setText(processFormat);
        arrivalText.setText("");
        priorityText.setText("");
        burstText.setText("");
        quantumText.setText(timeQuantum);
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

    private void simulateFCFS() {
        cpuSchedulingAlgorithms.simulateFCFS(processArrayList);
    }

    private void simulateSJFNP() {
        cpuSchedulingAlgorithms.simulateSJFNP(processArrayList);
    }


    private void simulateRR() {
        cpuSchedulingAlgorithms.simulateRR(processArrayList);
    }

    private void simulatePriorityNP() {
        cpuSchedulingAlgorithms.simulatePriorityNP(processArrayList);
    }

    private void updateSimulatedProcess(SimulatedProcess simulatedProcess) {
        float turnaroundTime = simulatedProcess.getAverageTurnaroundTime();
        float waitingTime = simulatedProcess.getAverageWaitingTime();
        ArrayList<Process> simulatedProcessList = simulatedProcess.getSimulatedProcessList();
        for (int i = 0; i < simulatedProcessList.size(); i++) {
            processArrayList.set(i, simulatedProcessList.get(i));
        }
        setResultVisibility(true);
        String turnaroundTimeText = String.format("%.2f", turnaroundTime) + getString(R.string.ms);
        String waitingTimeText = String.format("%.2f", waitingTime) + getString(R.string.ms);
        turnaroundTimeDisplay.setText(turnaroundTimeText);
        waitingTimeDisplay.setText(waitingTimeText);
configureShimmer();



    }

    private void showProcessDetails(int i) {
        Process process = processArrayList.get(i);
        if (process.isSimulated()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Process Details");
            alertDialog.setIcon(R.drawable.processgrey);
            String processName = "Process Name : P" + process.getProcessNo();
            String processArrivalTime = "Arrival Time : " + process.getArrivalTime() + getString(R.string.ms);
            String processBurstTime = "Burst Time : " + process.getBurstTime() + getString(R.string.ms);
            String processPriority = "Priority : " + process.getPriority();
            String timeQuantum = "Time Quantum : " + process.getTimeQuantum();
            String processCompletionTime = "Completion Time : " + process.getCompletionTime() + getString(R.string.ms);
            String processTurnaroundTime = "Turnaround Time : " + process.getTurnaroundTime() + getString(R.string.ms);
            String processWaitingTime = "Waiting Time : " + process.getWaitingTime() + getString(R.string.ms);
            String doubleSpace = System.getProperty("line.separator") + System.getProperty("line.separator");
            String message = "";
            if (algorithmKey.equals(Keys.FCFS)) {
                message = processName + doubleSpace + processArrivalTime + doubleSpace + processBurstTime + doubleSpace + processCompletionTime + doubleSpace + processTurnaroundTime + doubleSpace + processWaitingTime;
            } else if (algorithmKey.equals(Keys.SJFNP)) {
                message = processName + doubleSpace + processBurstTime + doubleSpace + processWaitingTime + doubleSpace + processTurnaroundTime;
            } else if (algorithmKey.equals(Keys.PriorityNP)) {
                message = processName + doubleSpace + processPriority + doubleSpace + processBurstTime + doubleSpace + processWaitingTime + doubleSpace + processTurnaroundTime;
            } else if (algorithmKey.equals(Keys.RR)) {
                message = processName + doubleSpace + timeQuantum + doubleSpace + processBurstTime + doubleSpace + processWaitingTime + doubleSpace + processTurnaroundTime;
            }
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.okay),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }


}
