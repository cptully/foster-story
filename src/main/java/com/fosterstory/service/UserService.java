package com.fosterstory.service;

import com.fosterstory.entity.User;
import com.fosterstory.exceptions.UserNotFoundException;
import com.fosterstory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Created by win808mac on 10/13/16.
 */
@Service
public class UserService {
  
  final UserRepository userRepository;
  
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  
  public User findById(Integer userId) throws UserNotFoundException
  {
    Optional<User> optionalUser = userRepository.findById(userId);
    return optionalUser.orElse(null);
  }
}
