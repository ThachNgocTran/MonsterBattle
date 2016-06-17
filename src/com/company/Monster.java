package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Monster {

    private int m_id = 0;
    private City m_City = null;

    public Monster(int id, City city){
        m_id = id;
        m_City = city;
    }

    public City getCity(){
        return m_City;
    }

    public int getId(){
        return m_id;
    }

    public void moveNext(){

        City north = m_City.getNorth();
        City south = m_City.getSouth();
        City east = m_City.getEast();
        City west = m_City.getWest();

        ArrayList<City> availableMoves = new ArrayList<>();
        if (north != null){
            availableMoves.add(north);
        }
        if (south != null){
            availableMoves.add(south);
        }
        if (east != null){
            availableMoves.add(east);
        }
        if (west != null){
            availableMoves.add(west);
        }

        // If this monster can move, it moves randomly.
        if (availableMoves.size() > 0){ // we have options to move next
            City nextCity = availableMoves.get(Utilities.getNextRandom(availableMoves.size()));

            // Finish moving!
            m_City = nextCity;
        }
        else{
            // TRAPPED!!!
        }
    }

    @Override
    public String toString(){
        return String.format("monster %d", m_id);
    }
}
