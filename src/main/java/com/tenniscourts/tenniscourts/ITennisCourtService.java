package com.tenniscourts.tenniscourts;

public interface ITennisCourtService {

    TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt);

    TennisCourtDTO findTennisCourtById(Long id);

    TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId);
}
