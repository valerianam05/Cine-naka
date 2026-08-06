package com.example.demo.endpoint.rest.controller;

import com.example.demo.model.Reservation;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.ReservationService;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {
  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping("/reservations")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
  public List<Reservation> getAllReservations() {
    return reservationService.getAllReservations();
  }

  @GetMapping("/reservationById")
  public Reservation getReservationById(
      @RequestParam UUID id, @AuthenticationPrincipal UserPrincipal principal)
      throws AccessDeniedException {

    return reservationService.getReservationById(id, principal.getUser());
  }

  @PutMapping("/reservation")
  public Reservation createOrUpdateReservation(
      @RequestBody Reservation dto, @AuthenticationPrincipal UserPrincipal principal)
      throws AccessDeniedException {

    return reservationService.createOrUpdateReservation(dto, principal.getUser());
  }
}
