package com.github.johanfredin.exstensionsdemo.repository;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;

public class AddressRepositoryIntegrationTest extends BaseRepositoryTest<Long, Address, AddressRepository> {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressRepository getRepository() {
        return addressRepository;
    }

    @Override
    public Address getEntity1() {
        return TestFixture.getValidAddressWithoutReference();
    }

    @Override
    public Address getEntity2() {
        return TestFixture.getValidAddressWithoutReference("Street 2", "Zip code 2", "City 2", "Country 2");
    }
}
