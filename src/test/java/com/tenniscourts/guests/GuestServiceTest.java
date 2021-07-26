package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    @InjectMocks
    private GuestService service;

    @Mock
    private GuestRepository repository;

    @Mock
    private GuestMapper mapper;

    private static final String GUEST_NAME = "Name Surname";

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void testCreate_fail_noName() {
        assertThrows(IllegalArgumentException.class, () -> service.create(new GuestDTO(null, "")));
    }

    @Test
    void testCreate_fail_duplicate() {
        GuestDTO guestDTO = new GuestDTO(null, GUEST_NAME);
        when(repository.findByName(anyString())).thenReturn(Optional.of(new Guest()));

        assertThrows(IllegalArgumentException.class, () -> service.create(guestDTO));

        verify(repository).findByName(GUEST_NAME);
    }

    @Test
    void testCreate_fail() {
        GuestDTO guestDTO = new GuestDTO(null, GUEST_NAME);
        Guest guest = new Guest();

        when(repository.findByName(anyString())).thenReturn(Optional.empty());
        when(mapper.map(any(GuestDTO.class))).thenReturn(guest);
        when(repository.saveAndFlush(any(Guest.class))).thenReturn(new Guest());
        when(mapper.map(any(Guest.class))).thenReturn(guestDTO);

        GuestDTO response = service.create(guestDTO);
        assertEquals(guestDTO, response);

        verify(repository).findByName(GUEST_NAME);
        verify(mapper).map(guestDTO);
        verify(repository).saveAndFlush(guest);
        verify(mapper).map(guest);
    }

    @Test
    void testFind_fail() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.find(1L));

        verify(repository).findById(1L);
    }

    @Test
    void testFind() {
        Guest guest = new Guest();
        GuestDTO guestDTO = new GuestDTO();

        when(repository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(mapper.map(any(Guest.class))).thenReturn(guestDTO);

        GuestDTO response = service.find(1L);
        assertEquals(guestDTO, response);

        verify(repository).findById(1L);
        verify(mapper).map(guest);
    }

    @Test
    void testFindByName_fail_notFound() {
        when(repository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findByName(GUEST_NAME));

        verify(repository).findByName(GUEST_NAME);
    }

    @Test
    void testFindByName() {
        Guest guest = new Guest();
        GuestDTO guestDTO = new GuestDTO();

        when(repository.findByName(anyString())).thenReturn(Optional.of(guest));
        when(mapper.map(any(Guest.class))).thenReturn(guestDTO);

        GuestDTO response = service.findByName(GUEST_NAME);
        assertEquals(guestDTO, response);

        verify(repository).findByName(GUEST_NAME);
        verify(mapper).map(guest);
    }

    @Test
    void testDelete() {
        Guest guest = new Guest();
        GuestDTO guestDTO = new GuestDTO();

        when(repository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(mapper.map(any(Guest.class))).thenReturn(guestDTO);
        when(mapper.map(any(GuestDTO.class))).thenReturn(guest);

        GuestDTO response = service.delete(1L);
        assertEquals(guestDTO, response);

        verify(repository).findById(1L);
        verify(mapper).map(guest);
        verify(mapper).map(guestDTO);
        verify(repository).delete(guest);
    }

    @Test
    void testUpdate() {
        Guest guest = new Guest();
        GuestDTO guestDTO = new GuestDTO(1L, GUEST_NAME);

        when(repository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(mapper.map(any(Guest.class))).thenReturn(guestDTO);
        when(mapper.map(any(GuestDTO.class))).thenReturn(guest);
        when(repository.saveAndFlush(guest)).thenReturn(guest);

        GuestDTO response = service.update(guestDTO);
        assertEquals(guestDTO, response);

        verify(repository).findById(1L);
        verify(mapper, times(2)).map(guest);
        verify(mapper).map(guestDTO);
        verify(repository).saveAndFlush(guest);
    }

    @Test
    void testFindAll(){
        List<Guest> guestList = Collections.singletonList(new Guest());
        when(repository.findAll()).thenReturn(guestList);
        when(mapper.map(anyList())).thenReturn(Collections.singletonList(new GuestDTO()));

        List<GuestDTO> response = service.findAll();
        assertEquals(1, response.size());

        verify(repository).findAll();
        verify(mapper).map(guestList);
    }
}
