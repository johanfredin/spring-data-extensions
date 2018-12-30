package com.github.johanfredin.exstensionsdemo.service;

import com.github.johanfredin.springdataextensions.domain.Address;
import com.github.johanfredin.springdataextensions.repository.AddressRepository;

public interface AddressService extends ServiceBase<Long, Address, AddressRepository> {
}
