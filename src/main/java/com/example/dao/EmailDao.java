package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Attendee;
import com.example.entities.Email;


@Repository
public interface EmailDao extends JpaRepository<Email, Integer> {

    // Query de SQL
    // SELECT atd.first_name, e.email
    // FROM attendees atd JOIN emails e
    // ON atd.id = e.attendee_id;

    @Query(value = "SELECT a FROM Email a JOIN FETCH a.attendee WHERE a.id = :id")
    List<Email> findByAttendee(Attendee attendee);

}