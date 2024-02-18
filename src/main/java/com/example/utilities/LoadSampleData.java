package com.example.utilities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.entities.Attendee;
import com.example.entities.Feedback;
import com.example.entities.Level;
import com.example.entities.Profile;
import com.example.entities.Status;
import com.example.services.AttendeeService;
import com.example.services.EmailService;
import com.example.services.FeedbackService;
import com.example.services.ProfileService;

@Configuration
public class LoadSampleData {

    @Bean
    public CommandLineRunner saveSampleData(AttendeeService attendeeService, ProfileService profileService, FeedbackService feedbackService, EmailService emailService ) {

        return data -> { 
            //Profiles
            profileService.save(Profile.builder()
                .profile("Bootcamp")
                .build()
            );

            profileService.save(Profile.builder()
                .profile("Internal")
                .build()
            );

            //Feedbacks
            List<Feedback> feedbacksConstanza= new ArrayList<>();

		Feedback feedback1Constanza = Feedback.builder()
				.Level(Level.B1)
                .date(LocalDate.of(2024, 1, 23))
                .comments("She have participed correctly")
				.build();

                feedbackService.save(feedback1Constanza);


         Feedback feedback2Constanza = Feedback.builder()
				.Level(Level.B2)
                .date(LocalDate.of(2024, 2, 5))
                .comments("She have participed better")
				.build();

                feedbackService.save(feedback2Constanza);

       feedbacksConstanza.add(feedback1Constanza);
       feedbacksConstanza.add(feedback2Constanza);
       
       Feedback lastLevelConstanza = Collections.max(feedbacksConstanza, Comparator.comparing(Feedback::getDate));
         lastLevelConstanza = feedbacksConstanza.get(0);
        

          

            //Attendees
           Attendee attendee1 = attendeeService.save(Attendee.builder()
                .firstName("Isabel")
                .surname("Alvarez")
                .globalId(12345678)
                .initialLevel(Level.A2)
                .status(Status.ENABLE)
                .profile(profileService.findById(1))
                .build()
            );

            Attendee attendee2 = attendeeService.save(Attendee.builder()
                .firstName("Andrea")
                .surname("Serge")
                .globalId(12345679)
                .initialLevel(Level.B2)
                .status(Status.ENABLE)
                .profile(profileService.findById(2))
                .build()
            );

            Attendee attendee3 =  attendeeService.save(Attendee.builder()
            .firstName("Constanza")
            .surname("Arnau")
            .globalId(12345670)
            .initialLevel(Level.B1)
            .status(Status.ENABLE)
            .profile(profileService.findById(1))
            .lastLevel(lastLevelConstanza.getLevel())
            .build()
            );

            Attendee attendee4 = attendeeService.save(Attendee.builder()
            .firstName("Celia")
            .surname("Luque")
            .globalId(12345671)
            .initialLevel(Level.C1)
            .status(Status.ENABLE)
            .profile(profileService.findById(2))
            .build()
            );

        attendee4.setFeedbacks(feedbacksConstanza);
        attendee4.setLastLevel(attendeeService.getLastLevelForAttendee(attendee4));
        };
    }
}
