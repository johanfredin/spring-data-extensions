package com.github.johanfredin.exstensionsdemo.service.impl;

import com.github.johanfredin.springdataextensions.repository.AddressRepository;
import com.github.johanfredin.springdataextensions.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("addressService")
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressRepository getRepository() {
        return this.addressRepository;
    }
}
