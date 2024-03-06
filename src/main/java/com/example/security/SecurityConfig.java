package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // The New Lambda DSL Syntax

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/attendees").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/attendees/{globalId}").permitAll();
                    auth.requestMatchers("/users/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/attendees/admin/**").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.POST, "/attendees").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PUT, "/attendees/{globalId}").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PATCH, "/attendees/status/**").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.GET, "/attendees/{globalId}/feedbacks/**").hasAuthority("TRAINER");
                    auth.requestMatchers(HttpMethod.POST, "/attendees/{globalId}/feedbacks/**").hasAuthority("TRAINER");
                    auth.requestMatchers(HttpMethod.PUT, "/attendees/{globalId}/feedbacks/**").hasAuthority("TRAINER");
                    auth.requestMatchers(HttpMethod.DELETE, "/attendees/{globalId}/feedbacks/**").hasAuthority("TRAINER");

                    auth.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults()).build();

        
     } 

}
