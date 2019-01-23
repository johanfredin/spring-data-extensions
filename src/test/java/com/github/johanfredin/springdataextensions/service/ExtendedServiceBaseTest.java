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

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import com.github.johanfredin.springdataextensions.repository.ExtendedBaseRepository;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for all the methods in {@link ExtendedServiceBase}.
 * Just like {@link ServiceBaseTest} (which this class extends) we are using
 * {@link org.mockito.Mockito} for mocking the repository dependency.
 * The methods {@link ExtendedServiceBase#saveAll(Identifiable[])},
 * {@link ExtendedServiceBase#saveAllAsSet(Identifiable[])} and {@link ExtendedServiceBase#deleteAll(Identifiable[])}
 * are excluded since Mockito would not let me test those properly at this level. Or maybe I'm just
 * doing it wrong :). You will not be forced to implement tests for this but it is of course always recommended.
 * Check out the <a href='https://github.com/johanfredin/spring-data-extensions-demo'>demo project</a> for examples of this.
 *
 * <p>
 * Extensions of this class must provide a valid {@link ExtendedServiceBase} implementation
 * as well as a mocked {@link ExtendedBaseRepository} implementation to provide that service with.
 * See the docs on {@link ServiceBaseTest} for more information.
 *
 * @param <ID> Any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any Repository class extending {@link BaseRepository}
 * @param <S>  Any Service class extending {@link ServiceBase}
 * @author johan
 */
public abstract class ExtendedServiceBaseTest<ID, T extends Identifiable<ID>,
        R extends ExtendedBaseRepository<ID, T>, S extends ExtendedServiceBase<ID, T, R>> extends ServiceBaseTest<ID, T, R, S> {

    /** Test {@link ExtendedServiceBase#saveAll(List)} */
    @Test
    public void saveAll_listArg() {
        List<T> list = getEntities();
        when(getMockRepository().saveAll(list)).thenReturn(list);
        List<T> saved = getService().saveAll(list);
        assertEquals("saved=list", saved, list);
        verify(getMockRepository()).saveAll(list);
    }

    /** Test {@link ExtendedServiceBase#saveAll(Set)} */
    @Test
    public void saveAll_setArg() {
        Set<T> set = getEntitiesAsSet();
        when(getMockRepository().saveAll(set)).thenReturn(set);
        Set<T> saved = getService().saveAll(set);
        assertEquals("saved=set", saved, set);
        verify(getMockRepository()).saveAll(set);
    }



}