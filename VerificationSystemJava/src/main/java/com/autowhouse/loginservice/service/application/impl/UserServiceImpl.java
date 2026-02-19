package com.autowhouse.loginservice.service.application.impl;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.database.RoleTable;
import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.PasswordDTO;
import com.autowhouse.loginservice.data.enumeration.Role;
import com.autowhouse.loginservice.data.repository.AppUserRepository;
import com.autowhouse.loginservice.data.repository.RoleTableRepository;
import com.autowhouse.loginservice.exception.custom.SamePasswordException;
import com.autowhouse.loginservice.service.application.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author : Asnit Bakhati
 * @Date : 10th Feb,2026
 */
@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final RoleTableRepository roleTableRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(AppUserRepository appUserRepository, RoleTableRepository roleTableRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleTableRepository = roleTableRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public ApplicationUser save(DetailsCodeDTO detailsCodeDTO) {
        ApplicationUser user = detailsCodeDTO.build();
        user.createRoleTable();
        RoleTable roleTable = roleTableRepository.findByRole(Role.USER);
        user.addRoleTable(roleTable);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public boolean resetPassword(PasswordDTO passwordDTO) {
        if(passwordDTO.areSamePasswords()) throw
            new SamePasswordException("New password cannot be same as the Old Password");
        ApplicationUser user = findUserByEmail(passwordDTO.getEmail());
        if(passwordEncoder.matches(passwordDTO.getOldPassword(),user.getPassword())){
            String newPassword = passwordDTO.getNewPassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            appUserRepository.save(user);
            return true;
        }else throw new BadCredentialsException("The passwords do not match.");
    }

    private ApplicationUser findUserByEmail(String email){
        return appUserRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("User with this email not found"));
    }
}
