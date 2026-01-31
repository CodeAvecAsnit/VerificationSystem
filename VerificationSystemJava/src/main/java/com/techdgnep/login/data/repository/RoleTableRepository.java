package com.techdgnep.login.data.repository;

import com.techdgnep.login.data.database.RoleTable;
import com.techdgnep.login.data.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTableRepository extends JpaRepository<RoleTable,Integer> {
    RoleTable findByRole(Role role);
}
