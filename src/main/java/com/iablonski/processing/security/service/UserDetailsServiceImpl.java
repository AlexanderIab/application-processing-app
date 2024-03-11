package com.iablonski.processing.security.service;

import com.iablonski.processing.domain.User;
import com.iablonski.processing.exception.UserNotFoundException;
import com.iablonski.processing.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
        return UserDetailsImpl.build(user);
    }

    public UserDetails loadUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return UserDetailsImpl.build(user);
    }
}