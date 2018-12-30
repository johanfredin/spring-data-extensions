package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.domain.Person;

public class MockPersonRepository extends MockRepository<Long, Person> {

    private long id;

    @Override
    public Long nextId() {
        return ++id;
    }
}
