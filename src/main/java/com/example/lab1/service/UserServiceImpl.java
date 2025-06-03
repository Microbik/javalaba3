package com.example.lab1.service;

import com.example.lab1.model.User;
import com.example.lab1.model.UserAuthority;
import com.example.lab1.model.UserRole;
import com.example.lab1.repository.UserRepository;
import com.example.lab1.repository.UserRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void registration(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setExpired(false);
        user.setLocked(false);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        // По умолчанию даем роль USER с правом VIEW_JOKES
        UserRole userRole = new UserRole();
        userRole.setUserAuthority(UserAuthority.VIEW_JOKES);
        userRole.setUser(savedUser);
        userRoleRepository.save(userRole);
    }

    public void addAuthority(Long userId, UserAuthority authority) {
        User user = userRepository.findById(userId).orElseThrow();

        UserRole userRole = new UserRole();
        userRole.setUserAuthority(authority);
        userRole.setUser(user);
        userRoleRepository.save(userRole);
    }

    public void removeAuthority(Long userId, UserAuthority authority) {
        userRoleRepository.deleteByUserIdAndUserAuthority(userId, authority);
    }
}