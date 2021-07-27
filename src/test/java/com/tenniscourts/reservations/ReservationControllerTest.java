package com.tenniscourts.reservations;

import com.tenniscourts.TennisCourtApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TennisCourtApplication.class)
@WebAppConfiguration
public class ReservationControllerTest {

    private static final String URL = "/reservation";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testBookReservation() throws Exception {
        MvcResult result = mockMvc.perform(post(URL + "?guestId=1&scheduleId=4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertNotNull(result);
    }

    @Test
    void testBookReservation_fail() throws Exception {
        mockMvc.perform(post(URL + "?guestId=12&scheduleId=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testFindReservation() throws Exception {
        mockMvc.perform(get(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindReservation_fail() throws Exception {
        mockMvc.perform(get(URL + "/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCancelReservation() throws Exception {
        MvcResult result = mockMvc.perform(delete(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(result);
    }

    @Test
    void testCancelReservation_fail() throws Exception {
        mockMvc.perform(delete(URL + "/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void testRescheduleReservation() throws Exception {
        MvcResult result = mockMvc.perform(delete(URL + "/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertNotNull(result);
    }

    @Test
    void testRescheduleReservation_fail() throws Exception {
        mockMvc.perform(delete(URL + "/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testFindPastReservations() throws Exception {
        mockMvc.perform(get(URL + "/pastReservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

/*    @Test
    void testRefundDeposit(ReservationStatus status, BigDecimal refund) {
        mockMvc.perform(get(URL+"/refund/1"));
    }*/

}
