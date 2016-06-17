package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WorldX {

    private HashMap<String, City> m_worldMap = null;    // e.g.: elements ("Alexisna", CityObject), ("Dagalu", CityObject)...

    public WorldX(){
        m_worldMap = new HashMap<String, City>();
    }

    public City updateWorldMap(String newCityName, String north, String south, String east, String west) throws GameException {

        if (newCityName == null || newCityName.length() == 0){
            throw new GameException("New city name: invalid");
        }

        if (!m_worldMap.containsKey(newCityName)){
            m_worldMap.put(newCityName, new City(newCityName, this));
        }

        City currCity = m_worldMap.get(newCityName);

        // Now ensure the correctness of the world map.
        // North Direction.
        if (north != null && north.length() > 0){
            if (!m_worldMap.containsKey(north)){
                m_worldMap.put(north, new City(north, this));
            }

            City northCity = m_worldMap.get(north);
            currCity.setNorth(northCity);
            northCity.setSouth(currCity);
        }

        // South Direction.
        if (south != null && south.length() > 0){
            if (!m_worldMap.containsKey(south)){
                m_worldMap.put(south, new City(south, this));
            }

            City southCity = m_worldMap.get(south);
            currCity.setSouth(southCity);
            southCity.setNorth(currCity);
        }

        // East Direction.
        if (east != null && east.length() > 0){
            if (!m_worldMap.containsKey(east)){
                m_worldMap.put(east, new City(east, this));
            }

            City eastCity = m_worldMap.get(east);
            currCity.setEast(eastCity);
            eastCity.setWest(currCity);
        }

        // West Direction.
        if (west != null && west.length() > 0){
            if (!m_worldMap.containsKey(west)){
                m_worldMap.put(west, new City(west, this));
            }

            City westCity = m_worldMap.get(west);
            currCity.setWest(westCity);
            westCity.setEast(currCity);
        }

        return currCity;
    }

    public void deleteCity(String cityName) throws GameException {
        if (cityName == null || cityName.length() == 0){
            throw new GameException("City to delete: Invalid name");
        }

        if (m_worldMap.containsKey(cityName)){
            City cityToDelete = m_worldMap.get(cityName);

            // Maintain the integrity of the world map.
            City north = cityToDelete.getNorth();
            City south = cityToDelete.getSouth();
            City east = cityToDelete.getEast();
            City west = cityToDelete.getWest();

            if (north != null){
                north.setSouth(null);
            }

            if (south != null){
                south.setNorth(null);
            }

            if (east != null){
                east.setWest(null);
            }

            if (west != null){
                west.setEast(null);
            }

            // remove the city.
            m_worldMap.remove(cityName);
        }
    }

    public ArrayList<City> getRandomCities(int num) throws GameException{

        if (num <= 0){
            throw new GameException("Number of random city: invalid");
        }

        City[] listCities = m_worldMap.values().toArray(new City[0]);

        ArrayList<City> res = new ArrayList<>();

        for(int i = 0; i < num; i++){
            int nextRand = Utilities.getNextRandom(listCities.length);
            res.add(listCities[nextRand]);
        }

        return res;
    }

    public String printWorldMap(){
        StringBuilder strBuilder = new StringBuilder();

        for (City entry : m_worldMap.values()){
            String cityData = entry.getName();

            if (entry.getNorth() != null){
                cityData += " north=" +  entry.getNorth().getName();
            }
            if (entry.getSouth() != null){
                cityData += " south=" + entry.getSouth().getName();
            }
            if (entry.getEast() != null){
                cityData += " east=" + entry.getEast().getName();
            }
            if (entry.getWest() != null){
                cityData += " west=" + entry.getWest().getName();
            }

            cityData += "\n";

            strBuilder.append(cityData);
        }

        return strBuilder.toString();
    }

    public int getNumberOfCities(){
        return m_worldMap.size();
    }
}
