package com.github.johanfredin.springdataextensions.api.service;

import com.github.johanfredin.springdataextensions.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonRepository getRepository() {
        return personRepository;
    }
}
