package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    @Query(value = "SELECT * FROM SCHEDULE S WHERE S.START_DATE_TIME >= :startDateTime AND S.END_DATE_TIME <= :endDateTime", nativeQuery = true)
    List<Schedule> findByDates(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
}