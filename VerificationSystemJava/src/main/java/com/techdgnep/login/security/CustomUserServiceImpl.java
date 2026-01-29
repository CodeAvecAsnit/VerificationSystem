package com.techdgnep.login.security;

import com.techdgnep.login.data.database.ApplicationUser;
import com.techdgnep.login.data.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public CustomUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = appUserRepository.findByEmail(username).orElseThrow(
                ()->new UsernameNotFoundException("User was not found"));
        return CustomUser.build(user);
    }
}
