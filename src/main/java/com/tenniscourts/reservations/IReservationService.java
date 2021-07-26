package com.tenniscourts.reservations;

import java.math.BigDecimal;

public interface IReservationService {
    ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO);

    ReservationDTO findReservation(Long reservationId);

    ReservationDTO cancelReservation(Long reservationId);

    BigDecimal getRefundValue(Reservation reservation);

    ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId);
}
