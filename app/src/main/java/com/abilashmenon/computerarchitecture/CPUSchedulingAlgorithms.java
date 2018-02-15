package com.abilashmenon.computerarchitecture;

import java.util.ArrayList;

/**
 * Created by abilashmenon on 2/2/18.
 */

public class CPUSchedulingAlgorithms {

    private simulationListener simulationListener;

    public CPUSchedulingAlgorithms(simulationListener simulationListener){
        this.simulationListener = simulationListener;
    }

    public void simulateFCFS(ArrayList<Process> processArrayList){
        SimulatedProcess simulatedProcess = null;
        if(processArrayList.get(0).getType().equals(Keys.FCFS)){
            ArrayList<Integer> processNos = new ArrayList<>();
            ArrayList<Integer> arrivalTimes = new ArrayList<>();
            ArrayList<Integer> burstTimes = new ArrayList<>();
            ArrayList<Integer> completionTimes = new ArrayList<>();
            ArrayList<Integer> turnaroundTimes = new ArrayList<>();
            ArrayList<Integer> waitingTimes = new ArrayList<>();
            float totalTurnaroundTime = 0;
            float totalWaitingTime = 0;
            float averageTT, averageWT;
            int temp;

            for (int i = 0; i < processArrayList.size(); i++) {
                Process process = processArrayList.get(i);
                processNos.add(process.getProcessNo());
                arrivalTimes.add(process.getArrivalTime());
                burstTimes.add(process.getBurstTime());
            }
            //sorting according to arrival times
            for (int i = 0; i < processArrayList.size(); i++) {

                for (int j = 0; j < processArrayList.size() - (i + 1); j++) {

                    if (arrivalTimes.get(j) > arrivalTimes.get(j + 1)) {
                        temp = arrivalTimes.get(j);
                        arrivalTimes.set(j, arrivalTimes.get(j + 1));
                        arrivalTimes.set(j + 1, temp);

                        temp = burstTimes.get(j);
                        burstTimes.set(j, burstTimes.get(j + 1));
                        burstTimes.set(j + 1, temp);

                        temp = processNos.get(j);
                        processNos.set(j, processNos.get(j + 1));
                        processNos.set(j + 1, temp);
                    }
                }
            }
            // finding completion times
            for (int i = 0; i < processArrayList.size(); i++) {
                if (i == 0) {
                    completionTimes.add(i, arrivalTimes.get(i) + burstTimes.get(i));
                    processArrayList.get(i).setCompletionTime(completionTimes.get(i));
                } else {
                    if (arrivalTimes.get(i) > completionTimes.get(i - 1)) {
                        completionTimes.add(i, arrivalTimes.get(i) + burstTimes.get(i));
                        processArrayList.get(i).setCompletionTime(completionTimes.get(i));
                    } else {
                        completionTimes.add(i, completionTimes.get(i - 1) + burstTimes.get(i));
                        processArrayList.get(i).setCompletionTime(completionTimes.get(i));
                    }
                }
                turnaroundTimes.add(i, completionTimes.get(i) - arrivalTimes.get(i));
                processArrayList.get(i).setTurnaroundTime(turnaroundTimes.get(i));
                waitingTimes.add(i, turnaroundTimes.get(i) - burstTimes.get(i));
                processArrayList.get(i).setWaitingTime(waitingTimes.get(i));
                totalTurnaroundTime += turnaroundTimes.get(i);
                totalWaitingTime += waitingTimes.get(i);
                processArrayList.get(i).setSimulated(true);
            }

            averageTT = totalTurnaroundTime / processArrayList.size();
            averageWT = totalWaitingTime / processArrayList.size();
            simulatedProcess = new SimulatedProcess(processArrayList, averageTT, averageWT);
            simulationListener.onAlgorithmSimulationCompleted(true, simulatedProcess);
        }else{
            simulationListener.onAlgorithmSimulationCompleted(false, simulatedProcess);
        }
    }

