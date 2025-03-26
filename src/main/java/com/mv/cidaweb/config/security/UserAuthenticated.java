package com.mv.cidaweb.config.security;

import com.mv.cidaweb.model.beans.Pessoa;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserAuthenticated implements UserDetails {
    private final Pessoa pessoa;

    public UserAuthenticated(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String getUsername() {
        return pessoa.getLogin();
    }

    @Override
    public String getPassword() {
        return pessoa.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "read");
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
}
