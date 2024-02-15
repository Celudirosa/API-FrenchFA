package com.example.services;

import java.util.List;

import com.example.entities.Feedback;

public interface FeedbackService {

    public List<Feedback> findById(int idAttendee);
    
}
