package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.security.entities.OurUser;
import com.example.security.entities.Role;
import com.example.security.repository.OurUserRepository;

import lombok.var;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Porque tenemos una base de datos relacional MySql
public class UserRepositoryTests {

    @Autowired
    private OurUserRepository ourUserRepository;

    // Usuario 0
    private OurUser ourUser0;

    @BeforeEach // Que se ejecute antes de cualquier test
    void setUp() {
        ourUser0 = OurUser.builder()
                .password("Aaaaaa1!")
                .email("user0@test.com")
                .role(Role.ADMINISTRATOR)
                .build();
    }

    // Test para agregar un user
    @Test
    @SuppressWarnings("null")
    @DisplayName("Test para agregar un usuario")
    public void testAddUser() {

        // Given
        OurUser ourUser = ourUser0;

        // When
        OurUser ourUserGuardado = ourUserRepository.save(ourUser);

        // Then
        assertThat(ourUserGuardado).isNotNull();
        assertThat(ourUserGuardado.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("test para recuperar un listado de usuarios")
    public void testFindAllUsers() {
        // given
        OurUser ourUser1 = OurUser.builder()
                .email("user1@test.com")
                .password("Bbbbbb1!")
                .role(Role.ADMINISTRATOR)
                .build();

        ourUserRepository.save(ourUser0);
        ourUserRepository.save(ourUser1);

        // Dado los empleados guardados
        // when
        var usuarios = ourUserRepository.findAll();

        // then
        assertThat(usuarios).isNotNull();
        assertThat(usuarios.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para recuperar un user por su ID")
    public void findUserById() {

        // given
        ourUserRepository.save(ourUser0);

        // when
        OurUser foundUser = ourUserRepository.findById(ourUser0.getId()).get();

        // then
        assertThat(ourUser0.getId()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("Test para actualizar un user")
    public void testUpdateUser() {

        // given
        ourUserRepository.save(ourUser0);

        // when
        OurUser userGuardado = ourUserRepository.findByEmail(ourUser0.getEmail()).get();

        userGuardado.setEmail("perico@juan.com");
        userGuardado.setPassword("0000");
        userGuardado.setRole(Role.ADMINISTRATOR);

        OurUser userUpdated = ourUserRepository.save(userGuardado);

        // then
        assertThat(userUpdated.getEmail()).isEqualTo("perico@juan.com");
        assertThat(userUpdated.getPassword()).isEqualTo("0000");
        assertThat(userUpdated.getRole()).isEqualTo(Role.ADMINISTRATOR);
    }

    @DisplayName("Test para eliminar un user")
    @Test
    public void testDeleteUser() {

        // given
        ourUserRepository.save(ourUser0);

        // when
        ourUserRepository.delete(ourUser0);
        Optional<OurUser> optionalUser = ourUserRepository.findByEmail(ourUser0.getEmail());

        // then
        assertThat(optionalUser).isEmpty();
    }

}