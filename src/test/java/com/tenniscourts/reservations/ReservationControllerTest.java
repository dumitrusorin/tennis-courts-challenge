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
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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
        MvcResult mvcResult = mockMvc.perform(post(URL + "?guestId=12&scheduleId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Guest not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testFindReservation() throws Exception {
        mockMvc.perform(get(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindReservation_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Reservation not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testCancelReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id\":1"));
    }

    @Test
    void testCancelReservation_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(URL + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Reservation not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
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
        MvcResult mvcResult = mockMvc.perform(delete(URL + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Reservation not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testFindPastReservations() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/pastReservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id\":3"));
    }

    @Test
    void testRefundDeposit_none() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/refund/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals("0", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testRefundDeposit_full() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/refund/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals("10", mvcResult.getResponse().getContentAsString());
    }

}
