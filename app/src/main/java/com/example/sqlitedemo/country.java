package com.example.sqlitedemo;
public class country {

    int id;
    String countryName;
    long population;

    public country() {
        super();
    }
//    public country(int i, String countryName, long population) {
//        super();
//        this.id = i;
//        this.countryName = countryName;
//        this.population=population;
//    }

    // constructor
    public country(String countryName, long population){
        this.countryName = countryName;
        this.population = population;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public long getPopulation() {
        return population;
    }
    public void setPopulation(long population) {
        this.population = population;
    }

}
