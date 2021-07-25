package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.IScheduleService;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TennisCourtService implements ITennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;

    private final IScheduleService scheduleService;

    private final TennisCourtMapper tennisCourtMapper;

    @Override
    public TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt) {
        Optional<TennisCourt> duplicate = tennisCourtRepository.findByName(tennisCourt.getName());

        if (duplicate.isPresent()) {
            throw new IllegalStateException("Tennis court already exists");
        }

        return tennisCourtMapper.map(tennisCourtRepository.saveAndFlush(tennisCourtMapper.map(tennisCourt)));
    }

    @Override
    public TennisCourtDTO findTennisCourtById(Long id) {
        return tennisCourtRepository.findById(id).map(tennisCourtMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
    }

    @Override
    public TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId) {
        TennisCourtDTO tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }
}
