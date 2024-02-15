package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Attendee;
import com.example.entities.Feedback;

public interface FeedbackDao extends JpaRepository<Feedback, Integer> {

    @Query(value = "SELECT a FROM Feedback a JOIN FETCH a.attendee WHERE a.id = :id")
    List<Feedback> findByAttendee(Attendee attendee);

}
