package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.custom.VenueSearchRepository;
import com.github.johanfredin.springdataextensions.service.VenueSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("venueSearchService")
@Transactional
public class VenueSearchServiceImpl implements VenueSearchService {

    @Autowired
    private VenueSearchRepository venueSearchRepository;

    @Override
    public VenueSearchRepository getRepository() {
        return this.venueSearchRepository;
    }

    @Override
    public void setRepository(VenueSearchRepository repository) {
        this.venueSearchRepository = repository;
    }

}
