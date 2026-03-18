package com.autowhouse.loginservice.service.application;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.PasswordDTO;

import java.util.Optional;


/**
 * @author : Asnit Bakhati
 * @Date : 10th Feb,2026
 */
public interface UserService {

    Optional<ApplicationUser> findByEmail(String email);
    ApplicationUser save(DetailsCodeDTO detailsCodeDTO);
    boolean resetPassword(PasswordDTO passwordDTO);
}
