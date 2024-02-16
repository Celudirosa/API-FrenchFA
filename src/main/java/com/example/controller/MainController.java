package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Attendee;
import com.example.services.AttendeeService;

import jakarta.validation.Valid;
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
        Sort sortByName = Sort.by("firstName");
        List<Attendee> attendees = new ArrayList<>();

        // Comprobamos si llega page y size
        if (page != null && size != null) { // si se mete aqui te devuelve los productos paginados
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

    // Metodo que persiste un attendee, y valida que esten bien formados los campos
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveAttendee(@Valid @RequestBody Attendee attendee,
            BindingResult validationResult) {

                Map<String, Object> responseAsMap = new HashMap<>();
                ResponseEntity<Map<String, Object>> responseEntity = null;

                // Comprobar si el attendee tiene errores
                if (validationResult.hasErrors()) {
                    List<String> errors = new ArrayList<>();
                    List<ObjectError> objectErrors = validationResult.getAllErrors();

                    objectErrors.forEach(objectError -> errors.add(objectError.getDefaultMessage()));

                    responseAsMap.put("errors", errors);
                    responseAsMap.put("Attendee Error", attendee);

                    responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

                    return responseEntity;

                    
                }

                // Si no hay errores en el attendee, lo persistimos

                try {
                    Attendee attendeePersist = attendeeService.save(attendee);
                    String succesMessage = "The attendee has been saved succesfully";
                    responseAsMap.put("Succes Message", succesMessage);
                    responseAsMap.put("Attendee Persist", attendeePersist);
                    responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.CREATED);
                } catch (DataAccessException e) {
                    String error = "Error saving attendee" + e.getMostSpecificCause() ;
                    responseAsMap.put("error", error);
                    responseAsMap.put("The attende has attempted to persist", attendee);
                    responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
                }

        return responseEntity;
    }

}
