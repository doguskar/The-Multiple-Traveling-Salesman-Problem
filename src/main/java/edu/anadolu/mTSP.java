package edu.anadolu;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class mTSP implements Cloneable{
    private int numDepots;
    private int numSalesmen;
    private List<Integer> reminderCities;
    private List<Integer> selectedDepots;
    private List<List<List<Integer>>> routes;


    private final int MIN_ROUTE = 1;
    private final Random r = new Random();
    private int curCost;


    public mTSP(int numDepots, int numSalesmen){
        if(numDepots < 1 || numSalesmen < 1)
            throw new IllegalArgumentException("Number of salesmen or number of depots cannot be smaller than 1!");
        this.numDepots = numDepots;
        this.numSalesmen = numSalesmen;
        selectedDepots = new ArrayList<>();
        routes = new ArrayList<>();
        reminderCities = new ArrayList<>();
        for(int i=0; i<81; i++)
            reminderCities.add(i);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        mTSP clone = new mTSP(numDepots,numSalesmen);
        clone.setRoutes(this.getCloneRoutes());
        clone.setSelectedDepots(new ArrayList<>(this.getSelectedDepots()));
        clone.setReminderCities(new ArrayList<>(this.getReminderCities()));
        return clone;
    }

    public void randomSolution(){
        if(reminderCities.size() == 0){
            selectedDepots = new ArrayList<>();
            routes = new ArrayList<>();
            reminderCities = new ArrayList<>();
            for(int i=0; i<81; i++)
                reminderCities.add(i);
        }

        //Selecting Depots
        for(int i = 0; i < numDepots; i++){
            int rCity = getCityNotSelected();
            selectedDepots.add(rCity);
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
                }
            }
        }
        //Assigning Min Routes END

        //Assignment of remaining cities
        while (reminderCities.size() != 0){
            int rDepot = r.nextInt(numDepots);
            int rSalesman = r.nextInt(numSalesmen);
            int rCity = getCityNotSelected();
            routes.get(rDepot).get(rSalesman).add(rCity);
        }
        //Assignment of remaining cities END
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
        curCost = cost;
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

        //CONVERT TO IMG
        /*try{
            writeImgSol(jsonDir);
        }
        catch (ParseException e){
            e.printStackTrace();
        }*/
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
    public void setRoutes(List<List<List<Integer>>> routes) {
        this.routes = routes;
    }
    public List<Integer> getSelectedDepots() {
        return selectedDepots;
    }
    public void setSelectedDepots(List<Integer> selectedDepots) {
        this.selectedDepots = selectedDepots;
    }
    public List<Integer> getReminderCities() {
        return reminderCities;
    }
    public void setReminderCities(List<Integer> reminderCities) {
        this.reminderCities = reminderCities;
    }

    //Move operations
    public void swapNodesInRoute(){
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

    public void swapHubWithNodeInRoute(){
        int rDepotIn = r.nextInt(routes.size());
        List<List<Integer>> rDepot = routes.get(rDepotIn);
        List<Integer>  seletedRoute = rDepot.get(r.nextInt(rDepot.size()));

        int rHubIn = r.nextInt(seletedRoute.size());
        int rHub = seletedRoute.get(rHubIn);

        seletedRoute.set(rHubIn, selectedDepots.get(rDepotIn));
        selectedDepots.set(rDepotIn,rHub);
    }

    public void swapNodesBetweenRoutes(){
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

    public void insertNodeInRoute(){
        List<List<Integer>> rDepot = routes.get(r.nextInt(routes.size()));
        List<Integer>  seletedRoute = rDepot.get(r.nextInt(rDepot.size()));
        if(seletedRoute.size() < 2)
            return;

        int rHubIn = r.nextInt(seletedRoute.size());
        int rHub = seletedRoute.get(rHubIn);
        seletedRoute.remove(rHubIn);
        seletedRoute.add(rHub);
    }

    public void insertNodeBetweenRoutes(){
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

    public void writeMoveOpeJSON(JSONObject json) throws IOException{
        File jsonDir = new File(System.getProperty("user.dir")
                + "\\solution_d" + numDepots + "s" + numSalesmen + "_moveOpeNums.json");

        Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonDir), "UTF8"));
        output.write(json.toJSONString());
        output.close();
    }

    public List<List<List<Integer>>> getCloneRoutes(){
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
