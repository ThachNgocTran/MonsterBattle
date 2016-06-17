package com.company;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class MonsterBattle {

    public static void main(String[] args) {

        System.out.println("*** PROGRAM BEGINS ***");

        // Some constants
	    final String INPUT_PATH = "map.txt";
        final int MAX_ROUND = 10000;

        try{
            // Get the number of monsters
            if (args == null || args.length == 0){
                throw new GameException("Number of monsters: not indicated");
            }

            int numMonsters = -1;
            try{
                numMonsters = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException nfe){
                throw new GameException("Number of monsters: invalid format");
            }

            if (numMonsters <= 0){
                throw new GameException("Number of monsters: must be greater than 0");
            }

            // Get the world map
            if (!Files.exists(Paths.get(INPUT_PATH))){
                throw new GameException(String.format("[%s] not found", INPUT_PATH));
            }

            WorldX myWorld = Utilities.parseMap(INPUT_PATH);

            // Create and initialize the first position for monsters
            ArrayList<City> randomCities = myWorld.getRandomCities(numMonsters);
            if (randomCities.size() != numMonsters){
                throw new GameException("First random cities for monsters: cannot get");
            }

            HashMap<Integer, Monster> currMonstersInWorld = new HashMap<>();

            for (int i = 0; i < numMonsters; i++){
                currMonstersInWorld.put(i, new Monster(i, randomCities.get(i)));
                // probably there are some monsters who are in the same city already!
            }

            // for debug, print some statistics
            int numCitiesBeforeBattles = myWorld.getNumberOfCities();
            int numMonstersBeforeBattles = currMonstersInWorld.size();
            System.out.println(String.format("*** Statistics: Number Of Cities: [%d]; Number of Monsters: [%d] ***",
                                                                    numCitiesBeforeBattles,
                                                                    numMonstersBeforeBattles));


            // Now the game begins
            int currRound = 1;  // current round

            // The battle continues as long as the number of battle rounds is under the THRESHOLD,
            // and there are monsters in the world.
            while(currRound <= MAX_ROUND && currMonstersInWorld.size() > 0){

                //System.out.println(String.format("Current Round: %d; Current Monsters In World: %d",
                //                                           currRound, currMonstersInWorld.size()));

                // Check if there are monsters in the same city; if so, they fight each other to death,
                // and the city is removed.
                HashMap<String, Monster> currCitiesOccupiedByMonsters = new HashMap<>();    // e.g.: "Enegenixu" is occupied by Monster 5, "Musmo" by Monster 9...
                ArrayList<Monster> monsterMustDie = new ArrayList<>();

                for(Monster mon: currMonstersInWorld.values()){
                    String currCity = mon.getCity().getName();

                    if (!currCitiesOccupiedByMonsters.containsKey(currCity)){
                        currCitiesOccupiedByMonsters.put(currCity, mon);
                    }
                    else {
                        // Here, in this city, there is a conflict of at least two monsters.
                        monsterMustDie.add(mon);    // the new monster

                        // Check if we need to add the old one (run once only).
                        Monster theOldOne = currCitiesOccupiedByMonsters.get(currCity);
                        if (!monsterMustDie.contains(theOldOne)){
                            monsterMustDie.add(theOldOne);
                        }
                    }
                }

                // Release the resource (for sure).
                currCitiesOccupiedByMonsters.clear();

                // If there is any fight in THIS round, resolve the consequences.
                if (monsterMustDie.size() > 0){

                    // for display/report, such as "Bar has been destroyed by monster 10 and monster 34!"
                    HashMap<String, String> reportStatus = new HashMap<>();
                    for(int i = 0; i < monsterMustDie.size(); i++){
                        Monster monsterToReport = monsterMustDie.get(i);
                        String monsterName = monsterToReport.toString();
                        String monsterCity = monsterToReport.getCity().getName();

                        if (reportStatus.containsKey(monsterCity)){
                            reportStatus.put(monsterCity, reportStatus.get(monsterCity) + " and " +
                                                                                    monsterName);
                        }else{
                            reportStatus.put(monsterCity, monsterCity + " has been destroyed by " +
                                                                                    monsterName);
                        }
                    }

                    for(String status : reportStatus.values()){
                        System.out.println(status);
                    }

                    // We need to remove these monsters, and the city they are in.
                    for(int i = 0; i < monsterMustDie.size(); i++){
                        Monster monsterToDelete = monsterMustDie.get(i);

                        // THESE monsters die, so be removed off the world.
                        // The cities they ruined are also removed.
                        currMonstersInWorld.remove(monsterToDelete.getId());
                        myWorld.deleteCity(monsterToDelete.getCity().getName());
                    }
                }

                // Release the resource (for sure).
                monsterMustDie.clear();

                // Move the monsters remaining (if still there are some left!)
                if (currMonstersInWorld.size() > 0){
                    for(Monster mon: currMonstersInWorld.values()){
                        mon.moveNext();
                    }
                }

                // Next Round in battle
                currRound++;

            } // end while

            // for debug, print some statistics
            System.out.println(String.format("*** Statistics (After battles): Number Of Cities: [%d] (changed: %d);"
                                                + " Number of Monsters: [%d] (changed: %d);"
                                                + " Number of Batttles: [%d] ***",
                    myWorld.getNumberOfCities(),
                    numCitiesBeforeBattles - myWorld.getNumberOfCities(),
                    currMonstersInWorld.size(),
                    numMonstersBeforeBattles - currMonstersInWorld.size(),
                    currRound));

            // Now print the remaining map of the world.
            Files.write(Paths.get("mapNew.txt"), Arrays.asList(myWorld.printWorldMap()),
                                                                StandardCharsets.UTF_8,
                                                                StandardOpenOption.CREATE);

            System.out.println("*** PROGRAM DONE ***");
        }
        catch(GameException ge){
            System.out.println(String.format("Game Exception: %s", ge.getMessage()));
        }
        catch (Exception ex){
            System.out.println(String.format("Something unexpected! MUST PAY ATTENTION! (Error: %s)",
                                                                                    ex.toString()));
        }

        System.out.println("*** PROGRAM ENDS ***");
    }
}
