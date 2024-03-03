package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.dto.AttendeeListDTO;
import com.example.dto.AttendeeProfileDTO;
import com.example.entities.Attendee;

public interface AttendeeService {

    
    public List<AttendeeListDTO> findAllEnable();
    public Page<AttendeeListDTO> findAllEnable(Pageable pageable); 
    public Page<AttendeeListDTO> findAllDisable(Pageable pageable);
    public List<AttendeeListDTO> findAllDisable();
    public List<Attendee> findAll(Sort sort);
    public String getLastLevel(Attendee attendee);
    public Attendee findById(int id);
    AttendeeProfileDTO findByGlobalIdDTO(int globalId);
    Attendee findByGlobalId(int globalId);
    public Attendee save(Attendee attendee);
    public void delete(Attendee attendee);
    
    

    public boolean existsById(Integer globalId);

}

