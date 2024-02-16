package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Attendee;
import com.example.services.AttendeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class MainController {

    private final AttendeeService attendeeService;

    // Metodo que devuelve los attendees
    @GetMapping
    public ResponseEntity<List<Attendee>> findAll(
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "size", required = false) Integer size) {

            ResponseEntity<List<Attendee>> responseEntity = null;
            Sort sortByName = Sort.by("name");
            List<Attendee> attendees = new ArrayList<>();

            // Comprobamos si llega page y size
            if(page != null && size != null) { // si se mete aqui te devuelve los productos paginados
                Pageable pageable = PageRequest.of(page, size, sortByName);
                Page<Attendee> pageAttendees = attendeeService.findAll(pageable);
                attendees = pageAttendees.getContent();
                responseEntity = new ResponseEntity<List<Attendee>>(attendees, HttpStatus.OK);
            } else { // solo ordenados alfabeticamente
                attendees = attendeeService.findAll(sortByName);
                responseEntity = new ResponseEntity<List<Attendee>>(attendees, HttpStatus.OK);
            }

            return responseEntity;
        }


}
