package com.github.johanfredin.exstensionsdemo.service;

import com.github.johanfredin.springdataextensions.domain.Person;
import com.github.johanfredin.springdataextensions.repository.PersonRepository;

public interface PersonService extends ServiceBase<Long, Person, PersonRepository> {

}
