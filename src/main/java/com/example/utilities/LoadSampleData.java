package com.example.utilities;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.entities.Attendee;
import com.example.entities.Level;
import com.example.entities.Profile;
import com.example.entities.Status;
import com.example.services.AttendeeService;
import com.example.services.ProfileService;

@Configuration
public class LoadSampleData {

    @Bean
    public CommandLineRunner saveSampleData(AttendeeService attendeeService, ProfileService profileService) {

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

            //Attendees
            attendeeService.save(Attendee.builder()
                .firstName("Isabel")
                .surname("Alvarez")
                .globalId(12345678)
                .initialLevel(Level.A2)
                .status(Status.ENABLE)
                .profile(profileService.findById(1))
                .build()
            );

            attendeeService.save(Attendee.builder()
                .firstName("Andrea")
                .surname("Serge")
                .globalId(12345679)
                .initialLevel(Level.B2)
                .status(Status.ENABLE)
                .profile(profileService.findById(2))
                .build()
            );
        };
    }
}
