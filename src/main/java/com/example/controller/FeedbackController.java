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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.customsExceptions.ResourceNotFoundException;
import com.example.entities.Attendee;
import com.example.entities.Feedback;
import com.example.entities.Status;
import com.example.services.AttendeeService;
import com.example.services.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final AttendeeService attendeeService;

    // Método para sacar los feedbacks por globalId
    // ADMIN
    @GetMapping("/attendees/admin/{globalId}/feedbacks")
    public ResponseEntity<List<Feedback>> findFeedbacksByGlobalId(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @PathVariable(value = "globalId") Integer globalId) throws IOException {

        Attendee attendee = attendeeService.findByGlobalId(globalId);
        if (attendee == null) {
            throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
        }

        ResponseEntity<List<Feedback>> responseEntity = null;
        Sort sortByDate = Sort.by("date").descending();
        List<Feedback> feedbacks = new ArrayList<>();

        if (page != null && size != null) {

            Pageable pageable = PageRequest.of(page, size, sortByDate);
            Page<Feedback> pageFeedbacks = feedbackService.findFeedbacksByGlobalId(globalId, pageable);
            feedbacks = pageFeedbacks.getContent();
            responseEntity = new ResponseEntity<List<Feedback>>(feedbacks, HttpStatus.OK);

        } else {
            feedbacks = feedbackService.findFeedbacksByGlobalId(globalId, sortByDate);
            responseEntity = new ResponseEntity<List<Feedback>>(feedbacks, HttpStatus.OK);
        }

        return responseEntity;
    }

    // Método para sacar los feedbacks por globalId que estén Enables
    // TRAINER
    @GetMapping("/attendees/{globalId}/feedbacks")
    public ResponseEntity<List<Feedback>> findFeedbacksByGlobalIdEnables(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @PathVariable(value = "globalId") Integer globalId) throws IOException {

        Attendee attendee = attendeeService.findByGlobalId(globalId);
        if (attendee == null) {
            throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
        } else if (attendee.getStatus() != Status.ENABLE) {
            throw new ResourceNotFoundException("The attendee with " + globalId + " is DISABLE");
        }

        ResponseEntity<List<Feedback>> responseEntity = null;
        Sort sortByDate = Sort.by("date").descending();
        List<Feedback> feedbacks = new ArrayList<>();

        if (page != null && size != null) {

            Pageable pageable = PageRequest.of(page, size, sortByDate);
            Page<Feedback> pageFeedbacks = feedbackService.findFeedbacksByGlobalId(globalId, pageable);
            feedbacks = pageFeedbacks.getContent();
            responseEntity = new ResponseEntity<List<Feedback>>(feedbacks, HttpStatus.OK);

        } else {
            feedbacks = feedbackService.findFeedbacksByGlobalId(globalId, sortByDate);
            responseEntity = new ResponseEntity<List<Feedback>>(feedbacks, HttpStatus.OK);
        }

        return responseEntity;
    }

    // metodo para añadir feedback por globalID
    // TRAINER
    @PostMapping("/attendees/{globalId}/feedback")
    public ResponseEntity<Map<String, Object>> addFeedbackByGlobalId(
            @PathVariable(value = "globalId") Integer globalId,
            @Valid @RequestBody Feedback feedbackRequest,
            BindingResult validationResult) {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        // verificar que existe el attendee con ese globalid y esta ENABLE
        Attendee attendee = attendeeService.findByGlobalId(globalId);
        if (attendee == null) {
            throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
        } else if (attendee.getStatus() != Status.ENABLE) {
            throw new ResourceNotFoundException("The attendee with " + globalId + " is DISABLE");
        }

        // validar si el feedback tiene errores
        if (validationResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            List<ObjectError> objectErrors = validationResult.getAllErrors();

            objectErrors.forEach(objectError -> errors.add(objectError.getDefaultMessage()));

            responseAsMap.put("errors", errors);
            responseAsMap.put("Error feedback", attendee);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

            return responseEntity;
        }

        feedbackRequest.setAttendee(attendee);

        try {
            Feedback savedFeedback = feedbackService.save(feedbackRequest);
            String succesMessage = "The feedback has been saved succesfully";
            responseAsMap.put("succesMessage", succesMessage);
            responseAsMap.put("The feedback has been saved", savedFeedback);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            String errorMessage = "Error saving feedback: " + e.getMostSpecificCause();
            responseAsMap.put("errors", errorMessage);
            responseAsMap.put("Error feedback", attendee);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    // Metodo para modificar feedbacks por globalId del attendee
    // TRAINER
    @PutMapping("/attendees/{globalId}/feedback/{id}")
    public ResponseEntity<Map<String, Object>> updateFeedback(
            @PathVariable(name = "id") Integer id,
            @PathVariable(value = "globalId") Integer globalId,
            @Valid @RequestBody Feedback feedbackRequest,
            BindingResult validationResult) {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        // verificar que existe el attendee con ese globalid y esta ENABLE
        Attendee attendee = attendeeService.findByGlobalId(globalId);
        if (attendee == null) {
            throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
        } else if (attendee.getStatus() != Status.ENABLE) {
            throw new ResourceNotFoundException("The attendee with " + globalId + " is DISABLE");
        }

        // Verificar que existe el feedback
        // y verificar si el id del feedback coincide con el que le estamos pasando
        Feedback existFeedback = feedbackService.findByFeedBackId(id);
        if (existFeedback == null) {
            throw new ResourceNotFoundException("Not found Feedback with Id = " + id);
        } else if (!id.equals(existFeedback.getId())) {
            String errorMessage = "The feedback id doesn't match";
            responseAsMap.put("errorMessage", errorMessage);

            return new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);

        }

        // Verificar si el feedback pertenece al Attendee con el globalId proporcionado
        if (!existFeedback.getAttendee().equals(attendee)) {
            String errorMessage = "The feedback does not belong to the specified Attendee";
            responseAsMap.put("errorMessage", errorMessage);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
        }

        existFeedback.setLevel(feedbackRequest.getLevel());
        existFeedback.setComments(feedbackRequest.getComments());
        existFeedback.setDate(feedbackRequest.getDate());
        existFeedback.setAttendee(attendee);

        // Comprobar si feedback tiene errores
        if (validationResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            List<ObjectError> objectErrors = validationResult.getAllErrors();

            objectErrors.forEach(objectError -> errors.add(objectError.getDefaultMessage()));

            responseAsMap.put("errors", errors);
            responseAsMap.put("Error feedback", existFeedback);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

            return responseEntity;
        }

        try {
            Feedback feedbackUpdate = feedbackService.save(existFeedback);
            String succesMessage = "The feedback has been updated succesfully";
            responseAsMap.put("succesMessage", succesMessage);
            responseAsMap.put("Feedback update", feedbackUpdate);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.OK);
        } catch (DataAccessException e) {
            String error = "Error updating the feedback: " + e.getMostSpecificCause();
            responseAsMap.put("Error", error);
            responseAsMap.put("The feedback has attemped to uptade", existFeedback);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    // metodo para elimiar un feedback
    // Trainer
    @DeleteMapping("/attendees/{globalId}/feedback/{id}")
    public ResponseEntity<Map<String, Object>> deleteFeedbackByGlobalId(
            @PathVariable(name = "id") Integer id,
            @PathVariable(value = "globalId") Integer globalId) {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        // verificar que existe el attendee con ese globalid y esta ENABLE
        Attendee attendee = attendeeService.findByGlobalId(globalId);
        if (attendee == null) {
            throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
        } else if (attendee.getStatus() != Status.ENABLE) {
            throw new ResourceNotFoundException("The attendee with " + globalId + " is DISABLE");
        }

        // Verificar que existe el feedback
        // y verificar si el id del feedback coincide con el que le estamos pasando
        Feedback existFeedback = feedbackService.findByFeedBackId(id);
        if (existFeedback == null) {
            throw new ResourceNotFoundException("Not found Feedback with Id = " + id);
        } else if (!id.equals(existFeedback.getId())) {
            String errorMessage = "The feedback id doesn't match";
            responseAsMap.put("errorMessage", errorMessage);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        // Verificar si el feedback pertenece al Attendee con el globalId proporcionado
        if (!existFeedback.getAttendee().equals(attendee)) {
            String errorMessage = "The feedback does not belong to the specified Attendee";
            responseAsMap.put("errorMessage", errorMessage);
            return new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
        }

        try {
            feedbackService.delete(feedbackService.findByFeedBackId(id));
            String succesMessage = "The feedback has been deleted succesfully";
            responseAsMap.put("succesMessage", succesMessage);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.OK);
        } catch (DataAccessException e) {
            String error = "Error deleting the feedback: " + e.getMostSpecificCause();
            responseAsMap.put("Error", error);
            responseAsMap.put("The feedback has attemped to delete", existFeedback);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

}
