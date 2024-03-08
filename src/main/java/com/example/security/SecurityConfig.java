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
                    auth.requestMatchers(HttpMethod.POST,"/users/add/admin").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/attendees").hasAnyAuthority("TRAINER", "ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.GET, "/attendees/{globalId}").hasAnyAuthority("TRAINER", "ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.POST,"/users/add/trainer").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.GET,"/users/*.*").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PUT,"/users/*.*").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.DELETE,"/users/*.*").hasAuthority("ADMINISTRATOR");


                    auth.requestMatchers(HttpMethod.GET, "/attendees/admin/**").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.POST, "/attendees").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PUT, "/attendees/{globalId}").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.PATCH, "/attendees/status/**").hasAuthority("ADMINISTRATOR");
                    auth.requestMatchers(HttpMethod.GET, "/attendees/{globalId}/feedback/**").hasAuthority("TRAINER");
                    auth.requestMatchers(HttpMethod.POST, "/attendees/{globalId}/feedback/**").hasAuthority("TRAINER");
                    auth.requestMatchers(HttpMethod.PUT, "/attendees/{globalId}/feedback/**").hasAuthority("TRAINER");
                    auth.requestMatchers(HttpMethod.DELETE, "/attendees/{globalId}/feedback/**").hasAuthority("TRAINER");

                    auth.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults()).build();

        
     } 

}
