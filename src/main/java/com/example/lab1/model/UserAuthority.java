package com.example.lab1.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {
    VIEW_JOKES,
    ADD_JOKES,
    EDIT_JOKES,
    DELETE_JOKES,
    MANAGE_USERS,
    FULL;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
