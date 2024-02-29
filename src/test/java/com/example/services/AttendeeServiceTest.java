package com.example.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.entities.Attendee;
import com.example.entities.Feedback;
import com.example.entities.Level;
import com.example.entities.Profile;
import com.example.entities.Status;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AttendeeServiceTest {

    @Mock
    private AttendeeService attendeeService;

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private ProfileService profileService;

    private Attendee attendeeTest;

    @BeforeEach
    void setUp() {

        // Creo un Attende con su feedback

        profileService.save(Profile.builder()
                .profile("Internal")
                .build());

        Attendee attendeeTest = attendeeService.save(Attendee.builder()
                .firstName("NameTest")
                .surname("SurnameTest")
                .globalId(00000000)
                .initialLevel(Level.A0)
                .status(Status.ENABLE)
                .profile(profileService.findById(1))
                .build());

        Feedback feedbackTest = Feedback.builder()
                .Level(Level.A1)
                .attendee(attendeeTest)
                .date(LocalDate.of(2024, 1, 01))
                .comments("Comment Test")
                .build();

        feedbackService.save(feedbackTest);                

    }

    @Test
    @DisplayName("Test de servicio para persistir un attendee")
    public void testSaveAttendee() {

        // given
        given(attendeeService.save(attendeeTest)).willReturn(attendeeTest);

        // when
        Attendee attendeeSaved = attendeeService.save(attendeeTest);

        // then
        assertThat(attendeeSaved).isNull();
    }

}