    public void  simulateSJFNP(ArrayList<Process> processArrayList){ //something is weird with waiting time only. Requires fix.
        SimulatedProcess simulatedProcess = null;
        if(processArrayList.get(0).getType().equals(Keys.SJFNP)){
            ArrayList<Integer> processNos = new ArrayList<>();
            ArrayList<Integer> burstTimes = new ArrayList<>();
            ArrayList<Integer> turnaroundTimes = new ArrayList<>();
            ArrayList<Integer> waitingTimes = new ArrayList<>();
            ArrayList<Integer> shortestJob = new ArrayList<>();
            waitingTimes.add(0, 0);
            float totalTurnaroundTime = 0;
            float totalWaitingTime = 0;
            float averageTT, averageWT;
            int temp;

            for (int i = 0; i < processArrayList.size(); i++) { //populate arraylist
                Process process = processArrayList.get(i);
                processNos.add(process.getProcessNo());
                burstTimes.add(process.getBurstTime());
            }

            for (int i = 0; i < processArrayList.size(); i++) { // add burst times
                shortestJob.add(i, burstTimes.get(i));
            }

            for (int i = 0; i < processArrayList.size(); i++) { //sort according to shortest time
                for (int j = i + 1; j < processArrayList.size(); j++) {
                    if (shortestJob.get(i) > shortestJob.get(j)) {
                        temp = shortestJob.get(i);
                        shortestJob.set(i, shortestJob.get(j));
                        shortestJob.set(j, temp);
                    }
                }
            }

            for (int i = 0; i < processArrayList.size(); i++) {
                for (int j = 0; j < processArrayList.size(); j++) {
                    if (shortestJob.get(i) == burstTimes.get(j)) {
                        processNos.set(i, j + 1);
                    }
                }
            }

            for (int i = 0; i < processArrayList.size(); i++) {

                waitingTimes.add(i + 1, shortestJob.get(i) + waitingTimes.get(i));
                processArrayList.get(i).setWaitingTime(waitingTimes.get(i));
                totalWaitingTime += waitingTimes.get(i);
            }

            for (int i = 0; i < processArrayList.size(); i++) {
                turnaroundTimes.add(i, burstTimes.get(i) + waitingTimes.get(i));
                processArrayList.get(i).setTurnaroundTime(turnaroundTimes.get(i));
                totalTurnaroundTime += turnaroundTimes.get(i);
                processArrayList.get(i).setSimulated(true);
            }
            averageWT = totalWaitingTime / processArrayList.size();
            averageTT = totalTurnaroundTime / processArrayList.size();
            simulatedProcess = new SimulatedProcess(processArrayList, averageTT, averageWT);
            simulationListener.onAlgorithmSimulationCompleted(true, simulatedProcess);
        }else{
            simulationListener.onAlgorithmSimulationCompleted(false, simulatedProcess);
        }
    }

    public void simulateRR(ArrayList<Process> processArrayList){
        SimulatedProcess simulatedProcess = null;
        if(processArrayList.get(0).getType().equals(Keys.RR)){
            ArrayList<Integer> processNos = new ArrayList<>();
            ArrayList<Integer> burstTimes = new ArrayList<>();
            ArrayList<Integer> turnaroundTimes = new ArrayList<>();
            ArrayList<Integer> waitingTimes = new ArrayList<>();
            ArrayList<Integer> a = new ArrayList<>();
            float totalTurnaroundTime = 0;
            float totalWaitingTime = 0;
            float averageTT, averageWT;
            int tq = processArrayList.get(0).getTimeQuantum();
            int n = processArrayList.size();
            int sum = 0;

            for (int i = 0; i < n; i++) {
                Process process = processArrayList.get(i);
                processNos.add(process.getProcessNo());
                burstTimes.add(process.getBurstTime());
                a.add(i, burstTimes.get(i));
                waitingTimes.add(i, 0);
            }

            do {
                for (int i = 0; i < n; i++) {
                    if (burstTimes.get(i) > tq) {
                        burstTimes.set(i, burstTimes.get(i) - tq);
                        for (int j = 0; j < n; j++) {
                            if ((j != i) && (burstTimes.get(j) != 0)) {
                                waitingTimes.set(j, waitingTimes.get(j) + tq);
                            }
                        }
                    } else {
                        for (int j = 0; j < n; j++) {
                            if ((j != i) && (burstTimes.get(j) != 0)) {
                                waitingTimes.set(j, waitingTimes.get(j) + burstTimes.get(i));
                            }
                        }
                        burstTimes.set(i, 0);
                    }
                }
                sum = 0;
                for (int k = 0; k < n; k++) {
                    sum = sum + burstTimes.get(k);
                }

            } while (sum != 0);


            for (int i = 0; i < n; i++) {
                turnaroundTimes.add(i, waitingTimes.get(i) + a.get(i));
                processArrayList.get(i).setWaitingTime(waitingTimes.get(i));
                processArrayList.get(i).setTurnaroundTime(turnaroundTimes.get(i));
                totalTurnaroundTime += turnaroundTimes.get(i);
                totalWaitingTime += waitingTimes.get(i);
                processArrayList.get(i).setSimulated(true);
            }

            averageWT = totalWaitingTime / processArrayList.size();
            averageTT = totalTurnaroundTime / processArrayList.size();
            simulatedProcess = new SimulatedProcess(processArrayList, averageTT, averageWT);
            simulationListener.onAlgorithmSimulationCompleted(true, simulatedProcess);
        }else{
            simulationListener.onAlgorithmSimulationCompleted(false, simulatedProcess);
        }
    }

