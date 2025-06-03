package com.example.lab1.controller;

import com.example.lab1.model.User;
import com.example.lab1.model.UserAuthority;
import com.example.lab1.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestParam String username,
                         @RequestParam String password) {
        userService.registration(username, password);
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @PostMapping("/{userId}/role")
    public void addRole(@PathVariable Long userId,
                        @RequestParam UserAuthority authority) {
        userService.addAuthority(userId, authority);
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @DeleteMapping("/{userId}/role")
    public void removeRole(@PathVariable Long userId,
                           @RequestParam UserAuthority authority) {
        userService.removeAuthority(userId, authority);
    }
}
