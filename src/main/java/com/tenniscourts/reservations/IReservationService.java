package com.tenniscourts.reservations;

import java.math.BigDecimal;
import java.util.List;

public interface IReservationService {
    ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO);

    ReservationDTO findReservation(Long reservationId);

    ReservationDTO cancelReservation(Long reservationId);

    BigDecimal getRefundValue(Reservation reservation);

    ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId);

    List<ReservationDTO> findPastReservations();

    BigDecimal getRefundDeposit(Long reservationId);
}
