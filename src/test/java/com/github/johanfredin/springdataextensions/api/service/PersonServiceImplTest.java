package com.github.johanfredin.springdataextensions.api.service;

import com.github.johanfredin.springdataextensions.api.doman.Person;
import com.github.johanfredin.springdataextensions.api.repository.PersonRepository;
import com.github.johanfredin.springdataextensions.service.ServiceBaseTest;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest extends ServiceBaseTest<Long, Person, PersonRepository, PersonService> {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Override
    public PersonService getService() {
        return this.personService;
    }

    @Override
    public PersonRepository getMockRepository() {
        return this.personRepository;
    }

    @Override
    public Person getEntity1() {
        return mock(Person.class);
    }

    @Override
    public Person getEntity2() {
        return mock(Person.class);
    }

    @Override
    public Long getId() {
        return 1L;
    }
}