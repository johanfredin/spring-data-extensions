package com.github.johanfredin.springdataextensions.api.repository;

import com.github.johanfredin.springdataextensions.api.doman.Person;
import com.github.johanfredin.springdataextensions.api.doman.Pet;
import com.github.johanfredin.springdataextensions.repository.CascadeIntegrationTest;
import com.github.johanfredin.springdataextensions.repository.ExtendedBaseRepositoryTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonRepositoryIntegrationTest extends ExtendedBaseRepositoryTest<Long, Person, PersonRepository> implements CascadeIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public PersonRepository getRepository() {
        return this.personRepository;
    }

    @Override
    public Person getEntity1() {
        return new Person("Johan", mListOf(new Pet("Greg")));
    }

    @Override
    public Person getEntity2() {
        return new Person("Evelyn", mListOf(new Pet("Ava")));
    }

    @Override
    public void testCascadePersist() {
        Person p = getEntity1();
        p.setPets(new ArrayList<>(Arrays.asList(new Pet("Fido"))));
        Pet fido = p.getPets().get(0);
        assertFalse("Fido not persisted", fido.isPersistedEntity());

        getRepository().save(p);
        assertTrue("Fido persisted", fido.isPersistedEntity());
    }

    @Override
    public void testCascadeDelete() {
        Person p = getEntity1();
        p.setPets(new ArrayList<>(Arrays.asList(new Pet("Fido"))));
        Pet fido = p.getPets().get(0);
        getRepository().save(p);
        assertTrue("Fido persisted", fido.isPersistedEntity());

        getRepository().delete(p);
        assertFalse("Fido is gone", fido.isPersistedEntity());
    }
}
