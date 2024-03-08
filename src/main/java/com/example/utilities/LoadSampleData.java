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
                .profile("BOOTCAMP")
                .build());

            profileService.save(Profile.builder()
                .profile("INTERNAL")
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

            // Feedbacks para attendee2 DISABLE
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

            // Feedbacks attendee3
            List<Feedback> feedbacksAttendee3 = new ArrayList<>();

            Feedback feedback1Attendee3 = Feedback.builder()
                .level(Level.C1)
                .attendee(attendee3)
                .date(LocalDate.of(2024, 1, 01))
                .comments("She speaks well")
                .build();

            Feedback feedback2Attendee3 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee3)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has improved")
                .build();
            
            Feedback feedback3Attendee3 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee3)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has advances")
                .build();

            feedbackService.save(feedback1Attendee3);
            feedbackService.save(feedback2Attendee3);
            feedbackService.save(feedback3Attendee3);

            feedbacksAttendee3.add(feedback1Attendee3);
            feedbacksAttendee3.add(feedback2Attendee3);
            feedbacksAttendee3.add(feedback3Attendee3);

            // Feedbacks attendee4
            List<Feedback> feedbacksAttendee4 = new ArrayList<>();

            Feedback feedback1Attendee4 = Feedback.builder()
                .level(Level.C1)
                .attendee(attendee4)
                .date(LocalDate.of(2024, 1, 01))
                .comments("She speaks very well")
                .build();

            Feedback feedback2Attendee4 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee4)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has improved")
                .build();
            
            Feedback feedback3Attendee4 = Feedback.builder()
                .level(Level.C2)
                .attendee(attendee4)
                .date(LocalDate.of(2024, 2, 01))
                .comments("She has advances")
                .build();

            feedbackService.save(feedback1Attendee4);
            feedbackService.save(feedback2Attendee4);
            feedbackService.save(feedback3Attendee4);

            feedbacksAttendee4.add(feedback1Attendee4);
            feedbacksAttendee4.add(feedback2Attendee4);
            feedbacksAttendee4.add(feedback3Attendee4);

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
            attendee2.setFeedbacks(feedbacksAttendee2);
            attendee3.setFeedbacks(feedbacksAttendee3);
            attendee4.setFeedbacks(feedbacksAttendee4);
            attendee5.setFeedbacks(feedbacksAttendee5);
            attendee1.setLastLevel(attendeeService.getLastLevel(attendee1));
            attendee2.setLastLevel(attendeeService.getLastLevel(attendee2));
            attendee3.setLastLevel(attendeeService.getLastLevel(attendee3));
            attendee4.setLastLevel(attendeeService.getLastLevel(attendee4));
            attendee5.setLastLevel(attendeeService.getLastLevel(attendee5));


            // Add email attendee1

            List<Email> emailsAttendee1 = new ArrayList<>();

            Email email1Attendee1 = Email.builder()
                .email("isabel@blue.com")
                .attendee(attendee1)
                .build();

            emailService.save(email1Attendee1);
            emailsAttendee1.add(email1Attendee1);

            // Add email attendee2

            List<Email> emailsAttendee2 = new ArrayList<>();

            Email email1Attendee2 = Email.builder()
                .email("andrea@blue.com")
                .attendee(attendee5)
                .build();

            emailService.save(email1Attendee2);
            emailsAttendee2.add(email1Attendee2);

            // Add email attendee3

            List<Email> emailsAttendee3 = new ArrayList<>();

            Email email1Attendee3 = Email.builder()
                .email("coty@blue.com")
                .attendee(attendee3)
                .build();

            emailService.save(email1Attendee3);
            emailsAttendee3.add(email1Attendee3);

            // Add email attendee4

            List<Email> emailsAttendee4 = new ArrayList<>();

            Email email1Attendee4 = Email.builder()
                .email("celudirosa@blue.com")
                .attendee(attendee4)
                .build();

            emailService.save(email1Attendee4);
            emailsAttendee4.add(email1Attendee4);


            // Add email attendee5

            List<Email> emailsAttendee5 = new ArrayList<>();

            Email email1Attendee5 = Email.builder()
                .email("anita@blue.com")
                .attendee(attendee5)
                .build();

            emailService.save(email1Attendee5);
            emailsAttendee5.add(email1Attendee5);

        };

    }

}