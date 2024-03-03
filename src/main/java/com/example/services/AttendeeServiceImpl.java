package com.example.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.dao.AttendeeDao;
import com.example.dao.FeedbackDao;
import com.example.dto.AttendeeListDTO;
import com.example.dto.AttendeeProfileDTO;
import com.example.dto.FeedbackDTO;
import com.example.entities.Attendee;
import com.example.entities.Email;
import com.example.entities.Feedback;
import com.example.entities.Status;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {

    private final AttendeeDao attendeeDao;
    private final FeedbackDao feedbackDao;

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

    // Método que recupera una lista de los feedbacks ordenados por fecha en order
    // desc
    @Override
    public String getLastLevel(Attendee attendee) {
        List<Feedback> feedbacks = feedbackDao.findFeedbacksByAttendeeOrderByDateDesc(attendee);
        String lastLevel = feedbacks.get(0).getLevel().toString();
        attendee.setLastLevel(lastLevel); // Actualizar el campo lastLevel del Attendee
        attendeeDao.save(attendee); // Guardar el Attendee actualizado en la base de datos
        return lastLevel;
    }

    // Metodo para recuperar un Attendee por globalId
    @Override
    public AttendeeProfileDTO findByGlobalIdDTO(int globalId) {
        Attendee attendee = attendeeDao.findByGlobalId(globalId);
        AttendeeProfileDTO attendeeProfileDTO = null;

        String emailsAsString = attendee.getEmails().stream()
                .map(Email::getEmail)
                .collect(Collectors.joining("; "));

        List<FeedbackDTO> feedbackDTOs = attendee.getFeedbacks().stream()
                .map(feedback -> new FeedbackDTO(feedback.getLevel(), feedback.getDate(), feedback.getComments()))
                .collect(Collectors.toList());

        attendeeProfileDTO = new AttendeeProfileDTO(
                attendee.getFirstName(),
                attendee.getSurname(),
                attendee.getGlobalId(),
                emailsAsString, // Pasar el String de correos electrónicos aquí
                attendee.getInitialLevel(),
                attendee.getProfile().getProfile(),
                feedbackDTOs

        );
        return attendeeProfileDTO;
    }

    // @Override
    // public Page<Attendee> findAll(Pageable pageable) {
    // return attendeeDao.findAll(pageable);
    // }

    // List<Attendee> attendeesEnable = attendees.stream().filter(a -> a.getStatus()
    // == Status.ENABLE).collect(Collectors.toList());

    // Metodo para encontrar todos los AttendeeDto enable ordenados por firstName
    @Override
    public List<AttendeeListDTO> findAllEnable() {

        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        List<Attendee> attendees = attendeeDao.findAllByOrderByFirstName();
        List<Attendee> attendeesEnable = attendees.stream().filter(a -> a.getStatus() == Status.ENABLE)
                .collect(Collectors.toList());

        for (Attendee a : attendeesEnable) {
            String emailsAsString = a.getEmails().stream()
                    .map(Email::getEmail)
                    .collect(Collectors.joining("; "));

            AttendeeListDTO attendeeDTO = new AttendeeListDTO(
                    a.getFirstName(),
                    a.getSurname(),
                    a.getGlobalId(),
                    emailsAsString, // Pasar el String de correos electrónicos aquí
                    a.getInitialLevel(),
                    a.getLastLevel(),
                    a.getProfile().getProfile());

            attendeeListDTOs.add(attendeeDTO);

        }

        return attendeeListDTOs;
    }

    // Metodo para encontrar todos los AttendeeDto enable ordenados por firstName y
    // paginados
    public Page<AttendeeListDTO> findAllEnable(Pageable pageable) {
        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        List<Attendee> attendees = attendeeDao.findAllByOrderByFirstName();
        List<Attendee> attendeesEnable = attendees.stream()
                .filter(a -> a.getStatus() == Status.ENABLE)
                .collect(Collectors.toList());

        for (Attendee a : attendeesEnable) {
            String emailsAsString = a.getEmails().stream()
                    .map(Email::getEmail)
                    .collect(Collectors.joining("; "));

            AttendeeListDTO attendeeDTO = new AttendeeListDTO(
                    a.getFirstName(),
                    a.getSurname(),
                    a.getGlobalId(),
                    emailsAsString, // Pasar el String de correos electrónicos aquí
                    a.getInitialLevel(),
                    a.getLastLevel(),
                    a.getProfile().getProfile());

            attendeeListDTOs.add(attendeeDTO);
        }

        // Aplicar paginación
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<AttendeeListDTO> pagedAttendeeList;

        if (attendeeListDTOs.size() < startItem) {
            pagedAttendeeList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, attendeeListDTOs.size());
            pagedAttendeeList = attendeeListDTOs.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedAttendeeList, PageRequest.of(currentPage, pageSize), attendeeListDTOs.size());
    }

    // Metodo para encontrar todos los AttendeeDto enable ordenados por firstName
    @Override
    public List<AttendeeListDTO> findAllDisable() {

        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        List<Attendee> attendees = attendeeDao.findAllByOrderByFirstName();
        List<Attendee> attendeesDisable = attendees.stream().filter(a -> a.getStatus() == Status.DISABLE)
                .collect(Collectors.toList());

        for (Attendee a : attendeesDisable) {
            String emailsAsString = a.getEmails().stream()
                    .map(Email::getEmail)
                    .collect(Collectors.joining("; "));

            AttendeeListDTO attendeeDTO = new AttendeeListDTO(
                    a.getFirstName(),
                    a.getSurname(),
                    a.getGlobalId(),
                    emailsAsString, // Pasar el String de correos electrónicos aquí
                    a.getInitialLevel(),
                    a.getLastLevel(),
                    a.getProfile().getProfile());

            attendeeListDTOs.add(attendeeDTO);

        }

        return attendeeListDTOs;
    }

    // Metodo para encontrar todos los AttendeeDto disable ordenados por firstName y
    // paginados
    public Page<AttendeeListDTO> findAllDisable(Pageable pageable) {
        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        List<Attendee> attendees = attendeeDao.findAllByOrderByFirstName();
        List<Attendee> attendeesDisable = attendees.stream()
                .filter(a -> a.getStatus() == Status.DISABLE)
                .collect(Collectors.toList());

        for (Attendee a : attendeesDisable) {
            String emailsAsString = a.getEmails().stream()
                    .map(Email::getEmail)
                    .collect(Collectors.joining("; "));

            AttendeeListDTO attendeeDTO = new AttendeeListDTO(
                    a.getFirstName(),
                    a.getSurname(),
                    a.getGlobalId(),
                    emailsAsString, // Pasar el String de correos electrónicos aquí
                    a.getInitialLevel(),
                    a.getLastLevel(),
                    a.getProfile().getProfile());

            attendeeListDTOs.add(attendeeDTO);
        }

        // Aplicar paginación
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<AttendeeListDTO> pagedAttendeeList;

        if (attendeeListDTOs.size() < startItem) {
            pagedAttendeeList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, attendeeListDTOs.size());
            pagedAttendeeList = attendeeListDTOs.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedAttendeeList, PageRequest.of(currentPage, pageSize), attendeeListDTOs.size());
    }

    public boolean existsById(Integer globalId) {
        return attendeeDao.existsById(globalId);
    }

    @Override
    public Attendee findByGlobalId(int globalId) {
        return attendeeDao.findByGlobalId(globalId);
    }

}
