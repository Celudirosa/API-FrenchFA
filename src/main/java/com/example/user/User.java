package com.example.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true) // No se repite, pero no forma parte de la PK
    @Pattern(regexp = "[a-zA-Z0-9]*@thefutureyouwant.com$", message = "The email should contain the domain @thefutureyouwant.com")
    private String email;

    @Size(min = 8, max = 10, message = "The password must contain between eight and 10 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])(?!.*\\s).*$",
            message = "The password must contain at least one uppercase letter, exactly one number, and one special character")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
