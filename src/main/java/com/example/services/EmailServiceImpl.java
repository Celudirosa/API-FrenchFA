package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dao.AttendeeDao;
import com.example.dao.EmailDao;
import com.example.entities.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    
    private final EmailDao emailDao;
    private final AttendeeDao attendeeDao;

    @Override
    public List<Email> findById(int idAttendee) {
        return emailDao.findByAttendee(attendeeDao.findById(idAttendee));
    }

    @Override
    public void save(Email email) {
        emailDao.save(email);
    }

}
