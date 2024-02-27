package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.dao.AttendeeDao;
import com.example.dao.FeedbackDao;
import com.example.dto.AttendeeListDTO;
import com.example.entities.Attendee;
import com.example.entities.Feedback;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {
    
    private final AttendeeDao attendeeDao;
    private final FeedbackDao feedbackDao;

    @Override
    public List<AttendeeListDTO> findAll() {

        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        // Comprobamos si llega page y size
        List<Attendee> listAttendees = attendeeDao.findAll();

        for(Attendee a : listAttendees) {
            attendeeListDTOs.add(new AttendeeListDTO(a.getFirstName(), a.getSurname(), a.getGlobalId(), a.getEmails(), a.getInitialLevel(), a.getLastLevel(), a.getProfile()));
        }
        
        return attendeeListDTOs;
    }

     //attendeesEnable = pageAttendees.stream().filter(a -> a.getStatus() == Status.ENABLE).collect(Collectors.toList());

    @Override
    public List<Attendee> findAll(Sort sort) {
        return attendeeDao.findAll(sort);
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
    public String getLastLevel(Attendee attendee) {
        List<Feedback> feedbacks = feedbackDao.findFeedbacksByAttendeeOrderByDateDesc(attendee);
            String lastLevel = feedbacks.get(0).getLevel().toString();
            attendee.setLastLevel(lastLevel); // Actualizar el campo lastLevel del Attendee
            attendeeDao.save(attendee); // Guardar el Attendee actualizado en la base de datos
                return lastLevel;
            }

    @Override
    public Attendee findByGlobalId(int globalId) {
        return attendeeDao.findByGlobalId(globalId);
    }

    @Override
    public Page<AttendeeListDTO> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    // Si no hay feedbacks, devolver el nivel inicial del Attendee

    }


