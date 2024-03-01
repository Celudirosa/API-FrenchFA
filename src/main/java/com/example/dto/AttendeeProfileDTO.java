package com.example.dto;

import java.util.List;

import com.example.entities.Level;

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
public class AttendeeProfileDTO {

    private String firstName;
    private String surname;
    private int globalId;
    private String email;
    private Level initialLevel;
    private String profile;
    private List<FeedbackDTO> feedbacks;

}
