package com.github.johanfredin.springdataextensions.api.doman;

import com.github.johanfredin.springdataextensions.domain.Copyable;
import com.github.johanfredin.springdataextensions.domain.CrossReferenceHolder;
import com.github.johanfredin.springdataextensions.domain.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Person implements Identifiable<Long>, Copyable<Person> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Pet> pets;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Person(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person(long id, String name, List<Pet> pets) {
        this.name = name;
        this.pets = pets;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public boolean isPersistedEntity() {
        return id > 0;
    }

    @Override
    public void copyFrom(@NotNull Person populatedEntity) {
        setId(populatedEntity.getId());
        setName(populatedEntity.getName());
        setPets(populatedEntity.getPets());
    }

    @Override
    public Person createCopy() {
        return new Person(getId(), getName(), getPets());
    }

}
