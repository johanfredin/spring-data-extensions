package com.github.johanfredin.springdataextensions.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public abstract class EmailHolderRepositoryTest<E extends EmailHolder, F extends EmailHolderRepository<E>> extends AbstractJpaRepositoryTest<E, F> {

    @Test
    public void testIsNoOtherEntityWithEmail() {
        E e1 = getPersistedEntity1();
        E e2 = getPersistedEntity2();

        e1.setEmail("sabo@email.com");
        e2.setEmail("dabo@email.com");
        super.save(e1, e2);

        assertTrue("email should be unique for e1", getRepository().isNoOtherEntityWithEmail(e1.getId(), e1.getEmail()));
        e1.setEmail(e2.getEmail());
        super.save(e1, e2);
        assertFalse("email should no longer be unique for a1", getRepository().isNoOtherEntityWithEmail(e1.getId(), e1.getEmail()));
    }

    @Test
    public void testIsEmailUnique() {
        E e = getEntity1();
        String email = "sabo@email.com";
        String email2 = "dabo@email.com";

        e.setEmail(email);
        getRepository().save(e);

        assertTrue("email=" + email2 + " should be unique", getRepository().isEmailUnique(email2));
        assertFalse("email=" + email + " should not be unique", getRepository().isEmailUnique(email));
    }


}
