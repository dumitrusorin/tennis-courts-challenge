package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleRequestDTO {

    @NotNull
    private Long tennisCourtId;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime startDateTime;

}
