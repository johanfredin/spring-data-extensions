package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.repository.custom.VenueSearchRepository;

import java.util.List;

public interface VenueSearchService {

    default List<Venue> getVenuesMatchingSearchParams(String name, String city, String country, Genre genre) {
        return getRepository().getVenuesMatchingSearchParams(name, city, country, genre);
    }

    default List<Venue> getVenuesMatchingSearchParams(String name, String city, String country, Genre genre, int maxResults) {
        return getRepository().getVenuesMatchingSearchParams(name, city, country, genre, maxResults);
    }

    VenueSearchRepository getRepository();

    void setRepository(VenueSearchRepository repository);

}
