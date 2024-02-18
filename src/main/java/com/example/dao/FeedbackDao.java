package com.example.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Attendee;
import com.example.entities.Feedback;
import com.example.entities.Level;

public interface FeedbackDao extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByAttendee(Attendee attendee);
    List<Feedback> findFeedbacksByAttendeeOrderByDateDesc(Attendee attendee);
   


   


   
}
