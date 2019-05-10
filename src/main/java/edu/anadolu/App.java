package edu.anadolu;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        Params params;
        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }
        int d = params.getNumDepots();
        int s = params.getNumSalesmen();

        //Illegal params control
        if(81-d-s*d < 0 || d <= 0 || s <= 0){
            throw new IllegalArgumentException();
        }

        mTSP mTSP = new mTSP(d, s);
        int minCost = Integer.MAX_VALUE;

        //Commands
        RandomSolutionCommand rsC = new RandomSolutionCommand(mTSP);
        SwapNodesInRouteCommand snirC = new SwapNodesInRouteCommand(mTSP);
        SwapHubWithNodeInRouteCommand shwnirC = new SwapHubWithNodeInRouteCommand(mTSP);
        SwapNodesBetweenRoutesCommand snbrC = new SwapNodesBetweenRoutesCommand(mTSP);
        InsertNodeInRouteCommand inirC = new InsertNodeInRouteCommand(mTSP);
        InsertNodeBetweenRoutesCommand inbrC = new InsertNodeBetweenRoutesCommand(mTSP);

        //Invoker
        Invoker invoker = new Invoker(rsC,snirC,shwnirC,snbrC,inirC,inbrC);

        //Random solution
        for(int i = 0; i < 100_000; i++){
            invoker.executeRandomSolution();
            int curCost = mTSP.cost();
            if(curCost < minCost)
                minCost = curCost;
            else
                invoker.unexecuteRandomSolution();
        }

        System.out.println("**Random sol. cost is :" + mTSP.cost());
        // Move Operations
        Random r = new Random();
        int op0 = 0, op1 = 0, op2 = 0, op3 = 0, op4 = 0;
        for(int i = 0; i < 5_000_000; i++){
            int rOpe = r.nextInt(5);
            if(rOpe == 0){

                int curCost = mTSP.cost();
                invoker.executeSwapNodesInRoute();
                int newCost = mTSP.cost();

                if(newCost > curCost)
                    invoker.unexecuteSwapNodesInRoute();
                else
                    op0++;
            }
            else if(rOpe == 1){
                int curCost = mTSP.cost();
                invoker.executeSwapHubWithNodeInRoute();
                int newCost = mTSP.cost();

                if(newCost > curCost)
                    invoker.unexecuteSwapHubWithNodeInRoute();
                else
                    op1++;
            }
            else if(rOpe == 2){
                int curCost = mTSP.cost();
                invoker.executeSwapNodesBetweenRoutes();
                int newCost = mTSP.cost();

                if(newCost > curCost)
                    invoker.unexecuteSwapNodesBetweenRoutes();
                else
                    op2++;
            }
            else if(rOpe == 3){
                int curCost = mTSP.cost();
                invoker.executeInsertNodeInRoute();
                int newCost = mTSP.cost();

                if(newCost > curCost)
                    invoker.unexecuteInsertNodeInRoute();
                else
                    op3++;
            }
            else if(rOpe == 4){
                int curCost = mTSP.cost();
                invoker.executeInsertNodeBetweenRoutes();
                int newCost = mTSP.cost();

                if(newCost > curCost)
                    invoker.unexecuteInsertNodeBetweenRoutes();
                else
                    op4++;
            }
        }

        JSONObject opJson = new JSONObject();
        opJson.put("swapNodesInRoute",op0);
        opJson.put("swapHubWithNodeInRoute",op1);
        opJson.put("swapNodesBetweenRoutes",op2);
        opJson.put("insertNodeInRoute",op3);
        opJson.put("insertNodeBetweenRoutes",op4);

        try {
            mTSP.writeMoveOpeJSON(opJson);
        }catch (IOException e){
            e.printStackTrace();
        }

        mTSP.print(params.getVerbose());
        System.out.println("**Total cost is :" + mTSP.cost());


        System.out.println("The process have been taken " + (System.currentTimeMillis()-t1) + "ms");
    }
}
