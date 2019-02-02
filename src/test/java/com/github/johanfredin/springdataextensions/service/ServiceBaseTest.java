/*
 * Copyright 2018 Johan Fredin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.TransactionalTest;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.junit.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for all methods in {@link ServiceBase} using {@link org.mockito.Mockito}
 * for mocking {@link BaseRepository} dependencies. As mentioned in {@link ServiceBase}
 * documentation, the service holds all methods available in {@link BaseRepository} with the
 * same name and all they do is pass forward the call to the repository. That is what we are verifying
 * here. If you have your own implementation of {@link ServiceBase} and override the standard behaviour
 * then you must also override the method that tests that behaviour in your test (should you extend this class).
 * <p>
 * Also, if you implement this class then you MUST supply a @{@link org.mockito.Mock} of the
 * {@link BaseRepository} implementation and then inject that mock into you {@link ServiceBase} implementation.
 *
 * @param <ID> Any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any Repository class extending {@link BaseRepository}
 * @param <S>  Any Service class extending {@link ServiceBase}
 * @author johan
 */
public abstract class ServiceBaseTest<ID, T extends Identifiable<ID>,
        R extends BaseRepository<ID, T>, S extends ServiceBase<ID, T, R>> implements TransactionalTest<T> {

    /**
     * Get the {@link ServiceBase} implementation supplied in subclass.
     * The implementation supplied must be given a mocked version of its real {@link BaseRepository}
     * implementation. You can achieve this either in a @{@link org.junit.Before} method giving your mocked repo
     * to this service or by marking the field given in the subclass with @{@link org.mockito.InjectMocks}.
     *
     * @return the service implementation
     */
    public abstract S getService();

    /**
     * Get the mocked implementation of your {@link BaseRepository} implementation.
     * You achieve this either by a @{@link org.junit.Before} method using {@link org.mockito.Mockito#mock(Class)} passing in
     * the repo or creating a field annotated with @{@link Mock}
     *
     * @return a mocked implementation of {@link BaseRepository}
     */
    public abstract R getMockRepository();

    /**
     * Return a value matching the type your entity of type T uses as ID.
     * Example: Entity Person uses Long as ID, then return 1L, if it uses String return "Person" etc.
     *
     * @return a value matching the ID type of your entity.
     */
    public abstract ID getId();

    /**
     * Test {@link ServiceBase#save(Identifiable)}
     */
    @Test
    public void testSave() {
        T t = getEntity1();
        when(getMockRepository().save(t)).thenReturn(t);
        T saved = getService().save(t);
        assertEquals("saved equals t", saved, t);
        verify(getMockRepository()).save(t);
    }

    /**
     * Test {@link ServiceBase#saveAll(Iterable)}
     */
    @Test
    public void testSaveAll() {
        Iterable<T> entities = getEntities();
        when(getMockRepository().saveAll(entities)).thenReturn(entities);
        Iterable<T> saved = getService().saveAll(entities);
        assertEquals("saved equals entities", saved, entities);
        verify(getMockRepository()).saveAll(entities);
    }

    /**
     * Test {@link ServiceBase#findById(Object)}
     */
    @Test
    public void testFindById() {
        ID id = getId();
        Optional<T> t = Optional.of(getEntity1());
        when(getMockRepository().findById(id)).thenReturn(t);
        Optional<T> byId = getService().findById(id);
        assertNotNull("byId exists", byId);
        verify(getMockRepository()).findById(id);
    }

    /**
     * Test {@link ServiceBaseTest#testGetEntityById()} and expect an exception.
     */
    @Test(expected = IllegalStateException.class)
    public void test_getEntity_ExpectException() {
        ID id = getId();
        when(getMockRepository().findById(id)).thenThrow(IllegalStateException.class);
        T t = getService().getEntityById(id);
        verify(getMockRepository()).findById(id);
    }

    /**
     * Test {@link ServiceBase#getEntityById(Object)}
     */
    @Test
    public void testGetEntityById() {
        ID id = getId();
        T t = getEntity1();
        Optional<T> ot = Optional.of(t);
        when(getMockRepository().findById(id)).thenReturn(ot);
        T byId = getService().getEntityById(id);
        assertNotNull("byId != null", byId);
        verify(getMockRepository()).findById(id);
    }

    /**
     * Test {@link ServiceBase#existsById(Object)}
     */
    @Test
    public void testExistsById() {
        ID id = getId();
        when(getMockRepository().existsById(id)).thenReturn(true);
        boolean existsById = getService().existsById(id);
        assertTrue("Exists by id=true", existsById);
        verify(getMockRepository()).existsById(id);
    }

    /**
     * Test {@link ServiceBase#findAll()}
     */
    @Test
    public void testFindAll() {
        Iterable<T> ts = getEntities();
        when(getMockRepository().findAll()).thenReturn(ts);
        Iterable<T> all = getService().findAll();
        assertEquals("all=ts", ts, all);
        verify(getMockRepository()).findAll();
    }

    /**
     * Test {@link ServiceBase#findAllById(Iterable)}
     */
    @Test
    public void testFindAllById() {
        Iterable<T> result = getEntities().subList(0, 1);
        Iterable<ID> ids = Arrays.asList(getId());
        when(getMockRepository().findAllById(ids)).thenReturn(result);
        Iterable<T> allById = getService().findAllById(ids);
        assertEquals("Size=1", 1, new ArrayList<T>((Collection<? extends T>) allById).size());
        verify(getMockRepository()).findAllById(ids);
    }

    /**
     * Test {@link ServiceBase#count()}
     */
    @Test
    public void testCount() {
        when(getMockRepository().count()).thenReturn(2L);
        long count = getService().count();
        assertEquals("2 entities in db", 2L, count);
        verify(getMockRepository()).count();
    }

    /**
     * Test {@link ServiceBase#deleteById(Object)}
     */
    @Test
    public void testDeleteById() {
        ID id = getId();
        doNothing().when(getMockRepository()).deleteById(id);
        getService().deleteById(id);
        verify(getMockRepository()).deleteById(id);
    }

    /**
     * Test {@link ServiceBase#delete(Identifiable)}
     */
    @Test
    public void testDelete() {
        T t = getEntity1();
        doNothing().when(getMockRepository()).delete(t);
        getService().delete(t);
        verify(getMockRepository()).delete(t);
    }

    /**
     * Test {@link ServiceBase#deleteAll(Iterable)}
     */
    @Test
    public void testDeleteAll_iterableAsArgument() {
        Collection<T> ts = getEntities();
        doNothing().when(getMockRepository()).deleteAll(ts);
        getService().deleteAll(ts);
        verify(getMockRepository()).deleteAll(ts);
    }

    /**
     * Test {@link ServiceBase#deleteAll()}
     */
    @Test
    public void deleteAll_noArguments() {
        doNothing().when(getMockRepository()).deleteAll();
        getService().deleteAll();
        verify(getMockRepository()).deleteAll();
    }
}