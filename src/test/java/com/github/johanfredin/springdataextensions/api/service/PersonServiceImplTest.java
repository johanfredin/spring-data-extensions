package com.github.johanfredin.springdataextensions.api.service;

import com.github.johanfredin.springdataextensions.api.doman.Person;
import com.github.johanfredin.springdataextensions.api.repository.PersonRepository;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.service.ExtendedServiceBase;
import com.github.johanfredin.springdataextensions.service.ExtendedServiceBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest extends ExtendedServiceBaseTest<Long, Person, PersonRepository, PersonService> {

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

    @Test
    public void saveAllAsSet_ArbitraryArgsReturnsSet() {
        Person t1 = getEntity1();
        Person t2 = getEntity2();
        Set<Person> set = mSetOf(t1, t2);
        when(getMockRepository().saveAllAsSet(t1, t2)).thenReturn(set);
        Set<Person> saved = getService().saveAllAsSet(t1, t2);
        assertEquals("saved=setOf(t1, t2)", saved, set);
        verify(getMockRepository()).saveAllAsSet(t1, t2);
    }

    @Test
    public void saveAll_arbitraryArgsReturnsList() {
        Person t1 = getEntity1();
        Person t2 = getEntity2();
        List<Person> list = mListOf(t1, t2);
        when(getMockRepository().saveAll(t1, t2)).thenReturn(list);
        List<Person> saved = getService().saveAll(t1, t2);
        assertEquals("saved=list(t1, t2)", saved, list);
        verify(getMockRepository()).saveAll(t1, t2);
    }

    /** Test {@link ExtendedServiceBase#deleteAll(Identifiable[])} */
    @Test
    public void deleteAll_collectionArg() {
        Person t1 = getEntity1();
        Person t2 = getEntity2();
        doNothing().when(getMockRepository()).deleteAll(t1, t2);
        getService().deleteAll(t1, t2);
        verify(getMockRepository()).deleteAll(t1, t2);
    }
}