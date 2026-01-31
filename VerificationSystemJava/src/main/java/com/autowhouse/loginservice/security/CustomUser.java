package com.autowhouse.loginservice.security;

import com.autowhouse.loginservice.data.database.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public static CustomUser build(ApplicationUser user){
        List<SimpleGrantedAuthority> authority = user.getUserRoles().
                stream().map(role->new SimpleGrantedAuthority(role.getRole().toString())).collect(Collectors.toUnmodifiableList());
        return new CustomUser(user.getRegisterId(),user.getEmail(),user.getPassword(),authority);
    }
}
