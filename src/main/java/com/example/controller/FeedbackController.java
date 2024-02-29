package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Binding;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.example.entities.Feedback;
import com.example.exception.ResourceNotFoundException;
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

    // Metodo para sacar todos los feedbacks
    @GetMapping("/all-feedbacks")
    public ResponseEntity<List<Feedback>> findAllFeedbacks(
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "size", required = false) Integer size) {

            ResponseEntity<List<Feedback>> responseEntity = null;
            Sort sortByDate = Sort.by("date").descending();
            List<Feedback> feedbacks = new ArrayList<>();

            if (page != null && size != null) {
                
                Pageable pageable = PageRequest.of(page, size, sortByDate);
                Page<Feedback> pageFeedbacks = feedbackService.findAll(pageable);
                feedbacks = pageFeedbacks.getContent();
                responseEntity = new ResponseEntity<List<Feedback>>(feedbacks, HttpStatus.OK);

            } else {
                feedbacks = feedbackService.findAll(sortByDate);
                responseEntity = new ResponseEntity<List<Feedback>>(feedbacks, HttpStatus.OK);
            }

            return responseEntity;
    }

    // Método para sacar los feedbacks por globalId 
    @GetMapping("/attendees/{globalId}/feedbacks")
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

    @PostMapping("/attendees/{globalId}/feedback")
    public ResponseEntity<Map<String, Object>> addFeedbackByGlobalId(
        @PathVariable(value = "globalId") Integer globalId,
        @Valid
        @RequestBody Feedback feedbackRequest,
        BindingResult validationResult) {
        
            Map<String, Object> responseAsMap = new HashMap<>();
            ResponseEntity<Map<String, Object>> responseEntity = null;

            Attendee attendee = attendeeService.findByGlobalId(globalId);
            if (attendee == null) {
                throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
            }

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




    




    // Metodo para modificar feedbacks
    // @PutMapping("/{globalId}/{id}")
    // public ResponseEntity<Feedback> updateFeedback(
    //     @PathVariable("id") int id,
    //     @RequestBody Feedback feedbackRequest) {
 
    //         Feedback feedback = feedbackService.findById(id).orElseThrow(
    //             () -> new ResourceNotFoundException("PresentacionId " + id + " not found"));
 
    //         presentacion.setName(presentacionRequest.getName());
 
    //         return new ResponseEntity<>(feedbackService.save(presentacion), HttpStatus.OK);
 
    // }


}
