package com.github.johanfredin.springdataextensions.domain;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public interface CrossReferenceHolder {

    /**
     * Our entities have {@link OneToOne}, {@link ManyToOne}, {@link OneToMany} relations
     * this method should make sure that relations are set both ways.<br>
     * <b>Example</b>:<br>
     * class Entity1 has an instance of Class Entity2 and they have a {@link OneToOne} relation<br>
     * Entity1 must then both set the Entity2 instance the usual way<br>
     * and assign itself to Entity2 so that:<br><br>
     * <code>public void setCrossRelations() {<br>
     * this.getEntity2().setEntity1(this);<br>
     * }<br>
     * Many-To-Many relations will most likely not be possible with this (not in a good way at least)
     */
    void setCrossRelations();

}
