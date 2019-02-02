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
package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests all the methods in {@link ExtendedBaseRepository}
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any class extending {@link BaseRepository}
 * @author johan
 */
public abstract class ExtendedBaseRepositoryTest<ID, T extends Identifiable<ID>, R extends ExtendedBaseRepository<ID, T>>
        extends BaseRepositoryIntegrationTest<ID, T, R> {

    /** Test {@link ExtendedBaseRepository#saveAll(Identifiable[])} */
    @Test
    public void testSaveAll_ArbitraryArguments_ReturnsList() {
        List<T> listWithArbitraryArguments = getRepository().saveAll(getEntity1(), getEntity2());
        assertEquals("List", true, listWithArbitraryArguments instanceof List);
        assertPersisted(listWithArbitraryArguments);
    }

    /** Test {@link ExtendedBaseRepository#saveAllAsSet(Identifiable[])}  */
    @Test
    public void testSaveAllAsSet() {
        Set<T> setWithArbitraryArguments = getRepository().saveAllAsSet(getEntity1(), getEntity2());
        assertEquals("Set", true, setWithArbitraryArguments instanceof Set);
        assertPersisted(setWithArbitraryArguments);
    }

    /** Test {@link ExtendedBaseRepository#saveAll(List)} */
    @Test
    public void testSaveAll_ReturnsList() {
        List<T> list = getRepository().saveAll(Arrays.asList(getEntity1(), getEntity2()));
        assertEquals("List", true, list instanceof List);
        assertPersisted(list);
    }

    /** Test {@link ExtendedBaseRepository#saveAll(Set)} */
    @Test
    public void testSaveAll_ReturnsSet() {
        Set<T> set = getRepository().saveAll(mSetOf(getEntity1(), getEntity2()));
        assertEquals("Set", true, set instanceof Set);
        assertPersisted(set);
    }

    /** Test {@link ExtendedBaseRepository#deleteAll(Identifiable[])}  */
    @Test
    public void testDeleteAll_ArbitraryAmount() {
        Set<T> set = getRepository().saveAllAsSet(getEntity1(), getEntity2());
        getRepository().deleteAll(set);
        assertDeleted(set);
    }

}