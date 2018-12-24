package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.ArtistRepository;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("artistService")
@Transactional
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public ArtistRepository getRepository() {
        return this.artistRepository;
    }

    @Override
    public void setRepository(ArtistRepository repository) {
        this.artistRepository = repository;
    }


}
