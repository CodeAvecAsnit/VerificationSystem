package com.autowhouse.loginservice.service.application.impl;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.dto.PasswordDTO;
import com.autowhouse.loginservice.data.repository.AppUserRepository;
import com.autowhouse.loginservice.data.repository.RoleTableRepository;
import com.autowhouse.loginservice.exception.custom.SamePasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author : Asnit Bakhati
 * @Date : 14th Feb, 2026
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private RoleTableRepository roleTableRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Fail: New password is same as old")
    void resetPassword_ShouldThrowException_WhenPasswordsAreSame() {
        PasswordDTO dto = new PasswordDTO("user@test.com", "Pass123", "Pass123");
        assertThrows(SamePasswordException.class, () -> userService.resetPassword(dto));
        verifyNoInteractions(appUserRepository);
    }

    @Test
    @DisplayName("Fail: User not found")
    void resetPassword_ShouldThrowException_WhenUserNotFound() {
        PasswordDTO dto = new PasswordDTO("unknown@test.com", "oldPass", "newPass");
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.resetPassword(dto));
        verify(appUserRepository).findByEmail(dto.getEmail());
    }

    @Test
    @DisplayName("Fail: Old password does not match")
    void resetPassword_ShouldThrowException_WhenOldPasswordIncorrect() {
        PasswordDTO dto = new PasswordDTO("user@test.com", "wrongOldPass", "newPass");
        ApplicationUser user = new ApplicationUser();
        user.setPassword("encodedOldPass");
        when(appUserRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getOldPassword(), user.getPassword())).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> userService.resetPassword(dto));
    }

    @Test
    @DisplayName("Success: Old password matches and updated")
    void resetPassword_ShouldReturnTrue_WhenSuccessful() {
        PasswordDTO dto = new PasswordDTO("user@test.com", "correctOldPass", "brandNewPass");
        ApplicationUser user = new ApplicationUser();
        user.setPassword("encodedOldPass");

        when(appUserRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.getNewPassword())).thenReturn("encodedNewPass");

        boolean result = userService.resetPassword(dto);
        assertTrue(result);
        assertEquals("encodedNewPass", user.getPassword());
        verify(appUserRepository).save(user);
    }
}