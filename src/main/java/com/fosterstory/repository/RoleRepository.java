package com.fosterstory.repository;

import com.fosterstory.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by win808mac on 10/6/16.
 */
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findByNameEquals(String name);
}
