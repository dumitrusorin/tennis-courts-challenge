package com.tenniscourts.schedules;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Getter
@Setter
public class ScheduleDTO {

    private Long id;

    private TennisCourtDTO tennisCourt;

    @NotNull
    private Long tennisCourtId;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

}
