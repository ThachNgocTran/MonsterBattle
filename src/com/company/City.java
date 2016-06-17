package com.company;

public class City {
    WorldX m_world = null;

    String m_name = "";

    City m_north = null;
    City m_south = null;
    City m_east = null;
    City m_west = null;

    public City(String name, WorldX world){
        m_name = name;
        m_world = world;

        // for debugging: make sure that a city is created only once, given a specific name.
        //System.out.println(String.format("Creating city: %s", name));
    }

    public String getName(){
        return m_name;
    }

    public City getNorth(){
        return m_north;
    }

    public City getSouth(){
        return m_south;
    }

    public City getEast(){
        return m_east;
    }

    public City getWest(){
        return m_west;
    }

    public void setNorth(City north){
        m_north = north;
    }

    public void setSouth(City south){
        m_south = south;
    }

    public void setEast(City east){
        m_east = east;
    }

    public void setWest(City west){
        m_west = west;
    }

}
