package com.example.demo.config;

import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Enable CORS
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permit all static resources and the login page
                        .requestMatchers(
                            new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString()),
                            new AntPathRequestMatcher("/"),
                            new AntPathRequestMatcher("/*.html"),
                            new AntPathRequestMatcher("/css/**"),
                            new AntPathRequestMatcher("/js/**")
                        ).permitAll()
                        
                        // API endpoints for authenticated users
                        .requestMatchers("/api/user/me").authenticated()

                        // Admin-only API endpoints
                        .requestMatchers("/api/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/leaverequests").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/leaverequests/{id}/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/leaverequests/{id}/reject").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/payrolls/calculate").hasRole("ADMIN")

                        // Employee-only API endpoints
                        .requestMatchers("/api/timesheets/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/leaverequests").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/leaverequests/my").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/payrolls/my").hasRole("EMPLOYEE")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
