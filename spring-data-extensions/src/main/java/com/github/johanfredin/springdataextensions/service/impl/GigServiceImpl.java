package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.GigRepository;
import com.github.johanfredin.springdataextensions.service.GigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("gigService")
@Transactional
public class GigServiceImpl implements GigService {

    @Autowired
    private GigRepository gigRepository;

    @Override
    public GigRepository getRepository() {
        return this.gigRepository;
    }

    @Override
    public void setRepository(GigRepository repository) {
        this.gigRepository = repository;
    }

}
