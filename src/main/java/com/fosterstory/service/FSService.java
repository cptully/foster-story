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
    PhoneRepository phoneRepository;

    @Autowired
    UserRepository userRepository;

}
