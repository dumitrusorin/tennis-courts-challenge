package com.tenniscourts.tenniscourts;

import com.tenniscourts.TennisCourtApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.core.MediaType;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TennisCourtApplication.class)
@WebAppConfiguration
public class TennisCourtControllerTest {

    private static final String URL = "/tennisCourt";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testAddTennisCourt() throws Exception {

        mockMvc.perform(post(URL + "?name=courtName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddTennisCourt_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(URL + "?name=")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage(), "Court name cannot be null.");
    }

    @Test
    void testFindTennisCourtById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id\":1"));
    }

    @Test
    void testFindTennisCourtById_noValue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/10")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage(), "Tennis Court not found.");
    }

    @Test
    void testFindTennisCourtWithSchedulesById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/withSchedules/1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"id\":1"));
    }

    @Test
    void testFindTennisCourtWithSchedulesById_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/withSchedules/10")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage(), "Tennis Court not found.");
    }


}
