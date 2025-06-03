package com.example.lab1.service;

import com.example.lab1.model.UserAuthority;

public interface UserService {
    void registration(String username, String password);
    void addAuthority(Long userId, UserAuthority authority);
    void removeAuthority(Long userId, UserAuthority authority);
}
