package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dao.AttendeeDao;
import com.example.dao.FeedbackDao;
import com.example.entities.Feedback;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    
    private final FeedbackDao feedbackDao;
    private final AttendeeDao attendeeDao;
    
    @Override
    public List<Feedback> findById(int idAttendee) {
        return feedbackDao.findByAttendee(attendeeDao.findById(idAttendee));
    }

}
