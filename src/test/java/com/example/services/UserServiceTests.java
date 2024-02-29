package com.example.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

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

import com.example.exception.ResourceNotFoundException;
import com.example.security.entities.OurUser;
import com.example.security.entities.Role;
import com.example.security.repository.OurUserRepository;
import com.example.security.services.OurUserDetailsService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE) // Indicar que no me cambie la base de datos de MySQL
public class UserServiceTests {

    @Mock // Esta anotación me permite simular el repositorio
    private OurUserRepository ouruserRepository;

    @InjectMocks // Inyectar la implementación de la capa de servicios
    private OurUserDetailsService OurUserDetailsService;

    private OurUser ourUser;

    @BeforeEach
    void setUp() {
        ourUser = OurUser.builder()
                .id(1)
                .email("user0@thefutureyouwant.com")
                .password("Aaaaaaa1!")
                .role(Role.ADMINISTRATOR)
                .build();
    }

    // Test para guardar un user y que se genere una exception
    // Verifica que nunca se sea posible agregar un user: admin/trainer cuyo email ya exista
    @Test
    @DisplayName("Test para guardar un user y genere una exception")
    public void testSaveUserWithThrowException() {

        // given
        given(ouruserRepository.findByEmail(ourUser.getEmail()))
                .willReturn(Optional.of(ourUser));

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            OurUserDetailsService.add(ourUser);
        });

        // Then
        verify(ouruserRepository, never()).save(any(OurUser.class));

    }

}

