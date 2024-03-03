package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AttendeeListDTO;
import com.example.dto.AttendeeProfileDTO;
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

    // Metodo que devuelve todos los atendees paginados u ordenados
    @GetMapping
    public ResponseEntity<List<AttendeeListDTO>> findAllEnable(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<AttendeeListDTO>> responseEntity = null;
        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        // Comprobamos si llega page y size
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<AttendeeListDTO> pageAttendees = attendeeService.findAllEnable(pageable);
            attendeeListDTOs = pageAttendees.getContent();
            responseEntity = new ResponseEntity<List<AttendeeListDTO>>(attendeeListDTOs, HttpStatus.OK);
        } else { // solo ordenados alfabeticamente
            attendeeListDTOs = attendeeService.findAllEnable();
            responseEntity = new ResponseEntity<List<AttendeeListDTO>>(attendeeListDTOs, HttpStatus.OK);
        }
        return responseEntity;
    }

    // Metodo que devuelve todos los atendees paginados u ordenados
    @GetMapping("/admin/disable")
    public ResponseEntity<List<AttendeeListDTO>> findAllDisable(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<AttendeeListDTO>> responseEntity = null;
        List<AttendeeListDTO> attendeeListDTOs = new ArrayList<>();
        // Comprobamos si llega page y size
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<AttendeeListDTO> pageAttendees = attendeeService.findAllDisable(pageable);
            attendeeListDTOs = pageAttendees.getContent();
            responseEntity = new ResponseEntity<List<AttendeeListDTO>>(attendeeListDTOs, HttpStatus.OK);
        } else { // solo ordenados alfabeticamente
            attendeeListDTOs = attendeeService.findAllDisable();
            responseEntity = new ResponseEntity<List<AttendeeListDTO>>(attendeeListDTOs, HttpStatus.OK);
        }
        return responseEntity;
    }

    // Metodo que devuelve los attendees por su globalId (solo enables)
    @GetMapping("/{globalId}")
    public ResponseEntity<Map<String, Object>> findAttendeeByGlobalIdEnables(
            @PathVariable(name = "globalId", required = true) Integer globalIdAttendee) throws IOException {

        Map<String, Object> responseMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Attendee attendeeEnable = attendeeService.findByGlobalId(globalIdAttendee);
        if (attendeeEnable.getStatus() == Status.ENABLE) {
            try {
                AttendeeProfileDTO attendee = attendeeService.findByGlobalIdDTO(globalIdAttendee);

                // Verifica si el Attendee fue encontrado
                if (attendee != null) {
                    responseMap.put("attendee", attendee);
                    responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
                } else {
                    // Si no se encuentra el Attendee, devuelve un error 404 Not Found
                    responseMap.put("error", "Not found Attendee with GlobalId = " + globalIdAttendee);
                    responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
                }

            } catch (DataAccessException e) {
                String error = "Error searching for Attendee with id: " + globalIdAttendee
                        + " and the most likely cause is: "
                        + e.getMostSpecificCause();
                responseMap.put("error", error);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            // Si no es ENABLE, devuelve un error
            responseMap.put("error", "The attendee with " + globalIdAttendee
                    + " is DISABLE");
            responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);

        }

        return responseEntity;
    }

    // Metodo que devuelve los attendees por su globalId (Enables y desables)
    @GetMapping("/admin/{globalId}")
    public ResponseEntity<Map<String, Object>> findAttendeeByGlobalId(
            @PathVariable(name = "globalId", required = true) Integer globalIdAttendee) throws IOException {

        Map<String, Object> responseMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;
        try {
            AttendeeProfileDTO attendee = attendeeService.findByGlobalIdDTO(globalIdAttendee);

            // Verifica si el Attendee fue encontrado
            if (attendee != null) {
                responseMap.put("attendee", attendee);
                responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
            } else {
                // Si no se encuentra el Attendee, devuelve un error 404 Not Found
                responseMap.put("error", "Not found Attendee with GlobalId = " + globalIdAttendee);
                responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            String error = "Error searching for Attendee with id: " + globalIdAttendee + " and the most likely cause is: "
                    + e.getMostSpecificCause();
            responseMap.put("error", error);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
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

    // Metodo para modificar attendee
    @PutMapping("/{globalId}")
    public ResponseEntity<Map<String, Object>> updateAttendee(@RequestBody Attendee attendee,
            @PathVariable(name = "globalId") Integer globalIdAttendee) {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity;

        // Verificar que el attendee existe
        Attendee existingAttendee = attendeeService.findByGlobalId(globalIdAttendee);
        if (existingAttendee == null) {
            String errorMessage = "Not found Attendee with GlobalId = " + globalIdAttendee;
            responseAsMap.put("errorMessage", errorMessage);
            return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
        }

        // Verificar que el globalId del cuerpo coincida con el globalId del attendee
        // existente
        Integer existingAttendeeGlobalId = existingAttendee.getGlobalId();
        if (!globalIdAttendee.equals(existingAttendeeGlobalId)) {
            String errorMessage = "Modification of globalId is not allowed";
            responseAsMap.put("errorMessage", errorMessage);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
        }

        // Actualizar el Attendee
        try {
            // Actualizar solo los campos no nulos del objeto Attendee
            if (attendee.getFirstName() != null) {
                existingAttendee.setFirstName(attendee.getFirstName());
            }
            if (attendee.getSurname() != null) {
                existingAttendee.setSurname(attendee.getSurname());
            }
            if (attendee.getEmails() != null) {
                existingAttendee.setEmails(attendee.getEmails());
            }
            if (attendee.getProfile() != null) {
                existingAttendee.setProfile(attendee.getProfile());
            }
            if (attendee.getInitialLevel() != null) {
                existingAttendee.setInitialLevel(attendee.getInitialLevel());
            }
            if (attendee.getStatus() != null) {
                existingAttendee.setStatus(attendee.getStatus());
            }

            Attendee attendeeUpdate = attendeeService.save(existingAttendee);
            String successMessage = "The attendee has been saved successfully";
            responseAsMap.put("successMessage", successMessage);
            responseAsMap.put("Attendee update", attendeeUpdate);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.OK);
        } catch (DataAccessException e) {
            String error = "Error updating the attendee: " + e.getMostSpecificCause();
            responseAsMap.put("error", error);
            responseAsMap.put("The attendee has attempted to update", attendee);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    // Metodo delete logico de attendee
    @PatchMapping("status/{globalId}")
    public ResponseEntity<Map<String, Object>> changeStatus(@RequestBody Attendee attendee,
            @PathVariable(name = "globalId") Integer globalIdAttendee) {

        Map<String, Object> responseAsMap = new HashMap<>();

        // llamar al attendee y ver si existe
        Attendee existingAttendee = attendeeService.findByGlobalId(globalIdAttendee);
        if (existingAttendee == null) {
            String errorMessage = "Not found Attendee with GlobalId = " + globalIdAttendee ;
            responseAsMap.put("errorMessage", errorMessage);
            return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
        } else {

            // Actualizar status
            existingAttendee.setStatus(attendee.getStatus());
            Attendee updatedAttendee = attendeeService.save(existingAttendee);

            String successMessage = "Attendee status updated successfully";
            responseAsMap.put("successMessage", successMessage);
            responseAsMap.put("updatedAttendee", updatedAttendee);
            return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
        }
    }

}
