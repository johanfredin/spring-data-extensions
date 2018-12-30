package com.github.johanfredin.exstensionsdemo;

import com.github.johanfredin.springdataextensions.domain.Address;
import com.github.johanfredin.springdataextensions.domain.Person;
import org.junit.Ignore;

@Ignore
public class TestFixture {

    public static Person getValidPersonWithoutReference() {
        return getValidPersonWithoutReference("Donald");
    }

    public static Person getValidPersonWithReference(boolean updateReferences) {
        return getValidPersonWithReference("Donald", getValidAddressWithoutReference(), updateReferences);
    }

    public static Person getValidPersonWithoutReference(String name) {
        Person p = new Person();
        p.setNewDate();
        p.setName("Donald");
        return p;
    }

    public static Person getValidPersonWithReference(String name, Address address, boolean updateCrossReferences) {
        Person p = new Person();
        p.setNewDate();
        p.setName("Donald");
        p.setAddress(address);
        if(updateCrossReferences) {
            p.setCrossRelations();
        }
        return p;
    }

    public static Address getValidAddressWithoutReference() {
        return getValidAddressWithoutReference("Street 1", "Zip code 1", "City 1", "Country 1");
    }

    public static Address getValidAddressWithoutReference(String street, String zipCode, String city, String country) {
        Address a = new Address();
        a.setStreet(street);
        a.setZipCode(zipCode);
        a.setCity(city);
        a.setCountry(country);
        return a;
    }

    public static Address getValidAddressWithReference(boolean updateCrossReference) {
        return getValidAddressWithReference("Street 1", "Zip code 1", "City 1", "Country 1", getValidPersonWithoutReference(), updateCrossReference);
    }

    public static Address getValidAddressWithReference(String street, String zipCode, String city, String country, Person person, boolean updateCrossReference) {
        Address a = new Address();
        a.setStreet(street);
        a.setZipCode(zipCode);
        a.setCity(city);
        a.setCountry(country);
        a.setPerson(person);
        if(updateCrossReference) {
            a.setCrossRelations();
        }
        return a;
    }


}
