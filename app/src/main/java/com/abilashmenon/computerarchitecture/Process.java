package com.abilashmenon.computerarchitecture;

/**
 * Created by abilashmenon on 1/29/2018.
 */

public class Process {
    private int processNo;
    private int arrivalTime;
    private  int burstTime;
    private int Priority;
    private int timeQuantum;
    private String type;
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private boolean simulated;

    public Process(String type, int processNo, int arrivalTime, int burstTime, int priority, int timeQuantum) {
        this.type = type;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.Priority = priority;
        this.timeQuantum = timeQuantum;
        this.processNo = processNo;
    }

    public boolean isPriorityNP(){
        if(getType().equals(Keys.PriorityNP)){
            return true;
        }else{
            return false;
        }
    }

    public boolean isRoundRobin(){
        if(getType().equals(Keys.RR)){
            return true;
        }else{
            return false;
        }
    }

    public boolean isSJFNP(){
        if(getType().equals(Keys.SJFNP)){
            return true;
        }else{
            return false;
        }
    }

    public String getType() {
        return type;
    }

    public int getProcessNo() {
        return processNo;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return Priority;
    }

    public void setCompletionTime(int completionTime){
        this.completionTime = completionTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setSimulated(boolean simulated){
        this.simulated = simulated;
    }

    public boolean isSimulated(){
        return simulated;
    }
}


