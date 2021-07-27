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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
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
        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.of(2020, 12, 20, 20, 0, 0));
        createScheduleRequestDTO.setTennisCourtId(1L);

        MvcResult mvcResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(createScheduleRequestDTO)))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("The schedule already exists.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testFindSchedulesByDates() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/byDates?startDate=2020-12-20&endDate=2020-12-20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals("[]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testFindSchedulesByDates_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/byDates?startDate=2020-12-20&endDate=2020-12-20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals("[]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testFindByScheduleId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id\":1"));
    }

    @Test
    void testFindByScheduleId_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id\":null"));
    }
}