package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Attendee;
import com.example.entities.Feedback;

public interface FeedbackDao extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByAttendee(Attendee attendee);

    // Recuperar el last level por globalId
    // @Query(value = "SELECT f FROM Feedback f left join fetch a.attendee ORDER BY
    // f.date DESC")
    List<Feedback> findFeedbacksByAttendeeOrderByDateDesc(Attendee attendee);

    Page<Feedback> findFeedbacksByAttendeeGlobalId(Integer globalId, Pageable pageable);

    List<Feedback> findFeedbacksByAttendeeGlobalId(Integer globalId, Sort sort);

    Feedback findById(int id);

}
