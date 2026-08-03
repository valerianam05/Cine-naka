package com.example.demo.service;

import com.example.demo.endpoint.rest.exception.ForbiddenOperationException;
import com.example.demo.endpoint.rest.exception.ResourceNotFoundException;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.ReservationStatus;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final EntityManager entityManager;

  public ReservationService(
      ReservationRepository reservationRepository,
      ReservationMapper reservationMapper,
      EntityManager entityManager) {
    this.reservationRepository = reservationRepository;
    this.reservationMapper = reservationMapper;
    this.entityManager = entityManager;
  }

  @Transactional(readOnly = true)
  public List<Reservation> getAllReservations() {
    return reservationMapper.toModelList(reservationRepository.findAll());
  }

  @Transactional(readOnly = true)
  public Reservation getReservationById(UUID id, UserEntity currentUser)
      throws AccessDeniedException {
    ReservationEntity reservation = findOrThrow(id);
    boolean isOwner = reservation.getUser().getId().equals(currentUser.getId());

    if (!isOwner && !isStaff(currentUser)) {
      throw new AccessDeniedException("Vous n'avez pas accès à cette réservation.");
    }
    return reservationMapper.toModel(reservation);
  }

  @Transactional
  public Reservation createOrUpdateReservation(Reservation dto, UserEntity currentUser)
      throws AccessDeniedException {
    return dto.getId() == null
        ? createReservation(dto, currentUser)
        : updateReservation(dto, currentUser);
  }

  private Reservation createReservation(Reservation dto, UserEntity currentUser) {
    boolean isClient = currentUser.getRole() == UserRole.CLIENT;

    if (isClient && dto.getStatus() != null && dto.getStatus() != ReservationStatus.PENDING) {
      throw new ForbiddenOperationException(
          "Un CLIENT ne peut créer une réservation qu'avec le statut initial PENDING.");
    }

    ReservationEntity reservation = new ReservationEntity();
    reservation.setUser(currentUser);
    reservation.setProjection(
        entityManager.getReference(ProjectionEntity.class, dto.getProjectionId()));
    reservation.setStatus(ReservationStatus.PENDING);

    return reservationMapper.toModel(reservationRepository.save(reservation));
  }

  private Reservation updateReservation(Reservation dto, UserEntity currentUser)
      throws AccessDeniedException {
    ReservationEntity reservation = findOrThrow(dto.getId());
    boolean isOwner = reservation.getUser().getId().equals(currentUser.getId());
    boolean isClient = currentUser.getRole() == UserRole.CLIENT;
    boolean isStaff = isStaff(currentUser);

    if (isClient) {
      if (!isOwner) {
        throw new AccessDeniedException("Vous ne pouvez modifier que vos propres réservations.");
      }
      if (dto.getStatus() != null && dto.getStatus() != reservation.getStatus()) {
        throw new ForbiddenOperationException(
            "Seuls EMPLOYEE et MANAGER peuvent modifier le statut d'une réservation.");
      }
    } else if (!isStaff) {
      throw new AccessDeniedException("Opération non autorisée.");
    }

    if (isStaff && dto.getStatus() != null) {
      reservation.setStatus(dto.getStatus());
    }

    return reservationMapper.toModel(reservationRepository.save(reservation));
  }

  private ReservationEntity findOrThrow(UUID id) {
    return reservationRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable : " + id));
  }

  private boolean isStaff(UserEntity user) {
    return user.getRole() == UserRole.EMPLOYEE || user.getRole() == UserRole.MANAGER;
  }
}
