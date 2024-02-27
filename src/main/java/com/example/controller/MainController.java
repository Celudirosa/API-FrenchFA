package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Attendee;
import com.example.entities.Status;
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
            List<Attendee> attendees = attendeeService.findAll(sortByName);
            attendeesEnable = attendees.stream().filter(a -> a.getStatus() == Status.ENABLE).collect(Collectors.toList());

            responseEntity = new ResponseEntity<List<Attendee>>(attendeesEnable, HttpStatus.OK);
        }

        return responseEntity;
    }

        // Metodo que devuelve los attendees DISABLE
    @GetMapping("/disable")
    public ResponseEntity<List<Attendee>> findByStatusDisable(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<Attendee>> responseEntity = null;
        Sort sortByName = Sort.by("firstName");
        List<Attendee> attendeesDisable = new ArrayList<>();
    
        // Comprobamos si llega page y size
        if (page != null && size != null) { // si se mete aqui te devuelve los productos paginados
            Pageable pageable = PageRequest.of(page, size, sortByName);
            Page<Attendee> pageAttendees = attendeeService.findAll(pageable);
            
            attendeesDisable = pageAttendees.stream().filter(a -> a.getStatus() == Status.DISABLE).collect(Collectors.toList());

            responseEntity = new ResponseEntity<List<Attendee>>(attendeesDisable, HttpStatus.OK);
        } else { // solo ordenados alfabeticamente
            List<Attendee> attendees = attendeeService.findAll(sortByName);
            attendeesDisable = attendees.stream().filter(a -> a.getStatus() == Status.DISABLE).collect(Collectors.toList());

            responseEntity = new ResponseEntity<List<Attendee>>(attendeesDisable, HttpStatus.OK);
        }

        return responseEntity;
    }

    @GetMapping("/{globalId}")
    public ResponseEntity<Map<String, Object>> findAttendeeByGlobalId(@PathVariable(name = "globalId", required = true) Integer globalIdAttendee) throws IOException {

        Map<String, Object> responseMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        try {
            Attendee attendee = attendeeService.findByGlobalId(globalIdAttendee);

            // Verifica si el Attendee fue encontrado
            if (attendee != null) {
                responseMap.put("attendee", attendee);
                responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
            } else {
                // Si no se encuentra el Attendee, devuelve un error 404 Not Found
                responseMap.put("error", "No se encontró el Attendee con globalId: " + globalIdAttendee);
                responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }
            
        } catch (DataAccessException e) {
            String error = "Error al buscar el producto con id " + globalIdAttendee + " y la causa mas probable es: " + e.getMostSpecificCause();
            responseMap.put("error", error);
            responseEntity = new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
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

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

            return responseEntity;

        }

        // Si no hay errores en el attendee, lo persistimos

        try {
            Attendee attendeePersist = attendeeService.save(attendee);
            String succesMessage = "The attendee has been saved succesfully";
            responseAsMap.put("Succes Message", succesMessage);
            responseAsMap.put("Attendee Persist", attendeePersist);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            String error = "Error saving attendee" + e.getMostSpecificCause();
            responseAsMap.put("error", error);
            responseAsMap.put("The attende has attempted to persist", attendee);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
    // Metodo para modificar Attendee por globalId
    @PutMapping("/{globalId}")
    public ResponseEntity<Map<String, Object>> updateAttendee(@Valid @RequestBody Attendee attendee, 
    BindingResult validationResults, @PathVariable(name = "globalId") Integer globalIdAttendee) {

    Map<String, Object> responseAsMap = new HashMap<>();
    ResponseEntity<Map<String, Object>> responseEntity = null;

    // Verificar que el attendee existe
    int idAttendee = attendee.getId();
    Attendee existingAttendee = attendeeService.findById(idAttendee);
    if (existingAttendee == null) {
        String errorMessage = "Attendee with global Id " + attendee.getGlobalId() + " not found";
        responseAsMap.put("errorMessage", errorMessage);
        return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
    }

    // Verificar que el globalId del cuerpo coincida con el globalId del attendee existente
    Integer attendeGlobalId = attendee.getGlobalId();
    Integer existingAttendeGlobalId = existingAttendee.getGlobalId();
    if (!attendeGlobalId.equals(existingAttendeGlobalId)) {
        String errorMessage = "Modification of globalId is not allowed";
        responseAsMap.put("errorMessage", errorMessage);
        return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
    }

    // Comprobar si el attendee tiene errores
    if (validationResults.hasErrors()) {
        List<String> errors = new ArrayList<>();
        
        List<ObjectError> objectErrors = validationResults.getAllErrors();

        objectErrors.forEach(objectError -> errors.add(objectError.getDefaultMessage()));

        responseAsMap.put("errors", errors);
        responseAsMap.put("Attendee Error", attendee);

        return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
    }

    // Actualizar el Attendee
    try {
        Attendee attendeeUpdate = attendeeService.save(attendee);
        String succesMessage = "The attendee has been saved succesfully";
        responseAsMap.put("Succes Message", succesMessage);
        responseAsMap.put("Attendee update", attendeeUpdate);
        responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.OK);
    } catch (DataAccessException e) {
        String error = "Error updating the attendee: " + e.getMostSpecificCause();
        responseAsMap.put("error", error);
        responseAsMap.put("The attende has attempted to update", attendee);
        responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return responseEntity;
}


// @GetMapping("/{globalId}")
// public ResponseEntity<Attendee> getAttendeeById(@Valid @RequestBody Attendee attendee, 
//  @PathVariable(name = "globalId") Integer globalIdAttendee) {

//      int idAttendee = attendee.getId();
//      Attendee thisAttendee = attendeeService.findById(idAttendee);
//      thisAttendee= thisAttendee.builder()
//      .firstName(thisAttendee.getFirstName())
//      .surname(thisAttendee.getSurname())
//      .emails(thisAttendee.getEmails())
//   .profile(thisAttendee.getProfile())
//   .feedbacks(thisAttendee.getFeedbacks())
//   .initialLevel(thisAttendee.getInitialLevel())
//   .globalId(thisAttendee.getGlobalId())
//   .build();
  
      
  
//   return new ResponseEntity<>(thisAttendee, HttpStatus.OK);
// }

@GetMapping("/{globalId}")
public ResponseEntity<Attendee> getAttendeeById(@PathVariable(name = "globalId") Integer globalIdAttendee) {
    Attendee attendee = attendeeService.findById(globalIdAttendee);
    
    if (attendee == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Crear un objeto de respuesta con solo los campos necesarios
    Attendee thisAttendee = new Attendee();
    thisAttendee.setFirstName(attendee.getFirstName());
    thisAttendee.setSurname(attendee.getSurname());
    thisAttendee.setId(attendee.getId());
    thisAttendee.setEmails(attendee.getEmails());

    return new ResponseEntity<Attendee> (thisAttendee, HttpStatus.OK);
}

    
}


