package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.VenueRepository;
import com.github.johanfredin.springdataextensions.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("venueService")
@Transactional
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public VenueRepository getRepository() {
        return this.venueRepository;
    }

    @Override
    public void setRepository(VenueRepository repository) {
        this.venueRepository = repository;
    }

}
