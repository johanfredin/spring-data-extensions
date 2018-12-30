package com.github.johanfredin.exstensionsdemo.repository;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonRepositoryIntegrationTest extends BaseRepositoryTest<Long, Person, PersonRepository> {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public PersonRepository getRepository() {
        return this.personRepository;
    }

    @Override
    public Person getEntity1() {
        return TestFixture.getValidPersonWithReference(true);
    }

    @Override
    public Person getEntity2() {
        return TestFixture.getValidPersonWithReference(
                "Ulf",
                TestFixture.getValidAddressWithoutReference("Street 2", "Zip code 2", "City 2", "Country 2"),
                true
        );
    }
}