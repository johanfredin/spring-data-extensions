package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.VenueReviewRepository;
import com.github.johanfredin.springdataextensions.service.VenueReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("reviewService")
@Transactional
public class VenueReviewServiceImpl implements VenueReviewService {

    @Autowired
    private VenueReviewRepository venueReviewRepository;

    @Override
    public VenueReviewRepository getRepository() {
        return this.venueReviewRepository;
    }

    @Override
    public void setRepository(VenueReviewRepository repository) {
        this.venueReviewRepository = repository;
    }

}
   