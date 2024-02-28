package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.Feedback;

public interface FeedbackService {

    public List<Feedback> findById(int idAttendee);

    public void save(Feedback feedback);

    public Page<Feedback> findAll(Pageable pageable);
    public List<Feedback> findAll(Sort sort);
    public List<Feedback> findAll();
    public Page<Feedback> findFeedbacksByGlobalId(Integer globalId, Pageable pageable);
    public List<Feedback> findFeedbacksByGlobalId(Integer globalId, Sort sort);
}
