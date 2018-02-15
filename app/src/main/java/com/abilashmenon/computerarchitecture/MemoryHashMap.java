package com.abilashmenon.computerarchitecture;

/**
 * Created by men0n on 14/2/2018.
 */
public class MemoryHashMap {
    int jobNo;
    int jobSize;
    public MemoryHashMap( int jobNo, int jobSize){
        this.jobNo = jobNo;
        this.jobSize = jobSize;
    }


    public int getJobNo() {
        return jobNo;
    }

    public int getJobSize() {
        return jobSize;
    }
}
