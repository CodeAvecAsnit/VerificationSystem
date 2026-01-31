package com.autowhouse.loginservice.service.application;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;

import java.util.Optional;

public interface UserService {

    Optional<ApplicationUser> findByEmail(String email);
    ApplicationUser save(DetailsCodeDTO detailsCodeDTO);
}
