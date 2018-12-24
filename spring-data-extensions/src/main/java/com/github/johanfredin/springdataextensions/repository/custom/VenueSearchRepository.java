package com.github.johanfredin.springdataextensions.repository.custom;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

@NoRepositoryBean
@Component(value = "venueSearchRepository")
public interface VenueSearchRepository {

    List<Venue> getVenuesMatchingSearchParams(String name, String city, String country, Genre genre);

    List<Venue> getVenuesMatchingSearchParams(String name, String city, String country, Genre genre, int maxResults);

}
