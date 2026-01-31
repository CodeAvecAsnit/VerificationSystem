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
        List<RoleTable> roleTableList = new ArrayList<>();
        RoleTable roleTable = roleTableRepository.findByRole(Role.USER);
        roleTableList.add(roleTable);
        ApplicationUser user = detailsCodeDTO.build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRoles(roleTableList);
        return appUserRepository.save(user);
    }
}
