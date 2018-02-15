package com.abilashmenon.computerarchitecture;

import java.util.ArrayList;

/**
 * Created by men0n on 14/2/2018.
 */

public class SimulatedJob {
    private ArrayList<MemoryBlock> simulatedMemoryBlocks;
    private float internalFragmentation;
    private float externalFragmentation;

    public SimulatedJob(ArrayList<MemoryBlock> simulatedMemoryBlocks, float internalFragmentation, float externalFragmentation) {
        this.simulatedMemoryBlocks = simulatedMemoryBlocks;
        this.internalFragmentation = internalFragmentation;
        this.externalFragmentation = externalFragmentation;
    }

    public ArrayList<MemoryBlock> getSimulatedMemoryBlocks() {
        return simulatedMemoryBlocks;
    }

    public float getInternalFragmentation() {
        return internalFragmentation;
    }

    public float getExternalFragmentation() {
        return externalFragmentation;
    }
}
