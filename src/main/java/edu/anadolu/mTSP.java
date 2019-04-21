package edu.anadolu;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class mTSP {
    private int numDepots;
    private int numSalesmen;
    private List<Integer> selectedCities;
    private List<Integer> selectedDepots;
    private List<List<List<Integer>>> routes;
    private final int MIN_ROUTE = 1;


    public mTSP(int numDepots, int numSalesmen){
        if(numDepots < 1 || numSalesmen < 1)
            throw new IllegalArgumentException("Number of salesmen or number of depots cannot be smaller than 1!");
        this.numDepots = numDepots;
        this.numSalesmen = numSalesmen;
        selectedCities = new ArrayList<>();
        selectedDepots = new ArrayList<>();
        routes = new ArrayList<>();
    }
    public void randomSolution(){
        Random random = new Random();

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
            int rDepot = random.nextInt(numDepots);
            int rSalesman = random.nextInt(numSalesmen);
            int rCity = getCityNotSelected();
            routes.get(rDepot).get(rSalesman).add(rCity);
            selectedCities.add(rCity);
        }
        //Assignment of remaining cities END
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

    private int getRandomCity(){
        Random random = new Random();
        return random.nextInt(81);
    }
    private int getCityNotSelected(){
        while (true){
            int rCity = getRandomCity();
            if(!selectedCities.contains(rCity))
                return rCity;
        }
    }
}
