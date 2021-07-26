package com.tenniscourts.guests;

import java.util.List;

public interface IGuestService {
    GuestDTO create(GuestDTO guest);

    GuestDTO find(Long id);

    GuestDTO findByName(String name);

    GuestDTO delete(Long id);

    GuestDTO update(GuestDTO guest);

    List<GuestDTO> findAll();
}