    public void simulatePriorityNP(ArrayList<Process> processArrayList){
        SimulatedProcess simulatedProcess = null;
        if(processArrayList.get(0).getType().equals(Keys.PriorityNP)){
            ArrayList<Integer> processNos = new ArrayList<>();
            ArrayList<Integer> burstTimes = new ArrayList<>();
            ArrayList<Integer> turnaroundTimes = new ArrayList<>();
            ArrayList<Integer> waitingTimes = new ArrayList<>();
            ArrayList<Integer> priority = new ArrayList<>();
            ArrayList<Integer> priority1 = new ArrayList<>();
            waitingTimes.add(0, 0);
            int n = processArrayList.size();
            float totalTurnaroundTime = 0;
            float totalWaitingTime = 0;
            float averageTT = 0;
            float averageWT = 0;
            int temp;

            for (int i = 0; i < n; i++) {
                Process process = processArrayList.get(i);
                processNos.add(process.getProcessNo());
                burstTimes.add(process.getBurstTime());
                priority.add(process.getPriority());
            }

            for (int i = 0; i < n; i++) {
                priority1.add(i, priority.get(i));
            }

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (priority1.get(i) > priority1.get(j)) {
                        temp = priority1.get(i);
                        priority1.set(i, priority1.get(j));
                        priority1.set(j, temp);
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (priority1.get(i) == priority.get(j)) {
                        priority.set(i, j + 1);
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                int k = processNos.get(i);
                waitingTimes.add(i + 1, burstTimes.get(k - 1) + waitingTimes.get(i));
                processArrayList.get(i).setWaitingTime(waitingTimes.get(i));
                totalWaitingTime += waitingTimes.get(i);
            }

            for (int i = 0; i < n; i++) {
                int k = processNos.get(i);
                turnaroundTimes.add(i, burstTimes.get(k - 1) + waitingTimes.get(i));
                processArrayList.get(i).setTurnaroundTime(turnaroundTimes.get(i));
                totalTurnaroundTime += turnaroundTimes.get(i);
                processArrayList.get(i).setSimulated(true);
            }

            averageWT = totalWaitingTime / processArrayList.size();
            averageTT = totalTurnaroundTime / processArrayList.size();
            simulatedProcess = new SimulatedProcess(processArrayList, averageTT, averageWT);
            simulationListener.onAlgorithmSimulationCompleted(true, simulatedProcess);
        }else{
            simulationListener.onAlgorithmSimulationCompleted(false, simulatedProcess);
        }
    }


    public void simulateSJFP(ArrayList<Process> processArrayList){
        SimulatedProcess simulatedProcess = null;
        if(processArrayList.get(0).getType().equals(Keys.SJFP)){

            simulationListener.onAlgorithmSimulationCompleted(true, simulatedProcess);
        }else{
            simulationListener.onAlgorithmSimulationCompleted(false, simulatedProcess);
        }
    }


    public void simulatePriorityP(ArrayList<Process> processArrayList){
        SimulatedProcess simulatedProcess = null;
        if(processArrayList.get(0).getType().equals(Keys.PriorityP)){

            simulationListener.onAlgorithmSimulationCompleted(true, simulatedProcess);
        }else{
            simulationListener.onAlgorithmSimulationCompleted(false, simulatedProcess);
        }
    }



    public interface simulationListener {
        void onAlgorithmSimulationCompleted(boolean success, SimulatedProcess simulatedProcess);
    }
}
