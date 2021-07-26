package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService implements IGuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Override
    public GuestDTO create(GuestDTO guest) {
        if(guest.getName().isEmpty()){
            throw new IllegalArgumentException("Guest must have name.");
        }

        Optional<Guest> duplicate = guestRepository.findByName(guest.getName());
        if(duplicate.isPresent()){
            throw new IllegalArgumentException("Guest already exists.");
        }

        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guest)));
    }

    @Override
    public GuestDTO find(Long id) {
        return guestRepository.findById(id).map(guestMapper::map).<EntityNotFoundException>orElseThrow(()->{
            throw new EntityNotFoundException("Guest not found.");
        });
    }

    @Override
    public GuestDTO findByName(String name) {
        return guestRepository.findByName(name).map(guestMapper::map).<EntityNotFoundException>orElseThrow(()->{
            throw new EntityNotFoundException("Guest not found.");
        });
    }

    @Override
    public GuestDTO delete(Long id) {
        GuestDTO guest = find(id);
        try {
            guestRepository.delete(guestMapper.map(guest));
        } catch (Exception e){
            throw new IllegalArgumentException("This guest has history and cannot be deleted.");
        }
        return new GuestDTO();
    }

    @Override
    public GuestDTO update(GuestDTO guest) {
        GuestDTO guestDTO = find(guest.getId());
        guestDTO.setName(guest.getName());
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    @Override
    public List<GuestDTO> findAll() {
        return guestMapper.map(guestRepository.findAll());
    }
}
