package com.abilashmenon.computerarchitecture;

/**
 * Created by abilashmenon on 11/2/18.
 */

public class MemoryBlock {
    private int size;
    private boolean isJob = false;
    private String jobNo;
    private boolean hasCalled = false;
 //   private int allocatedBlockNo;
//
//    public boolean isPieFlag() {
//        return pieFlag;
//    }
//
//    public void setAllocatedBlockNo(int allocatedBlockNo) {
//        this.allocatedBlockNo = allocatedBlockNo;
//    }
//
//    public int getAllocatedBlockNo() {
//        return allocatedBlockNo;
//    }

//    private boolean pieFlag = false;
//
//    public void setPieFlag(){
//        this.pieFlag = true;
//    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked() {
        this.locked = true;
    }

    private boolean locked = false;




    private int color;

    public MemoryBlock(int size, int color){
        this.size = size;
        this.color = color;
    }


    public int getColor() {
        return color;
    }


    public void setHasCalled() {
        this.hasCalled = true;
    }


    public boolean hasCalled() {
        return hasCalled;
    }



    public void setIsJob() {
        isJob = true;
    }

    public boolean getIsJob(){
        return isJob;
    }


    public int getSize() {
        return size;
    }


    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }



    public String getJobNo() {
        return jobNo;
    }
}
