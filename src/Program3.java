
//Name: Meyer Dauber
//EID: mjd3375


import java.util.Iterator;

public class Program3 {

    DamageCalculator calculator;
    PlanetPathScenario planetScenario;
    boolean[] visited;

    public Program3() {
        this.calculator = null;
        this.planetScenario = null;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 2.
     */
    public void initialize(PlanetPathScenario ps) {
        this.planetScenario = ps;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 1.
     */
    public void initialize(DamageCalculator dc) {
        this.calculator = dc;
    }


    /*
     * This method returns an integer that is the minimum amount of time necessary to travel
     * from the start planet to the end planet in the PlanetPathScenario given the total
     * amout of fuel that Thanos has. If a path is not possible given the amount of fuel, return -1.
     */
     public int computeMinimumTime() {
        int start = planetScenario.getStartPlanet();
        int end = planetScenario.getEndPlanet();
        int totalFuel = planetScenario.getTotalFuel();

        //handling entry edge cases
        if(totalFuel == 0 || start == end)
             return -1;

        //map grid to handle flight planning
        int[][] paths = new int[totalFuel+1][planetScenario.getNumPlanets()];

        //recursive calls from initial planet
        for(SpaceFlight flight : planetScenario.getFlightsFromPlanet(start))
        {
            recursive_DFS(flight, 0, start, paths);
        }

        //loop to determine shortest possible flight path with fuel requirement
        int shortest = Integer.MAX_VALUE;
        for(int fuel = 0; fuel < paths.length; fuel++){
            if(paths[fuel][end] != 0 && paths[fuel][end] < shortest){
                shortest = paths[fuel][end];
            }
        }

        if(shortest != Integer.MAX_VALUE)
            return shortest;
        else
            return -1;
     }

    /**
     * Recursive helper function to set items in grid with possible paths
     * @param link the current link being examined which has a destination and the time that leg of the trip will take
     * @param fuel current fuel usage to determine when you will run out in the trip
     * @param planet current planet to check against
     * @param matrix grid to use for mappings
     */
    void recursive_DFS(SpaceFlight link, int fuel, int planet, int[][] matrix)
    {
        //calculate new fuel value and determine if it is out of bounds
        int new_fuel = fuel + link.getFuel();
        if(new_fuel > matrix.length-1){
            return;
        }

        //set new time value for fuel slot as long as it is smaller than the current or if the current is 0
        int newtime = matrix[fuel][planet] + link.getTime();
        if(matrix[new_fuel][link.getDestination()]>newtime || matrix[new_fuel][link.getDestination()]==0)
            matrix[new_fuel][link.getDestination()] = newtime;

        //outgoing recursive calls for all possible flights out of the current node
        for(SpaceFlight flight : planetScenario.getFlightsFromPlanet(link.getDestination()))
        {
            recursive_DFS(flight, new_fuel, link.getDestination(), matrix);
        }
    }

    /*
     * This method returns an integer that is the maximum possible damage that can be dealt
     * given a certain amount of time.
     */
    public int computeDamage() {

        int totalTime = calculator.getTotalTime();
        int numAttacks = calculator.getNumAttacks();

        //create grid to store OPT values
        //NOTE: all values are initialized to 0 by default, which is the function value if no attack is engaged
        int[][] damage_map = new int[numAttacks+1][totalTime+1];

        //iterate through OPT table in row major order to replace optimal values
        for(int attack = 0; attack < numAttacks; attack++)
            for(int time = 1; time <= totalTime; time++)
                for(int func = 0; func <= time; func ++)
                    //replace value in optimal grid with new value if that character attack has a higher value
                    if(calculator.calculateDamage(attack, func) + damage_map[attack][time-func] > damage_map[attack+1][time])
                        damage_map[attack+1][time] = calculator.calculateDamage(attack, func) + damage_map[attack][time-func];

        //total damage done by all characters
        return damage_map[numAttacks][totalTime];
    }

    /**
     * Helper function to print out grid
     * @param matrix grid to print
     */
    void printMatrix(int[][] matrix){
        System.out.println();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


}


