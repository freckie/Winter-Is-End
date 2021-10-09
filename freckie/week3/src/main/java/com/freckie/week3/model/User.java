package com.freckie.week3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name="users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id" })
    })
public class User implements UserDetails {
    @Id
    @Getter
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="encrypted_password")
    private String encryptedPassword;

    @Getter
    @Column(name="name")
    private String name;

    @Getter
    @Column(name="email", unique=true)
    private String email;

    @Getter
    @Column(name="role")
    private int role;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        if (role == 1) roles.add(new SimpleGrantedAuthority("ADMIN"));
        return roles;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
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

    public void setPassword(String password) {
        encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Boolean comparePassword(String password) {
        return BCrypt.checkpw(encryptedPassword, password);
    }
}
