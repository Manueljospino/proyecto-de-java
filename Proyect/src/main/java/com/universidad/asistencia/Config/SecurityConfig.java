package com.universidad.asistencia.Config;

import com.universidad.asistencia.Utils.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ← AÑADIR ESTO
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/reset-password").hasRole("ADMIN")
                        .requestMatchers("/api/assistance/session/**").hasRole("DOCENTE")
                        .requestMatchers("/api/inscription/inscribe").hasRole("ADMIN")
                        .requestMatchers("/api/inscription/mine").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/assistance/active-sessions").hasRole("ESTUDIANTE")
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}