package com.example.lab1.config;

import com.example.lab1.model.UserAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll() // Явно разрешаем POST
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jokes/**").hasAuthority(UserAuthority.VIEW_JOKES.name())
                        .requestMatchers(HttpMethod.POST, "/api/jokes/**").hasAuthority(UserAuthority.ADD_JOKES.name())
                        .requestMatchers(HttpMethod.PUT, "/api/jokes/**").hasAuthority(UserAuthority.EDIT_JOKES.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/jokes/**").hasAuthority(UserAuthority.DELETE_JOKES.name())
                        .requestMatchers("/api/users/**").hasAuthority(UserAuthority.MANAGE_USERS.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable); // CSRF отключён

        return http.build();
    }
}