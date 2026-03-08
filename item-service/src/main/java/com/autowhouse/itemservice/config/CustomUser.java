package com.autowhouse.itemservice.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUser implements UserDetails {

    private long userId;
    private String userName;
    private String password;
    private Collection<?extends GrantedAuthority> authorities;

    public long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

}
