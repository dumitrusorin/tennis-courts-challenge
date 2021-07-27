package com.tenniscourts.guests;

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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TennisCourtApplication.class)
@WebAppConfiguration
public class GuestControllerTest {

    private static final String URL = "/guest";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testAddGuest() throws Exception {
        mockMvc.perform(post(URL + "?name=Guest Name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddGuest_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(URL + "?name=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Guest must have name.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testFindGuestById() throws Exception {
        mockMvc.perform(get(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindGuestById_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Guest not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testFindByName() throws Exception {
        mockMvc.perform(get(URL + "/byName/Roger Federer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByName_fail_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL + "/byName/RogerFederer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Guest not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(URL + "/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_fail_integrity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("This guest has history and cannot be deleted.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testDelete_fail_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(URL + "/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Guest not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testUpdate() throws Exception {
        mockMvc.perform(put(URL + "?id=4&name=Venus Williams")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_fail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put(URL + "?id=9&name=Venus Williams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
        assertEquals("Guest not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
