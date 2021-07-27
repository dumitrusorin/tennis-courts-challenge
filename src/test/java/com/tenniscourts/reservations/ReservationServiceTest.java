package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleRepository;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService service;

    @Mock
    private ReservationRepository repository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ReservationMapper mapper;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, guestRepository, scheduleRepository, mapper);
    }

    @Test
    void testBookReservation_noGuest() {

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(1L, 1L);

        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.bookReservation(createReservationRequestDTO));

        verify(guestRepository).findById(1L);
    }

    @Test
    void testBookReservation_noSchedule() {

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(1L, 1L);

        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(new Guest()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.bookReservation(createReservationRequestDTO));

        verify(guestRepository).findById(1L);
        verify(scheduleRepository).findById(1L);
    }

    @Test
    void testBookReservation_reservationExists() {

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(1L, 1L);

        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(new Guest()));
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(repository.findBySchedule_Id(anyLong())).thenReturn(Collections.singletonList(new Reservation()));

        assertThrows(IllegalArgumentException.class, () -> service.bookReservation(createReservationRequestDTO));

        verify(guestRepository).findById(1L);
        verify(scheduleRepository).findById(1L);
        verify(repository).findBySchedule_Id(1L);
    }

    @Test
    void testBookReservation() {

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(1L, 1L);

        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(new Guest()));
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(repository.findBySchedule_Id(anyLong())).thenReturn(new ArrayList<>());
        Reservation reservation = new Reservation();
        when(repository.saveAndFlush(any(Reservation.class))).thenReturn(reservation);
        ReservationDTO reservationDTO = new ReservationDTO();
        when(mapper.map(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO response = service.bookReservation(createReservationRequestDTO);
        assertEquals(reservationDTO, response);

        verify(guestRepository).findById(1L);
        verify(scheduleRepository).findById(1L);
        verify(repository).findBySchedule_Id(1L);
        verify(repository).saveAndFlush(any(Reservation.class));
        verify(mapper).map(reservation);
    }

    @Test
    void testFindReservation_exception() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findReservation(1L));
        verify(repository).findById(1L);
    }

    @Test
    void testFindReservation() {
        Reservation reservation = new Reservation();
        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
        ReservationDTO reservationDTO = new ReservationDTO();
        when(mapper.map(reservation)).thenReturn(reservationDTO);

        ReservationDTO response = service.findReservation(1L);
        assertEquals(reservationDTO, response);

        verify(repository).findById(1L);
        verify(mapper).map(reservation);
    }

    @Test
    void testCancelReservation_noReservation() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.cancelReservation(1L));
        verify(repository).findById(1L);
    }

    @Test
    void testCancelReservation_invalidStatus() {
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));

        assertThrows(IllegalArgumentException.class, () -> service.cancelReservation(1L));

        verify(repository).findById(1L);
    }

    @Test
    void testCancelReservation_invalidDate() {
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().minusHours(1L));
        reservation.setSchedule(schedule);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));

        assertThrows(IllegalArgumentException.class, () -> service.cancelReservation(1L));

        verify(repository).findById(1L);
    }

    @Test
    void testCancelReservation() {
        Reservation reservation = new Reservation();
        reservation.setValue(new BigDecimal(10));
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().plusHours(1L));
        reservation.setSchedule(schedule);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(repository.save(any(Reservation.class))).thenReturn(reservation);
        ReservationDTO reservationDTO = new ReservationDTO();
        when(mapper.map(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO response = service.cancelReservation(1L);
        assertEquals(reservationDTO, response);

        verify(repository).findById(1L);
        verify(repository).save(reservation);
        verify(mapper).map(reservation);
    }

    @ParameterizedTest
    @MethodSource("getNumberOfHours")
    void testGetRefundValue(Long hours, BigDecimal refund) {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(hours);

        schedule.setStartDateTime(startDateTime);

        assertEquals(refund, service.getRefundValue(Reservation.builder().schedule(schedule).value(BigDecimal.valueOf(10)).build()));
    }

    private static Stream<Arguments> getNumberOfHours() {
        return Stream.of(
                Arguments.of(48L, BigDecimal.TEN),
                Arguments.of(24L, BigDecimal.TEN),
                Arguments.of(18L, BigDecimal.valueOf(7)),
                Arguments.of(12L, BigDecimal.valueOf(7)),
                Arguments.of(8L, BigDecimal.valueOf(5)),
                Arguments.of(2L, BigDecimal.valueOf(5)),
                Arguments.of(1L, BigDecimal.valueOf(2)),
                Arguments.of(0L, BigDecimal.valueOf(2)),
                Arguments.of(-1L, BigDecimal.ZERO)
        );
    }

    @Test
    void testRescheduleReservation_sameReservation() {

        Reservation reservation = new Reservation();
        reservation.setSchedule(new Schedule());
        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));

        ReservationDTO reservationDTO = new ReservationDTO();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1L);
        reservationDTO.setSchedule(scheduleDTO);
        when(mapper.map(any(Reservation.class))).thenReturn(reservationDTO);

        assertThrows(IllegalArgumentException.class, () -> service.rescheduleReservation(1L, 1L));

        verify(repository).findById(1L);
        verify(mapper).map(reservation);
    }

    @Test
    void testRescheduleReservation() {

        Reservation reservation = new Reservation();
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        reservation.setSchedule(schedule);
        reservation.setValue(BigDecimal.valueOf(10));
        Guest guest = new Guest();
        guest.setId(1L);
        reservation.setGuest(guest);
        schedule.setStartDateTime(LocalDateTime.now().plusDays(1));
        reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));

        ReservationDTO reservationDTO = new ReservationDTO();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(2L);
        reservationDTO.setSchedule(scheduleDTO);

        when(repository.save(any(Reservation.class))).thenReturn(reservation);
        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(repository.findBySchedule_Id(anyLong())).thenReturn(new ArrayList<>());
        when(repository.saveAndFlush(any(Reservation.class))).thenReturn(reservation);
        when(mapper.map(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO response = service.rescheduleReservation(1L, 1L);
        assertEquals(reservationDTO, response);

        verify(repository, times(2)).findById(1L);
        verify(mapper, times(3)).map(reservation);
        verify(repository, times(2)).save(reservation);
        verify(repository).saveAndFlush(any(Reservation.class));
        verify(guestRepository).findById(1L);
        verify(scheduleRepository).findById(1L);
        verify(repository).findBySchedule_Id(1L);
    }

    @Test
    void testFindPastReservations() {
        List<Reservation> reservationList = Collections.singletonList(new Reservation());
        when(repository.findPastReservations(any(LocalDateTime.class))).thenReturn(reservationList);
        when(mapper.map(anyList())).thenReturn(Collections.singletonList(new ReservationDTO()));

        List<ReservationDTO> response = service.findPastReservations();
        assertEquals(1, response.size());

        verify(repository).findPastReservations(any(LocalDateTime.class));
        verify(mapper).map(reservationList);
    }

    @ParameterizedTest
    @MethodSource("refundSource")
    void testRefundDeposit(ReservationStatus status, BigDecimal refund) {

        Reservation reservation = new Reservation();
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationStatus(status.name());
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setEndDateTime(LocalDateTime.now().minusDays(1));
        reservationDTO.setSchedule(scheduleDTO);

        when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(mapper.map(any(Reservation.class))).thenReturn(reservationDTO);

        if (status.equals(ReservationStatus.READY_TO_PLAY)) {
            when(mapper.map(any(ReservationDTO.class))).thenReturn(reservation);
        }

        assertEquals(refund, service.getRefundDeposit(1L));

        verify(repository).findById(1L);
        verify(mapper).map(reservation);

        if (status.equals(ReservationStatus.READY_TO_PLAY)) {
            verify(mapper).map(reservationDTO);
            verify(repository).saveAndFlush(reservation);
        }
    }


    private static Stream<Arguments> refundSource() {
        return Stream.of(
                Arguments.of(ReservationStatus.READY_TO_PLAY, BigDecimal.TEN),
                Arguments.of(ReservationStatus.REFUNDED, BigDecimal.ZERO),
                Arguments.of(ReservationStatus.RESCHEDULED, BigDecimal.ZERO),
                Arguments.of(ReservationStatus.CANCELLED, BigDecimal.ZERO)
        );
    }
}