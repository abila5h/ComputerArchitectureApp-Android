package com.abilashmenon.computerarchitecture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by men0n on 14/2/2018.
 */

public class MemoryAllocationAlgorithms {
    private simulationListener simulationListener;

    public MemoryAllocationAlgorithms(simulationListener simulationListener) {
        this.simulationListener = simulationListener;
    }

    public void simulateFirstFitNonContiguous(ArrayList<Job> jobsArray, ArrayList<MemoryBlock> memoryBlocks) {
        int m = memoryBlocks.size();
        int n = jobsArray.size();
        int externalFrag = 0;
        int internalFrag = 0;
        ArrayList<Integer> allocation = new ArrayList<>();
        ArrayList<MemoryBlock> memBlk = new ArrayList<>();
        ArrayList<Job> jobArr = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            allocation.add(i, -1);
            jobArr.add(i, jobsArray.get(i));
        }
        for (int i = 0; i < m; i++) {
            memBlk.add(i, memoryBlocks.get(i));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!memBlk.get(j).isLocked()) {
                    if (memBlk.get(j).getSize() >= jobArr.get(i).getJobSize()) {
                        allocation.set(i, j);
                        MemoryBlock mBlock = new MemoryBlock(memBlk.get(j).getSize() - jobArr.get(i).getJobSize(), memBlk.get(j).getColor());
                        mBlock.setLocked();
                        mBlock.setUsedPartition();
                        memBlk.set(j, mBlock);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            jobArr.get(i).setAllocatedBlockNo(allocation.get(i));
            if (jobArr.get(i).getAllocatedBlockNo() != -1) {
                if (!memBlk.get(jobArr.get(i).getAllocatedBlockNo()).hasCalled()) {
                    internalFrag += memBlk.get(jobArr.get(i).getAllocatedBlockNo()).getSize();
                    memBlk.get(jobArr.get(i).getAllocatedBlockNo()).setHasCalled();
                }
            }
        }
        Integer[] blockNos = new Integer[n];
        HashMap<Integer, MemoryHashMap> hashMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int allocatedBlockNo = jobArr.get(i).getAllocatedBlockNo();
            hashMap.put(allocatedBlockNo, new MemoryHashMap(i + 1, jobArr.get(i).getJobSize()));
            blockNos[i] = allocatedBlockNo;
        }
        Arrays.sort(blockNos, Collections.<Integer>reverseOrder());
        for (int i = 0; i < n; i++) {
            int remainingSpace;
            int allocatedBlockNo = blockNos[i];
            if (allocatedBlockNo != -1) {
                remainingSpace = memBlk.get(allocatedBlockNo).getSize();
                if (remainingSpace != 0) {
                    MemoryBlock mBlock = new MemoryBlock(hashMap.get(allocatedBlockNo).getJobSize(), memBlk.get(allocatedBlockNo).getColor()); //+ i is used to hop as the length of mem array increases
                    mBlock.setIsJob();
                    mBlock.setUsedPartition();
                    mBlock.setJobNo("Job " + hashMap.get(allocatedBlockNo).getJobNo());
                    memBlk.add(allocatedBlockNo, mBlock);
                } else {
                    MemoryBlock mBlock = new MemoryBlock(hashMap.get(allocatedBlockNo).getJobSize(), memBlk.get(allocatedBlockNo).getColor());
                    mBlock.setJobNo("Job " + hashMap.get(allocatedBlockNo).getJobNo());
                    mBlock.setIsJob();
                    mBlock.setUsedPartition();
                    memBlk.set(allocatedBlockNo, mBlock);
                }
            }
        }

        for (int i = 0; i<memBlk.size(); i++){
            for(int j=0; j<memoryBlocks.size(); j++){
                if(memBlk.get(i).getSize()== memoryBlocks.get(j).getSize()){
                    if(!memBlk.get(i).isUsedPartition()){
                        externalFrag += memBlk.get(i).getSize();
                    }
                }
            }
        }

