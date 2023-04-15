package com.example.blps_lab1.repository.basic;

import com.example.blps_lab1.model.basic.ERole;
import com.example.blps_lab1.model.basic.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
