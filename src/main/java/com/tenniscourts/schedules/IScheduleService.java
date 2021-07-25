package com.tenniscourts.schedules;

import java.time.LocalDateTime;
import java.util.List;

public interface IScheduleService {
    ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO);

    List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate);

    ScheduleDTO findSchedule(Long scheduleId);

    List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId);
}
