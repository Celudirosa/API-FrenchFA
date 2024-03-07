package com.example.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.dao.AttendeeDao;
import com.example.dao.EmailDao;
import com.example.dao.FeedbackDao;
import com.example.dao.ProfileDao;
import com.example.entities.Attendee;
import com.example.entities.Email;
import com.example.entities.Feedback;
import com.example.entities.Level;
import com.example.entities.Perfil;
import com.example.entities.Profile;
import com.example.entities.Status;
import com.example.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class AttendeeServiceTests {
    @Mock
    private AttendeeDao attendeeDao;

    @Mock
    private ProfileDao profileDao;

    @Mock
    private FeedbackDao feedbackDao;

    @Mock // simulacion
    private EmailDao emailDao;

    @InjectMocks // le inyecto el servicio a lo que se simula
    private AttendeeServiceImpl attendeeService;

    private Attendee attendee;

    @BeforeEach
    void setUp() {

        Profile profile = Profile.builder()
                .id(20)
                .profile(Perfil.BOOTCAMP)
                .build();

        attendee = Attendee.builder()
                .id(20)
                .firstName("NameTest")
                .surname("SurnameTest")
                .globalId(00000000)
                .initialLevel(Level.A0)
                .status(Status.ENABLE)
                .profile(profile)
                .build();

        List<Email> emails = new ArrayList<>();

        Email email = Email.builder()
                .email("test@blue.com")
                .attendee(attendee)
                .build();

        emailDao.save(email);
        emails.add(email);
        attendee.setEmails(emails);

        List<Feedback> feedbacks = new ArrayList<>();

        Feedback feedback = Feedback.builder()
                .Level(Level.A0)
                .date(LocalDate.of(2024, 1, 01))
                .comments("Comment Test")
                .build();

        feedbacks.add(feedback);
        attendee.setFeedbacks(feedbacks);
        attendeeDao.save(attendee);
        // attendee.setLastLevel(attendeeService.getLastLevel(attendee));

    }

    // This test is not assesing the saving of lastLevel
    @Test
    @DisplayName("Service test to persist an attendee")
    public void testSaveAttendee() {

        // given
        given(attendeeDao.save(attendee)).willReturn(attendee);

        // when
        Attendee attendeeSaved = attendeeDao.save(attendee);

        // then
        assertThat(attendeeSaved).isNotNull(); // To verify if the saved attendee is not null
        assertEquals(attendee, attendeeSaved); // Check if the saved attendee is equal to the original attendee

    }

    @DisplayName("Retrieve an empty list of products")
    @Test
    public void testEmptyAttendeeList() {

        // given
        given(attendeeDao.findAll()).willReturn(Collections.emptyList());

        // when
        List<Attendee> attendes = attendeeDao.findAll();

        // then
        assertThat(attendes).isEmpty();
    }

/*     @Test
    @DisplayName("Retrieve a user by ID.")
    public void findUserById() {

        // given
        attendeeDao.save(attendee);

        // when
        Attendee attendeeFound = attendeeService.findByGlobalId(11122233);

        // then
        assertThat(attendeeFound.getGlobalId()).isNotEqualTo(0);

    } */

}
