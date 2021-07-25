package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService service;

    @Mock
    private ScheduleRepository repository;

    @Mock
    private TennisCourtRepository tennisCourtRepository;

    @Mock
    private ScheduleMapper mapper;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, tennisCourtRepository, mapper);
    }

    @Test
    void testAddSchedule_noCourtException() {

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setTennisCourtId(1L);

        when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.addSchedule(createScheduleRequestDTO));

        verify(tennisCourtRepository).findById(1L);
    }

    @Test
    void testAddSchedule_duplicateScheduleException() {

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setTennisCourtId(1L);
        LocalDateTime now = LocalDateTime.now();
        createScheduleRequestDTO.setStartDateTime(now);

        when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.of(new TennisCourt()));
        List<Schedule> list = Collections.singletonList(new Schedule());
        when(repository.findByDates(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(list);
        when(mapper.map(anyList())).thenReturn(Collections.singletonList(new ScheduleDTO()));

        assertThrows(IllegalArgumentException.class, () -> service.addSchedule(createScheduleRequestDTO));

        verify(tennisCourtRepository).findById(1L);
        verify(repository).findByDates(now, now.plusHours(1));
        verify(mapper).map(list);
    }

    @Test
    void testAddSchedule() {

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setTennisCourtId(1L);
        LocalDateTime now = LocalDateTime.now();
        createScheduleRequestDTO.setStartDateTime(now);

        when(tennisCourtRepository.findById(anyLong())).thenReturn(Optional.of(new TennisCourt()));
        List<Schedule> list = new ArrayList<>();
        when(repository.findByDates(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(list);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        when(mapper.map(anyList())).thenReturn(new ArrayList<>());
        Schedule schedule = new Schedule();
        when(repository.saveAndFlush(any(Schedule.class))).thenReturn(schedule);
        when(mapper.map(any(Schedule.class))).thenReturn(scheduleDTO);

        ScheduleDTO response = service.addSchedule(createScheduleRequestDTO);
        assertEquals(scheduleDTO, response);

        verify(tennisCourtRepository).findById(1L);
        verify(repository).findByDates(now, now.plusHours(1));
        verify(mapper).map(list);
        verify(repository).saveAndFlush(any(Schedule.class));
        verify(mapper).map(schedule);
    }

    @Test
    void testFindSchedulesByDates() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);

        List<Schedule> scheduleList = Collections.singletonList(new Schedule());
        when(repository.findByDates(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(scheduleList);
        List<ScheduleDTO> scheduleDTOList = Collections.singletonList(new ScheduleDTO());
        when(mapper.map(anyList())).thenReturn(scheduleDTOList);

        List<ScheduleDTO> response = service.findSchedulesByDates(startDate, endDate);
        assertEquals(scheduleDTOList, response);

        verify(repository).findByDates(startDate, endDate);
        verify(mapper).map(scheduleList);
    }

    @Test
    void testFindSchedule() {

        Schedule schedule = new Schedule();
        when(repository.findById(anyLong())).thenReturn(Optional.of(schedule));
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        when(mapper.map(any(Schedule.class))).thenReturn(scheduleDTO);

        ScheduleDTO response = service.findSchedule(1L);
        assertEquals(scheduleDTO, response);

        verify(repository).findById(1L);
        verify(mapper).map(schedule);
    }

    @Test
    void testFindSchedule_noResult() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        when(mapper.map(any(Schedule.class))).thenReturn(scheduleDTO);

        ScheduleDTO response = service.findSchedule(1L);
        assertEquals(scheduleDTO, response);

        verify(repository).findById(1L);
        verify(mapper).map(any(Schedule.class));
    }

    @Test
    void testFindSchedulesByTennisCourtId() {

        List<Schedule> scheduleList = Collections.singletonList(new Schedule());
        when(repository.findByTennisCourt_IdOrderByStartDateTime(anyLong())).thenReturn(scheduleList);
        List<ScheduleDTO> scheduleDTOList = Collections.singletonList(new ScheduleDTO());
        when(mapper.map(anyList())).thenReturn(scheduleDTOList);

        List<ScheduleDTO> response = service.findSchedulesByTennisCourtId(1L);
        assertEquals(scheduleDTOList, response);

        verify(repository).findByTennisCourt_IdOrderByStartDateTime(1L);
        verify(mapper).map(scheduleList);
    }

    @Test
    void testFindSchedulesByTennisCourtId_noResult() {

        List<Schedule> scheduleList = new ArrayList<>();
        when(repository.findByTennisCourt_IdOrderByStartDateTime(anyLong())).thenReturn(scheduleList);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        when(mapper.map(anyList())).thenReturn(scheduleDTOList);

        List<ScheduleDTO> response = service.findSchedulesByTennisCourtId(1L);
        assertEquals(scheduleDTOList, response);

        verify(repository).findByTennisCourt_IdOrderByStartDateTime(1L);
        verify(mapper).map(scheduleList);
    }
}
