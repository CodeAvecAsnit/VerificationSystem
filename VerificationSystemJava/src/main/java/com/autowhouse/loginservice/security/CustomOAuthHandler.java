package com.autowhouse.loginservice.security;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import com.autowhouse.loginservice.data.database.RoleTable;
import com.autowhouse.loginservice.data.enumeration.Role;
import com.autowhouse.loginservice.data.repository.AppUserRepository;
import com.autowhouse.loginservice.data.repository.RoleTableRepository;
import com.autowhouse.loginservice.util.JwtUtils;
import com.autowhouse.loginservice.util.SecurePasswordGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
@Component
@Slf4j
public class CustomOAuthHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final CustomUserServiceImpl customUserServiceImpl;
    private final RoleTableRepository roleTableRepository;
    private final AppUserRepository appUserRepository;


    public CustomOAuthHandler(JwtUtils jwtUtils, CustomUserServiceImpl customUserServiceImpl, RoleTableRepository roleTableRepository, AppUserRepository appUserRepository) {
        this.jwtUtils = jwtUtils;
        this.customUserServiceImpl = customUserServiceImpl;
        this.roleTableRepository = roleTableRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = user.getAttributes();

        String email  = (String) attributes.get("email");
        String token = "";
        try {
            CustomUser customUser = customUserServiceImpl.loadUserByUsername(email);
            token = jwtUtils.generateJwtTokens(customUser);
            setJwtCookie(response,token);
            log.info("{} has logged in with google.",email);
        }catch (UsernameNotFoundException uNFEx){
            RoleTable roleTable = roleTableRepository.findByRole(Role.USER);
            ApplicationUser newUser = new ApplicationUser();
            newUser.createRoleTable();
            newUser.addRoleTable(roleTable);
            newUser.setEmail(email);
            newUser.setPassword(SecurePasswordGenerator.generate(14));
            ApplicationUser savedUser = appUserRepository.save(newUser);

            token = jwtUtils.generateAccessTokenFromUser(savedUser);
            setJwtCookie(response,token);
            log.info("{} has created account with OAuth Google",email);
        }catch (Exception ex){
            log.error("OAuth2 login error: {}", ex.getMessage());
            response.sendRedirect("/index.html?error");
        }
        response.sendRedirect("/index.html?code="+token);
    }

    private void setJwtCookie(HttpServletResponse response, String jwt) {
        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
