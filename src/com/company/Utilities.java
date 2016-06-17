package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Utilities {
    private static Random rd = new Random(123L);  // for reproducibility

    public static int getNextRandom(int bound){
        int res = rd.nextInt(bound);

        return res;
    }

    public static WorldX parseMap(String input_path) throws GameException, IOException{
        WorldX myWorld = new WorldX();

        String cityPattern = "^[A-Za-z-]+( (north|south|east|west)=[A-Za-z-]+)+$";

        List<String> lineArray = Files.readAllLines(Paths.get(input_path), StandardCharsets.UTF_8);

        for(int lineIndex = 0; lineIndex < lineArray.size(); lineIndex++){
            /*
            Sample input 1: Denalmo north=Agixo-A south=Amolusnisnu east=Elolesme west=Migina
            Sample input 2: Axu-Mu north=Mixesno south=Esnimu east=Esmelmismo
             */

            String currLine = lineArray.get(lineIndex);
            if (!currLine.matches(cityPattern)){
                throw new GameException(String.format("[%s] not matched the pattern for city", currLine));
            }

            String[] arr = currLine.split(" ");

            String name = null;
            String north = null;
            String south = null;
            String east = null;
            String west = null;

            for (int i = 0; i < arr.length; i++){
                if (arr[i].startsWith("north=")){
                    north = arr[i].split("=")[1];
                }
                else if (arr[i].startsWith("south=")){
                    south = arr[i].split("=")[1];
                }
                else if (arr[i].startsWith("east=")){
                    east = arr[i].split("=")[1];
                }
                else if (arr[i].startsWith("west=")){
                    west = arr[i].split("=")[1];
                }
                else{
                    name = arr[i];
                }
            }

            myWorld.updateWorldMap(name, north, south, east, west);
        }

        return myWorld;
    }
}
