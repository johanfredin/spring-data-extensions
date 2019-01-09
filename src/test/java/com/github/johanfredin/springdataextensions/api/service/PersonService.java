package com.github.johanfredin.springdataextensions.api.service;

import com.github.johanfredin.springdataextensions.api.doman.Person;
import com.github.johanfredin.springdataextensions.api.repository.PersonRepository;
import com.github.johanfredin.springdataextensions.service.ServiceBase;

public interface PersonService extends ServiceBase<Long, Person, PersonRepository> {
}
