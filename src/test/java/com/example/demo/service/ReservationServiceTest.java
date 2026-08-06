package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.demo.endpoint.rest.exception.ForbiddenOperationException;
import com.example.demo.endpoint.rest.exception.ResourceNotFoundException;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.ReservationStatus;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.model.Reservation;
import com.example.demo.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

  @Mock private ReservationRepository reservationRepository;
  @Mock private EntityManager entityManager;

  private final ReservationMapper reservationMapper = new ReservationMapper();
  private ReservationService service;

  private UserEntity client;
  private UserEntity otherClient;
  private UserEntity employee;

  @BeforeEach
  void setUp() {
    service = new ReservationService(reservationRepository, reservationMapper, entityManager);

    client = UserEntity.builder().id(UUID.randomUUID()).role(UserRole.CLIENT).build();
    otherClient = UserEntity.builder().id(UUID.randomUUID()).role(UserRole.CLIENT).build();
    employee = UserEntity.builder().id(UUID.randomUUID()).role(UserRole.EMPLOYEE).build();
  }

  @Test
  void getReservationById_ownerClient_returnsReservation() throws AccessDeniedException {
    ReservationEntity reservation = reservationOf(client);
    when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

    Reservation result = service.getReservationById(reservation.getId(), client);

    assertThat(result.getId()).isEqualTo(reservation.getId());
  }

  @Test
  void getReservationById_otherClient_throwsAccessDenied() {
    ReservationEntity reservation = reservationOf(client);
    when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

    assertThatThrownBy(() -> service.getReservationById(reservation.getId(), otherClient))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void getReservationById_employee_returnsReservation() throws AccessDeniedException {
    ReservationEntity reservation = reservationOf(client);
    when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

    Reservation result = service.getReservationById(reservation.getId(), employee);

    assertThat(result.getId()).isEqualTo(reservation.getId());
  }

  @Test
  void getReservationById_notFound_throwsResourceNotFound() {
    UUID missingId = UUID.randomUUID();
    when(reservationRepository.findById(missingId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getReservationById(missingId, employee))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void create_client_defaultStatusIsPending() throws AccessDeniedException {
    Reservation dto = new Reservation();
    dto.setProjectionId(UUID.randomUUID());

    when(entityManager.getReference(eq(Projection.class), any())).thenReturn(new Projection());
    when(reservationRepository.save(any(ReservationEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Reservation result = service.createOrUpdateReservation(dto, client);

    assertThat(result.getStatus()).isEqualTo(ReservationStatus.PENDING);
  }

  @Test
  void create_client_forcingNonPendingStatus_throwsForbidden() {
    Reservation dto = new Reservation();
    dto.setProjectionId(UUID.randomUUID());
    dto.setStatus(ReservationStatus.CONFIRMED);

    assertThatThrownBy(() -> service.createOrUpdateReservation(dto, client))
        .isInstanceOf(ForbiddenOperationException.class);
  }

  @Test
  void update_ownerClient_changingStatus_throwsForbidden() {
    ReservationEntity reservation = reservationOf(client);
    Reservation dto = new Reservation();
    dto.setId(reservation.getId());
    dto.setStatus(ReservationStatus.CONFIRMED);

    when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

    assertThatThrownBy(() -> service.createOrUpdateReservation(dto, client))
        .isInstanceOf(ForbiddenOperationException.class);
  }

  @Test
  void update_otherClient_throwsAccessDenied() {
    ReservationEntity reservation = reservationOf(client);
    Reservation dto = new Reservation();
    dto.setId(reservation.getId());

    when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

    assertThatThrownBy(() -> service.createOrUpdateReservation(dto, otherClient))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void update_employee_canChangeStatus() throws AccessDeniedException {
    ReservationEntity reservation = reservationOf(client);
    Reservation dto = new Reservation();
    dto.setId(reservation.getId());
    dto.setStatus(ReservationStatus.CONFIRMED);

    when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
    when(reservationRepository.save(any(ReservationEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Reservation result = service.createOrUpdateReservation(dto, employee);

    assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
  }

  private ReservationEntity reservationOf(UserEntity owner) {
    return ReservationEntity.builder()
        .id(UUID.randomUUID())
        .user(owner)
        .projection(Projection.builder().id(UUID.randomUUID()).build())
        .status(ReservationStatus.PENDING)
        .build();
  }
}
