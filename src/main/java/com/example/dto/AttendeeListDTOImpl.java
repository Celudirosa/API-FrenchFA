package com.example.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dao.AttendeeDao;
import com.example.dao.EmailDao;
import com.example.entities.Attendee;
import com.example.entities.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeListDTOImpl implements AttendeeListDTOService{

    private final AttendeeDao attendeeDao;

    @Override
    public List<AttendeeListDTO> findAll() {
        
        List<Attendee> attendees = attendeeDao.findAll();
        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        AttendeeListDTO auxDTO = new AttendeeListDTO();

        for(Attendee attendee : attendees) {
            auxDTO.setFirstName(attendee.getFirstName());
            auxDTO.setSurname(attendee.getSurname());
            auxDTO.setGlobalId(attendee.getGlobalId());
            auxDTO.setProfile(attendee.getProfile());
            auxDTO.setInitialLevel(attendee.getInitialLevel());
            auxDTO.setLastLevel(attendee.getLastLevel());

            // auxDTO.setEmails(attendee.getEmails().stream().map(Email::getEmail)
            //     .collect(Collectors.joining("; ")));
            
            attendeeListDTOs.add(auxDTO);
        }
    
        return attendeeListDTOs;
    }

}
