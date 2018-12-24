package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.util.comparator.DateComparator;

import java.util.List;

public abstract class Bean<E extends AbstractEntity> {

    // All beans will need a member id since no controllers (after login) holds it as a path variable
    private long memberId;

    private DateComparator dateComparator;

    public Bean() {
    }

    public Bean(long memberId) {
        setMemberId(memberId);
    }

    public Bean(E entity) {
        this(entity, 0L, null);
    }

    public Bean(E entity, long memberId) {
        this(entity, memberId, null);
    }

    public Bean(E entity, long memberId, DateComparator dateComparator) {
        setDateComparator(dateComparator);
        setEntity(entity);
        setMemberId(memberId);
    }

    public abstract E getEntity();

    public abstract void setEntity(E entity);

    public DateComparator getDateComparator() {
        return dateComparator;
    }

    public void setDateComparator(DateComparator dateComparator) {
        this.dateComparator = dateComparator;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    /**
     * Helper method that prevents unnecessary sorting
     *
     * @param entities
     * @return <code>true</code> if entities passed in are not <code>null</code> and size > 1
     */
    public boolean isQualifiedForSort(List<? extends AbstractEntity> entities) {
        return entities != null && entities.size() > 1;
    }

    /**
     * Sorts the passed in entity {@link List}
     * Calls {@link #isQualifiedForSort(List)} to verify if list can be sorted
     *
     * @param entities
     * @return <code>true</code> if list was sorted
     */
    public boolean sort(List<? extends AbstractEntity> entities) {
        if (isQualifiedForSort(entities)) {
            entities.sort(getDateComparator());
            return true;
        }
        return false;
    }

}
