package com.example.lab1.repository;

import com.example.lab1.model.UserAuthority;
import com.example.lab1.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    void deleteByUserIdAndUserAuthority(Long userId, UserAuthority authority);
}
