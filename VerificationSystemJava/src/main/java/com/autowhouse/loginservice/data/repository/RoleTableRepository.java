package com.autowhouse.loginservice.data.repository;

import com.autowhouse.loginservice.data.database.RoleTable;
import com.autowhouse.loginservice.data.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTableRepository extends JpaRepository<RoleTable,Integer> {
    RoleTable findByRole(Role role);
}
