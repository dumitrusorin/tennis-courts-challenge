package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        Optional<TennisCourt> tennisCourt = tennisCourtRepository.findById(tennisCourtId);
        List<ScheduleDTO> duplicate = findSchedulesByDates(createScheduleRequestDTO.getStartDateTime(), createScheduleRequestDTO.getStartDateTime().plusHours(1));

        if (!tennisCourt.isPresent()) {
            throw new EntityNotFoundException("Tennis court not found.");
        } else if (!duplicate.isEmpty()) {
            throw new IllegalStateException("There schedule already exists.");
        } else {
            return scheduleMapper.map(scheduleRepository.saveAndFlush(Schedule.builder().tennisCourt(tennisCourt.get())
                    .startDateTime(createScheduleRequestDTO.getStartDateTime())
                    .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1)).build()));
        }
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findByDates(startDate, endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleMapper.map(scheduleRepository.findById(scheduleId).orElse(new Schedule()));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
