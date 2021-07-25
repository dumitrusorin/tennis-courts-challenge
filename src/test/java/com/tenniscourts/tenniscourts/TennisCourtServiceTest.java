package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.IScheduleService;
import com.tenniscourts.schedules.ScheduleDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TennisCourtServiceTest {

    @InjectMocks
    private TennisCourtService service;

    @Mock
    private TennisCourtRepository repository;

    @Mock
    private TennisCourtMapper mapper;

    @Mock
    private IScheduleService scheduleService;

    @AfterEach
    void tearDown(){
        verifyNoMoreInteractions(repository, mapper, scheduleService);
    }

    @Test
    void testAddTennisCourt() {
        String court_name = "Court Name";
        TennisCourtDTO tennisCourtDTO = TennisCourtDTO.builder().name(court_name).build();
        TennisCourt tennisCourt = new TennisCourt();

        when(repository.findByName(anyString())).thenReturn(Optional.empty());
        when(mapper.map(any(TennisCourtDTO.class))).thenReturn(tennisCourt);
        when(repository.saveAndFlush(any(TennisCourt.class))).thenReturn(tennisCourt);
        when(mapper.map(any(TennisCourt.class))).thenReturn(tennisCourtDTO);

        TennisCourtDTO response = service.addTennisCourt(tennisCourtDTO);
        assertEquals(tennisCourtDTO, response);

        verify(repository).findByName(court_name);
        verify(mapper).map(tennisCourtDTO);
        verify(repository).saveAndFlush(tennisCourt);
        verify(mapper).map(tennisCourt);
    }

    @Test
    void testAddTennisCourt_exception() {
        String court_name = "Court Name";
        TennisCourtDTO tennisCourtDTO = TennisCourtDTO.builder().name(court_name).build();
        TennisCourt tennisCourt = new TennisCourt();

        when(repository.findByName(anyString())).thenReturn(Optional.of(tennisCourt));

        assertThrows(IllegalStateException.class, ()-> service.addTennisCourt(tennisCourtDTO));

        verify(repository).findByName(court_name);
    }

    @Test
    void testFindTennisCourtById() {
        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();
        TennisCourt tennisCourt = new TennisCourt();

        when(mapper.map(any(TennisCourt.class))).thenReturn(tennisCourtDTO);
        when(repository.findById(anyLong())).thenReturn(Optional.of(tennisCourt));

        TennisCourtDTO response = service.findTennisCourtById(1L);
        assertEquals(tennisCourtDTO, response);

        verify(mapper).map(tennisCourt);
        verify(repository).findById(1L);
    }

    @Test
    void testFindTennisCourtById_exception() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findTennisCourtById(1L));

        verify(repository).findById(1L);
    }

    @Test
    void testFindTennisCourtWithSchedulesById_noSchedules() {
        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();
        TennisCourt tennisCourt = new TennisCourt();

        when(mapper.map(any(TennisCourt.class))).thenReturn(tennisCourtDTO);
        when(repository.findById(anyLong())).thenReturn(Optional.of(tennisCourt));
        when(scheduleService.findSchedulesByTennisCourtId(anyLong())).thenReturn(new ArrayList<>());

        TennisCourtDTO response = service.findTennisCourtWithSchedulesById(1L);
        assertNotNull(response);
        assertTrue(response.getTennisCourtSchedules().isEmpty());

        verify(mapper).map(tennisCourt);
        verify(repository).findById(1L);
        verify(scheduleService).findSchedulesByTennisCourtId(1L);
    }

    @Test
    void testFindTennisCourtWithSchedulesById_exception() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> service.findTennisCourtWithSchedulesById(1L));

        verify(repository).findById(1L);
    }

    @Test
    void testFindTennisCourtWithSchedulesById() {
        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();
        TennisCourt tennisCourt = new TennisCourt();

        when(mapper.map(any(TennisCourt.class))).thenReturn(tennisCourtDTO);
        when(repository.findById(anyLong())).thenReturn(Optional.of(tennisCourt));
        when(scheduleService.findSchedulesByTennisCourtId(anyLong())).thenReturn(Collections.singletonList(new ScheduleDTO()));

        TennisCourtDTO response = service.findTennisCourtWithSchedulesById(1L);
        assertNotNull(response);
        assertEquals(1, response.getTennisCourtSchedules().size());

        verify(mapper).map(tennisCourt);
        verify(repository).findById(1L);
        verify(scheduleService).findSchedulesByTennisCourtId(1L);
    }

}
