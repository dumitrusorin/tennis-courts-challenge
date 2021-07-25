package com.tenniscourts.tenniscourts;

public interface ITennisCourtService {
    // TODO: 25-Jul-21 check same insert twice
    TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt);

    TennisCourtDTO findTennisCourtById(Long id);

    TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId);
}
