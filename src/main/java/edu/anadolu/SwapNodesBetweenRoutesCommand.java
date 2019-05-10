package edu.anadolu;

import java.util.List;

public class SwapNodesBetweenRoutesCommand implements ICommand{
    private mTSP mTSP;
    private List<List<List<Integer>>> cloneRoutes;

    public SwapNodesBetweenRoutesCommand(mTSP mTSP){
        this.mTSP = mTSP;
    }

    @Override
    public void execute() {
        cloneRoutes = mTSP.getCloneRoutes();
        mTSP.swapNodesBetweenRoutes();
    }

    @Override
    public void unexecute() {
        mTSP.setRoutes(cloneRoutes);
    }
}
