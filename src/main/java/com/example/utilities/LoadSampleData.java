package com.example.utilities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.entities.Attendee;
import com.example.entities.Email;
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
    public CommandLineRunner saveSampleData(AttendeeService attendeeService, ProfileService profileService,
            FeedbackService feedbackService, EmailService emailService) {

        return data -> {

            // Profiles
            profileService.save(Profile.builder()
                    .profile("Bootcamp")
                    .build());

            profileService.save(Profile.builder()
                    .profile("Internal")
                    .build());

            // Feedbacks Bootcamper
            List<Feedback> feedbacksBootcamper1 = new ArrayList<>();

            Feedback feedback1Bootcamper1 = Feedback.builder()
                    .Level(Level.C1)
                    .date(LocalDate.of(2024, 1, 01))
                    .comments("She speaks very well")
                    .build();

            Feedback feedback2Bootcamper1 = Feedback.builder()
                    .Level(Level.C2)
                    .date(LocalDate.of(2024, 2, 01))
                    .comments("She has improved")
                    .build();

            feedbackService.save(feedback1Bootcamper1);
            feedbackService.save(feedback2Bootcamper1);

            feedbacksBootcamper1.add(feedback1Bootcamper1);
            feedbacksBootcamper1.add(feedback2Bootcamper1);

            // Feedback internal
            List<Feedback> feedbacksIntern1 = new ArrayList<>();

            Feedback feedback1Intern1 = Feedback.builder()
                    .Level(Level.A2)
                    .date(LocalDate.of(2024, 1, 01))
                    .comments("She shows an initial level")
                    .build();

            Feedback feedback2Intern1 = Feedback.builder()
                    .Level(Level.A2)
                    .date(LocalDate.of(2024, 2, 01))
                    .comments("She does not shows an improvement")
                    .build();

            Feedback feedback3Intern1 = Feedback.builder()
                    .Level(Level.B1)
                    .date(LocalDate.of(2024, 3, 01))
                    .comments("She has improved")
                    .build();

            feedbackService.save(feedback1Intern1);
            feedbackService.save(feedback2Intern1);
            feedbackService.save(feedback3Intern1);

            feedbacksIntern1.add(feedback1Intern1);
            feedbacksIntern1.add(feedback2Intern1);
            feedbacksIntern1.add(feedback3Intern1);

            // Attendees
            Attendee attendee1 = attendeeService.save(Attendee.builder()
                    .firstName("Isabel")
                    .surname("Alvarez")
                    .globalId(12345678)
                    .initialLevel(Level.A2)
                    .status(Status.ENABLE)
                    .profile(profileService.findById(1))
                    .build());

            Attendee attendee2 = attendeeService.save(Attendee.builder()
                    .firstName("Andrea")
                    .surname("Serge")
                    .globalId(12345679)
                    .initialLevel(Level.B2)
                    .status(Status.DISABLE)
                    .profile(profileService.findById(1))
                    .build());

            Attendee attendee3 = attendeeService.save(Attendee.builder()
                    .firstName("Constanza")
                    .surname("Arnau")
                    .globalId(12345670)
                    .initialLevel(Level.B1)
                    .status(Status.ENABLE)
                    .profile(profileService.findById(1))
                    .lastLevel(null)
                    .build());

            Attendee attendee4 = attendeeService.save(Attendee.builder()
                    .firstName("Celia")
                    .surname("Luque")
                    .globalId(12345671)
                    .initialLevel(Level.C1)
                    .status(Status.ENABLE)
                    .profile(profileService.findById(1))
                    .lastLevel(null)
                    .build());

            Attendee attendee5 = attendeeService.save(Attendee.builder()
                    .firstName("Ana")
                    .surname("Ana")
                    .globalId(12345688)
                    .initialLevel(Level.A0)
                    .status(Status.ENABLE)
                    .profile(profileService.findById(2))
                    .build());

            attendee4.setFeedbacks(feedbacksBootcamper1);
            attendee5.setFeedbacks(feedbacksIntern1);
            attendee5.setLastLevel(attendeeService.getLastLevel(attendee5));


            // Add correos
            List<Email> emailsBootcamper1 = new ArrayList<>();

            Email email1Bootcamper1 = Email.builder()
                    .email("andrea@serge.com")
                    .attendee(attendee5)
                    .build();

            emailService.save(email1Bootcamper1);
            emailsBootcamper1.add(email1Bootcamper1);

        };
    }

}