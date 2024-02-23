package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.Attendee;
import com.example.entities.Status;


public interface AttendeeService {

    //public Page<Attendee> findAll(Pageable pageable);
    public List<Attendee> findAll(Sort sort);
    public List<Attendee> findAll();
    public List<Attendee> findByStatus(Status status, Pageable pageable);
    public List<Attendee> findByStatus(Status status, Sort sort);
    public Attendee findById(int id);
    public Attendee save(Attendee attendee);
    public void delete(Attendee attendee);
    public String getLastLevel(Attendee attendee);

}
