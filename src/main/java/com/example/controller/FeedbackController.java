package com.example.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Feedback;
import com.example.services.FeedbackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // Metodo para sacar todos los feedbacks
    @GetMapping
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


}
