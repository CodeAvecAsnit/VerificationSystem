package com.techdgnep.login.service.application;

import com.techdgnep.login.data.database.ApplicationUser;
import com.techdgnep.login.data.dto.DetailsCodeDTO;

import java.util.Optional;

public interface UserService {

    Optional<ApplicationUser> findByEmail(String email);
    ApplicationUser save(DetailsCodeDTO detailsCodeDTO);
}
