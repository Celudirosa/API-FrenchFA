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
import com.example.entities.Perfil;
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
                .profile(Perfil.BOOTCAMP)
                .build());

            profileService.save(Profile.builder()
                .profile(Perfil.INTERNAL)
                .build());

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
                .build());

            Attendee attendee4 = attendeeService.save(Attendee.builder()
                .firstName("Celia")
                .surname("Luque")
                .globalId(12345671)
                .initialLevel(Level.C1)
                .status(Status.ENABLE)
                .profile(profileService.findById(1))
                .build());

            Attendee attendee5 = attendeeService.save(Attendee.builder()
                .firstName("Ana")
                .surname("Ana")
                .globalId(12345688)
                .initialLevel(Level.A0)
                .status(Status.ENABLE)
                .profile(profileService.findById(2))
                .build());

            // Feedbacks 
            List<Feedback> feedbacksAttendee1 = new ArrayList<>();

            Feedback feedback1Attendee1 = Feedback.builder()
                .level(Level.C1)
                .attendee(attendee1)
                .date(LocalDate.of(2024, 1, 01))
                .comments("She speaks very well")
                .build();

            Feedback feedback2Attendee1 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee1)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has improved")
                .build();
            
            Feedback feedback3Attendee1 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee1)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has advances")
                .build();

            feedbackService.save(feedback1Attendee1);
            feedbackService.save(feedback2Attendee1);
            feedbackService.save(feedback3Attendee1);

            feedbacksAttendee1.add(feedback1Attendee1);
            feedbacksAttendee1.add(feedback2Attendee1);
            feedbacksAttendee1.add(feedback3Attendee1);

            // Feedbacks para attendee DISABLE
            List<Feedback> feedbacksAttendee2 = new ArrayList<>();

            Feedback feedback1Attendee2 = Feedback.builder()
                .level(Level.C1)
                .attendee(attendee2)
                .date(LocalDate.of(2024, 1, 01))
                .comments("She speaks very well")
                .build();

            Feedback feedback2Attendee2 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee2)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has improved")
                .build();
            
            Feedback feedback3Attendee2 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee2)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has advances")
                .build();

            feedbackService.save(feedback1Attendee2);
            feedbackService.save(feedback2Attendee2);
            feedbackService.save(feedback3Attendee2);

            feedbacksAttendee2.add(feedback1Attendee2);
            feedbacksAttendee2.add(feedback2Attendee2);
            feedbacksAttendee2.add(feedback3Attendee2);

            // Feedback internal
            List<Feedback> feedbacksAttendee5 = new ArrayList<>();

            Feedback feedback1Attendee5 = Feedback.builder()
                .level(Level.A2)
                .attendee(attendee5)
                .date(LocalDate.of(2024, 1, 01))
                .comments("She shows an initial level")
                .build();

            Feedback feedback2Attendee5 = Feedback.builder()
                .level(Level.A2)
                .attendee(attendee5)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She does not shows an improvement")
                .build();

            Feedback feedback3Attendee5 = Feedback.builder()
                .level(Level.B1)
                .attendee(attendee5)
                .date(LocalDate.of(2024, 3, 01))
                .comments("She has improved")
                .build();

            feedbackService.save(feedback1Attendee5);
            feedbackService.save(feedback2Attendee5);
            feedbackService.save(feedback3Attendee5);

            feedbacksAttendee5.add(feedback1Attendee5);
            feedbacksAttendee5.add(feedback2Attendee5);
            feedbacksAttendee5.add(feedback3Attendee5);
            attendee1.setFeedbacks(feedbacksAttendee1);
            attendee5.setFeedbacks(feedbacksAttendee5);
            attendee2.setFeedbacks(feedbacksAttendee2);
            attendee5.setLastLevel(attendeeService.getLastLevel(attendee5));
            attendee1.setLastLevel(attendeeService.getLastLevel(attendee1));
            attendee2.setLastLevel(attendeeService.getLastLevel(attendee2));


            // Add email

            List<Email> emailsAttendee5 = new ArrayList<>();

            Email email1Attendee5 = Email.builder()
                .email("andrea@blue.com")
                .attendee(attendee5)
                .build();

            emailService.save(email1Attendee5);
            emailsAttendee5.add(email1Attendee5);

        };

    }

}