package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/attendees/{globalId}/feedbacks")
    public ResponseEntity<Feedback> addFeedbackByGlobalId(
        @PathVariable(value = "globalId") Integer globalId,
        @RequestBody Feedback feedbackRequest) {

            Attendee attendee = attendeeService.findByGlobalId(globalId);
            if (attendee == null) {
                throw new ResourceNotFoundException("Not found Attendee with GlobalId = " + globalId);
            }

            feedbackRequest.setAttendee(attendee);

            Feedback saveFeedback = feedbackService.saveFeedback(feedbackRequest);

            return new ResponseEntity<>(saveFeedback, HttpStatus.CREATED);
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
