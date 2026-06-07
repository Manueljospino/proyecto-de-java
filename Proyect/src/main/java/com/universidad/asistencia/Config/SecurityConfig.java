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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/hash/**").permitAll()
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")
                        .requestMatchers("/api/auth/reset-password").hasRole("ADMIN")
                        .requestMatchers("/api/auth/delete/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/find/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/users").hasRole("ADMIN")
                        .requestMatchers("/api/auth/change-password").authenticated()

                        // Materias
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/subjects").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/subjects/with-teachers").hasRole("ADMIN")
                        .requestMatchers("/api/subjects/**").hasRole("ADMIN")

                        // Asignación docente-materia
                        .requestMatchers("/api/teacher-subjects/assign").hasRole("ADMIN")
                        .requestMatchers("/api/teacher-subjects/unassign").hasRole("ADMIN")
                        .requestMatchers("/api/teacher-subjects/by-teacher/**").hasRole("ADMIN")
                        .requestMatchers("/api/teacher-subjects/by-subject/**").hasRole("ADMIN")
                        .requestMatchers("/api/teacher-subjects/my-subjects").hasRole("DOCENTE")

                        // Inscripciones
                        .requestMatchers("/api/inscription/inscribe").hasRole("ADMIN")
                        .requestMatchers("/api/inscription/mine").hasRole("ESTUDIANTE")

                        // Asistencia - Docente
                        .requestMatchers("/api/assistance/open").hasRole("DOCENTE")
                        .requestMatchers("/api/assistance/close/**").hasRole("DOCENTE")
                        .requestMatchers("/api/assistance/my-sessions").hasRole("DOCENTE")
                        .requestMatchers("/api/assistance/my-subjects").hasRole("DOCENTE")
                        .requestMatchers("/api/assistance/session/**").hasRole("DOCENTE")

                        // Asistencia - Estudiante
                        .requestMatchers("/api/assistance/active-sessions").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/assistance/register").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/assistance/my-history").hasRole("ESTUDIANTE")

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