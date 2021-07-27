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
public class ScheduleService implements IScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleMapper scheduleMapper;

    @Override
    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        Optional<TennisCourt> tennisCourt = tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId());
        if (!tennisCourt.isPresent()) {
            throw new EntityNotFoundException("Tennis court not found.");
        }

        List<ScheduleDTO> duplicate = findSchedulesByDates(createScheduleRequestDTO.getStartDateTime(), createScheduleRequestDTO.getStartDateTime().plusHours(1));
        if (!duplicate.isEmpty()) {
            throw new IllegalArgumentException("The schedule already exists.");
        }

        return scheduleMapper.map(scheduleRepository.saveAndFlush(Schedule.builder().tennisCourt(tennisCourt.get())
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1)).build()));
    }

    @Override
    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findByDates(startDate, endDate));
    }

    @Override
    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleMapper.map(scheduleRepository.findById(scheduleId).orElse(new Schedule()));
    }

    @Override
    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
