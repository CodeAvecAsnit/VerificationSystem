package com.autowhouse.loginservice.service.application.impl;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.database.RoleTable;
import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.enumeration.Role;
import com.autowhouse.loginservice.data.repository.AppUserRepository;
import com.autowhouse.loginservice.data.repository.RoleTableRepository;
import com.autowhouse.loginservice.service.application.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
}
