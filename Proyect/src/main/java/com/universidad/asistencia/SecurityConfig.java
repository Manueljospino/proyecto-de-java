package com.universidad.asistencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/auth/reset-password").hasRole("ADMIN")
                                .requestMatchers("/api/auth/login").permitAll()
                                .requestMatchers("/api/auth/hash/**").permitAll()
                                .requestMatchers("/api/auth/register").hasRole("ADMIN")
                                .requestMatchers("/api/assistance/open").hasRole("DOCENTE")
                                .requestMatchers("/api/assistance/close/**").hasRole("DOCENTE")
                                .requestMatchers("/api/assistance/register").hasRole("ESTUDIANTE")
                                .requestMatchers("/api/assistance/mine").hasRole("ESTUDIANTE")
                                .requestMatchers("/api/auth/change-password").authenticated()
                                .anyRequest().authenticated()

                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}