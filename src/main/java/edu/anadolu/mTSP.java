package edu.anadolu;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class mTSP{
    private int numDepots;
    private int numSalesmen;
    private List<Integer> reminderCities;
    private List<Integer> selectedCities;
    private List<Integer> selectedDepots;
    private List<List<List<Integer>>> routes;
    private final int MIN_ROUTE = 1;
    private final Random r = new Random();


    public mTSP(int numDepots, int numSalesmen){
        if(numDepots < 1 || numSalesmen < 1)
            throw new IllegalArgumentException("Number of salesmen or number of depots cannot be smaller than 1!");
        this.numDepots = numDepots;
        this.numSalesmen = numSalesmen;
        selectedCities = new ArrayList<>();
        selectedDepots = new ArrayList<>();
        routes = new ArrayList<>();
        reminderCities = new ArrayList<>();
        for(int i=0; i<81; i++)
            reminderCities.add(i);
    }
    public void randomSolution(){
        //Selecting Depots
        for(int i = 0; i < numDepots; i++){
            int rCity = getCityNotSelected();
            selectedDepots.add(rCity);
            selectedCities.add(rCity);
        }
        //Selecting Depots END

        //Creating Routes Lists
        for(int i = 0; i < numDepots; i++){
            List<List<Integer>> tmpNull = new ArrayList<>();
            for(int f = 0; f < numSalesmen; f++)
                tmpNull.add(new ArrayList<>());
            routes.add(tmpNull);
        }
        //Creating Routes Lists END

        //Assigning Min Routes
        for(List<List<Integer>> list1: routes){
            for (List<Integer> list2: list1){
                for(int i = 0; i < MIN_ROUTE; i++){
                    int rCity = getCityNotSelected();
                    list2.add(rCity);
                    selectedCities.add(rCity);
                }
            }
        }
        //Assigning Min Routes END

        //Assignment of remaining cities
        while (selectedCities.size() != TurkishNetwork.cities.length){
            int rDepot = r.nextInt(numDepots);
            int rSalesman = r.nextInt(numSalesmen);
            int rCity = getCityNotSelected();
            routes.get(rDepot).get(rSalesman).add(rCity);
            selectedCities.add(rCity);
        }
        //Assignment of remaining cities END

        //Use Move Operations
        useMoveOpe();
    }
    public void validate(){

    }
    public int cost(){
        int cost = 0;
        for(List<List<Integer>> listOfDepots: routes){
            int selectedDepot = 0;
            int depotCity = selectedDepots.get(selectedDepot);
            for(List<Integer> listOfSalesmen: listOfDepots){
                int currentCity = depotCity;
                for(Integer cityNum: listOfSalesmen){
                    cost += TurkishNetwork.distance[currentCity][cityNum];
                    currentCity = cityNum;
                }
                cost += TurkishNetwork.distance[currentCity][depotCity];
            }
        }
        return cost;
    }
    public void print(boolean getVerbose){
        int selectedDepot = 0;
        for(List<List<Integer>> listOfDepots: routes){
            int depotCity = selectedDepots.get(selectedDepot);

            System.out.print("Depot" + (selectedDepot+1) + ": ");
            System.out.println((getVerbose)? TurkishNetwork.cities[depotCity]: depotCity);

            int selectedSalesman = 1;
            for(List<Integer> listOfSalesmen: listOfDepots){

                System.out.print("\tRoute" + selectedSalesman + ": ");

                for(int i = 0; i < listOfSalesmen.size(); i++){
                    int cityNum = listOfSalesmen.get(i);
                    System.out.print((getVerbose)? TurkishNetwork.cities[cityNum]: cityNum);
                    if(i != listOfSalesmen.size()-1)
                        System.out.print(",");

                }
                selectedSalesman++;

                System.out.println();
            }
            selectedDepot++;
        }

        try{
            writeJson();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void writeJson() throws IOException{
        JSONObject solution = new JSONObject();
        List<Map> solMap = new ArrayList<>();

        int selectedDepot = 0;
        for(List<List<Integer>> listOfDepots: routes){
            int depotCity = selectedDepots.get(selectedDepot);
            selectedDepot++;

            Map<String,Object> solSub = new HashMap<>();
            solSub.put("depot", (""+depotCity));

            List<String> routes = new ArrayList<>();
            for(int f = 0; f < listOfDepots.size(); f++){
                List<Integer> listOfSalesmen = listOfDepots.get(f);

                /**/String routeString = "";
                for(int i = 0; i < listOfSalesmen.size(); i++){
                    int cityNum = listOfSalesmen.get(i);
                    routeString += cityNum;
                    if(i != listOfSalesmen.size()-1)
                        routeString += " ";
                }
                routes.add(routeString);
            }
            solSub.put("routes", routes);
            solMap.add(solSub);
        }

        solution.put("solution",solMap);

        File jsonDir = new File(System.getProperty("user.dir")
                + "\\solution_d" + numDepots + "s" + numSalesmen + ".json");

        Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonDir), "UTF8"));
        output.write(solution.toJSONString());
        output.close();
        try{
            writeImgSol(jsonDir);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
    private void writeImgSol(File jsonDir) throws ParseException, IOException{
        CreateImgSol.create(jsonDir);
    }

    private int getCityNotSelected(){
        int sizeOfRC = reminderCities.size();
        int rNum = r.nextInt(sizeOfRC);
        int cityNum = reminderCities.get(rNum);
        reminderCities.remove(rNum);
        return cityNum;
    }


    /*Part II*/

    public void useMoveOpe(){
        int op0 = 0, op1 = 0, op2 = 0, op3 = 0, op4 = 0;
        for (int i = 0; i < 5_000_000; i++) {

            int rOpe = r.nextInt(5);
            //rOpe = 4;
            if(rOpe == 0){
                List<List<List<Integer>>> tmpRoutes = getCloneRoutes();
                int tmpCost = cost();

                swapNodesInRoute();

                int newCost = cost();
                if(newCost > tmpCost)
                    routes = tmpRoutes;
                else
                    op0++;
            }
            else if(rOpe == 1){
                List<Integer> tmpSelectedDepots = new ArrayList<>(selectedDepots);
                List<List<List<Integer>>> tmpRoutes = getCloneRoutes();
                int tmpCost = cost();

                swapHubWithNodeInRoute();

                int newCost = cost();
                if(newCost > tmpCost){
                    selectedDepots = tmpSelectedDepots;
                    routes = tmpRoutes;
                }
                else
                    op1++;
            }
            else if(rOpe == 2){
                List<List<List<Integer>>> tmpRoutes = getCloneRoutes();
                int tmpCost = cost();

                swapNodesBetweenRoutes();

                int newCost = cost();
                if(newCost > tmpCost)
                    routes = tmpRoutes;
                else
                    op2++;
            }
            else if(rOpe == 3){
                List<List<List<Integer>>> tmpRoutes = getCloneRoutes();
                int tmpCost = cost();

                insertNodeInRoute();

                int newCost = cost();
                if(newCost > tmpCost)
                    routes = tmpRoutes;
                else
                    op3++;
            }
            else if(rOpe == 4){
                List<Integer> tmpSelectedDepots = new ArrayList<>(selectedDepots);
                List<List<List<Integer>>> tmpRoutes = getCloneRoutes();
                int tmpCost = cost();

                insertNodeBetweenRoutes();

                int newCost = cost();
                if(newCost > tmpCost){
                    selectedDepots = tmpSelectedDepots;
                    routes = tmpRoutes;
                }
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
            writeMoveOpeJSON(opJson);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void writeMoveOpeJSON(JSONObject json) throws IOException{
        File jsonDir = new File(System.getProperty("user.dir")
                + "\\solution_d" + numDepots + "s" + numSalesmen + "_moveOpeNums.json");

        Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonDir), "UTF8"));
        output.write(json.toJSONString());
        output.close();
    }

    private void swapNodesInRoute(){
        List<List<Integer>> rDepot = routes.get(r.nextInt(routes.size()));
        List<Integer>  seletedRoute = rDepot.get(r.nextInt(rDepot.size()));
        if(seletedRoute.size() == 1)
            return;
        int[] rHubs = getTwoRondomUniqueNum(seletedRoute.size());

        int tmpHub = seletedRoute.get(rHubs[0]);
        seletedRoute.set(rHubs[0],seletedRoute.get(rHubs[1]));
        seletedRoute.set(rHubs[1],tmpHub);
    }
    private int[] getTwoRondomUniqueNum(int sizeOfHub){
        int[] nums = new int[2];
        nums[0] = r.nextInt(sizeOfHub);
        do {
            nums[1] = r.nextInt(sizeOfHub);
        }while (nums[0] == nums[1]);

        return nums;
    }

    private void swapHubWithNodeInRoute(){
        int rDepotIn = r.nextInt(routes.size());
        List<List<Integer>> rDepot = routes.get(rDepotIn);
        List<Integer>  seletedRoute = rDepot.get(r.nextInt(rDepot.size()));

        int rHubIn = r.nextInt(seletedRoute.size());
        int rHub = seletedRoute.get(rHubIn);

        seletedRoute.set(rHubIn, selectedDepots.get(rDepotIn));
        selectedDepots.set(rDepotIn,rHub);
    }

    private void swapNodesBetweenRoutes(){
        List<List<Integer>> rDepot1 = routes.get(r.nextInt(routes.size()));
        List<Integer>  seletedRoute1 = rDepot1.get(r.nextInt(rDepot1.size()));
        List<List<Integer>> rDepot2;
        List<Integer>  seletedRoute2;
        do{
            rDepot2 = routes.get(r.nextInt(routes.size()));
            seletedRoute2 = rDepot2.get(r.nextInt(rDepot2.size()));
        }while (seletedRoute1.get(0) == seletedRoute2.get(0));

        int rHubF1In = r.nextInt(seletedRoute1.size());
        int rHubF2In = r.nextInt(seletedRoute2.size());
        int rHubF1 = seletedRoute1.get(rHubF1In);

        seletedRoute1.set(rHubF1In,seletedRoute2.get(rHubF2In));
        seletedRoute2.set(rHubF2In,rHubF1);
    }

    private void insertNodeInRoute(){
        List<List<Integer>> rDepot = routes.get(r.nextInt(routes.size()));
        List<Integer>  seletedRoute = rDepot.get(r.nextInt(rDepot.size()));
        if(seletedRoute.size() < 2)
            return;

        int rHubIn = r.nextInt(seletedRoute.size());
        int rHub = seletedRoute.get(rHubIn);
        seletedRoute.remove(rHubIn);
        seletedRoute.add(rHub);
    }

    private void insertNodeBetweenRoutes(){
        List<List<Integer>> rDepot1 = routes.get(r.nextInt(routes.size()));
        List<Integer>  seletedRoute1 = rDepot1.get(r.nextInt(rDepot1.size()));
        if(seletedRoute1.size() < 2)
            return;
        List<List<Integer>> rDepot2;
        List<Integer>  seletedRoute2;
        do{
            rDepot2 = routes.get(r.nextInt(routes.size()));
            seletedRoute2 = rDepot2.get(r.nextInt(rDepot2.size()));
        }while (seletedRoute1.get(0) == seletedRoute2.get(0));
        if(seletedRoute2.size() < 2)
            return;

        int rHubF1In = r.nextInt(seletedRoute1.size());
        int rHubF2In = r.nextInt(seletedRoute2.size());
        int rHubF1 = seletedRoute1.get(rHubF1In);

        seletedRoute1.remove(rHubF1In);
        if(rHubF2In == seletedRoute2.size())
            seletedRoute2.add(rHubF1);
        else
            seletedRoute2.add((rHubF2In+1),rHubF1);

    }

    private List<List<List<Integer>>> getCloneRoutes(){
        List<List<List<Integer>>> clone = new ArrayList<>();

        for(List<List<Integer>> depot : routes){
            List<List<Integer>> cloneDepot = new ArrayList<>();
            for (List<Integer> route: depot){
                cloneDepot.add(new ArrayList<>(route));
            }
            clone.add(cloneDepot);
        }

        return clone;
    }
}
