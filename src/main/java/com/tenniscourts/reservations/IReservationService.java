package com.tenniscourts.reservations;

import java.math.BigDecimal;

public interface IReservationService {
    ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO);

    ReservationDTO findReservation(Long reservationId);

    ReservationDTO cancelReservation(Long reservationId);

    BigDecimal getRefundValue(Reservation reservation);

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
                "Cannot reschedule to the same slot.*/
    ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId);
}
