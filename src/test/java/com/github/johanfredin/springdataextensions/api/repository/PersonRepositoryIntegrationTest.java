package com.github.johanfredin.springdataextensions.api.repository;

import com.github.johanfredin.springdataextensions.api.doman.Person;
import com.github.johanfredin.springdataextensions.api.doman.Pet;
import com.github.johanfredin.springdataextensions.repository.CascadeIntegrationTest;
import com.github.johanfredin.springdataextensions.repository.ExtendedBaseRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class PersonRepositoryIntegrationTest extends ExtendedBaseRepositoryTest<Long, Person, PersonRepository> implements CascadeIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public PersonRepository getRepository() {
        return this.personRepository;
    }

    @Override
    public Person getEntity1() {
        return new Person("Johan");
    }

    @Override
    public Person getEntity2() {
        return new Person("Evelyn");
    }

    @Override
    public void testCascadePersist() {
        var p = getEntity1();
        p.setPets(new ArrayList<>(List.of(new Pet("Fido"))));
        var fido = p.getPets().get(0);
        assertFalse("Fido not persisted", fido.isPersistedEntity());

        getRepository().save(p);
        assertTrue("Fido persisted", fido.isPersistedEntity());
    }

    @Override
    public void testCascadeDelete() {
        var p = getEntity1();
        p.setPets(new ArrayList<>(List.of(new Pet("Fido"))));
        var fido = p.getPets().get(0);
        getRepository().save(p);
        assertTrue("Fido persisted", fido.isPersistedEntity());

        getRepository().delete(p);
        assertFalse("Fido is gone", fido.isPersistedEntity());
    }
}
