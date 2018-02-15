package com.abilashmenon.computerarchitecture;

/**
 * Created by abilashmenon on 6/2/18.
 */

public class Job {
    private int jobNo;
    private int jobSize;
    private int allocatedBlockNo;

    public Job(int jobNo, int jobSize){
        this.jobNo = jobNo;
        this.jobSize = jobSize;
    }

    public int getJobNo() {
        return jobNo;
    }

    public int getJobSize() {
        return jobSize;
    }

    public int getAllocatedBlockNo() {
        return allocatedBlockNo;
    }

    public void setAllocatedBlockNo(int allocatedBlockNo) {
        this.allocatedBlockNo = allocatedBlockNo;
    }
}