package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

    private final IGuestService guestService;

    @PostMapping
    public ResponseEntity<Void> addGuest(GuestDTO guestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.create(guestDTO).getId())).build();
    }

    @GetMapping(path = "/{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.find(guestId));
    }

    @GetMapping(path = "/byName/{guestName}")
    public ResponseEntity<GuestDTO> findGuestByName(@PathVariable String guestName) {
        return ResponseEntity.ok(guestService.findByName(guestName));
    }

    @DeleteMapping(path = "/{guestId}")
    public ResponseEntity<GuestDTO> deleteGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.delete(guestId));
    }

    @PutMapping
    public ResponseEntity<GuestDTO> updateGuest(GuestDTO guestDTO) {
        if (guestDTO.getId() == 0) {
            throw new IllegalArgumentException("For update, guestId must be provided.");
        }
        if (guestDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Guest name must not be empty.");
        }
        return ResponseEntity.ok(guestService.update(guestDTO));
    }

    @GetMapping
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAll());
    }
}
