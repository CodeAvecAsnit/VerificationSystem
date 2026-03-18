package com.autowhouse.loginservice.data.repository;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<ApplicationUser,Long> {
    Optional<ApplicationUser> findByEmail(String email);
}
