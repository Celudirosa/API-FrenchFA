package com.example.dto;

import java.util.List;

import com.example.entities.Email;
import com.example.entities.Level;
import com.example.entities.Profile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeListDTO {

    private String firstName;
    private String surname;
    private int globalId;
    private String email;
    private Level initialLevel;
    private String lastLevel;
    private String profile;

}
