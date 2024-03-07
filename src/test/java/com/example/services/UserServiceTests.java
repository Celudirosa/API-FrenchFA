package com.example.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.customsExceptions.ResourceNotFoundException;
import com.example.user.Role;
import com.example.user.User;
import com.example.user.UserRepository;
import com.example.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class) // Mockito simulates dependencies
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("AdminName")
                .lastName("AdminLastName")
                .password("Aaaaaa1!")
                .email("admin@blue.com")
                .role(Role.ADMINISTRATOR)
                .build();
    }
    // Ensure it's never possible to add a user with an existing email
    @Test
    @DisplayName("Test for saving a user and generating an exception ")
    public void testSaveUserWithThrowException() {

        // given
        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.add(user);
        });

        // Then
        verify(userRepository, never()).save(any(User.class));

    }

}