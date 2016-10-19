package com.fosterstory.service;

import com.fosterstory.entity.User;
import com.fosterstory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by win808mac on 10/13/16.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public User findById(Integer userId) {
        return userRepository.findById(userId);
    }
}