        SimulatedJob simulatedJob = new SimulatedJob(memBlk, internalFrag, externalFrag);
        simulationListener.onAlgorithmSimulationCompleted(simulatedJob);
    }


    public void simulateBestFitContiguous(ArrayList<Job> jobsArray, ArrayList<MemoryBlock> memoryBlocks) {
        int m = memoryBlocks.size();
        int n = jobsArray.size();
        int externalFrag = 0;
        int internalFrag = 0;
        ArrayList<Integer> allocation = new ArrayList<>();
        ArrayList<MemoryBlock> memBlk = new ArrayList<>();
        ArrayList<Job> jobArr = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            allocation.add(i, -1);
            jobArr.add(i, jobsArray.get(i));
        }
        for (int i = 0; i < m; i++) {
            memBlk.add(i, memoryBlocks.get(i));
        }

        for (int i = 0; i < n; i++) {
            //find best fit block for current process
            int bestIdx = -1;
            for (int j = 0; j < m; j++) {
                if (memBlk.get(j).getSize() >= jobArr.get(i).getJobSize()) {
                    if (bestIdx == -1)
                        bestIdx = j;
                    else if (memBlk.get(bestIdx).getSize() > memBlk.get(j).getSize())
                        bestIdx = j;
                }
            }
            //if can't find a block for current process
            if (bestIdx != -1) {
                allocation.set(i, bestIdx);
                MemoryBlock memoryBlock = new MemoryBlock(memBlk.get(bestIdx).getSize() - jobArr.get(i).getJobSize(), memBlk.get(bestIdx).getColor());
                // memoryBlock.setLocked();
                memoryBlock.setUsedPartition();
                memBlk.set(bestIdx, memoryBlock);
            }
        }

        for (int i = 0; i < n; i++) {
            jobArr.get(i).setAllocatedBlockNo(allocation.get(i));

            if (jobArr.get(i).getAllocatedBlockNo() != -1) {
                if (!memBlk.get(jobArr.get(i).getAllocatedBlockNo()).hasCalled()) {
                    internalFrag += memBlk.get(jobArr.get(i).getAllocatedBlockNo()).getSize();
                    memBlk.get(jobArr.get(i).getAllocatedBlockNo()).setHasCalled();
                }
            }
        }

        Integer[] blockNos = new Integer[n];
        HashMap<Integer, MemoryHashMap> hashMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int allocatedBlockNo = jobArr.get(i).getAllocatedBlockNo();
            hashMap.put(allocatedBlockNo, new MemoryHashMap(i + 1, jobArr.get(i).getJobSize()));
            blockNos[i] = allocatedBlockNo;
        }
        Arrays.sort(blockNos, Collections.<Integer>reverseOrder());


        for (int i = 0; i < n; i++) {
            int remainingSpace;
            int allocatedBlockNo = blockNos[i];
            if (allocatedBlockNo != -1) {
                remainingSpace = memBlk.get(allocatedBlockNo).getSize();
                if (remainingSpace != 0) {
                    MemoryBlock mBlock = new MemoryBlock(hashMap.get(allocatedBlockNo).getJobSize(), memBlk.get(allocatedBlockNo).getColor()); //+ i is used to hop as the length of mem array increases
                    mBlock.setIsJob();
                    mBlock.setUsedPartition();
                    mBlock.setJobNo("Job " + hashMap.get(allocatedBlockNo).getJobNo());
                    memBlk.add(allocatedBlockNo, mBlock);
                } else {
                    MemoryBlock mBlock = new MemoryBlock(hashMap.get(allocatedBlockNo).getJobSize(), memBlk.get(allocatedBlockNo).getColor());
                    mBlock.setJobNo("Job " + hashMap.get(allocatedBlockNo).getJobNo());
                    mBlock.setIsJob();
                    mBlock.setUsedPartition();
                    memBlk.set(allocatedBlockNo, mBlock);
                }
            }
        }

        for (int i = 0; i<memBlk.size(); i++){
            for(int j=0; j<memoryBlocks.size(); j++){
                if(memBlk.get(i).getSize()== memoryBlocks.get(j).getSize()){
                    if(!memBlk.get(i).isUsedPartition()){
                        externalFrag += memBlk.get(i).getSize();
                    }
                }
            }
        }

        SimulatedJob simulatedJob = new SimulatedJob(memBlk, internalFrag, externalFrag);
        simulationListener.onAlgorithmSimulationCompleted(simulatedJob);
    }


    public void simulateWorstFitNonContiguous(ArrayList<Job> jobsArray, ArrayList<MemoryBlock> memoryBlocks){
        int m = memoryBlocks.size();
        int n = jobsArray.size();
        int externalFrag = 0;
        int internalFrag = 0;
        ArrayList<Integer> allocation = new ArrayList<>();
        ArrayList<MemoryBlock> memBlk = new ArrayList<>();
        ArrayList<Job> jobArr = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            allocation.add(i, -1);
            jobArr.add(i, jobsArray.get(i));
        }
        for (int i = 0; i < m; i++) {
            memBlk.add(i, memoryBlocks.get(i));
        }

        for (int i = 0; i < n; i++) {
            //find worst fit block for current process
            int wstIdx = -1;
            for (int j = 0; j < m; j++) {
                if (memBlk.get(j).getSize() >= jobArr.get(i).getJobSize()) {
                    if (wstIdx == -1)
                        wstIdx = j;
                    else if (memBlk.get(wstIdx).getSize() < memBlk.get(j).getSize())
                        wstIdx = j;
                }
            }
            //if can't find a block for current process
            if (wstIdx != -1) {
                allocation.set(i, wstIdx);
                MemoryBlock memoryBlock = new MemoryBlock(memBlk.get(wstIdx).getSize() - jobArr.get(i).getJobSize(), memBlk.get(wstIdx).getColor());
                // memoryBlock.setLocked();
                memoryBlock.setUsedPartition();
                memBlk.set(wstIdx, memoryBlock);
            }
        }

        for (int i = 0; i < n; i++) {
            jobArr.get(i).setAllocatedBlockNo(allocation.get(i));
            if (jobArr.get(i).getAllocatedBlockNo() != -1) {
                if (!memBlk.get(jobArr.get(i).getAllocatedBlockNo()).hasCalled()) {
                    internalFrag += memBlk.get(jobArr.get(i).getAllocatedBlockNo()).getSize();
                    memBlk.get(jobArr.get(i).getAllocatedBlockNo()).setHasCalled();
                }
            }
        }

        Integer[] blockNos = new Integer[n];
        HashMap<Integer, MemoryHashMap> hashMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int allocatedBlockNo = jobArr.get(i).getAllocatedBlockNo();
            hashMap.put(allocatedBlockNo, new MemoryHashMap(i + 1, jobArr.get(i).getJobSize()));
            blockNos[i] = allocatedBlockNo;
        }
        Arrays.sort(blockNos, Collections.<Integer>reverseOrder());


        for (int i = 0; i < n; i++) {
            int remainingSpace;
            int allocatedBlockNo = blockNos[i];
            if (allocatedBlockNo != -1) {
                remainingSpace = memBlk.get(allocatedBlockNo).getSize();
                if (remainingSpace != 0) {
                    MemoryBlock mBlock = new MemoryBlock(hashMap.get(allocatedBlockNo).getJobSize(), memBlk.get(allocatedBlockNo).getColor()); //+ i is used to hop as the length of mem array increases
                    mBlock.setIsJob();
                    mBlock.setUsedPartition();
                    mBlock.setJobNo("Job " + hashMap.get(allocatedBlockNo).getJobNo());
                    memBlk.add(allocatedBlockNo, mBlock);
                } else {
                    MemoryBlock mBlock = new MemoryBlock(hashMap.get(allocatedBlockNo).getJobSize(), memBlk.get(allocatedBlockNo).getColor());
                    mBlock.setJobNo("Job " + hashMap.get(allocatedBlockNo).getJobNo());
                    mBlock.setIsJob();
                    mBlock.setUsedPartition();
                    memBlk.set(allocatedBlockNo, mBlock);
                }
            }
        }
        for (int i = 0; i<memBlk.size(); i++){
            for(int j=0; j<memoryBlocks.size(); j++){
                if(memBlk.get(i).getSize()== memoryBlocks.get(j).getSize()){
                    if(!memBlk.get(i).isUsedPartition()){
                        externalFrag += memBlk.get(i).getSize();
                    }
                }
            }
        }

        SimulatedJob simulatedJob = new SimulatedJob(memBlk, internalFrag, externalFrag);
        simulationListener.onAlgorithmSimulationCompleted(simulatedJob);

    }

    public interface simulationListener {
        void onAlgorithmSimulationCompleted(SimulatedJob simulatedJob);
    }
}
