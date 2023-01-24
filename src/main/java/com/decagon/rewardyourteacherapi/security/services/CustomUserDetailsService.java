package com.decagon.rewardyourteacherapi.security.services;

import com.decagon.rewardyourteacherapi.entity.User;

import com.decagon.rewardyourteacherapi.enums.Roles;
import com.decagon.rewardyourteacherapi.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private Roles role;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository
                .findUserByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("com.decagon.rewardyourteacherapi.entity.User not found with username or email:" + email));

         role = user.getRole();
        return   new org.springframework.security.core.userdetails.User( user.getEmail(), user.getPassword(), mapRoleToAuthority());
    }

    private Collection <? extends GrantedAuthority> mapRoleToAuthority (){

        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }
}

