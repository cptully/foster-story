package com.fosterstory.repository;

import com.fosterstory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by chris on 10/3/16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);
}
