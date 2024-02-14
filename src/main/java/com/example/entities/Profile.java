package com.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public enum Profile {
    INTERNAL, BOOTCAMP

}
