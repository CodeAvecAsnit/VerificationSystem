package com.techdgnep.login.Service;

import com.techdgnep.login.DataModel.Database.FinalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<FinalUser,Long> {
}
