package com.tenniscourts.schedules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.TennisCourtApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TennisCourtApplication.class)
@WebAppConfiguration
public class ScheduleControllerTest {

    private static final String URL = "/schedule";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // TODO: 25-Jul-21 error parsing date
    @Test
    void testAddScheduleTennisCourt() throws Exception {
        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.of(2002, 12, 20, 19, 0, 0));
        createScheduleRequestDTO.setTennisCourtId(1L);


        MvcResult result = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(createScheduleRequestDTO)))
                .andExpect(status().isCreated()).andReturn();
        assertNotNull(result);
    }

    @Test
    void testAddScheduleTennisCourt_fail() throws Exception {
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testFindSchedulesByDates() throws Exception {
        mockMvc.perform(get(URL + "/byDates?startDate=2020-12-20&endDate=2020-12-20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindSchedulesByDates_fail() throws Exception {
        mockMvc.perform(get(URL + "/byDates?startDate=2020-12-20&endDate=2020-12-20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByScheduleId() throws Exception {
        mockMvc.perform(get(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByScheduleId_fail() throws Exception {
        mockMvc.perform(get(URL + "/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}