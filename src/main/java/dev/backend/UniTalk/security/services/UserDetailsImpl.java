package dev.backend.UniTalk.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.backend.UniTalk.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Collection<? extends GrantedAuthority> authorities;

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
        authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(getId(), user.getId());
    }
}