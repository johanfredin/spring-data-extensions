package com.github.johanfredin.exstensionsdemo.service.impl;

import com.github.johanfredin.springdataextensions.repository.PersonRepository;
import com.github.johanfredin.springdataextensions.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("memberService")
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonRepository getRepository() {
        return this.personRepository;
    }

}
