package com.example.security.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exception.ResourceNotFoundException;
import com.example.security.entities.OurUser;
import com.example.security.repository.OurUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OurUserDetailsService implements UserDetailsService {

    private final OurUserRepository ourUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return ourUserRepository.findByEmail(username).orElseThrow();
    }

    public OurUser add(OurUser user) {
        Optional<OurUser> theUser = ourUserRepository.findByEmail(user.getEmail());

        if (theUser.isPresent()) {
            // Deberiamos devolver una exception personalizada
            throw new ResourceNotFoundException("A user with the provided email already exists");
        }

        // Encriptamos la password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        return ourUserRepository.save(user);
    }
}
