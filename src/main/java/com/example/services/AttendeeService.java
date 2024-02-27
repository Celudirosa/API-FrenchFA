package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.dto.AttendeeListDTO;
import com.example.entities.Attendee;


public interface AttendeeService {

    public Page<AttendeeListDTO> findAll(Pageable pageable);
    public List<Attendee> findAll(Sort sort);
    public List<AttendeeListDTO> findAll();
    public Attendee findById(int id);
    public Attendee save(Attendee attendee);
    public void delete(Attendee attendee);
    public String getLastLevel(Attendee attendee);
    public Attendee findByGlobalId(int globalId);

}
