package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.dao.AttendeeDao;
import com.example.dao.FeedbackDao;
import com.example.entities.Attendee;
import com.example.entities.Feedback;
import com.example.entities.Level;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {
    
    private final AttendeeDao attendeeDao;
    private final FeedbackDao feedbackDao;

    @Override
    public Page<Attendee> findAll(Pageable pageable) {
        return attendeeDao.findAll(pageable);
    }

    @Override
    public List<Attendee> findAll(Sort sort) {
        return attendeeDao.findAll(sort);
    }

    @Override
    public List<Attendee> findAll() {
        return attendeeDao.findAll();
    }

    @Override
    public Attendee findById(int id) {
        return attendeeDao.findById(id);
    }

    @Override
    public Attendee save(Attendee attendee) {
        return attendeeDao.save(attendee);
    }

    @Override
    public void delete(Attendee attendee) {
        attendeeDao.delete(attendee);
    }

    // Método que recupera una lista de los feedbacks ordenados por fecha en order desc 
    @Override
    public Level getLastLevel(Attendee attendee) {
        List<Feedback> feedbacks = feedbackDao.findFeedbacksByAttendeeOrderByDateDesc(attendee);
        if(!feedbacks.isEmpty()) {
            return feedbacks.get(0).getLevel();
        } else {
            // // Si no hay feedbacks, devolver el nivel inicial del Attendee
            return null;
        }
    }

}