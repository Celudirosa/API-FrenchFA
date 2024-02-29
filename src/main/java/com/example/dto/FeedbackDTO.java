package com.example.dto;

import java.time.LocalDate;

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
public class FeedbackDTO {
    private Level level;
    private LocalDate date;
    private String comments;
}
