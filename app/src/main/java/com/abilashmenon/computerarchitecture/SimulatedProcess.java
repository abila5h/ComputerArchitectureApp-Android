package com.abilashmenon.computerarchitecture;

import java.util.ArrayList;

/**
 * Created by abilashmenon on 13/2/18.
 */

public class SimulatedProcess {
    private ArrayList<Process> simulatedProcessList;
    private float averageWaitingTime;
    private float averageTurnaroundTime;

    public SimulatedProcess(ArrayList<Process> simulatedProcessList, float averageTurnaroundTime, float averageWaitingTime) {
        this.simulatedProcessList = simulatedProcessList;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageWaitingTime = averageWaitingTime;
    }

    public ArrayList<Process> getSimulatedProcessList() {
        return simulatedProcessList;
    }

    public float getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public float getAverageWaitingTime() {
        return averageWaitingTime;
    }
}
