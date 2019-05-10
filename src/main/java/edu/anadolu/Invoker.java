package edu.anadolu;

public class Invoker {
    private ICommand randomSolution;
    private ICommand swapNodesInRoute;
    private ICommand swapHubWithNodeInRoute;
    private ICommand swapNodesBetweenRoutes;
    private ICommand insertNodeInRoute;
    private ICommand insertNodeBetweenRoutes;

    public Invoker(ICommand randomSolution, ICommand swapNodesInRoute, ICommand swapHubWithNodeInRoute, ICommand swapNodesBetweenRoutes, ICommand insertNodeInRoute, ICommand insertNodeBetweenRoutes) {
        this.randomSolution = randomSolution;
        this.swapNodesInRoute = swapNodesInRoute;
        this.swapHubWithNodeInRoute = swapHubWithNodeInRoute;
        this.swapNodesBetweenRoutes = swapNodesBetweenRoutes;
        this.insertNodeInRoute = insertNodeInRoute;
        this.insertNodeBetweenRoutes = insertNodeBetweenRoutes;
    }

    public void executeRandomSolution(){
        this.randomSolution.execute();
    }
    public void unexecuteRandomSolution(){
        this.randomSolution.unexecute();
    }

    public void executeSwapNodesInRoute(){
        this.swapNodesInRoute.execute();
    }
    public void unexecuteSwapNodesInRoute(){
        this.swapNodesInRoute.unexecute();
    }

    public void executeSwapHubWithNodeInRoute(){
        this.swapHubWithNodeInRoute.execute();
    }
    public void unexecuteSwapHubWithNodeInRoute(){
        this.swapHubWithNodeInRoute.unexecute();
    }

    public void executeSwapNodesBetweenRoutes(){
        this.swapNodesBetweenRoutes.execute();
    }
    public void unexecuteSwapNodesBetweenRoutes(){
        this.swapNodesBetweenRoutes.unexecute();
    }

    public void executeInsertNodeInRoute(){
        this.insertNodeInRoute.execute();
    }
    public void unexecuteInsertNodeInRoute(){
        this.insertNodeInRoute.unexecute();
    }

    public void executeInsertNodeBetweenRoutes(){
        this.insertNodeBetweenRoutes.execute();
    }
    public void unexecuteInsertNodeBetweenRoutes(){
        this.insertNodeBetweenRoutes.unexecute();
    }

}
