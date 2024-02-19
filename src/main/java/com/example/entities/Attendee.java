package com.example.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attendees", uniqueConstraints = @UniqueConstraint(columnNames = "globalId"))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "The name cannot be empty")
    @Size(max = 20, message = "The name should not exceed 20 characters")
    private String firstName;

    @NotBlank(message = "The surname cannot be empty")
    @Size(max = 30, message = "The surname should not exceed 30 characters")
    private String surname;

    
    @Setter(value = AccessLevel.PRIVATE)
    @Min(value = 10000000, message = "GlobalId must contain 8 numbers")
    @Max(value = 99999999, message = "GlobalId must contain 8 numbers")
    private int globalId;
    
    @NotNull(message = "Initial level is required")
    @Enumerated(EnumType.STRING)
    private Level initialLevel;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Profile profile;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "attendee")
    @JsonIgnore
    private List<Email> emails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "attendee")
    @JsonIgnore
    private List<Feedback> feedbacks;

}
