package com.example.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.data.domain.Sort;

import com.example.entities.Attendee;
import com.example.entities.Email;
import com.example.entities.Feedback;
import com.example.entities.Level;
import com.example.entities.Profile;
import com.example.entities.Status;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AttendeeServiceTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private AttendeeService attendeeService;

    private Attendee attendeeTest;

    @BeforeEach
    void setUp() {

        // Creo un Attende con su feedback

        profileService.save(Profile.builder()
                .id(1)
                .profile("Internal")
                .build());

        attendeeTest = Attendee.builder()
                .firstName("NameTest")
                .surname("SurnameTest")
                .globalId(00000000)
                .initialLevel(Level.A0)
                .status(Status.ENABLE)
                .profile(profileService.findById(1))
                .build();

        Email email = Email.builder()
                .email("test@thefutureyouwant.com")
                .attendee(attendeeTest)
                .build();

        Feedback feedback = Feedback.builder()
                .Level(Level.A1)
                .attendee(attendeeTest)
                .date(LocalDate.of(2024, 1, 01))
                .comments("Comment Test")
                .build();

        attendeeTest.setLastLevel(attendeeService.getLastLevel(attendeeTest));

        attendeeService.save(attendeeTest);

    }

    @Test
    @DisplayName("Service test to persist an attendee")
    public void testSaveAttendee() {

        // given
        given(attendeeService.save(attendeeTest)).willReturn(attendeeTest);

        // when
        Attendee attendeeSaved = attendeeService.save(attendeeTest);

        // then
        assertThat(attendeeSaved).isNotNull();
    }

    // ASK: tengo que crear una lista para que se vuelva vacia? Creo que no, pero bueno
    @DisplayName("Recovers an empty list of attendees")
    @Test
    public void testEmptyAttendeeList() {

        // given
        given(attendeeService.findAll(null)).willReturn(Collections.emptyList());

        // when
        List<Attendee> attendees = attendeeService.findAll(null);

        // then
        assertThat(attendees).isEmpty();
    }

}
