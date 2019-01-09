package com.github.johanfredin.springdataextensions.api.repository;

import com.github.johanfredin.springdataextensions.api.doman.Person;
import com.github.johanfredin.springdataextensions.repository.ExtendedBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ExtendedBaseRepository<Long, Person> {

}
