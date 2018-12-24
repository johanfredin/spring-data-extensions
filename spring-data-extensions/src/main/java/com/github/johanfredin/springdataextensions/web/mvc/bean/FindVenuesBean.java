package com.github.johanfredin.springdataextensions.web.mvc.bean;

import javax.validation.Valid;
import java.util.List;

public class FindVenuesBean implements RefreshBean {

    private String name;
    private String city;
    private String country;
    private Genre genre;

    @Valid
    private List<Venue> results;

    public FindVenuesBean() {
    }

    public FindVenuesBean(String name, String city, String country, Genre genre) {
        setName(name);
        setCity(city);
        setCountry(country);
        setGenre(genre);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Venue> getResults() {
        return results;
    }

    public void setResults(List<Venue> results) {
        this.results = results;
    }

    @Override
    public boolean isNull() {
        return getName() == null && getCity() == null && getCountry() == null && getGenre() == null && getResults() == null;
    }

    @Override
    public boolean isAlive() {
        return getName() != null && getCity() != null && getCountry() != null && getGenre() != null && getResults() != null;
    }

}
