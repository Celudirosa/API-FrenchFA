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
import com.example.entities.Profile;
import com.example.entities.Status;

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

    @Mock
    private EmailDao emailDao;

    @InjectMocks
    private AttendeeServiceImpl attendeeService;

    private Attendee attendee;

    @BeforeEach
    void setUp() {

        Profile profile = Profile.builder()
                .profile("Internal")
                .build();

        attendee = Attendee.builder()
                .id(1)
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
                .level(Level.A0)
                .date(LocalDate.of(2024, 1, 01))
                .comments("Comment Test")
                .build();

        // feedbackDao.save(feedback);
        // feedbacks.add(feedback);

        feedbacks.add(feedback);
        attendee.setFeedbacks(feedbacks);
        attendee.setLastLevel(attendeeService.getLastLevel(attendee));
        // feedback.setAttendee(attendee);
        

        // attendeeService.save(attendee);

    }

    @Test
    @DisplayName("Service test to persist an attendee")
    public void testSaveAttendee() {

        // given
        given(attendeeService.save(attendee)).willReturn(attendee);

        // when
        Attendee attendeeSaved = attendeeService.save(attendee);

        // then
        assertThat(attendeeSaved).isNotNull(); // To verify if the saved attendee is not null
        // assertEquals(attendee, attendeeSaved); // Check if the saved attendee is
        // equal to the original attendee

        // assertThrows(IllegalArgumentException.class, () -> {
        // attendeeService.save(null);
        // }); // Check if an exception is thrown when attempting to save a null
        // attendee

        // To verify if different data types are correctly saved into the attendee
        // attendeeSaved.setFirstName(""); // Empty string
        // attendeeSaved.setSurname(""); // Empty string
        // attendeeSaved.setGlobalId(0);; // Empty int
        // attendeeSaved.setEmails(null); // Null
        // attendeeSaved.setInitialLevel(null);
        // attendeeSaved.setStatus(null); // Valor entero 0
        // attendeeSaved.setProfile(null);
        // attendeeSaved.setFeedbacks(null);
        // attendeeSaved.setId(0);

    }

    @DisplayName("Recupera una lista vacia de productos")
    @Test
    public void testEmptyProductList() {

        // given
        given(productoDao.findAll()).willReturn(Collections.emptyList());

        // when
        List<Producto> productos = productoDao.findAll();

        // then
        assertThat(productos).isEmpty();
    }
}
