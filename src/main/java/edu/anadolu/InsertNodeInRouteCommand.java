package edu.anadolu;

import java.util.List;

public class InsertNodeInRouteCommand implements ICommand {
    private mTSP mTSP;
    private List<List<List<Integer>>> cloneRoutes;

    public InsertNodeInRouteCommand(mTSP mTSP){
        this.mTSP = mTSP;
    }

    @Override
    public void execute() {
        cloneRoutes = mTSP.getCloneRoutes();
        mTSP.insertNodeInRoute();
    }

    @Override
    public void unexecute() {
        mTSP.setRoutes(cloneRoutes);
    }
}
