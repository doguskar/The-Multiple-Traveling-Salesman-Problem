package edu.anadolu;

import java.util.ArrayList;
import java.util.List;

public class RandomSolutionCommand implements ICommand {
    private mTSP mTSP;
    private List<List<List<Integer>>> cloneRoutes;
    private List<Integer> cloneSelectedDepots;

    public RandomSolutionCommand(mTSP mTSP){
        this.mTSP = mTSP;
    }

    @Override
    public void execute() {
        cloneSelectedDepots = new ArrayList<>(mTSP.getSelectedDepots());
        cloneRoutes = mTSP.getCloneRoutes();
        mTSP.randomSolution();
    }

    @Override
    public void unexecute() {
        mTSP.setSelectedDepots(cloneSelectedDepots);
        mTSP.setRoutes(cloneRoutes);
    }
}
