package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Person;
import com.github.johanfredin.springdataextensions.repository.BaseRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class MockPersonRepositoryTest extends BaseRepositoryTest<Long, Person, MockPersonRepository> {

    private MockPersonRepository mockPersonRepository = new MockPersonRepository();

    @Override
    public MockPersonRepository getRepository() {
        return mockPersonRepository;
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