package edu.anadolu;

import java.util.List;

public class SwapNodesInRouteCommand implements ICommand {
    private mTSP mTSP;
    private List<List<List<Integer>>> cloneRoutes;

    public SwapNodesInRouteCommand(mTSP mTSP){
        this.mTSP = mTSP;
    }
    @Override
    public void execute() {
        cloneRoutes = mTSP.getCloneRoutes();
        mTSP.swapNodesInRoute();
    }
    @Override
    public void unexecute() {
        mTSP.setRoutes(cloneRoutes);
    }
}
