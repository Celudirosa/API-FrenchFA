package com.example.services;

import java.util.List;

import com.example.entities.Email;

public interface EmailService {

    public List<Email> findById(int idAttendee);

    public void save(Email email);

}
