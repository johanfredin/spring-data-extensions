package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.util.CollectionHelper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Extension of {@link BaseRepository} that also holds a couple of extra helpful methods
 * for persisting and deleting entities by passing in and returning Lists, Sets and arbitrary amount of entities
 * instead of the rather primitive {@link CrudRepository} where we can "only" work with {@link Iterable}.
 * Check out these methods for more info:<br/>
 * {@link #saveAll(Set)}<br/>
 * {@link #saveAll(List)}<br/>
 * {@link #saveAll(Identifiable[])}<br/>
 * {@link #saveAllAsSet(Identifiable[])}<br/>
 * <p>
 * Also extends {@link CollectionHelper} so that those methods become available here as well.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @author johan
 */
@NoRepositoryBean
public interface ExtendedBaseRepository<ID, T extends Identifiable<ID>> extends BaseRepository<ID, T>, CollectionHelper {

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in an arbitrary
     * amount of entities.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(T... entities) {
        return saveAll(mListOf(entities));
    }

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in an arbitrary
     * amount of entities.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAllAsSet(T... entities) {
        return saveAll(mSetOf(entities));
    }

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in a list of entities to persist.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(List<T> entities) {
        entities.forEach(this::save);
        return entities;
    }

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in a set of entities to persist.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAll(Set<T> entities) {
        entities.forEach(this::save);
        return entities;
    }

    /**
     * Same as {@link CrudRepository#deleteAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in an arbitrary
     * amount of entities.
     *
     * @param entities the entities to delete.
     */
    default void deleteAll(T... entities) {
        deleteAll(List.of(entities));
    }

    /**
     * Same as {@link CrudRepository#deleteAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in a collection.
     *
     * @param entities the entities to delete.
     */
    default void deleteAll(Collection<T> entities) {
        entities.forEach(this::delete);
    }


}
