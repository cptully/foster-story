package com.fosterstory.service;

import com.fosterstory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chris on 10/3/16.
 */
public class FSService {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AddressTypeRepository addressTypeRepository;

    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    PhoneTypeRepository phoneTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebsiteRepository websiteRepository;

}
