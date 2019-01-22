package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.TransactionalTest;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public abstract class ServiceBaseTest<ID, T extends Identifiable<ID>,
        R extends BaseRepository<ID, T>, S extends ServiceBase<ID, T, R>> implements TransactionalTest<T> {

    public abstract S getService();

    public abstract R getMockRepository();

    public abstract ID getId();

    @Test
    public void testSave() {
        T t = getEntity1();
        when(getMockRepository().save(t)).thenReturn(t);
        T saved = getService().save(t);
        assertEquals("saved equals t", saved, t);
        verify(getMockRepository()).save(t);
    }

    @Test
    public void testSaveAll() {
        Iterable<T> entities = getEntities();
        when(getMockRepository().saveAll(entities)).thenReturn(entities);
        Iterable<T> saved = getService().saveAll(entities);
        assertEquals("saved equals entities", saved, entities);
        verify(getMockRepository()).saveAll(entities);
    }

    @Test
    public void testFindById() {
        ID id = getId();
        Optional<T> t = Optional.of(getEntity1());
        when(getMockRepository().findById(id)).thenReturn(t);
        Optional<T> byId = getService().findById(id);
        assertNotNull("byId exists", byId);
        verify(getMockRepository()).findById(id);
    }

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

    @Test
    public void testExistsById() {
        ID id = getId();
        when(getMockRepository().existsById(id)).thenReturn(true);
        boolean existsById = getService().existsById(id);
        assertTrue("Exists by id=true", existsById);
        verify(getMockRepository()).existsById(id);
    }

    @Test
    public void testFindAll() {
        Iterable<T> ts = getEntities();
        when(getMockRepository().findAll()).thenReturn(ts);
        Iterable<T> all = getService().findAll();
        assertEquals("all=ts", ts, all);
        verify(getMockRepository()).findAll();
    }

    @Test
    public void testFindAllById() {
        Iterable<T> result = getEntities().subList(0, 1);
        Iterable<ID> ids = List.of(getId());
        when(getMockRepository().findAllById(ids)).thenReturn(result);
        Iterable<T> allById = getService().findAllById(ids);
        assertEquals("Size=1", 1, new ArrayList<T>((Collection<? extends T>) allById).size());
        verify(getMockRepository()).findAllById(ids);
    }

    @Test
    public void testCount() {
        when(getMockRepository().count()).thenReturn(2L);
        long count = getService().count();
        assertEquals("2 entities in db", 2L, count);
        verify(getMockRepository()).count();
    }

    @Test
    public void testDeleteById() {
        ID id = getId();
        doNothing().when(getMockRepository()).deleteById(id);
        getService().deleteById(id);
        verify(getMockRepository()).deleteById(id);
    }

    @Test
    public void testDelete() {
        T t = getEntity1();
        doNothing().when(getMockRepository()).delete(t);
        getService().delete(t);
        verify(getMockRepository()).delete(t);
    }

    @Test
    public void testDeleteAll_iterableAsArgument() {
        Iterable<T> ts = getEntities();
        doNothing().when(getMockRepository()).deleteAll(ts);
        getService().deleteAll(ts);
        verify(getMockRepository()).deleteAll(ts);
    }

    @Test
    public void deleteAll_noArguments() {
        doNothing().when(getMockRepository()).deleteAll();
        getService().deleteAll();
        verify(getMockRepository()).deleteAll();
    }
}