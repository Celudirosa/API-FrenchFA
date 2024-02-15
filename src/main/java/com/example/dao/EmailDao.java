package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Attendee;
import com.example.entities.Email;

@Repository
public interface EmailDao extends JpaRepository<Email, Integer> {

    // este metodo viene del CRUD y nos da metodos para cada una de las entidades
    List<Email> findByAttendee(Attendee attendee);

}