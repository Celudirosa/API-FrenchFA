package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public void save(Feedback feedback) {
        feedbackDao.save(feedback);
    }

    @Override
    public Page<Feedback> findAll(Pageable pageable) {
        return feedbackDao.findAll(pageable);
    }

    @Override
    public List<Feedback> findAll(Sort sort) {
        return feedbackDao.findAll(sort);
    }

    @Override
    public List<Feedback> findAll() {
        return feedbackDao.findAll();
    }

}
