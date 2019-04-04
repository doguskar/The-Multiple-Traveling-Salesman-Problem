package edu.anadolu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    }
    public void writeJson(){
        System.out.println("{\n" +
                "  \"solution\": [");
        int selectedDepot = 0;
        for(List<List<Integer>> listOfDepots: routes){
            int depotCity = selectedDepots.get(selectedDepot);
            System.out.println("   {");
            System.out.println("    \"depot\": \"" + depotCity + "\",");
            System.out.println("    \"routes\": [");


            int selectedSalesman = 1;
            for(int f = 0; f < listOfDepots.size(); f++){
                List<Integer> listOfSalesmen = listOfDepots.get(f);
                System.out.print("     \"");

                for(int i = 0; i < listOfSalesmen.size(); i++){
                    int cityNum = listOfSalesmen.get(i);
                    System.out.print(cityNum);
                    if(i != listOfSalesmen.size()-1)
                        System.out.print(" ");

                }
                selectedSalesman++;
                System.out.print("\"");
                if(f != listOfDepots.size()-1)
                    System.out.print(",");
                System.out.println();
            }
            selectedDepot++;
            System.out.println("    ]");
            System.out.println("   },");
        }

        System.out.println("  ]\n" +
                "}");
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
